//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.api.features.settings.*;
import com.google.common.collect.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import java.util.*;
import net.minecraft.entity.*;
import java.awt.*;
import net.minecraft.util.math.*;
import java.math.*;

public class DamageNumbers extends Module
{
    private final Map<Integer, Float> hpData;
    private final List<Particle> particles;
    private final Timer timer;
    private final Setting<Integer> deleteAfter;
    private final Setting<Boolean> showHealing;
    
    public DamageNumbers() {
        super("DamageNumbers", "Shows floating damage numbers on entities", Module.Category.RENDER, false, false, false);
        this.hpData = (Map<Integer, Float>)Maps.newHashMap();
        this.particles = (List<Particle>)Lists.newArrayList();
        this.timer = new Timer();
        this.deleteAfter = (Setting<Integer>)this.register(new Setting("RemoveTicks", (Object)7, (Object)1, (Object)60));
        this.showHealing = (Setting<Boolean>)this.register(new Setting("ShowHealing", (Object)true));
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (DamageNumbers.mc.field_71441_e == null || this.particles.isEmpty()) {
            return;
        }
        for (final Particle particle : this.particles) {
            if (particle != null) {
                if (particle.ticks > (int)this.deleteAfter.getValue()) {
                    continue;
                }
                final double x = particle.posX - DamageNumbers.mc.func_175598_ae().field_78730_l;
                final double y = particle.posY - DamageNumbers.mc.func_175598_ae().field_78731_m;
                final double z = particle.posZ - DamageNumbers.mc.func_175598_ae().field_78728_n;
                GlStateManager.func_179094_E();
                GlStateManager.func_179097_i();
                GlStateManager.func_179147_l();
                GlStateManager.func_179140_f();
                GlStateManager.func_179090_x();
                GlStateManager.func_179120_a(770, 771, 1, 0);
                GlStateManager.func_179137_b(x, y, z);
                GlStateManager.func_187432_a(0.0f, 1.0f, 0.0f);
                GlStateManager.func_179114_b(-DamageNumbers.mc.func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
                final float viewX = (DamageNumbers.mc.field_71474_y.field_74320_O == 2) ? -1.0f : 1.0f;
                GlStateManager.func_179114_b(DamageNumbers.mc.func_175598_ae().field_78732_j, viewX, 0.0f, 0.0f);
                GlStateManager.func_179139_a(-0.03, -0.03, 0.03);
                GlStateManager.func_179098_w();
                GL11.glDepthMask(false);
                final String text = particle.str;
                final int textWidth = DamageNumbers.mc.field_71466_p.func_78256_a(text);
                DamageNumbers.mc.field_71466_p.func_175063_a(text, -textWidth / 2.0f, -DamageNumbers.mc.field_71466_p.field_78288_b / 2.0f, particle.color.getRGB());
                GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                GL11.glDepthMask(true);
                GlStateManager.func_179126_j();
                GlStateManager.func_179084_k();
                GlStateManager.func_179121_F();
            }
        }
    }
    
    public void onDisable() {
        this.particles.clear();
        this.hpData.clear();
    }
    
    public void onUpdate() {
        if (DamageNumbers.mc.field_71441_e == null) {
            return;
        }
        if (this.timer.passedMs(12000L)) {
            this.particles.clear();
            this.timer.reset();
        }
        for (final Particle particle : this.particles) {
            if (particle != null) {
                final Particle particle2 = particle;
                ++particle2.ticks;
                final Particle particle3 = particle;
                particle3.posY += 0.02;
            }
        }
        this.particles.removeIf(p -> p != null && p.ticks > (int)this.deleteAfter.getValue());
        for (final Entity entity : DamageNumbers.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityLivingBase) {
                final EntityLivingBase living = (EntityLivingBase)entity;
                final int entityId = living.func_145782_y();
                final float previousHp = this.hpData.getOrDefault(entityId, living.func_110138_aP());
                final float currentHp = living.func_110143_aJ();
                this.hpData.put(entityId, currentHp);
                if (previousHp == currentHp) {
                    continue;
                }
                final double damage = previousHp - currentHp;
                if (damage < 0.0 && !(boolean)this.showHealing.getValue()) {
                    continue;
                }
                final Color color = (damage > 0.0) ? Color.RED : Color.GREEN;
                final double offsetX = (Math.random() - 0.5) * 0.5;
                final double offsetZ = (Math.random() - 0.5) * 0.5;
                final Vec3d pos = new Vec3d(entity.field_70165_t + offsetX, entity.func_174813_aQ().field_72338_b + (entity.func_174813_aQ().field_72337_e - entity.func_174813_aQ().field_72338_b) * 0.5 + 0.5, entity.field_70161_v + offsetZ);
                final double displayDamage = new BigDecimal(Math.abs(damage)).setScale(1, RoundingMode.HALF_UP).doubleValue();
                final String prefix = (damage > 0.0) ? "-" : "+";
                this.particles.add(new Particle(prefix + displayDamage, pos.field_72450_a, pos.field_72448_b, pos.field_72449_c, color));
            }
        }
    }
    
    private static class Particle
    {
        public double posX;
        public double posY;
        public double posZ;
        public Color color;
        public String str;
        public int ticks;
        
        public Particle(final String str, final double x, final double y, final double z, final Color color) {
            this.str = str;
            this.posX = x;
            this.posY = y;
            this.posZ = z;
            this.color = color;
            this.ticks = 0;
        }
    }
}
