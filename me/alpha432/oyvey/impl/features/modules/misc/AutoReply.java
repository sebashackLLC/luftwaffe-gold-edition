//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.misc;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.network.play.server.*;
import me.alpha432.oyvey.*;
import net.minecraftforge.fml.common.eventhandler.*;
import java.util.*;

public class AutoReply extends Module
{
    private final Setting<Boolean> suffix;
    
    public AutoReply() {
        super("AutoReply", "Automatically responds your coords to friends", Module.Category.MISC, true, false, false);
        this.suffix = (Setting<Boolean>)this.register(new Setting("Suffix", (Object)false));
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive e) {
        if (!fullNullCheck() && !this.isDisabled() && e.getPacket() instanceof SPacketChat) {
            final SPacketChat p = (SPacketChat)e.getPacket();
            final String unformatted = p.func_148915_c().func_150260_c();
            if (unformatted.contains("says: ") || unformatted.contains("whispers: ")) {
                final String ign = unformatted.split(" ")[0];
                if (AutoReply.mc.field_71439_g.func_70005_c_().equals(ign) || !OyVey.friendManager.isFriend(ign)) {
                    return;
                }
                final String msg = unformatted.toLowerCase();
                if (msg.contains("my coordinates are")) {
                    return;
                }
                if ((boolean)this.isEnabled() && msg.matches(".*(cord|coord|coords|cords|wya|where are u|where are you|where r u|where ru).*") && !msg.matches(".*(discord|record).*")) {
                    final int x = (int)AutoReply.mc.field_71439_g.field_70165_t;
                    final int z = (int)AutoReply.mc.field_71439_g.field_70161_v;
                    final String dimension = this.getDimensionName(AutoReply.mc.field_71439_g.field_71093_bK);
                    if (this.suffix.getValue()) {
                        AutoReply.mc.field_71439_g.func_71165_d("/msg " + ign + " X: " + x + " Y: " + (int)AutoReply.mc.field_71439_g.field_70163_u + " Z: " + z + " " + dimension + " (Sent via luftwaffe technologies)");
                    }
                    else {
                        AutoReply.mc.field_71439_g.func_71165_d("/msg " + ign + " X: " + x + " Y: " + (int)AutoReply.mc.field_71439_g.field_70163_u + " Z: " + z + " " + dimension);
                    }
                }
            }
        }
    }
    
    private String getDimensionName(final int dimensionId) {
        final Map<Integer, String> dimensionMap = new HashMap<Integer, String>();
        dimensionMap.put(0, "OverWorld");
        dimensionMap.put(1, "End");
        dimensionMap.put(-1, "Nether");
        return dimensionMap.getOrDefault(dimensionId, "unkown dimension");
    }
}
