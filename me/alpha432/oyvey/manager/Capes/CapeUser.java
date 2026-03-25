//Decompiled by Procyon!

package me.alpha432.oyvey.manager.Capes;

import net.minecraft.util.*;

public class CapeUser
{
    private final ResourceLocation location;
    private final String[] UUIDs;
    
    public CapeUser(final ResourceLocation location, final String... UUIDs) {
        this.location = location;
        this.UUIDs = UUIDs;
    }
    
    public ResourceLocation getLocation() {
        return this.location;
    }
    
    public String[] getUUIDs() {
        return this.UUIDs;
    }
}
