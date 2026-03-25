//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.combat;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;

public class Surround extends Module
{
    private final Setting<Boolean> disableOnYChange;
    private double startY;
    
    public Surround() {
        super("Surround", "Places obsidian around you", Module.Category.COMBAT, false, false, false);
        this.disableOnYChange = (Setting<Boolean>)this.register(new Setting("DisableOnYChange", (Object)true));
    }
    
    public void onEnable() {
        if (Surround.mc.field_71439_g != null) {
            this.startY = Surround.mc.field_71439_g.field_70163_u;
        }
    }
    
    public void onUpdate() {
        if (Surround.mc.field_71439_g == null) {
            return;
        }
        if ((boolean)this.disableOnYChange.getValue() && Surround.mc.field_71439_g.field_70163_u != this.startY) {
            this.disable();
        }
    }
}
