//Decompiled by Procyon!

package me.alpha432.oyvey.impl.events;

import me.alpha432.oyvey.api.event.*;
import net.minecraftforge.fml.common.eventhandler.*;

@Cancelable
public class WalkingPlayerUpdateEvent extends EventStage
{
    private final double initialX;
    private final double initialY;
    private final double initialZ;
    private final float initialYaw;
    private final float initialPitch;
    private final boolean initialOnGround;
    private double x;
    private double y;
    private double z;
    private float rotationYaw;
    private float rotationPitch;
    private boolean onGround;
    protected boolean modified;
    protected boolean modifiedRots;
    
    public WalkingPlayerUpdateEvent() {
        this(0.0, 0.0, 0.0, 0.0f, 0.0f, false);
    }
    
    public WalkingPlayerUpdateEvent(final EventStage stage, final WalkingPlayerUpdateEvent event) {
        this(event.x, event.y, event.z, event.rotationYaw, event.rotationPitch, event.onGround);
    }
    
    public WalkingPlayerUpdateEvent(final double x, final double y, final double z, final float rotationYaw, final float rotationPitch, final boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
        this.onGround = onGround;
        this.initialX = x;
        this.initialY = y;
        this.initialZ = z;
        this.initialYaw = rotationYaw;
        this.initialPitch = rotationPitch;
        this.initialOnGround = onGround;
    }
    
    public double getInitialX() {
        return this.initialX;
    }
    
    public double getInitialY() {
        return this.initialY;
    }
    
    public double getInitialZ() {
        return this.initialZ;
    }
    
    public float getInitialYaw() {
        return this.initialYaw;
    }
    
    public float getInitialPitch() {
        return this.initialPitch;
    }
    
    public boolean isInitialOnGround() {
        return this.initialOnGround;
    }
    
    public float getRotationYaw() {
        return this.rotationYaw;
    }
    
    public float getRotationPitch() {
        return this.rotationPitch;
    }
    
    public boolean isModified() {
        return this.modified;
    }
    
    public boolean isRotsModified() {
        return this.modifiedRots;
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(final double x) {
        this.modified = true;
        this.x = x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(final double y) {
        this.modified = true;
        this.y = y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public void setZ(final double z) {
        this.modified = true;
        this.z = z;
    }
    
    public float getYaw() {
        return this.rotationYaw;
    }
    
    public void setYaw(final float rotationYaw) {
        this.modified = true;
        this.rotationYaw = rotationYaw;
        this.modifiedRots = true;
    }
    
    public float getPitch() {
        return this.rotationPitch;
    }
    
    public void setPitch(final float rotationPitch) {
        this.modified = true;
        this.rotationPitch = rotationPitch;
        this.modifiedRots = true;
    }
    
    public boolean isOnGround() {
        return this.onGround;
    }
    
    public void setOnGround(final boolean onGround) {
        this.modified = true;
        this.onGround = onGround;
    }
}
