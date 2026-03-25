//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.misc;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.gui.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.api.util.*;
import me.alpha432.oyvey.impl.features.modules.hud.*;
import me.alpha432.oyvey.impl.command.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class AutoRespawn extends Module
{
    private boolean deathHandled;
    public Setting<Boolean> antiDeathScreen;
    public Setting<Boolean> deathCoords;
    public Setting<Boolean> respawn;
    public Setting<String> command;
    
    public AutoRespawn() {
        super("AutoRespawn", "Respawns you instantly when you die", Module.Category.MISC, true, false, false);
        this.deathHandled = false;
        this.antiDeathScreen = (Setting<Boolean>)new Setting("AntiDeathScreen", (Object)true);
        this.deathCoords = (Setting<Boolean>)this.register(new Setting("DeathCoords", (Object)false));
        this.respawn = (Setting<Boolean>)new Setting("Respawn", (Object)true);
        this.command = (Setting<String>)this.register(new Setting("Command", (Object)""));
    }
    
    @SubscribeEvent
    public void onDisplayDeathScreen(final GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            if (!this.deathHandled) {
                this.deathHandled = true;
                if (this.deathCoords.getValue()) {
                    Command.sendMessage(ChatFormatting.WHITE + "You " + TextUtil.coloredString("died at ", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + ChatFormatting.RED + (int)AutoRespawn.mc.field_71439_g.field_70165_t + " " + (int)AutoRespawn.mc.field_71439_g.field_70163_u + " " + (int)AutoRespawn.mc.field_71439_g.field_70161_v);
                }
                if (((boolean)this.respawn.getValue() && AutoRespawn.mc.field_71439_g.func_110143_aJ() <= 0.0f) || ((boolean)this.antiDeathScreen.getValue() && AutoRespawn.mc.field_71439_g.func_110143_aJ() > 0.0f)) {
                    event.setCanceled(true);
                    AutoRespawn.mc.field_71439_g.func_71004_bE();
                    if (this.command.getValue() != "") {
                        AutoRespawn.mc.field_71439_g.func_71165_d((String)this.command.getValue());
                    }
                }
            }
        }
        else {
            this.deathHandled = false;
        }
    }
}
