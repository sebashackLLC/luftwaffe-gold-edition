//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import net.minecraft.item.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.init.*;
import java.util.function.*;
import net.minecraft.client.renderer.*;
import me.alpha432.oyvey.api.util.render.*;

public class TotemHUD extends Module
{
    private static TotemHUD INSTANCE;
    private static final ItemStack totem;
    
    public TotemHUD() {
        super("TotemHUD", "Show how much totems do you have", Module.Category.HUD, true, false, false);
        this.setInstance();
    }
    
    public static TotemHUD getInstance() {
        if (TotemHUD.INSTANCE == null) {
            TotemHUD.INSTANCE = new TotemHUD();
        }
        return TotemHUD.INSTANCE;
    }
    
    private void setInstance() {
        TotemHUD.INSTANCE = this;
    }
    
    public void onRender2D(final Render2DEvent event) {
        this.renderTotemHUD();
    }
    
    public void renderTotemHUD() {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        int totems = TotemHUD.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
        if (TotemHUD.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY) {
            totems += TotemHUD.mc.field_71439_g.func_184592_cb().func_190916_E();
        }
        if (totems > 0) {
            GlStateManager.func_179098_w();
            final int i = width / 2;
            final int iteration = 0;
            final int y = height - 55 - ((TotemHUD.mc.field_71439_g.func_70090_H() && TotemHUD.mc.field_71442_b.func_78763_f()) ? 10 : 0);
            final int x = i - 189 + 180 + 2;
            GlStateManager.func_179126_j();
            RenderUtil.itemRender.field_77023_b = 200.0f;
            RenderUtil.itemRender.func_180450_b(TotemHUD.totem, x, y);
            RenderUtil.itemRender.func_180453_a(TotemHUD.mc.field_71466_p, TotemHUD.totem, x, y, "");
            RenderUtil.itemRender.field_77023_b = 0.0f;
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179097_i();
            this.renderer.drawStringWithShadow(totems + "", (float)(x + 19 - 2 - this.renderer.getStringWidth(totems + "")), (float)(y + 9), 16777215);
            GlStateManager.func_179126_j();
            GlStateManager.func_179140_f();
        }
    }
    
    static {
        TotemHUD.INSTANCE = new TotemHUD();
        totem = new ItemStack(Items.field_190929_cY);
    }
}
