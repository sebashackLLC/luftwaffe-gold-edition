//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.render;

import me.alpha432.oyvey.api.util.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import java.awt.*;

public class Render2DMethods implements Util
{
    private static final Tessellator tessellator;
    
    public static void drawNameTagRect(final float left, final float top, final float right, final float bottom, final int color, final int border, final float width) {
        quickDrawRect(left, top, right, bottom, color, false);
        GL11.glLineWidth(width);
        quickDrawRect(left, top, right, bottom, border, true);
    }
    
    public static void quickDrawRect(final float x, final float y, final float x2, final float y2, final int color, final boolean line) {
        final float a = (color >> 24 & 0xFF) / 255.0f;
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.func_178181_a();
        final BufferBuilder bufferbuilder = tessellator.func_178180_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        bufferbuilder.func_181668_a(line ? 2 : 7, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b((double)x, (double)y2, 0.0).func_181666_a(r, g, b, a).func_181675_d();
        bufferbuilder.func_181662_b((double)x2, (double)y2, 0.0).func_181666_a(r, g, b, a).func_181675_d();
        bufferbuilder.func_181662_b((double)x2, (double)y, 0.0).func_181666_a(r, g, b, a).func_181675_d();
        bufferbuilder.func_181662_b((double)x, (double)y, 0.0).func_181666_a(r, g, b, a).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
    }
    
    public static void drawRect(final Rectangle rectangle, final int color) {
        drawRect((float)rectangle.x, (float)rectangle.y, (float)(rectangle.x + rectangle.width), (float)(rectangle.y + rectangle.height), color);
    }
    
    public static void drawRect(final float x, final float y, final float x1, final float y1, final int color, final float lineWidth) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GL11.glLineWidth(lineWidth);
        final BufferBuilder builder = Render2DMethods.tessellator.func_178180_c();
        builder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        builder.func_181662_b((double)x, (double)y1, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b((double)x1, (double)y1, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b((double)x1, (double)y, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b((double)x, (double)y, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        Render2DMethods.tessellator.func_78381_a();
        GL11.glLineWidth(1.0f);
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
    }
    
    public static void drawRect(final float x, final float y, final float x1, final float y1, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        final BufferBuilder builder = Render2DMethods.tessellator.func_178180_c();
        builder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        builder.func_181662_b((double)x, (double)y1, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b((double)x1, (double)y1, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b((double)x1, (double)y, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        builder.func_181662_b((double)x, (double)y, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        Render2DMethods.tessellator.func_78381_a();
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
    }
    
    public static void drawRect(final float x, final float y, final float x1, final float y1) {
        final BufferBuilder builder = Render2DMethods.tessellator.func_178180_c();
        builder.func_181668_a(7, DefaultVertexFormats.field_181705_e);
        builder.func_181662_b((double)x, (double)y1, 0.0).func_181675_d();
        builder.func_181662_b((double)x1, (double)y1, 0.0).func_181675_d();
        builder.func_181662_b((double)x1, (double)y, 0.0).func_181675_d();
        builder.func_181662_b((double)x, (double)y, 0.0).func_181675_d();
        Render2DMethods.tessellator.func_78381_a();
    }
    
    public static void drawOutline(final float x, final float y, final float width, final float height, final float lineWidth, final int color) {
        drawRect(x + lineWidth, y, x - lineWidth, y + lineWidth, color);
        drawRect(x + lineWidth, y, width - lineWidth, y + lineWidth, color);
        drawRect(x, y, x + lineWidth, height, color);
        drawRect(width - lineWidth, y, width, height, color);
        drawRect(x + lineWidth, height - lineWidth, width - lineWidth, height, color);
    }
    
    public static void drawBorderedRect(float x, float y, float x1, float y1, final int insideC, final int borderC) {
        enableGL2D();
        x *= 2.0f;
        x1 *= 2.0f;
        y *= 2.0f;
        y1 *= 2.0f;
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawVLine(x, y, y1 - 1.0f, borderC);
        drawVLine(x1 - 1.0f, y, y1, borderC);
        drawHLine(x, x1 - 1.0f, y, borderC);
        drawHLine(x, x1 - 2.0f, y1 - 1.0f, borderC);
        drawRect(x + 1.0f, y + 1.0f, x1 - 1.0f, y1 - 1.0f, insideC);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        disableGL2D();
    }
    
    public static void disableGL2D(final boolean ignored) {
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3152, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void enableGL2D(final boolean ignored) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
    }
    
    public static void drawRect(final float x, final float y, final float x1, final float y1, final int color, final int ignored) {
        enableGL2D(false);
        glColor(color);
        GL11.glBegin(7);
        GL11.glVertex2f(x, y1);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x1, y);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
        disableGL2D(false);
    }
    
    public static void enableGL2D() {
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }
    
    public static void disableGL2D() {
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
    }
    
    public static void drawGradientRect(final float left, final float top, final float right, final float bottom, final boolean sideways, final int topColor, final int bottomColor) {
        final float alpha = (topColor >> 24 & 0xFF) / 255.0f;
        final float red = (topColor >> 16 & 0xFF) / 255.0f;
        final float green = (topColor >> 8 & 0xFF) / 255.0f;
        final float blue = (topColor & 0xFF) / 255.0f;
        final float alpha2 = (bottomColor >> 24 & 0xFF) / 255.0f;
        final float red2 = (bottomColor >> 16 & 0xFF) / 255.0f;
        final float green2 = (bottomColor >> 8 & 0xFF) / 255.0f;
        final float blue2 = (bottomColor & 0xFF) / 255.0f;
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179103_j(7425);
        final BufferBuilder builder = Render2DMethods.tessellator.func_178180_c();
        builder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        if (sideways) {
            builder.func_181662_b((double)left, (double)top, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b((double)left, (double)bottom, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b((double)right, (double)bottom, 0.0).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b((double)right, (double)top, 0.0).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        }
        else {
            builder.func_181662_b((double)right, (double)top, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b((double)left, (double)top, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
            builder.func_181662_b((double)left, (double)bottom, 0.0).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
            builder.func_181662_b((double)right, (double)bottom, 0.0).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        }
        Render2DMethods.tessellator.func_78381_a();
        GlStateManager.func_179103_j(7424);
        GlStateManager.func_179084_k();
        GlStateManager.func_179098_w();
    }
    
    public static void drawGlow(final double x, final double y, final double x1, final double y1, final int color) {
        new Color(color);
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179103_j(7425);
        drawVGradientRect((float)(int)x, (float)(int)y, (float)(int)x1, (float)(int)(y + (y1 - y) / 2.0), color, ColorUtil.changeAlpha(new Color(color), 0).getRGB());
        drawVGradientRect((float)(int)x, (float)(int)(y + (y1 - y) / 2.0), (float)(int)x1, (float)(int)y1, color, ColorUtil.changeAlpha(new Color(color), 0).getRGB());
        final int radius = (int)((y1 - y) / 2.0);
        drawPolygonPart(x, y + (y1 - y) / 2.0, radius, 0, color, ColorUtil.changeAlpha(new Color(color), 0).getRGB());
        drawPolygonPart(x, y + (y1 - y) / 2.0, radius, 1, color, ColorUtil.changeAlpha(new Color(color), 0).getRGB());
        drawPolygonPart(x1, y + (y1 - y) / 2.0, radius, 2, color, ColorUtil.changeAlpha(new Color(color), 0).getRGB());
        drawPolygonPart(x1, y + (y1 - y) / 2.0, radius, 3, color, ColorUtil.changeAlpha(new Color(color), 0).getRGB());
        GlStateManager.func_179103_j(7424);
        GlStateManager.func_179098_w();
        GlStateManager.func_179141_d();
        GlStateManager.func_179084_k();
    }
    
    public static void drawVGradientRect(final float left, final float top, final float right, final float bottom, final int startColor, final int endColor) {
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179103_j(7425);
        final Tessellator tessellator = Tessellator.func_178181_a();
        final BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b((double)right, (double)top, 0.0).func_181666_a(f2, f3, f4, f).func_181675_d();
        bufferbuilder.func_181662_b((double)left, (double)top, 0.0).func_181666_a(f2, f3, f4, f).func_181675_d();
        bufferbuilder.func_181662_b((double)left, (double)bottom, 0.0).func_181666_a(f6, f7, f8, f5).func_181675_d();
        bufferbuilder.func_181662_b((double)right, (double)bottom, 0.0).func_181666_a(f6, f7, f8, f5).func_181675_d();
        tessellator.func_78381_a();
        GlStateManager.func_179103_j(7424);
        GlStateManager.func_179098_w();
        GlStateManager.func_179141_d();
        GlStateManager.func_179084_k();
    }
    
    public static void drawPolygonPart(final double x, final double y, final int radius, final int part, final int color, final int endcolor) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        final float alpha2 = (endcolor >> 24 & 0xFF) / 255.0f;
        final float red2 = (endcolor >> 16 & 0xFF) / 255.0f;
        final float green2 = (endcolor >> 8 & 0xFF) / 255.0f;
        final float blue2 = (endcolor & 0xFF) / 255.0f;
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.func_179103_j(7425);
        final Tessellator tessellator = Tessellator.func_178181_a();
        final BufferBuilder bufferbuilder = tessellator.func_178180_c();
        bufferbuilder.func_181668_a(6, DefaultVertexFormats.field_181706_f);
        bufferbuilder.func_181662_b(x, y, 0.0).func_181666_a(red, green, blue, alpha).func_181675_d();
        for (int i = part * 90; i <= part * 90 + 90; ++i) {
            final double angle = 6.283185307179586 * i / 360.0 + Math.toRadians(180.0);
            bufferbuilder.func_181662_b(x + Math.sin(angle) * radius, y + Math.cos(angle) * radius, 0.0).func_181666_a(red2, green2, blue2, alpha2).func_181675_d();
        }
        tessellator.func_78381_a();
        GlStateManager.func_179103_j(7424);
        GlStateManager.func_179098_w();
        GlStateManager.func_179141_d();
        GlStateManager.func_179084_k();
    }
    
    public static void drawHLine(float x, float y, final float x1, final int y1) {
        if (y < x) {
            final float var5 = x;
            x = y;
            y = var5;
        }
        drawRect(x, x1, y + 1.0f, x1 + 1.0f, y1);
    }
    
    public static void drawVLine(final float x, float y, float x1, final int y1) {
        if (x1 < y) {
            final float var5 = y;
            y = x1;
            x1 = var5;
        }
        drawRect(x, y + 1.0f, x + 1.0f, x1, y1);
    }
    
    public static void glColor(final int hex) {
        GL11.glColor4f((hex >> 16 & 0xFF) / 255.0f, (hex >> 8 & 0xFF) / 255.0f, (hex & 0xFF) / 255.0f, (hex >> 24 & 0xFF) / 255.0f);
    }
    
    static {
        tessellator = Tessellator.func_178181_a();
    }
}
