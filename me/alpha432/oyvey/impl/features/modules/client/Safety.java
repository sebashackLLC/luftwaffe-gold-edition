//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.entity.*;
import com.mojang.realmsclient.gui.*;

public class Safety extends Module
{
    public Safety() {
        super("Safety", "Shows if you are in a safe hole", Module.Category.CLIENT, true, false, false);
    }
    
    public String getDisplayInfo() {
        if (Safety.mc.field_71439_g == null) {
            return null;
        }
        final boolean safe = EntityUtil.isSafe((Entity)Safety.mc.field_71439_g);
        if (safe) {
            return ChatFormatting.GREEN + "safe";
        }
        return ChatFormatting.RED + "unsafe";
    }
}
