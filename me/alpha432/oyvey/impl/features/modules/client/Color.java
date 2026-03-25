//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.impl.gui.components.*;
import me.alpha432.oyvey.api.util.render.*;

public class Color extends Module
{
    private static Color INSTANCE;
    public Setting<ColorSetting> mainColor;
    public Setting<ColorSetting> gradientColor;
    public Setting<Boolean> rainbow;
    public Setting<Boolean> gradient;
    public Setting<Boolean> gradientUp;
    public Setting<Boolean> sidewaysGradient;
    public Setting<Integer> gradientSpeed;
    public Setting<Integer> sidewaysGradientSpeed;
    public Setting<rainbowMode> rainbowModeHud;
    public Setting<rainbowModeArray> rainbowModeA;
    public Setting<Integer> rainbowHue;
    public Setting<Float> rainbowBrightness;
    public Setting<Float> rainbowSaturation;
    public int red;
    public int green;
    public int blue;
    public int hoverAlpha;
    public int alpha;
    
    public Color() {
        super("Color", "Colorsync", Module.Category.CLIENT, true, false, false);
        this.mainColor = (Setting<ColorSetting>)this.register(new Setting("MainColor", (Object)new ColorSetting(155, 175, 255, 180)));
        this.gradientColor = (Setting<ColorSetting>)this.register(new Setting("GradientColor", (Object)new ColorSetting(255, 0, 0, 180), v -> (boolean)this.gradient.getValue()));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", (Object)false));
        this.gradient = (Setting<Boolean>)this.register(new Setting("Gradient", (Object)false));
        this.gradientUp = (Setting<Boolean>)this.register(new Setting("GradientUp", (Object)false, v -> (boolean)this.gradient.getValue()));
        this.sidewaysGradient = (Setting<Boolean>)this.register(new Setting("SidewaysGradient", (Object)false));
        this.gradientSpeed = (Setting<Integer>)this.register(new Setting("GradientSpeed", (Object)10, (Object)1, (Object)100, v -> (boolean)this.gradient.getValue()));
        this.sidewaysGradientSpeed = (Setting<Integer>)this.register(new Setting("SidewaysSpeed", (Object)50, (Object)1, (Object)200, v -> (boolean)this.sidewaysGradient.getValue()));
        this.rainbowModeHud = (Setting<rainbowMode>)this.register(new Setting("HRainbowMode", (Object)rainbowMode.Static, v -> (boolean)this.rainbow.getValue()));
        this.rainbowModeA = (Setting<rainbowModeArray>)this.register(new Setting("ARainbowMode", (Object)rainbowModeArray.Static, v -> (boolean)this.rainbow.getValue()));
        this.rainbowHue = (Setting<Integer>)this.register(new Setting("Delay", (Object)240, (Object)0, (Object)600, v -> (boolean)this.rainbow.getValue()));
        this.rainbowBrightness = (Setting<Float>)this.register(new Setting("Brightness ", (Object)150.0f, (Object)1.0f, (Object)255.0f, v -> (boolean)this.rainbow.getValue()));
        this.rainbowSaturation = (Setting<Float>)this.register(new Setting("Saturation", (Object)150.0f, (Object)1.0f, (Object)255.0f, v -> (boolean)this.rainbow.getValue()));
        this.red = 155;
        this.green = 175;
        this.blue = 255;
        this.hoverAlpha = 180;
        this.alpha = 240;
        this.setInstance();
    }
    
    public static Color getInstance() {
        if (Color.INSTANCE == null) {
            Color.INSTANCE = new Color();
        }
        return Color.INSTANCE;
    }
    
    private void setInstance() {
        Color.INSTANCE = this;
    }
    
    public void onLoad() {
        this.updateLegacyValues();
        OyVey.colorManager.setColor(this.red, this.green, this.blue, this.hoverAlpha);
    }
    
    public void onUpdate() {
        this.updateLegacyValues();
    }
    
    private void updateLegacyValues() {
        final ColorSetting main = (ColorSetting)this.mainColor.getValue();
        this.red = main.getRed();
        this.green = main.getGreen();
        this.blue = main.getBlue();
        this.hoverAlpha = main.getAlpha();
    }
    
    public int syncColor() {
        return this.syncColor(0);
    }
    
    public int syncColor(final int index) {
        if (this.rainbow.getValue()) {
            return ColorUtil.rainbow(Component.counter1[0] * (int)getInstance().rainbowHue.getValue()).getRGB();
        }
        if (this.gradient.getValue()) {
            return this.getGradientColor(index);
        }
        final ColorSetting main = (ColorSetting)this.mainColor.getValue();
        return ColorUtil.toARGB(main.getRed(), main.getGreen(), main.getBlue(), main.getAlpha());
    }
    
    public int getSidewaysGradientStartColor() {
        final ColorSetting main = (ColorSetting)this.mainColor.getValue();
        return ColorUtil.toARGB(main.getRed(), main.getGreen(), main.getBlue(), main.getAlpha());
    }
    
    public int getSidewaysGradientEndColor() {
        final ColorSetting grad = (ColorSetting)this.gradientColor.getValue();
        return ColorUtil.toARGB(grad.getRed(), grad.getGreen(), grad.getBlue(), grad.getAlpha());
    }
    
    private int getGradientColor(final int index) {
        final long time = System.currentTimeMillis();
        final double offset = this.gradientUp.getValue() ? (-index * 1.2) : (index * 1.2);
        final double factor = Math.sin((time * (int)this.gradientSpeed.getValue() / 10000.0 + offset) % 6.283185307179586) * 0.5 + 0.5;
        final ColorSetting main = (ColorSetting)this.mainColor.getValue();
        final ColorSetting grad = (ColorSetting)this.gradientColor.getValue();
        final int r = (int)(main.getRed() + (grad.getRed() - main.getRed()) * factor);
        final int g = (int)(main.getGreen() + (grad.getGreen() - main.getGreen()) * factor);
        final int b = (int)(main.getBlue() + (grad.getBlue() - main.getBlue()) * factor);
        return ColorUtil.toARGB(r, g, b, 255);
    }
    
    static {
        Color.INSTANCE = new Color();
    }
    
    public enum rainbowModeArray
    {
        Static, 
        Up;
    }
    
    public enum rainbowMode
    {
        Static, 
        Sideway;
    }
}
