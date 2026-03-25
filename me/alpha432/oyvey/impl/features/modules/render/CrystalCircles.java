//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import me.alpha432.oyvey.impl.events.*;
import java.util.*;
import org.lwjgl.opengl.*;
import me.alpha432.oyvey.api.util.render.*;

public class CrystalCircles extends Module
{
    private static CrystalCircles INSTANCE;
    private final Setting<Boolean> Rainbow;
    private final Setting<ColorSetting> color;
    public static HashMap<UUID, Thingering> thingers;
    
    public CrystalCircles() {
        super("Circles", "Draws circle when you are placing crystal", Module.Category.RENDER, true, false, false);
        this.Rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", (Object)true));
        this.color = (Setting<ColorSetting>)this.register(new Setting("Color", (Object)new ColorSetting(150, 0, 150, 255)));
        this.setInstance();
    }
    
    public static CrystalCircles getInstance() {
        if (CrystalCircles.INSTANCE == null) {
            CrystalCircles.INSTANCE = new CrystalCircles();
        }
        return CrystalCircles.INSTANCE;
    }
    
    private void setInstance() {
        CrystalCircles.INSTANCE = this;
    }
    
    public void onUpdate() {
        for (final Entity entity : CrystalCircles.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityEnderCrystal && !CrystalCircles.thingers.containsKey(entity.func_110124_au())) {
                CrystalCircles.thingers.put(entity.func_110124_au(), new Thingering(this, entity));
                CrystalCircles.thingers.get(entity.func_110124_au()).starTime = System.currentTimeMillis();
            }
        }
    }
    
    public void onRender3D(final Render3DEvent eventRender3D) {
        if (CrystalCircles.mc.field_71439_g != null && CrystalCircles.mc.field_71441_e != null) {
            for (final Map.Entry<UUID, Thingering> entry : CrystalCircles.thingers.entrySet()) {
                if (System.currentTimeMillis() - entry.getValue().starTime < 1500L) {
                    float opacity = Float.intBitsToFloat(Float.floatToIntBits(1.2886874E38f) ^ 0x7EC1E66F);
                    final long time = System.currentTimeMillis();
                    final long duration = time - entry.getValue().starTime;
                    if (duration < 1500L) {
                        opacity = Float.intBitsToFloat(Float.floatToIntBits(13.7902155f) ^ 0x7EDCA4B9) - duration / Float.intBitsToFloat(Float.floatToIntBits(6.1687006E-4f) ^ 0x7E9A3573);
                    }
                    drawCircle(entry.getValue().entity, eventRender3D.getPartialTicks(), Double.longBitsToDouble(Double.doubleToLongBits(205.3116845075892) ^ 0x7F89A9F951C9D87FL), (System.currentTimeMillis() - entry.getValue().starTime) / Float.intBitsToFloat(Float.floatToIntBits(0.025765074f) ^ 0x7E1B1147), opacity);
                }
            }
        }
    }
    
    public static void drawCircle(final Entity entity, final float partialTicks, final double rad, final float plusY, final float alpha) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        startSmooth();
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(Float.intBitsToFloat(Float.floatToIntBits(0.8191538f) ^ 0x7F11B410));
        GL11.glBegin(3);
        final double x = entity.field_70142_S + (entity.field_70165_t - entity.field_70142_S) * partialTicks - CrystalCircles.mc.func_175598_ae().field_78730_l;
        final double y = entity.field_70137_T + (entity.field_70163_u - entity.field_70137_T) * partialTicks - CrystalCircles.mc.func_175598_ae().field_78731_m;
        final double z = entity.field_70136_U + (entity.field_70161_v - entity.field_70136_U) * partialTicks - CrystalCircles.mc.func_175598_ae().field_78728_n;
        final double pix2 = Double.longBitsToDouble(Double.doubleToLongBits(0.12418750450734782) ^ 0x7FA6EB3BC22A7D2FL);
        for (int i = 0; i <= 90; ++i) {
            if (getInstance().Rainbow.getValue()) {
                GL11.glColor4f(ColorUtil.rainbow(50).getRed() / 255.0f, ColorUtil.rainbow(50).getGreen() / 255.0f, ColorUtil.rainbow(50).getBlue() / 255.0f, alpha);
            }
            else {
                final ColorSetting c = (ColorSetting)getInstance().color.getValue();
                GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, alpha);
            }
            GL11.glVertex3d(x + rad * Math.cos(i * Double.longBitsToDouble(Double.doubleToLongBits(0.038923223119235344) ^ 0x7FBACC45F0F011C7L) / Double.longBitsToDouble(Double.doubleToLongBits(0.010043755046771538) ^ 0x7FC211D1FBA3AC6BL)), y + plusY / Float.intBitsToFloat(Float.floatToIntBits(0.13022153f) ^ 0x7F2558CB), z + rad * Math.sin(i * Double.longBitsToDouble(Double.doubleToLongBits(0.012655047216797511) ^ 0x7F90CB18FB234FBFL) / Double.longBitsToDouble(Double.doubleToLongBits(0.00992417958121009) ^ 0x7FC2D320D5ED6BD3L)));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        endSmooth();
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
    
    public static void startSmooth() {
        GL11.glEnable(2848);
        GL11.glEnable(2881);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glHint(3152, 4354);
    }
    
    public static void endSmooth() {
        GL11.glDisable(2848);
        GL11.glDisable(2881);
        GL11.glEnable(3042);
    }
    
    static {
        CrystalCircles.INSTANCE = new CrystalCircles();
        CrystalCircles.thingers = new HashMap<UUID, Thingering>();
    }
    
    public class Thingering
    {
        public Entity entity;
        public long starTime;
        public CrystalCircles this$0;
        
        public Thingering(final CrystalCircles this$0x, final Entity entity) {
            this.this$0 = this$0x;
            this.entity = entity;
            this.starTime = 0L;
        }
    }
}
