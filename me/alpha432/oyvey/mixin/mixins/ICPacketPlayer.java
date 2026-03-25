//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.network.play.client.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin({ CPacketPlayer.class })
public interface ICPacketPlayer
{
    @Accessor("rotating")
    boolean isRotating();
    
    @Accessor("moving")
    boolean isMoving();
    
    @Accessor("x")
    void setX(final double p0);
    
    @Accessor("x")
    double getX();
    
    @Accessor("y")
    void setY(final double p0);
    
    @Accessor("y")
    double getY();
    
    @Accessor("z")
    void setZ(final double p0);
    
    @Accessor("z")
    double getZ();
    
    @Accessor("yaw")
    void setYaw(final float p0);
    
    @Accessor("yaw")
    float getYaw();
    
    @Accessor("pitch")
    void setPitch(final float p0);
    
    @Accessor("pitch")
    float getPitch();
    
    @Accessor("onGround")
    void setOnGround(final boolean p0);
}
