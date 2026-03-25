//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin({ Timer.class })
public interface ITimer
{
    @Accessor("tickLength")
    float getTickLength();
    
    @Accessor("tickLength")
    void setTickLength(final float p0);
}
