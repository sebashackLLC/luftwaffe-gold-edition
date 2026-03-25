//Decompiled by Procyon!

package me.alpha432.oyvey.impl.events;

import me.alpha432.oyvey.api.interfaces.*;

public class TickEvent implements Minecraftable
{
    public boolean isSafe() {
        return TickEvent.mc.field_71439_g != null && TickEvent.mc.field_71441_e != null;
    }
    
    public static final class Post extends TickEvent
    {
    }
}
