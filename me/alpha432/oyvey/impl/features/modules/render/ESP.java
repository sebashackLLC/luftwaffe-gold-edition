//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.util.math.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import java.awt.*;
import me.alpha432.oyvey.api.util.render.*;
import net.minecraft.entity.item.*;
import java.util.*;

public class ESP extends Module
{
    private static ESP INSTANCE;
    private final Setting<Boolean> items;
    private final Setting<Boolean> xporbs;
    private final Setting<Boolean> xpbottles;
    private final Setting<Boolean> pearl;
    public Setting<Boolean> players;
    public Setting<Boolean> crystals;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> boxAlpha;
    public Setting<Integer> alpha;
    public Setting<Boolean> interpolate;
    
    public ESP() {
        super("ESP", "Renders a nice ESP.", Module.Category.RENDER, false, false, false);
        this.items = (Setting<Boolean>)this.register(new Setting("Items", (Object)false));
        this.xporbs = (Setting<Boolean>)this.register(new Setting("XpOrbs", (Object)false));
        this.xpbottles = (Setting<Boolean>)this.register(new Setting("XpBottles", (Object)false));
        this.pearl = (Setting<Boolean>)this.register(new Setting("Pearls", (Object)false));
        this.players = (Setting<Boolean>)this.register(new Setting("Players", (Object)false));
        this.crystals = (Setting<Boolean>)this.register(new Setting("Crystals", (Object)false));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (Object)255, (Object)0, (Object)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (Object)255, (Object)0, (Object)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (Object)255, (Object)0, (Object)255));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (Object)120, (Object)0, (Object)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (Object)255, (Object)0, (Object)255));
        this.interpolate = (Setting<Boolean>)this.register(new Setting("Interpolate", (Object)true));
        this.setInstance();
    }
    
    public static ESP getInstance() {
        if (ESP.INSTANCE == null) {
            ESP.INSTANCE = new ESP();
        }
        return ESP.INSTANCE;
    }
    
    private void setInstance() {
        ESP.INSTANCE = this;
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (this.items.getValue()) {
            int i = 0;
            for (final Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (entity instanceof EntityItem && ESP.mc.field_71439_g.func_70068_e(entity) < 2500.0) {
                    Vec3d interp;
                    if (this.interpolate.getValue()) {
                        interp = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.func_184121_ak());
                    }
                    else {
                        interp = new Vec3d(entity.field_70165_t - ESP.mc.func_175598_ae().field_78730_l, entity.field_70163_u - ESP.mc.func_175598_ae().field_78731_m, entity.field_70161_v - ESP.mc.func_175598_ae().field_78728_n);
                    }
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179147_l();
                    GlStateManager.func_179097_i();
                    GlStateManager.func_179120_a(770, 771, 0, 1);
                    GlStateManager.func_179090_x();
                    GlStateManager.func_179132_a(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.func_189696_b(bb, (int)this.red.getValue() / 255.0f, (int)this.green.getValue() / 255.0f, (int)this.blue.getValue() / 255.0f, (int)this.boxAlpha.getValue() / 255.0f);
                    GL11.glDisable(2848);
                    GlStateManager.func_179132_a(true);
                    GlStateManager.func_179098_w();
                    GlStateManager.func_179126_j();
                    GlStateManager.func_179084_k();
                    GlStateManager.func_179121_F();
                    RenderUtil.drawBlockOutline(bb, new Color((int)this.red.getValue(), (int)this.green.getValue(), (int)this.blue.getValue(), (int)this.alpha.getValue()), 1.0f);
                    if (++i >= 50) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (this.xporbs.getValue()) {
            int i = 0;
            for (final Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (entity instanceof EntityXPOrb && ESP.mc.field_71439_g.func_70068_e(entity) < 2500.0) {
                    Vec3d interp;
                    if (this.interpolate.getValue()) {
                        interp = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.func_184121_ak());
                    }
                    else {
                        interp = new Vec3d(entity.field_70165_t - ESP.mc.func_175598_ae().field_78730_l, entity.field_70163_u - ESP.mc.func_175598_ae().field_78731_m, entity.field_70161_v - ESP.mc.func_175598_ae().field_78728_n);
                    }
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179147_l();
                    GlStateManager.func_179097_i();
                    GlStateManager.func_179120_a(770, 771, 0, 1);
                    GlStateManager.func_179090_x();
                    GlStateManager.func_179132_a(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.func_189696_b(bb, (int)this.red.getValue() / 255.0f, (int)this.green.getValue() / 255.0f, (int)this.blue.getValue() / 255.0f, (int)this.boxAlpha.getValue() / 255.0f);
                    GL11.glDisable(2848);
                    GlStateManager.func_179132_a(true);
                    GlStateManager.func_179098_w();
                    GlStateManager.func_179126_j();
                    GlStateManager.func_179084_k();
                    GlStateManager.func_179121_F();
                    RenderUtil.drawBlockOutline(bb, new Color((int)this.red.getValue(), (int)this.green.getValue(), (int)this.blue.getValue(), (int)this.alpha.getValue()), 1.0f);
                    if (++i >= 50) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (this.pearl.getValue()) {
            int i = 0;
            for (final Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (entity instanceof EntityEnderPearl && ESP.mc.field_71439_g.func_70068_e(entity) < 2500.0) {
                    Vec3d interp;
                    if (this.interpolate.getValue()) {
                        interp = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.func_184121_ak());
                    }
                    else {
                        interp = new Vec3d(entity.field_70165_t - ESP.mc.func_175598_ae().field_78730_l, entity.field_70163_u - ESP.mc.func_175598_ae().field_78731_m, entity.field_70161_v - ESP.mc.func_175598_ae().field_78728_n);
                    }
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179147_l();
                    GlStateManager.func_179097_i();
                    GlStateManager.func_179120_a(770, 771, 0, 1);
                    GlStateManager.func_179090_x();
                    GlStateManager.func_179132_a(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.func_189696_b(bb, (int)this.red.getValue() / 255.0f, (int)this.green.getValue() / 255.0f, (int)this.blue.getValue() / 255.0f, (int)this.boxAlpha.getValue() / 255.0f);
                    GL11.glDisable(2848);
                    GlStateManager.func_179132_a(true);
                    GlStateManager.func_179098_w();
                    GlStateManager.func_179126_j();
                    GlStateManager.func_179084_k();
                    GlStateManager.func_179121_F();
                    RenderUtil.drawBlockOutline(bb, new Color((int)this.red.getValue(), (int)this.green.getValue(), (int)this.blue.getValue(), (int)this.alpha.getValue()), 1.0f);
                    if (++i >= 50) {
                        break;
                    }
                    continue;
                }
            }
        }
        if (this.xpbottles.getValue()) {
            int i = 0;
            for (final Entity entity : ESP.mc.field_71441_e.field_72996_f) {
                if (entity instanceof EntityExpBottle && ESP.mc.field_71439_g.func_70068_e(entity) < 2500.0) {
                    Vec3d interp;
                    if (this.interpolate.getValue()) {
                        interp = EntityUtil.getInterpolatedRenderPos(entity, ESP.mc.func_184121_ak());
                    }
                    else {
                        interp = new Vec3d(entity.field_70165_t - ESP.mc.func_175598_ae().field_78730_l, entity.field_70163_u - ESP.mc.func_175598_ae().field_78731_m, entity.field_70161_v - ESP.mc.func_175598_ae().field_78728_n);
                    }
                    final AxisAlignedBB bb = new AxisAlignedBB(entity.func_174813_aQ().field_72340_a - 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72338_b - 0.0 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72339_c - 0.05 - entity.field_70161_v + interp.field_72449_c, entity.func_174813_aQ().field_72336_d + 0.05 - entity.field_70165_t + interp.field_72450_a, entity.func_174813_aQ().field_72337_e + 0.1 - entity.field_70163_u + interp.field_72448_b, entity.func_174813_aQ().field_72334_f + 0.05 - entity.field_70161_v + interp.field_72449_c);
                    GlStateManager.func_179094_E();
                    GlStateManager.func_179147_l();
                    GlStateManager.func_179097_i();
                    GlStateManager.func_179120_a(770, 771, 0, 1);
                    GlStateManager.func_179090_x();
                    GlStateManager.func_179132_a(false);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    GL11.glLineWidth(1.0f);
                    RenderGlobal.func_189696_b(bb, (int)this.red.getValue() / 255.0f, (int)this.green.getValue() / 255.0f, (int)this.blue.getValue() / 255.0f, (int)this.boxAlpha.getValue() / 255.0f);
                    GL11.glDisable(2848);
                    GlStateManager.func_179132_a(true);
                    GlStateManager.func_179098_w();
                    GlStateManager.func_179126_j();
                    GlStateManager.func_179084_k();
                    GlStateManager.func_179121_F();
                    RenderUtil.drawBlockOutline(bb, new Color((int)this.red.getValue(), (int)this.green.getValue(), (int)this.blue.getValue(), (int)this.alpha.getValue()), 1.0f);
                    if (++i >= 50) {
                        break;
                    }
                    continue;
                }
            }
        }
    }
    
    public int getColor() {
        return new Color((int)this.red.getValue(), (int)this.green.getValue(), (int)this.blue.getValue()).getRGB();
    }
    
    static {
        ESP.INSTANCE = new ESP();
    }
}
