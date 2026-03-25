//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.discord.connection;

import java.util.function.*;
import java.io.*;
import me.alpha432.oyvey.api.util.discord.*;
import com.google.gson.*;
import java.util.*;
import java.nio.*;

public abstract class Connection
{
    private static final String[] UNIX_TEMP_PATHS;
    
    public static Connection open(final Consumer<Packet> callback) {
        final String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            int i = 0;
            while (i < 10) {
                try {
                    return new WinConnection("\\\\?\\pipe\\discord-ipc-" + i, callback);
                }
                catch (IOException ex) {
                    ++i;
                    continue;
                }
                break;
            }
        }
        else {
            String name = null;
            for (final String tempPath : Connection.UNIX_TEMP_PATHS) {
                name = System.getenv(tempPath);
                if (name != null) {
                    break;
                }
            }
            if (name == null) {
                name = "/tmp";
            }
            name += "/discord-ipc-";
            int j = 0;
            while (j < 10) {
                try {
                    return new UnixConnection(name + j, callback);
                }
                catch (IOException ex2) {
                    ++j;
                    continue;
                }
                break;
            }
        }
        return null;
    }
    
    public void write(final Opcode opcode, final JsonObject o) {
        o.addProperty("nonce", UUID.randomUUID().toString());
        final byte[] d = o.toString().getBytes();
        final ByteBuffer packet = ByteBuffer.allocate(d.length + 8);
        packet.putInt(Integer.reverseBytes(opcode.ordinal()));
        packet.putInt(Integer.reverseBytes(d.length));
        packet.put(d);
        packet.rewind();
        this.write(packet);
    }
    
    protected abstract void write(final ByteBuffer p0);
    
    public abstract void close();
    
    static {
        UNIX_TEMP_PATHS = new String[] { "XDG_RUNTIME_DIR", "TMPDIR", "TMP", "TEMP" };
    }
}
