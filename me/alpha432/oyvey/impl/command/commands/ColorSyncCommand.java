//Decompiled by Procyon!

package me.alpha432.oyvey.impl.command.commands;

import me.alpha432.oyvey.impl.command.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.features.modules.client.*;

public class ColorSyncCommand extends Command
{
    public ColorSyncCommand() {
        super("colorsync", new String[0]);
    }
    
    private float[] calculateHSL(final int r, final int g, final int b) {
        final float rf = r / 255.0f;
        final float gf = g / 255.0f;
        final float bf = b / 255.0f;
        final float max = Math.max(rf, Math.max(gf, bf));
        final float min = Math.min(rf, Math.min(gf, bf));
        float h = 0.0f;
        final float l = (max + min) / 2.0f;
        float s;
        if (max == min) {
            s = 0.0f;
            h = 0.0f;
        }
        else {
            final float d = max - min;
            s = ((l > 0.5) ? (d / (2.0f - max - min)) : (d / (max + min)));
            if (max == rf) {
                h = (gf - bf) / d + ((gf < bf) ? 6 : 0);
            }
            else if (max == gf) {
                h = (bf - rf) / d + 2.0f;
            }
            else if (max == bf) {
                h = (rf - gf) / d + 4.0f;
            }
            h /= 6.0f;
        }
        final float d = h * 360.0f;
        final float saturation = s * 100.0f;
        final float lightness = l * 100.0f;
        return new float[] { d, saturation, lightness };
    }
    
    public void execute(final String[] commands) {
        final ColorSetting mainColor = (ColorSetting)Color.getInstance().mainColor.getValue();
        final int red = mainColor.getRed();
        final int green = mainColor.getGreen();
        final int blue = mainColor.getBlue();
        final float[] hsl = this.calculateHSL(red, green, blue);
        final float hue = hsl[0];
        final float saturation = hsl[1];
        final float lightness = hsl[2];
        ColorSyncCommand.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "colors hue " + hue);
        ColorSyncCommand.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "colors saturation " + saturation);
        ColorSyncCommand.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "colors lightness " + lightness);
    }
}
