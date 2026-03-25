//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.render;

import me.alpha432.oyvey.api.util.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;

public class NametagsUtil implements Util
{
    public static double timerPos(final double n, final double n2) {
        return n2 + (n - n2) * NametagsUtil.mc.func_184121_ak();
    }
    
    public static Vec3d renderPosEntity(final Entity entity) {
        return new Vec3d(timerPos(entity.field_70165_t, entity.field_70142_S), timerPos(entity.field_70163_u, entity.field_70137_T), timerPos(entity.field_70161_v, entity.field_70136_U));
    }
    
    private String stringForEnchants(final Enchantment enchantment, final int n) {
        final ResourceLocation resourceLocation = (ResourceLocation)Enchantment.field_185264_b.func_177774_c((Object)enchantment);
        String substring = (resourceLocation == null) ? enchantment.func_77320_a() : resourceLocation.toString();
        final int n3;
        final int n2 = n3 = ((n > 1) ? 12 : 13);
        if (substring.length() > n2) {
            substring = substring.substring(10, n2);
        }
        final StringBuilder sb = new StringBuilder();
        final String s = substring;
        final boolean n4 = false;
        String s2 = sb.insert(0, s.substring(0, 1).toUpperCase()).append(substring.substring(1)).toString();
        if (n > 1) {
            s2 = new StringBuilder().insert(0, s2).append(n).toString();
        }
        return s2;
    }
    
    public static void drawNametag(final double x, final double y, final double z, final int type, final EntityPlayer player) {
        final double dist = NametagsUtil.mc.field_71439_g.func_70011_f(x, y, z);
        double scale = 1.0;
        double offset = 0.0;
        int start = 0;
        switch (type) {
            case 0: {
                scale = dist / 20.0 * Math.pow(1.2589254, 0.1 / ((dist < 25.0) ? 0.5 : 2.0));
                scale = Math.min(Math.max(scale, 0.5), 5.0);
                offset = ((scale > 2.0) ? (scale / 2.0) : scale);
                scale /= 40.0;
                start = 10;
                break;
            }
            case 1: {
                scale = -(int)dist / 6.0;
                if (scale < 1.0) {
                    scale = 1.0;
                }
                scale *= 0.02666666666666667;
                break;
            }
            case 2: {
                scale = 0.0018 + 0.003 * dist;
                if (dist <= 8.0) {
                    scale = 0.0245;
                }
                start = -8;
                break;
            }
        }
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b(x - NametagsUtil.mc.func_175598_ae().field_78730_l, y + offset - NametagsUtil.mc.func_175598_ae().field_78731_m, z - NametagsUtil.mc.func_175598_ae().field_78728_n);
        GlStateManager.func_179114_b(-NametagsUtil.mc.func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
        GlStateManager.func_179114_b(NametagsUtil.mc.func_175598_ae().field_78732_j, (NametagsUtil.mc.field_71474_y.field_74320_O == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.func_179139_a(-scale, -scale, scale);
        GlStateManager.func_179098_w();
        final float width = 0.0f;
        final float offsetxd = 0.0f;
        GlStateManager.func_179090_x();
        if (type != 2) {
            GlStateManager.func_179121_F();
        }
    }
    
    public static void drawLog(final double x, final double y, final double z, final int type, final EntityPlayer player) {
        final double dist = NametagsUtil.mc.field_71439_g.func_70011_f(x, y, z);
        double scale = 1.0;
        double offset = 0.0;
        int start = 0;
        switch (type) {
            case 0: {
                scale = dist / 20.0 * Math.pow(1.2589254, 0.1 / ((dist < 25.0) ? 0.5 : 2.0));
                scale = Math.min(Math.max(scale, 0.5), 5.0);
                offset = ((scale > 2.0) ? (scale / 2.0) : scale);
                scale /= 40.0;
                start = 10;
                break;
            }
            case 1: {
                scale = -(int)dist / 6.0;
                if (scale < 1.0) {
                    scale = 1.0;
                }
                scale *= 0.02666666666666667;
                break;
            }
            case 2: {
                scale = 0.0018 + 0.003 * dist;
                if (dist <= 8.0) {
                    scale = 0.0245;
                }
                start = -8;
                break;
            }
        }
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b(x - NametagsUtil.mc.func_175598_ae().field_78730_l, y + offset - NametagsUtil.mc.func_175598_ae().field_78731_m, z - NametagsUtil.mc.func_175598_ae().field_78728_n);
        GlStateManager.func_179114_b(-NametagsUtil.mc.func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
        GlStateManager.func_179114_b(NametagsUtil.mc.func_175598_ae().field_78732_j, (NametagsUtil.mc.field_71474_y.field_74320_O == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.func_179139_a(-scale, -scale, scale);
        if (type == 2) {
            double width = 0.0;
            width /= 2.0;
        }
        GlStateManager.func_179098_w();
        final float width2 = 0.0f;
        final float offsetxd = 0.0f;
        GlStateManager.func_179090_x();
        if (type != 2) {
            GlStateManager.func_179121_F();
        }
    }
    
    public static void drawWaypoint(final double x, final double y, final double z, final int type) {
        final double dist = NametagsUtil.mc.field_71439_g.func_70011_f(x, y, z);
        double scale = 1.0;
        double offset = 0.0;
        int start = 0;
        switch (type) {
            case 0: {
                scale = dist / 20.0 * Math.pow(1.2589254, 0.1 / ((dist < 25.0) ? 0.5 : 2.0));
                scale = Math.min(Math.max(scale, 0.5), 5.0);
                offset = ((scale > 2.0) ? (scale / 2.0) : scale);
                scale /= 40.0;
                start = 10;
                break;
            }
            case 1: {
                scale = -(int)dist / 6.0;
                if (scale < 1.0) {
                    scale = 1.0;
                }
                scale *= 0.02666666666666667;
                break;
            }
            case 2: {
                scale = 0.0018 + 0.003 * dist;
                if (dist <= 8.0) {
                    scale = 0.0245;
                }
                start = -8;
                break;
            }
        }
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b(x - NametagsUtil.mc.func_175598_ae().field_78730_l, y + offset - NametagsUtil.mc.func_175598_ae().field_78731_m, z - NametagsUtil.mc.func_175598_ae().field_78728_n);
        GlStateManager.func_179114_b(-NametagsUtil.mc.func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
        GlStateManager.func_179114_b(NametagsUtil.mc.func_175598_ae().field_78732_j, (NametagsUtil.mc.field_71474_y.field_74320_O == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.func_179139_a(-scale, -scale, scale);
        GlStateManager.func_179098_w();
        final float width = 0.0f;
        final float offsetxd = 0.0f;
        GlStateManager.func_179090_x();
        if (type != 2) {
            GlStateManager.func_179121_F();
        }
    }
    
    public static void drawNametagAny(final double x, final double y, final double z, final int type) {
        final double dist = NametagsUtil.mc.field_71439_g.func_70011_f(x, y, z);
        double scale = 1.0;
        double offset = 0.0;
        int start = 0;
        switch (type) {
            case 0: {
                scale = dist / 20.0 * Math.pow(1.2589254, 0.1 / ((dist < 25.0) ? 0.5 : 2.0));
                scale = Math.min(Math.max(scale, 0.5), 5.0);
                offset = ((scale > 2.0) ? (scale / 2.0) : scale);
                scale /= 40.0;
                start = 10;
                break;
            }
            case 1: {
                scale = -(int)dist / 6.0;
                if (scale < 1.0) {
                    scale = 1.0;
                }
                scale *= 0.02666666666666667;
                break;
            }
            case 2: {
                scale = 0.0018 + 0.003 * dist;
                if (dist <= 8.0) {
                    scale = 0.0245;
                }
                start = -8;
                break;
            }
        }
        GlStateManager.func_179094_E();
        GlStateManager.func_179137_b(x - NametagsUtil.mc.func_175598_ae().field_78730_l, y + offset - NametagsUtil.mc.func_175598_ae().field_78731_m, z - NametagsUtil.mc.func_175598_ae().field_78728_n);
        GlStateManager.func_179114_b(-NametagsUtil.mc.func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
        GlStateManager.func_179114_b(NametagsUtil.mc.func_175598_ae().field_78732_j, (NametagsUtil.mc.field_71474_y.field_74320_O == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.func_179139_a(-scale, -scale, scale);
        GlStateManager.func_179098_w();
        final float width = 0.0f;
        final float offsetxd = 0.0f;
        GlStateManager.func_179090_x();
    }
    
    public static void glColor(final Color nameColor) {
        GL11.glColor4f(nameColor.getRed() / 255.0f, nameColor.getGreen() / 255.0f, nameColor.getBlue() / 255.0f, nameColor.getAlpha() / 255.0f);
    }
    
    public static void drawBorderedRectReliant(final float nameFloat1, final float nameFloat2, final float nameFloat3, final float nameFloat4, final float nameFloat5, final Color nameInt1, final Color nameInt2) {
        prepare();
        drawRectTest(nameFloat1, nameFloat2, nameFloat3, nameFloat4, nameInt1);
        glColor(nameInt2);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(nameFloat5);
        GL11.glBegin(3);
        GL11.glVertex2f(nameFloat1, nameFloat2);
        GL11.glVertex2f(nameFloat1, nameFloat4);
        GL11.glVertex2f(nameFloat3, nameFloat4);
        GL11.glVertex2f(nameFloat3, nameFloat2);
        GL11.glVertex2f(nameFloat1, nameFloat2);
        GL11.glEnd();
    }
    
    public static void drawRectTest(final float nameFloat1, final float nameFloat2, final float nameFloat3, final float nameFloat4, final Color nameInt) {
        glColor(nameInt);
        drawRectTest(nameFloat1, nameFloat2, nameFloat3, nameFloat4);
    }
    
    public static void drawRectTest(final float nameFloat1, final float nameFloat2, final float nameFloat3, final float nameFloat4) {
        GL11.glBegin(7);
        GL11.glVertex2f(nameFloat1, nameFloat4);
        GL11.glVertex2f(nameFloat3, nameFloat4);
        GL11.glVertex2f(nameFloat3, nameFloat2);
        GL11.glVertex2f(nameFloat1, nameFloat2);
        GL11.glEnd();
    }
    
    public static void drawBorderedRect(final double x, final double y, final double x1, final double y1, final float lineWidth, final Color inside, final Color border) {
        prepare();
        final Tessellator tessellator = Tessellator.func_178181_a();
        final BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179131_c(inside.getRed() / 255.0f, inside.getGreen() / 255.0f, inside.getBlue() / 255.0f, inside.getAlpha() / 255.0f);
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(x, y1, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(x1, y1, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(x1, y, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(x, y, 0.0).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179131_c(border.getRed() / 255.0f, border.getGreen() / 255.0f, border.getBlue() / 255.0f, border.getAlpha() / 255.0f);
        GlStateManager.func_187441_d(lineWidth);
        bufferbuilder.func_181668_a(3, DefaultVertexFormats.field_181705_e);
        bufferbuilder.func_181662_b(x, y, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(x, y1, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(x1, y1, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(x1, y, 0.0).func_181675_d();
        bufferbuilder.func_181662_b(x, y, 0.0).func_181675_d();
        tessellator.func_78381_a();
    }
    
    public static void prepare() {
        GL11.glHint(3154, 4354);
        GlStateManager.func_179120_a(770, 771, 0, 1);
        GlStateManager.func_179103_j(7425);
        GlStateManager.func_179132_a(false);
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179090_x();
        GlStateManager.func_179140_f();
        GlStateManager.func_179129_p();
        GlStateManager.func_179141_d();
        GL11.glEnable(2848);
        GL11.glEnable(34383);
    }
    
    public static void release() {
        GL11.glDisable(34383);
        GL11.glDisable(2848);
        GlStateManager.func_179141_d();
        GlStateManager.func_179089_o();
        GlStateManager.func_179098_w();
        GlStateManager.func_179126_j();
        GlStateManager.func_179084_k();
        GlStateManager.func_179132_a(true);
        GlStateManager.func_187441_d(1.0f);
        GlStateManager.func_179103_j(7424);
        GL11.glHint(3154, 4352);
    }
    
    public static void prepareTags() {
        GL11.glHint(3154, 4354);
        GlStateManager.func_179120_a(770, 771, 0, 1);
        GlStateManager.func_179103_j(7425);
        GlStateManager.func_179132_a(false);
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179090_x();
        GlStateManager.func_179140_f();
        GlStateManager.func_179129_p();
        GlStateManager.func_179141_d();
        GL11.glEnable(2848);
        GL11.glEnable(34383);
    }
    
    public static void enableGL2D() {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void disableGL2D() {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
}
