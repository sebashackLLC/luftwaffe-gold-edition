//Decompiled by Procyon!

package me.alpha432.oyvey.impl.events;

import me.alpha432.oyvey.api.event.*;

public class PlayerJumpEvent extends EventStage
{
    public double motionX;
    public double motionY;
    
    public PlayerJumpEvent(final double motionX, final double motionY) {
        this.motionX = motionX;
        this.motionY = motionY;
    }
}
