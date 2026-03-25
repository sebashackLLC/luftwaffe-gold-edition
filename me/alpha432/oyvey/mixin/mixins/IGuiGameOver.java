//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.gui.*;
import net.minecraft.util.text.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin({ GuiGameOver.class })
public interface IGuiGameOver
{
    @Accessor("causeOfDeath")
    ITextComponent getCause();
}
