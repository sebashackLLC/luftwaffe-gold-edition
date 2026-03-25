//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;

public class GlintColorizer extends Module
{
    private static GlintColorizer INSTANCE;
    public Setting<ColorSetting> color;
    public Setting<Double> speed;
    
    public GlintColorizer() {
        super("GlintColorizer", "Colorize your glint", Module.Category.RENDER, false, false, false);
        this.color = (Setting<ColorSetting>)this.register(new Setting("Color", (Object)new ColorSetting(255, 0, 0, 255)));
        this.speed = (Setting<Double>)this.register(new Setting("Speed", (Object)1.0, (Object)0.0, (Object)5.0));
        this.setInstance();
    }
    
    public static GlintColorizer getINSTANCE() {
        if (GlintColorizer.INSTANCE == null) {
            GlintColorizer.INSTANCE = new GlintColorizer();
        }
        return GlintColorizer.INSTANCE;
    }
    
    private void setInstance() {
        GlintColorizer.INSTANCE = this;
    }
    
    public int getRed() {
        return ((ColorSetting)this.color.getValue()).getRed();
    }
    
    public int getGreen() {
        return ((ColorSetting)this.color.getValue()).getGreen();
    }
    
    public int getBlue() {
        return ((ColorSetting)this.color.getValue()).getBlue();
    }
    
    static {
        GlintColorizer.INSTANCE = new GlintColorizer();
    }
}
