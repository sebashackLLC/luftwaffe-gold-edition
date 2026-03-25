//Decompiled by Procyon!

package me.alpha432.oyvey.impl.events;

import me.alpha432.oyvey.api.event.*;
import net.minecraft.entity.*;

public class EventEntityPositionChange extends EventStage
{
    private final Entity entity;
    private final double prevX;
    private final double prevY;
    private final double prevZ;
    private final double newX;
    private final double newY;
    private final double newZ;
    
    public EventEntityPositionChange(final int stage, final Entity entity, final double prevX, final double prevY, final double prevZ, final double newX, final double newY, final double newZ) {
        super(stage);
        this.entity = entity;
        this.prevX = prevX;
        this.prevY = prevY;
        this.prevZ = prevZ;
        this.newX = newX;
        this.newY = newY;
        this.newZ = newZ;
    }
    
    public EventEntityPositionChange(final int stage, final Entity entity, final double newX, final double newY, final double newZ) {
        this(stage, entity, entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, newX, newY, newZ);
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public double getPrevX() {
        return this.prevX;
    }
    
    public double getPrevY() {
        return this.prevY;
    }
    
    public double getPrevZ() {
        return this.prevZ;
    }
    
    public double getNewX() {
        return this.newX;
    }
    
    public double getNewY() {
        return this.newY;
    }
    
    public double getNewZ() {
        return this.newZ;
    }
    
    public boolean hasPositionChanged() {
        return this.prevX != this.newX || this.prevY != this.newY || this.prevZ != this.newZ;
    }
    
    public double getDeltaX() {
        return this.newX - this.prevX;
    }
    
    public double getDeltaY() {
        return this.newY - this.prevY;
    }
    
    public double getDeltaZ() {
        return this.newZ - this.prevZ;
    }
    
    public double getDistanceChanged() {
        final double dx = this.getDeltaX();
        final double dy = this.getDeltaY();
        final double dz = this.getDeltaZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
