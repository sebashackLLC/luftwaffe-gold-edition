//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.discord;

import com.google.gson.*;

public class Packet
{
    private final Opcode opcode;
    private final JsonObject data;
    
    public Packet(final Opcode opcode, final JsonObject data) {
        this.opcode = opcode;
        this.data = data;
    }
    
    public Opcode opcode() {
        return this.opcode;
    }
    
    public JsonObject data() {
        return this.data;
    }
}
