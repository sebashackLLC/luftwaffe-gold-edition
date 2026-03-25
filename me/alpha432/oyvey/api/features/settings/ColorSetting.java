//Decompiled by Procyon!

package me.alpha432.oyvey.api.features.settings;

import java.awt.*;

public class ColorSetting
{
    private int red;
    private int green;
    private int blue;
    private int alpha;
    
    public ColorSetting(final int red, final int green, final int blue, final int alpha) {
        this.red = this.clamp(red);
        this.green = this.clamp(green);
        this.blue = this.clamp(blue);
        this.alpha = this.clamp(alpha);
    }
    
    public ColorSetting(final int red, final int green, final int blue) {
        this(red, green, blue, 255);
    }
    
    public ColorSetting(final Color color) {
        this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    
    public ColorSetting(final int argb) {
        this.alpha = (argb >> 24 & 0xFF);
        this.red = (argb >> 16 & 0xFF);
        this.green = (argb >> 8 & 0xFF);
        this.blue = (argb & 0xFF);
    }
    
    private int clamp(final int value) {
        return Math.max(0, Math.min(255, value));
    }
    
    public int getRed() {
        return this.red;
    }
    
    public int getGreen() {
        return this.green;
    }
    
    public int getBlue() {
        return this.blue;
    }
    
    public int getAlpha() {
        return this.alpha;
    }
    
    public void setRed(final int red) {
        this.red = this.clamp(red);
    }
    
    public void setGreen(final int green) {
        this.green = this.clamp(green);
    }
    
    public void setBlue(final int blue) {
        this.blue = this.clamp(blue);
    }
    
    public void setAlpha(final int alpha) {
        this.alpha = this.clamp(alpha);
    }
    
    public void setColor(final int red, final int green, final int blue, final int alpha) {
        this.red = this.clamp(red);
        this.green = this.clamp(green);
        this.blue = this.clamp(blue);
        this.alpha = this.clamp(alpha);
    }
    
    public int getARGB() {
        return this.alpha << 24 | this.red << 16 | this.green << 8 | this.blue;
    }
    
    public int getRGB() {
        return 0xFF000000 | this.red << 16 | this.green << 8 | this.blue;
    }
    
    public Color getColor() {
        return new Color(this.red, this.green, this.blue, this.alpha);
    }
    
    public float[] getHSB() {
        return Color.RGBtoHSB(this.red, this.green, this.blue, null);
    }
    
    public void setFromHSB(final float hue, final float saturation, final float brightness) {
        final int rgb = Color.HSBtoRGB(hue, saturation, brightness);
        this.red = (rgb >> 16 & 0xFF);
        this.green = (rgb >> 8 & 0xFF);
        this.blue = (rgb & 0xFF);
    }
    
    public ColorSetting copy() {
        return new ColorSetting(this.red, this.green, this.blue, this.alpha);
    }
    
    @Override
    public String toString() {
        return this.red + "," + this.green + "," + this.blue + "," + this.alpha;
    }
    
    public static ColorSetting fromString(final String str) {
        final String[] parts = str.split(",");
        if (parts.length >= 4) {
            return new ColorSetting(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()), Integer.parseInt(parts[2].trim()), Integer.parseInt(parts[3].trim()));
        }
        if (parts.length == 3) {
            return new ColorSetting(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()), Integer.parseInt(parts[2].trim()));
        }
        return new ColorSetting(255, 255, 255, 255);
    }
}
