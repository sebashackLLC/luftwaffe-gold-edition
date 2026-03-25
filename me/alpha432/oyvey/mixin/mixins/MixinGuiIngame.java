//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.alpha432.oyvey.impl.gui.background.*;
import java.lang.reflect.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.client.gui.*;
import me.alpha432.oyvey.impl.features.modules.render.*;
import me.alpha432.oyvey.*;

@Mixin({ GuiIngame.class })
public class MixinGuiIngame extends Gui
{
    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    public void init(final Minecraft mcIn, final CallbackInfo ci) {
        try {
            final String[] array;
            final String[] possibleNames = array = new String[] { "persistentChatGUI", "persistantChatGUI", "chatGUI", "field_73840_e" };
            final int length = array.length;
            int i = 0;
            while (i < length) {
                final String fieldName = array[i];
                try {
                    final Field chatField = GuiIngame.class.getDeclaredField(fieldName);
                    chatField.setAccessible(true);
                    chatField.set(this, new GuiCustomNewChat(mcIn));
                    return;
                }
                catch (NoSuchFieldException ex) {
                    ++i;
                    continue;
                }
                break;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Inject(method = { "renderAttackIndicator" }, at = { @At("HEAD") }, cancellable = true)
    public void renderAttackIndicatorHook(final float partialTicks, final ScaledResolution resolution, final CallbackInfo ci) {
        final Crosshair crosshair = (Crosshair)OyVey.moduleManager.getModuleByClass((Class)Crosshair.class);
        if (crosshair != null && crosshair.isOn()) {
            ci.cancel();
        }
    }
}
