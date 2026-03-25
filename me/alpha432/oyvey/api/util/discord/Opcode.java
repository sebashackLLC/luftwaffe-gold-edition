//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.discord;

public enum Opcode
{
    Handshake, 
    Frame, 
    Close, 
    Ping, 
    Pong;
    
    private static final Opcode[] VALUES;
    
    public static Opcode valueOf(final int i) {
        return Opcode.VALUES[i];
    }
    
    static {
        VALUES = values();
    }
}
