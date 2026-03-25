//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.misc;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.events.*;
import java.awt.*;
import me.alpha432.oyvey.api.util.render.*;
import net.minecraft.util.math.*;

public class ColorTest extends Module
{
    public Setting<ColorSetting> testColor;
    public Setting<ColorSetting> testColor2;
    public Setting<Boolean> renderTest;
    
    public ColorTest() {
        super("ColorTest", "Test module for color picker", Module.Category.MISC, false, false, false);
        this.testColor = (Setting<ColorSetting>)this.register(new Setting("TestColor", (Object)new ColorSetting(255, 0, 0, 255)));
        this.testColor2 = (Setting<ColorSetting>)this.register(new Setting("TestColor2", (Object)new ColorSetting(0, 255, 0, 128)));
        this.renderTest = (Setting<Boolean>)this.register(new Setting("RenderTest", (Object)false));
    }
    
    public void onUpdate() {
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (!(boolean)this.renderTest.getValue() || ColorTest.mc.field_71439_g == null) {
            return;
        }
        final BlockPos pos = ColorTest.mc.field_71439_g.func_180425_c().func_177982_a(2, 0, 0);
        final ColorSetting color1 = (ColorSetting)this.testColor.getValue();
        final ColorSetting color2 = (ColorSetting)this.testColor2.getValue();
        RenderUtil.drawBoxESP(pos, new Color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()), 1.0f, true, true, color1.getAlpha());
        final BlockPos pos2 = pos.func_177982_a(2, 0, 0);
        RenderUtil.drawBoxESP(pos2, new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), color2.getAlpha()), 1.0f, true, true, color2.getAlpha());
    }
}
