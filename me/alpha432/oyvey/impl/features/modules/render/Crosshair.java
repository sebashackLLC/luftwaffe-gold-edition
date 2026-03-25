//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.client.gui.*;
import java.awt.*;
import me.alpha432.oyvey.api.util.entity.*;
import me.alpha432.oyvey.api.util.render.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;

public class Crosshair extends Module
{
    private final Setting<Boolean> indicator;
    private final Setting<Boolean> outline;
    private final Setting<GapMode> gapMode;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final Setting<Integer> outlineRed;
    private final Setting<Integer> outlineGreen;
    private final Setting<Integer> outlineBlue;
    private final Setting<Integer> outlineAlpha;
    private final Setting<Float> length;
    private final Setting<Float> gapSize;
    private final Setting<Float> width;
    
    public Crosshair() {
        super("Crosshair", "Gives you a custom crosshair", Module.Category.RENDER, true, false, false);
        this.indicator = (Setting<Boolean>)this.register(new Setting("AttackIndicator", (Object)true));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (Object)true));
        this.gapMode = (Setting<GapMode>)this.register(new Setting("GapMode", (Object)GapMode.NORMAL));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (Object)190, (Object)0, (Object)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (Object)60, (Object)0, (Object)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (Object)190, (Object)0, (Object)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (Object)255, (Object)0, (Object)255));
        this.outlineRed = (Setting<Integer>)this.register(new Setting("OutlineRed", (Object)0, (Object)0, (Object)255, v -> (boolean)this.outline.getValue()));
        this.outlineGreen = (Setting<Integer>)this.register(new Setting("OutlineGreen", (Object)0, (Object)0, (Object)255, v -> (boolean)this.outline.getValue()));
        this.outlineBlue = (Setting<Integer>)this.register(new Setting("OutlineBlue", (Object)0, (Object)0, (Object)255, v -> (boolean)this.outline.getValue()));
        this.outlineAlpha = (Setting<Integer>)this.register(new Setting("OutlineAlpha", (Object)255, (Object)0, (Object)255, v -> (boolean)this.outline.getValue()));
        this.length = (Setting<Float>)this.register(new Setting("Length", (Object)5.5f, (Object)0.5f, (Object)50.0f));
        this.gapSize = (Setting<Float>)this.register(new Setting("GapSize", (Object)2.0f, (Object)0.5f, (Object)20.0f));
        this.width = (Setting<Float>)this.register(new Setting("Width", (Object)1.0f, (Object)0.5f, (Object)10.0f));
    }
    
    public void onRender2D(final Render2DEvent event) {
        if (Crosshair.mc.field_71439_g == null || Crosshair.mc.field_71441_e == null) {
            return;
        }
        final ScaledResolution sr = new ScaledResolution(Crosshair.mc);
        final int screenMiddleX = sr.func_78326_a() / 2;
        final int screenMiddleY = sr.func_78328_b() / 2;
        final float w = (float)this.width.getValue();
        final float len = (float)this.length.getValue();
        final float gap = (float)this.gapSize.getValue();
        final int color = new Color((int)this.red.getValue(), (int)this.green.getValue(), (int)this.blue.getValue(), (int)this.alpha.getValue()).getRGB();
        final int outlineColor = new Color((int)this.outlineRed.getValue(), (int)this.outlineGreen.getValue(), (int)this.outlineBlue.getValue(), (int)this.outlineAlpha.getValue()).getRGB();
        final float dynamicGap = (MovementUtil.isMoving() && this.gapMode.getValue() == GapMode.DYNAMIC) ? gap : 0.0f;
        if (this.gapMode.getValue() != GapMode.NONE) {
            this.drawBorderedRect(screenMiddleX - w, screenMiddleY - (gap + len) - dynamicGap, screenMiddleX + w, screenMiddleY - gap - dynamicGap, 1.0f, color, outlineColor);
            this.drawBorderedRect(screenMiddleX - w, screenMiddleY + gap + dynamicGap, screenMiddleX + w, screenMiddleY + (gap + len) + dynamicGap, 1.0f, color, outlineColor);
            this.drawBorderedRect(screenMiddleX - (gap + len) - dynamicGap, screenMiddleY - w, screenMiddleX - gap - dynamicGap, screenMiddleY + w, 1.0f, color, outlineColor);
            this.drawBorderedRect(screenMiddleX + gap + dynamicGap, screenMiddleY - w, screenMiddleX + (gap + len) + dynamicGap, screenMiddleY + w, 1.0f, color, outlineColor);
        }
        if (this.indicator.getValue()) {
            final float f = Crosshair.mc.field_71439_g.func_184825_o(0.0f);
            final float totalWidth = (gap + len + dynamicGap) * 2.0f;
            final float indWidthInc = totalWidth / 17.0f;
            if (f < 1.0f) {
                final float finWidth = indWidthInc * (f * 17.0f);
                final float startX = screenMiddleX - (gap + len) - dynamicGap;
                final float startY = screenMiddleY + (gap + len) + dynamicGap + 2.0f;
                this.drawBorderedRect(startX, startY, startX + finWidth, startY + w * 2.0f, 1.0f, color, outlineColor);
            }
        }
    }
    
    private void drawBorderedRect(final float x1, final float y1, final float x2, final float y2, final float lineWidth, final int fillColor, final int outlineColor) {
        RenderUtil.drawRect(x1, y1, x2, y2, (long)fillColor);
        if (this.outline.getValue()) {
            final float a = (outlineColor >> 24 & 0xFF) / 255.0f;
            final float r = (outlineColor >> 16 & 0xFF) / 255.0f;
            final float g = (outlineColor >> 8 & 0xFF) / 255.0f;
            final float b = (outlineColor & 0xFF) / 255.0f;
            GlStateManager.func_179094_E();
            GlStateManager.func_179147_l();
            GlStateManager.func_179090_x();
            GlStateManager.func_179120_a(770, 771, 1, 0);
            GL11.glLineWidth(lineWidth);
            GL11.glEnable(2848);
            GL11.glBegin(2);
            GL11.glColor4f(r, g, b, a);
            GL11.glVertex2f(x1, y1);
            GL11.glVertex2f(x2, y1);
            GL11.glVertex2f(x2, y2);
            GL11.glVertex2f(x1, y2);
            GL11.glEnd();
            GL11.glDisable(2848);
            GlStateManager.func_179098_w();
            GlStateManager.func_179084_k();
            GlStateManager.func_179121_F();
        }
    }
    
    public enum GapMode
    {
        NONE, 
        NORMAL, 
        DYNAMIC;
    }
}
