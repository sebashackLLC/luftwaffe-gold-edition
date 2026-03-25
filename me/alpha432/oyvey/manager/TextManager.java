//Decompiled by Procyon!

package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.impl.gui.font.*;
import java.awt.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import net.minecraft.util.math.*;

public class TextManager extends Feature
{
    private final Timer idleTimer;
    public int scaledWidth;
    public int scaledHeight;
    public int scaleFactor;
    private CustomFont customFont;
    private boolean idling;
    
    public TextManager() {
        this.idleTimer = new Timer();
        this.customFont = new CustomFont(new Font("Verdana", 0, 17), true, false);
        this.updateResolution();
    }
    
    public void init(final boolean startup) {
        final me.alpha432.oyvey.impl.features.modules.client.Font cFont = (me.alpha432.oyvey.impl.features.modules.client.Font)OyVey.moduleManager.getModuleByClass((Class)me.alpha432.oyvey.impl.features.modules.client.Font.class);
        try {
            this.setFontRenderer(new Font((String)cFont.fontName.getValue(), (int)cFont.fontStyle.getValue(), (int)cFont.fontSize.getValue()), (boolean)cFont.antiAlias.getValue(), (boolean)cFont.fractionalMetrics.getValue());
        }
        catch (Exception ex) {}
    }
    
    public void drawStringWithShadow(final String text, final float x, final float y, final int color) {
        this.drawString(text, x, y, color, true);
    }
    
    public void drawStringWithoutShadow(final String text, final float x, final float y, final int color) {
        this.drawString(text, x, y, color, false);
    }
    
    public void drawString(final String text, final float x, final float y, final int color, final boolean shadow) {
        if (OyVey.moduleManager.isModuleEnabled(me.alpha432.oyvey.impl.features.modules.client.Font.getInstance().getName())) {
            if (shadow) {
                this.customFont.drawStringWithShadow(text, (double)x, (double)y, color);
            }
            else {
                this.customFont.drawString(text, x, y, color);
            }
            return;
        }
        if (shadow) {
            TextManager.mc.field_71466_p.func_175063_a(text, x, y, color);
        }
        else {
            TextManager.mc.field_71466_p.func_175065_a(text, x, y, color, false);
        }
    }
    
    public void drawStringWithGradient(final String text, final float x, final float y, final boolean shadow) {
        this.drawStringWithGradient(text, x, y, shadow, 0);
    }
    
    public void drawStringWithGradient(final String text, final float x, final float y, final boolean shadow, final int index) {
        final Color colorModule = Color.getInstance();
        if (colorModule.sidewaysGradient.getValue()) {
            this.drawSidewaysGradientString(text, x, y, colorModule.getSidewaysGradientStartColor(), colorModule.getSidewaysGradientEndColor(), (int)colorModule.sidewaysGradientSpeed.getValue(), shadow);
        }
        else if (colorModule.rainbow.getValue()) {
            if (colorModule.rainbowModeHud.getValue() == Color.rainbowMode.Sideway) {
                this.drawRainbowString(text, x, y, colorModule.syncColor(index), (float)text.length(), shadow);
            }
            else {
                this.drawString(text, x, y, colorModule.syncColor(index), shadow);
            }
        }
        else if (colorModule.gradient.getValue()) {
            this.drawString(text, x, y, colorModule.syncColor(index), shadow);
        }
        else {
            this.drawString(text, x, y, colorModule.syncColor(), shadow);
        }
    }
    
    public void drawRainbowString(final String text, final float x, final float y, final int startColor, final float factor, final boolean shadow) {
        java.awt.Color currentColor = new java.awt.Color(startColor);
        final float hueIncrement = 1.0f / factor;
        final String[] rainbowStrings = text.split("§.");
        float currentHue = java.awt.Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[0];
        final float saturation = java.awt.Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[1];
        final float brightness = java.awt.Color.RGBtoHSB(currentColor.getRed(), currentColor.getGreen(), currentColor.getBlue(), null)[2];
        int currentWidth = 0;
        boolean shouldRainbow = true;
        boolean shouldContinue = false;
        for (int i = 0; i < text.length(); ++i) {
            final char currentChar = text.charAt(i);
            final char nextChar = (i + 1 < text.length()) ? text.charAt(i + 1) : currentChar;
            if (currentChar == '§' && nextChar == 'r') {
                shouldRainbow = false;
            }
            else if (currentChar == '§' && nextChar == '+') {
                shouldRainbow = true;
            }
            if (shouldContinue) {
                shouldContinue = false;
            }
            else {
                if (currentChar == '§' && nextChar == 'r') {
                    final String escapeString = text.substring(i);
                    this.drawString(escapeString, x + currentWidth, y, java.awt.Color.WHITE.getRGB(), shadow);
                    break;
                }
                final String charToDraw = (currentChar == '§') ? "" : String.valueOf(currentChar);
                this.drawString(charToDraw, x + currentWidth, y, shouldRainbow ? currentColor.getRGB() : java.awt.Color.WHITE.getRGB(), shadow);
                if (currentChar == '§') {
                    shouldContinue = true;
                }
                currentWidth += this.getStringWidth(String.valueOf(currentChar));
                if (currentChar != ' ' && shouldRainbow) {
                    currentColor = new java.awt.Color(java.awt.Color.HSBtoRGB(currentHue, saturation, brightness));
                    currentHue += hueIncrement;
                }
            }
        }
    }
    
    public void drawSidewaysGradientString(final String text, final float x, final float y, final int startColor, final int endColor, final int speed, final boolean shadow) {
        if (text == null || text.isEmpty()) {
            return;
        }
        final String cleanText = text.replaceAll("§.", "");
        if (cleanText.isEmpty()) {
            return;
        }
        final int totalCleanWidth = this.getStringWidth(cleanText);
        if (totalCleanWidth == 0) {
            return;
        }
        final java.awt.Color start = new java.awt.Color(startColor, true);
        final java.awt.Color end = new java.awt.Color(endColor, true);
        final long time = System.currentTimeMillis();
        final float timeOffset = (float)(time * speed * 1.0E-4 % 2.0);
        int renderX = 0;
        int cleanX = 0;
        for (int i = 0; i < text.length(); ++i) {
            final char currentChar = text.charAt(i);
            if (currentChar == '§' && i + 1 < text.length()) {
                ++i;
            }
            else {
                final float baseProgress = cleanX / (float)totalCleanWidth;
                float progress = (baseProgress + timeOffset) % 1.0f;
                progress = (float)(Math.sin(progress * 3.141592653589793 * 2.0) * 0.5 + 0.5);
                final int r = (int)(start.getRed() + (end.getRed() - start.getRed()) * progress);
                final int g = (int)(start.getGreen() + (end.getGreen() - start.getGreen()) * progress);
                final int b = (int)(start.getBlue() + (end.getBlue() - start.getBlue()) * progress);
                final int a = (int)(start.getAlpha() + (end.getAlpha() - start.getAlpha()) * progress);
                final int gradientColor = a << 24 | r << 16 | g << 8 | b;
                final int charWidth = this.getStringWidth(String.valueOf(currentChar));
                this.drawString(String.valueOf(currentChar), x + renderX, y, gradientColor, shadow);
                renderX += charWidth;
                cleanX += charWidth;
            }
        }
    }
    
    public int getStringWidth(final String text) {
        if (OyVey.moduleManager.isModuleEnabled(me.alpha432.oyvey.impl.features.modules.client.Font.getInstance().getName())) {
            return this.customFont.getStringWidth(text);
        }
        return TextManager.mc.field_71466_p.func_78256_a(text);
    }
    
    public int getFontHeight() {
        if (OyVey.moduleManager.isModuleEnabled(me.alpha432.oyvey.impl.features.modules.client.Font.getInstance().getName())) {
            final String text = "A";
            return this.customFont.getStringHeight(text);
        }
        return TextManager.mc.field_71466_p.field_78288_b;
    }
    
    public void setFontRenderer(final Font font, final boolean antiAlias, final boolean fractionalMetrics) {
        this.customFont = new CustomFont(font, antiAlias, fractionalMetrics);
    }
    
    public Font getCurrentFont() {
        return this.customFont.getFont();
    }
    
    public void updateResolution() {
        this.scaledWidth = TextManager.mc.field_71443_c;
        this.scaledHeight = TextManager.mc.field_71440_d;
        this.scaleFactor = 1;
        final boolean flag = TextManager.mc.func_152349_b();
        int i = TextManager.mc.field_71474_y.field_74335_Z;
        if (i == 0) {
            i = 1000;
        }
        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }
        if (flag && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }
        final double scaledWidthD = this.scaledWidth / this.scaleFactor;
        final double scaledHeightD = this.scaledHeight / this.scaleFactor;
        this.scaledWidth = MathHelper.func_76143_f(scaledWidthD);
        this.scaledHeight = MathHelper.func_76143_f(scaledHeightD);
    }
    
    public String getIdleSign() {
        if (this.idleTimer.passedMs(500L)) {
            this.idling = !this.idling;
            this.idleTimer.reset();
        }
        if (this.idling) {
            return "_";
        }
        return "";
    }
}
