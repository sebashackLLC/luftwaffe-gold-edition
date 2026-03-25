//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.player;

import me.alpha432.oyvey.api.features.*;
import net.minecraft.item.*;
import me.alpha432.oyvey.api.util.entity.*;

public class FastPlace extends Module
{
    public FastPlace() {
        super("FastXP", "Fast everything", Module.Category.PLAYER, true, false, false);
    }
    
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if (InventoryUtil.holdingItem((Class)ItemExpBottle.class)) {
            FastPlace.mc.field_71467_ac = 0;
        }
    }
}
