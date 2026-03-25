//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.discord.connection;

import java.util.function.*;
import java.io.*;
import java.nio.*;
import me.alpha432.oyvey.api.util.discord.*;
import java.nio.charset.*;
import com.google.gson.*;

public class WinConnection extends Connection
{
    private final RandomAccessFile raf;
    private final Consumer<Packet> callback;
    
    WinConnection(final String name, final Consumer<Packet> callback) throws IOException {
        this.raf = new RandomAccessFile(name, "rw");
        this.callback = callback;
        final Thread thread = new Thread(this::run);
        thread.setName("discord IPC - Read thread");
        thread.start();
    }
    
    protected void write(final ByteBuffer buffer) {
        try {
            this.raf.write(buffer.array());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void run() {
        final ByteBuffer intB = ByteBuffer.allocate(4);
        try {
            while (true) {
                this.readFully(intB);
                final Opcode opcode = Opcode.valueOf(Integer.reverseBytes(intB.getInt(0)));
                this.readFully(intB);
                final int length = Integer.reverseBytes(intB.getInt(0));
                final ByteBuffer dataB = ByteBuffer.allocate(length);
                this.readFully(dataB);
                dataB.rewind();
                final byte[] bytes = new byte[dataB.remaining()];
                dataB.get(bytes);
                final String data = new String(bytes, Charset.defaultCharset());
                this.callback.accept(new Packet(opcode, new JsonParser().parse(data).getAsJsonObject()));
            }
        }
        catch (Exception ex) {}
    }
    
    private void readFully(final ByteBuffer buffer) throws IOException {
        buffer.rewind();
        while (this.raf.length() < buffer.remaining()) {
            try {
                Thread.sleep(1L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (buffer.hasRemaining()) {
            this.raf.getChannel().read(buffer);
        }
    }
    
    public void close() {
        try {
            this.raf.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
