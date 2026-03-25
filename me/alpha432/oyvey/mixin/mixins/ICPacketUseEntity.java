//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.network.play.client.*;
import org.spongepowered.asm.mixin.gen.*;
import net.minecraft.util.*;

@Mixin({ CPacketUseEntity.class })
public interface ICPacketUseEntity
{
    @Accessor("entityId")
    void setEntityId(final int p0);
    
    @Accessor("entityId")
    int getEntityId();
    
    @Accessor("action")
    void setAction(final CPacketUseEntity.Action p0);
    
    @Accessor("hand")
    void setHand(final EnumHand p0);
}
