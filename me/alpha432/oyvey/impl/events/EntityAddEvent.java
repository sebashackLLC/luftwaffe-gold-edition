//Decompiled by Procyon!

package me.alpha432.oyvey.impl.events;

import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.entity.*;

@Cancelable
public class EntityAddEvent extends Event
{
    public Entity entity;
    
    public EntityAddEvent(final Entity entity) {
        this.entity = entity;
    }
    
    public Entity getEntity() {
        return this.entity;
    }
}
