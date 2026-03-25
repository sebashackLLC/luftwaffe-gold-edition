//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.impl.events.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.util.math.*;
import java.awt.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.util.render.*;
import java.util.*;

public class PearlTrails extends Module
{
    public Setting<Boolean> sync;
    public Setting<Double> width;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> fadeDuration;
    public Setting<Integer> stayDuration;
    private final HashMap<Integer, EntityRenderVec> locs;
    private final HashMap<Integer, Long> landedTimes;
    
    public PearlTrails() {
        super("Trails", "Renders a line behind pearls", Module.Category.RENDER, true, false, false);
        this.sync = (Setting<Boolean>)this.register(new Setting("Sync", (Object)true));
        this.width = (Setting<Double>)this.register(new Setting("Width", (Object)1.5, (Object)0.1, (Object)3.0));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (Object)0, (Object)0, (Object)255, v -> !(boolean)this.sync.getValue()));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (Object)255, (Object)0, (Object)255, v -> !(boolean)this.sync.getValue()));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (Object)0, (Object)0, (Object)255, v -> !(boolean)this.sync.getValue()));
        this.fadeDuration = (Setting<Integer>)this.register(new Setting("FadeTime", (Object)1000, (Object)100, (Object)5000));
        this.stayDuration = (Setting<Integer>)this.register(new Setting("StayTime", (Object)1000, (Object)0, (Object)5000));
        this.locs = new HashMap<Integer, EntityRenderVec>();
        this.landedTimes = new HashMap<Integer, Long>();
    }
    
    public void onUpdate() {
        if (PearlTrails.mc.field_71439_g != null && PearlTrails.mc.field_71441_e != null) {
            this.attemptClear();
        }
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (PearlTrails.mc.field_71441_e != null && PearlTrails.mc.field_71439_g != null) {
            final long currentTime = System.currentTimeMillis();
            for (final Map.Entry<Integer, EntityRenderVec> entry : this.locs.entrySet()) {
                GL11.glBlendFunc(770, 771);
                GL11.glEnable(3042);
                GL11.glEnable(2848);
                GL11.glLineWidth(((Double)this.width.getValue()).floatValue());
                GL11.glDisable(3553);
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                GlStateManager.func_179103_j(7425);
                GL11.glLoadIdentity();
                PearlTrails.mc.field_71460_t.func_78473_a(event.getPartialTicks());
                GL11.glBegin(3);
                for (final ColorVector3d entityLineLoc : entry.getValue().vectors) {
                    final long timeSince = currentTime - entityLineLoc.timestamp;
                    if (timeSince <= (int)this.fadeDuration.getValue() + (int)this.stayDuration.getValue()) {
                        final float alpha = (timeSince < (int)this.stayDuration.getValue()) ? 1.0f : (1.0f - (timeSince - (int)this.stayDuration.getValue()) / (float)(int)this.fadeDuration.getValue());
                        final Color currentColor = this.getCurrentColor(entityLineLoc.color, alpha);
                        GL11.glColor4f(currentColor.getRed() / 255.0f, currentColor.getGreen() / 255.0f, currentColor.getBlue() / 255.0f, currentColor.getAlpha() / 255.0f);
                        final Vec3d pos = entityLineLoc.vector3d;
                        GL11.glVertex3d(pos.field_72450_a - PearlTrails.mc.func_175598_ae().field_78730_l, pos.field_72448_b - PearlTrails.mc.func_175598_ae().field_78731_m, pos.field_72449_c - PearlTrails.mc.func_175598_ae().field_78728_n);
                    }
                }
                GL11.glEnd();
                GL11.glDisable(2848);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glColor3f(1.0f, 1.0f, 1.0f);
                GlStateManager.func_179103_j(7424);
            }
            for (final Entity entity : PearlTrails.mc.field_71441_e.field_72996_f) {
                if (entity instanceof EntityEnderPearl) {
                    final EntityEnderPearl pearl = (EntityEnderPearl)entity;
                    final Color color = this.getCurrentColor();
                    final ColorVector3d firstPosition;
                    final EntityEnderPearl entityEnderPearl;
                    final Color color2;
                    final long timestamp;
                    final Object o;
                    this.locs.computeIfAbsent(Integer.valueOf(pearl.func_145782_y()), k -> {
                        // new(me.alpha432.oyvey.impl.features.modules.render.PearlTrails.EntityRenderVec.class)
                        new ColorVector3d(new Vec3d(entityEnderPearl.field_70165_t, entityEnderPearl.field_70163_u, entityEnderPearl.field_70161_v), color2, timestamp);
                        new EntityRenderVec(firstPosition, entityEnderPearl.func_145782_y());
                        return o;
                    }).vectors.add(new ColorVector3d(new Vec3d(pearl.field_70165_t, pearl.field_70163_u, pearl.field_70161_v), color, currentTime));
                }
            }
        }
    }
    
    private Color getCurrentColor(final Color baseColor, final float alpha) {
        return new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), (int)(alpha * 255.0f));
    }
    
    private Color getCurrentColor() {
        if (this.sync.getValue()) {
            final ColorSetting mainColor = (ColorSetting)me.alpha432.oyvey.impl.features.modules.client.Color.getInstance().mainColor.getValue();
            return me.alpha432.oyvey.impl.features.modules.client.Color.getInstance().rainbow.getValue() ? new Color(ColorUtil.toRGBA(ColorUtil.rainbow((int)me.alpha432.oyvey.impl.features.modules.client.Color.getInstance().rainbowHue.getValue()))) : new Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue());
        }
        return new Color((int)this.red.getValue(), (int)this.green.getValue(), (int)this.blue.getValue(), 255);
    }
    
    private void attemptClear() {
        final long currentTime = System.currentTimeMillis();
        try {
            final Iterator iterator = this.locs.entrySet().iterator();
            while (iterator.hasNext()) {
                final Map.Entry<Integer, EntityRenderVec> entry = iterator.next();
                final Entity entity = PearlTrails.mc.field_71441_e.func_73045_a((int)entry.getKey());
                if (entity == null) {
                    if (!this.landedTimes.containsKey(entry.getKey())) {
                        this.landedTimes.put(entry.getKey(), currentTime);
                    }
                    final long landedTime = this.landedTimes.get(entry.getKey());
                    if (currentTime - landedTime <= (int)this.stayDuration.getValue() + (int)this.fadeDuration.getValue()) {
                        continue;
                    }
                    iterator.remove();
                    this.landedTimes.remove(entry.getKey());
                }
                else {
                    this.landedTimes.remove(entry.getKey());
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public static class ColorVector3d
    {
        public Vec3d vector3d;
        public Color color;
        public long timestamp;
        
        public ColorVector3d(final Vec3d vector3d, final Color color, final long timestamp) {
            this.vector3d = vector3d;
            this.color = color;
            this.timestamp = timestamp;
        }
    }
    
    public static class EntityRenderVec
    {
        public ArrayList<ColorVector3d> vectors;
        public int entityId;
        
        public EntityRenderVec(final ColorVector3d firstPosition, final int entityId) {
            (this.vectors = new ArrayList<ColorVector3d>()).add(firstPosition);
            this.entityId = entityId;
        }
    }
}
