//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.misc;

import me.alpha432.oyvey.api.features.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import me.alpha432.oyvey.manager.*;
import me.alpha432.oyvey.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.impl.command.*;
import java.util.*;

public class PearlNotify extends Module
{
    private final HashMap<EntityPlayer, UUID> list;
    private Entity enderPearl;
    private boolean flag;
    
    public PearlNotify() {
        super("PearlNotify", "Notify pearl throws.", Module.Category.MISC, true, false, false);
        this.list = new HashMap<EntityPlayer, UUID>();
    }
    
    public void onEnable() {
        this.flag = true;
    }
    
    public void onUpdate() {
        if (PearlNotify.mc.field_71441_e == null || PearlNotify.mc.field_71439_g == null) {
            return;
        }
        this.enderPearl = null;
        for (final Entity e : PearlNotify.mc.field_71441_e.field_72996_f) {
            if (e instanceof EntityEnderPearl) {
                this.enderPearl = e;
                break;
            }
        }
        if (this.enderPearl == null) {
            this.flag = true;
            return;
        }
        EntityPlayer closestPlayer = null;
        for (final EntityPlayer entity : PearlNotify.mc.field_71441_e.field_73010_i) {
            if (closestPlayer == null) {
                closestPlayer = entity;
            }
            else {
                if (closestPlayer.func_70032_d(this.enderPearl) <= entity.func_70032_d(this.enderPearl)) {
                    continue;
                }
                closestPlayer = entity;
            }
        }
        if (closestPlayer == PearlNotify.mc.field_71439_g) {
            this.flag = false;
        }
        if (closestPlayer != null && this.flag) {
            String faceing = this.enderPearl.func_174811_aO().toString();
            if (faceing.equals("west")) {
                faceing = "east";
            }
            else if (faceing.equals("east")) {
                faceing = "west";
            }
            String displayName = IrcNameManager.getIrcNameForPlayer(closestPlayer.func_70005_c_());
            if (displayName == null) {
                displayName = closestPlayer.func_70005_c_();
            }
            Command.sendMessage(OyVey.friendManager.isFriend(closestPlayer.func_70005_c_()) ? (ChatFormatting.AQUA + displayName + ChatFormatting.DARK_GRAY + " has just thrown a pearl heading " + faceing + "!") : (ChatFormatting.RED + displayName + ChatFormatting.DARK_GRAY + " has just thrown a pearl heading " + faceing + "!"));
            this.flag = false;
        }
    }
}
