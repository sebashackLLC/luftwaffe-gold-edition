//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import java.awt.*;

public class SkyBox extends Module
{
    protected final Setting<Mode> skyMode;
    private final Setting<Integer> time;
    private final Setting<ColorSetting> skyColor;
    private final Setting<ColorSetting> fogColor;
    
    public SkyBox() {
        super("SkyBox", "", Module.Category.RENDER, true, false, false);
        this.skyMode = (Setting<Mode>)new Setting("Mode", (Object)Mode.SKY);
        this.time = (Setting<Integer>)new Setting("Time", (Object)0, (Object)0, (Object)24000);
        this.skyColor = (Setting<ColorSetting>)new Setting("SkyColor", (Object)new ColorSetting(255, 255, 255, 255), v -> this.skyMode.getValue() == Mode.SKY || this.skyMode.getValue() == Mode.BOTH);
        this.fogColor = (Setting<ColorSetting>)new Setting("FogColor", (Object)new ColorSetting(255, 255, 255, 255), v -> this.skyMode.getValue() == Mode.FOG || this.skyMode.getValue() == Mode.BOTH);
        this.register((Setting)this.skyColor);
        this.register((Setting)this.fogColor);
        this.register((Setting)this.time);
        this.register((Setting)this.skyMode);
    }
    
    @SubscribeEvent
    public void fogColors(final EntityViewRenderEvent.FogColors event) {
        if (this.skyMode.getValue() == Mode.FOG || this.skyMode.getValue() == Mode.BOTH) {
            final ColorSetting c = (ColorSetting)this.fogColor.getValue();
            event.setRed(c.getRed() / 255.0f);
            event.setGreen(c.getGreen() / 255.0f);
            event.setBlue(c.getBlue() / 255.0f);
        }
    }
    
    public Integer getTime() {
        return (Integer)this.time.getValue();
    }
    
    public Mode mode() {
        return (Mode)this.skyMode.getValue();
    }
    
    public Color getSkyColor() {
        final ColorSetting c = (ColorSetting)this.skyColor.getValue();
        return new Color(c.getRed(), c.getGreen(), c.getBlue());
    }
    
    public enum Mode
    {
        SKY, 
        FOG, 
        BOTH, 
        NONE;
    }
}
