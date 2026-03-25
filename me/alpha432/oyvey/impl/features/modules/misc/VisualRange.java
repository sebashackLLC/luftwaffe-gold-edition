//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.misc;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.manager.*;
import me.alpha432.oyvey.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.impl.features.modules.hud.*;
import me.alpha432.oyvey.impl.command.*;
import net.minecraft.init.*;
import me.alpha432.oyvey.api.util.*;
import net.minecraft.entity.item.*;
import java.util.*;

public class VisualRange extends Module
{
    public Setting<Boolean> visualrange;
    public Setting<Boolean> VisualRangeSound;
    public Setting<Boolean> coords;
    public Setting<Boolean> leaving;
    public Setting<Boolean> pearls;
    private List<EntityPlayer> knownPlayers;
    private Entity enderPearl;
    private boolean flag;
    
    public VisualRange() {
        super("VisualRange", "Notify you about new entities in render distance", Module.Category.MISC, true, false, false);
        this.visualrange = (Setting<Boolean>)this.register(new Setting("Players", (Object)false));
        this.VisualRangeSound = (Setting<Boolean>)this.register(new Setting("Sound", (Object)true));
        this.coords = (Setting<Boolean>)this.register(new Setting("Coords", (Object)false));
        this.leaving = (Setting<Boolean>)this.register(new Setting("Leaving", (Object)true));
        this.pearls = (Setting<Boolean>)this.register(new Setting("Pearls", (Object)false));
        this.knownPlayers = new ArrayList<EntityPlayer>();
    }
    
    public void onEnable() {
        new ArrayList();
        this.knownPlayers = new ArrayList<EntityPlayer>();
    }
    
    public void onUpdate() {
        final ArrayList<EntityPlayer> tickPlayerList = new ArrayList<EntityPlayer>(VisualRange.mc.field_71441_e.field_73010_i);
        if (tickPlayerList.size() > 0) {
            for (final EntityPlayer player : tickPlayerList) {
                if (!player.func_70005_c_().equals(VisualRange.mc.field_71439_g.func_70005_c_()) && !this.knownPlayers.contains(player) && (boolean)this.visualrange.getValue()) {
                    this.knownPlayers.add(player);
                    String displayName = IrcNameManager.getIrcNameForPlayer(player.func_70005_c_());
                    if (displayName == null) {
                        displayName = player.func_70005_c_();
                    }
                    if (OyVey.friendManager.isFriend(player)) {
                        Command.sendMessage(ChatFormatting.WHITE + displayName + TextUtil.coloredString(" Has entered your Visual Range", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + (this.coords.getValue() ? (TextUtil.coloredString(" at ", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + ChatFormatting.WHITE + (int)player.field_70165_t + " " + (int)player.field_70161_v + "") : ""));
                    }
                    else {
                        Command.sendMessage(ChatFormatting.WHITE + displayName + TextUtil.coloredString(" Has entered your Visual Range", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + (this.coords.getValue() ? (TextUtil.coloredString(" at ", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + ChatFormatting.WHITE + (int)player.field_70165_t + " " + (int)player.field_70161_v + "") : ""));
                    }
                    if (this.VisualRangeSound.getValue()) {
                        VisualRange.mc.field_71439_g.func_184185_a(SoundEvents.field_187604_bf, 1.0f, 1.0f);
                    }
                    return;
                }
            }
        }
        if (this.knownPlayers.size() > 0) {
            for (final EntityPlayer player : this.knownPlayers) {
                if (!tickPlayerList.contains(player) && (boolean)this.visualrange.getValue()) {
                    this.knownPlayers.remove(player);
                    if (this.leaving.getValue()) {
                        String displayName = IrcNameManager.getIrcNameForPlayer(player.func_70005_c_());
                        if (displayName == null) {
                            displayName = player.func_70005_c_();
                        }
                        if (OyVey.friendManager.isFriend(player)) {
                            Command.sendMessage(ChatFormatting.WHITE + displayName + TextUtil.coloredString(" Has left your Visual Range", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + (this.coords.getValue() ? (TextUtil.coloredString(" at ", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + ChatFormatting.WHITE + (int)player.field_70165_t + " " + (int)player.field_70161_v + "") : ""));
                        }
                        else {
                            Command.sendMessage(ChatFormatting.WHITE + displayName + TextUtil.coloredString(" Has left your VisualRange", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + (this.coords.getValue() ? (TextUtil.coloredString(" at ", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + ChatFormatting.WHITE + (int)player.field_70165_t + " " + (int)player.field_70161_v + "") : ""));
                        }
                    }
                    return;
                }
            }
        }
        if (!(Util.mc.field_71441_e == null | Util.mc.field_71439_g == null)) {
            final List<String> peoplenew = new ArrayList<String>();
            final List<EntityPlayer> playerEntities = (List<EntityPlayer>)Util.mc.field_71441_e.field_73010_i;
            for (final Entity e : playerEntities) {
                if (!e.func_70005_c_().equals(Util.mc.field_71439_g.func_70005_c_())) {
                    peoplenew.add(e.func_70005_c_());
                }
            }
        }
        if (PearlNotify.mc.field_71441_e == null || PearlNotify.mc.field_71439_g == null) {
            return;
        }
        this.enderPearl = null;
        for (final Entity e2 : PearlNotify.mc.field_71441_e.field_72996_f) {
            if (e2 instanceof EntityEnderPearl) {
                this.enderPearl = e2;
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
                faceing = "+x";
            }
            else if (faceing.equals("east")) {
                faceing = "-x";
            }
            else if (faceing.equals("north")) {
                faceing = "-z";
            }
            else if (faceing.equals("south")) {
                faceing = "+z";
            }
            if ((boolean)this.pearls.getValue() && this.isOn()) {
                String displayName2 = IrcNameManager.getIrcNameForPlayer(closestPlayer.func_70005_c_());
                if (displayName2 == null) {
                    displayName2 = closestPlayer.func_70005_c_();
                }
                Command.sendMessage("" + ChatFormatting.WHITE + ChatFormatting.BOLD + ChatFormatting.WHITE + displayName2 + ChatFormatting.RESET + TextUtil.coloredString(" has thrown a pearl in ", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + ChatFormatting.WHITE + faceing + TextUtil.coloredString(" direction", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()));
                this.flag = false;
            }
        }
    }
}
