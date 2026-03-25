//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.impl.events.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import me.alpha432.oyvey.api.util.render.*;
import net.minecraft.util.math.*;

public class Highlight extends Module
{
    private final Setting<Float> lineWidth;
    private final Setting<Integer> cAlpha;
    
    public Highlight() {
        super("Highlight", "BlockHighlight", Module.Category.RENDER, false, false, false);
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (Object)1.0f, (Object)0.1f, (Object)5.0f));
        this.cAlpha = (Setting<Integer>)this.register(new Setting("Alpha", (Object)255, (Object)0, (Object)255));
    }
    
    public void onRender3D(final Render3DEvent event) {
        final RayTraceResult ray = Highlight.mc.field_71476_x;
        if (ray != null && ray.field_72313_a == RayTraceResult.Type.BLOCK) {
            final BlockPos blockpos = ray.func_178782_a();
            final ColorSetting mainColor = (ColorSetting)Color.getInstance().mainColor.getValue();
            RenderUtil.drawBlockOutline(blockpos, ((boolean)Color.getInstance().rainbow.getValue()) ? ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()) : new java.awt.Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), (int)this.cAlpha.getValue()), (float)this.lineWidth.getValue(), false);
        }
    }
}
