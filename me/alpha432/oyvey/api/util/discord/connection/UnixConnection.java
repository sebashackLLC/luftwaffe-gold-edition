//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.discord.connection;

import java.nio.channels.*;
import java.util.function.*;
import java.io.*;
import java.nio.*;
import me.alpha432.oyvey.api.util.discord.*;
import java.nio.charset.*;
import com.google.gson.*;

public class UnixConnection extends Connection
{
    private final Selector s;
    private final SocketChannel sc;
    private final Consumer<Packet> callback;
    
    public UnixConnection(final String name, final Consumer<Packet> callback) throws IOException {
        this.s = Selector.open();
        try {
            this.sc = SocketChannel.open();
            this.callback = callback;
            this.sc.configureBlocking(false);
            this.sc.register(this.s, 1);
            this.connectToUnixSocket(name);
        }
        catch (Exception e) {
            throw new IOException("Failed to connect to Unix socket: " + name, e);
        }
        final Thread thread = new Thread(this::run);
        thread.setName("discord IPC - Read thread");
        thread.start();
    }
    
    private void connectToUnixSocket(final String name) throws IOException {
        throw new IOException("Unix domain sockets not supported in this Java version. Path: " + name);
    }
    
    private void run() {
        State state = State.Opcode;
        final ByteBuffer intB = ByteBuffer.allocate(4);
        ByteBuffer dataB = null;
        Opcode opcode = null;
        try {
            while (true) {
                this.s.select();
                switch (state) {
                    case Opcode: {
                        this.sc.read(intB);
                        if (intB.hasRemaining()) {
                            continue;
                        }
                        opcode = Opcode.valueOf(Integer.reverseBytes(intB.getInt(0)));
                        state = State.Length;
                        intB.rewind();
                        continue;
                    }
                    case Length: {
                        this.sc.read(intB);
                        if (intB.hasRemaining()) {
                            continue;
                        }
                        dataB = ByteBuffer.allocate(Integer.reverseBytes(intB.getInt(0)));
                        state = State.Data;
                        intB.rewind();
                        continue;
                    }
                    case Data: {
                        this.sc.read(dataB);
                        if (dataB.hasRemaining()) {
                            continue;
                        }
                        dataB.rewind();
                        final byte[] bytes = new byte[dataB.remaining()];
                        dataB.get(bytes);
                        final String data = new String(bytes, Charset.defaultCharset());
                        this.callback.accept(new Packet(opcode, new JsonParser().parse(data).getAsJsonObject()));
                        dataB = null;
                        state = State.Opcode;
                        continue;
                    }
                }
            }
        }
        catch (Exception ex) {}
    }
    
    protected void write(final ByteBuffer buffer) {
        try {
            this.sc.write(buffer);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void close() {
        try {
            this.s.close();
            this.sc.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private enum State
    {
        Opcode, 
        Length, 
        Data;
    }
}
