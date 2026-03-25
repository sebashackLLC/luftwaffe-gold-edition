//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.network.play.server.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin({ SPacketPlayerPosLook.class })
public interface ISPacketPlayerPosLook
{
    @Accessor("yaw")
    void setYaw(final float p0);
    
    @Accessor("pitch")
    void setPitch(final float p0);
    
    @Accessor("x")
    void setX(final double p0);
    
    @Accessor("y")
    void setY(final double p0);
    
    @Accessor("z")
    void setZ(final double p0);
}
