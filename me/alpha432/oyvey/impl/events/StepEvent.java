//Decompiled by Procyon!

package me.alpha432.oyvey.impl.events;

import me.alpha432.oyvey.api.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.entity.*;

@Cancelable
public class StepEvent extends EventStage
{
    private final Entity entity;
    private float height;
    
    public StepEvent(final int stage, final Entity entity) {
        super(stage);
        this.entity = entity;
        this.height = entity.field_70138_W;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public float getHeight() {
        return this.height;
    }
    
    public void setHeight(final float height) {
        this.height = height;
    }
}
