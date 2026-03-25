//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin({ Minecraft.class })
public interface IMinecraft
{
    @Accessor("timer")
    Timer getTimer();
    
    @Accessor("rightClickDelayTimer")
    void setRightClickDelayTimer(final int p0);
}
