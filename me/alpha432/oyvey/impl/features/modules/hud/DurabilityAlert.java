//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraft.item.*;
import java.util.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.client.gui.*;

public class DurabilityAlert extends Module
{
    private Setting<Integer> dura;
    private Setting<Boolean> chad;
    private boolean lowDura;
    
    public DurabilityAlert() {
        super("DurabilityAlert", "Alerts you and your friends if you have low durability", Module.Category.HUD, true, false, false);
        this.dura = (Setting<Integer>)this.register(new Setting("Durability", (Object)30, (Object)1, (Object)100));
        this.chad = (Setting<Boolean>)this.register(new Setting("Australian Mode", (Object)true));
        this.lowDura = false;
    }
    
    public void onUpdate() {
        this.lowDura = false;
        try {
            for (final ItemStack is : DurabilityAlert.mc.field_71439_g.func_184193_aE()) {
                if (is.func_190926_b()) {
                    continue;
                }
                final float green = (is.func_77958_k() - (float)is.func_77952_i()) / is.func_77958_k();
                final float red = 1.0f - green;
                final int dmg = 100 - (int)(red * 100.0f);
                if (dmg <= (int)this.dura.getValue()) {
                    this.lowDura = true;
                    break;
                }
            }
        }
        catch (Exception ex) {}
    }
    
    public void onRender2D(final Render2DEvent event) {
        if (this.lowDura) {
            final ScaledResolution sr = new ScaledResolution(DurabilityAlert.mc);
            final String message = "Warning: Your " + (this.chad.getValue() ? "armour" : "shits") + " is below " + this.dura.getValue() + "%";
            final int textWidth = DurabilityAlert.mc.field_71466_p.func_78256_a(message);
            DurabilityAlert.mc.field_71466_p.func_175063_a(message, (float)(sr.func_78326_a() / 2 - textWidth / 2), 15.0f, 16711680);
        }
    }
}
