//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import net.minecraft.client.gui.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.alpha432.oyvey.*;
import org.lwjgl.opengl.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ GuiChat.class })
public class MixinGuiChat
{
    @Shadow
    protected GuiTextField field_146415_a;
    
    @Inject(method = { "drawScreen" }, at = { @At("RETURN") })
    private void drawScreen(final int p_drawScreen_1_, final int p_drawScreen_2_, final float p_drawScreen_3_, final CallbackInfo ci) {
        if (OyVey.commandManager != null && this.field_146415_a.func_146179_b().startsWith(OyVey.commandManager.getPrefix())) {
            final boolean a = GL11.glIsEnabled(3042);
            final boolean b = GL11.glIsEnabled(3553);
            GL11.glDisable(3042);
            GL11.glDisable(3553);
            final Color colorModule = Color.getInstance();
            if (colorModule != null) {
                final int color = colorModule.syncColor();
                final float red = (color >> 16 & 0xFF) / 255.0f;
                final float green = (color >> 8 & 0xFF) / 255.0f;
                final float blue = (color & 0xFF) / 255.0f;
                GL11.glColor3f(red, green, blue);
            }
            else if (this.field_146415_a.func_146179_b().startsWith("=")) {
                GL11.glColor3f(0.0f, 0.0f, 1.0f);
            }
            else {
                GL11.glColor3f(0.0f, 1.0f, 1.0f);
            }
            GL11.glBegin(1);
            GL11.glVertex2f((float)(this.field_146415_a.field_146209_f - 2), (float)(this.field_146415_a.field_146210_g - 2));
            GL11.glVertex2f((float)(this.field_146415_a.field_146209_f + this.field_146415_a.func_146200_o() - 2), (float)(this.field_146415_a.field_146210_g - 2));
            GL11.glVertex2f((float)(this.field_146415_a.field_146209_f + this.field_146415_a.func_146200_o() - 2), (float)(this.field_146415_a.field_146210_g - 2));
            GL11.glVertex2f((float)(this.field_146415_a.field_146209_f + this.field_146415_a.func_146200_o() - 2), (float)(this.field_146415_a.field_146210_g + this.field_146415_a.field_146219_i - 2));
            GL11.glVertex2f((float)(this.field_146415_a.field_146209_f + this.field_146415_a.func_146200_o() - 2), (float)(this.field_146415_a.field_146210_g + this.field_146415_a.field_146219_i - 2));
            GL11.glVertex2f((float)(this.field_146415_a.field_146209_f - 2), (float)(this.field_146415_a.field_146210_g + this.field_146415_a.field_146219_i - 2));
            GL11.glVertex2f((float)(this.field_146415_a.field_146209_f - 2), (float)(this.field_146415_a.field_146210_g + this.field_146415_a.field_146219_i - 2));
            GL11.glVertex2f((float)(this.field_146415_a.field_146209_f - 2), (float)(this.field_146415_a.field_146210_g - 2));
            GL11.glEnd();
            if (a) {
                GL11.glEnable(3042);
            }
            if (b) {
                GL11.glEnable(3553);
            }
        }
    }
}
