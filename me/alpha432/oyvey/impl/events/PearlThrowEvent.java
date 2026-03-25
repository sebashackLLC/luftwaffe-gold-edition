//Decompiled by Procyon!

package me.alpha432.oyvey.impl.events;

import me.alpha432.oyvey.api.event.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.item.*;

public class PearlThrowEvent extends EventStage
{
    private final EntityPlayer thrower;
    private final EntityEnderPearl pearl;
    
    public EntityPlayer getThrower() {
        return this.thrower;
    }
    
    public EntityEnderPearl getPearl() {
        return this.pearl;
    }
    
    public PearlThrowEvent(final EntityPlayer thrower, final EntityEnderPearl pearl) {
        this.thrower = thrower;
        this.pearl = pearl;
    }
}
