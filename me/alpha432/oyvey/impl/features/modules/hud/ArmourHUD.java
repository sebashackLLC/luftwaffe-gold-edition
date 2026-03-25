//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.item.*;
import net.minecraft.client.renderer.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.api.util.render.*;
import java.util.*;

public class ArmourHUD extends Module
{
    public ArmourHUD() {
        super("ArmourHUD", "Shows your armour durability", Module.Category.HUD, true, false, false);
    }
    
    public void onRender2D(final Render2DEvent event) {
        if (ArmourHUD.mc.field_71439_g == null) {
            return;
        }
        final int width = event.scaledResolution.func_78326_a();
        final int height = event.scaledResolution.func_78328_b();
        GlStateManager.func_179098_w();
        final int i = width / 2;
        int iteration = 0;
        final int y = height - 55 - ((ArmourHUD.mc.field_71439_g.func_70090_H() && ArmourHUD.mc.field_71442_b.func_78763_f()) ? 10 : 0);
        for (final ItemStack is : ArmourHUD.mc.field_71439_g.field_71071_by.field_70460_b) {
            ++iteration;
            if (is.func_190926_b()) {
                continue;
            }
            final int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.func_179126_j();
            RenderHelper.func_74520_c();
            ArmourHUD.mc.func_175599_af().field_77023_b = 200.0f;
            ArmourHUD.mc.func_175599_af().func_180450_b(is, x, y);
            ArmourHUD.mc.func_175599_af().func_180453_a(ArmourHUD.mc.field_71466_p, is, x, y, "");
            ArmourHUD.mc.func_175599_af().field_77023_b = 0.0f;
            RenderHelper.func_74518_a();
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179097_i();
            final String s = (is.func_190916_E() > 1) ? (is.func_190916_E() + "") : "";
            OyVey.textManager.drawString(s, (float)(x + 19 - 2 - OyVey.textManager.getStringWidth(s)), (float)(y + 9), 16777215, true);
            final float green = (is.func_77958_k() - (float)is.func_77952_i()) / is.func_77958_k();
            final float red = 1.0f - green;
            final int dmg = 100 - (int)(red * 100.0f);
            OyVey.textManager.drawString(dmg + "", (float)(x + 8 - OyVey.textManager.getStringWidth(dmg + "") / 2), (float)(y - 11), ColorUtil.toRGBA((int)(red * 255.0f), (int)(green * 255.0f), 0), true);
        }
    }
}
