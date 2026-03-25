//Decompiled by Procyon!

package me.alpha432.oyvey.api.interfaces.ducks;

import net.minecraft.util.*;

public interface IMinecraft
{
    int getRightClickDelay();
    
    void setRightClickDelay(final int p0);
    
    void setLeftClickCounter(final int p0);
    
    Timer getTimer();
    
    void click(final Click p0);
    
    void setSession(final Session p0);
    
    public enum Click
    {
        RIGHT, 
        LEFT, 
        MIDDLE;
    }
}
