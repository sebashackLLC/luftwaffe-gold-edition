//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class HotbarKeys extends Module
{
    public HotbarKeys() {
        super("HotbarKeys", "Drawing numbers on hotbar", Module.Category.HUD, true, false, false);
    }
    
    @SubscribeEvent
    public void onHotbarRender(final RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            final int x = event.getResolution().func_78326_a() / 2 - 87;
            final int y = event.getResolution().func_78328_b() - 18;
            for (int length = HotbarKeys.mc.field_71474_y.field_151456_ac.length, i = 0; i < length; ++i) {
                HotbarKeys.mc.field_71466_p.func_78276_b(HotbarKeys.mc.field_71474_y.field_151456_ac[i].getDisplayName(), x + i * 20, y, -1);
            }
        }
    }
}
