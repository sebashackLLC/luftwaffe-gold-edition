//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.gui.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.*;
import me.alpha432.oyvey.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ GuiMainMenu.class })
public class MixinGuiMainMenu extends GuiScreen
{
    @Inject(method = { "initGui" }, at = { @At("HEAD") }, cancellable = true)
    private void onInitGui(final CallbackInfo ci) {
        Minecraft.func_71410_x().func_147108_a((GuiScreen)OyVey.customGuiScreen);
        ci.cancel();
    }
}
