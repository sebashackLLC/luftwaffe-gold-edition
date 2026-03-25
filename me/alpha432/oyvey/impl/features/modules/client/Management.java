//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client;

import me.alpha432.oyvey.api.features.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.api.features.settings.*;
import java.util.*;
import me.alpha432.oyvey.manager.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.impl.command.*;
import me.alpha432.oyvey.api.util.*;
import me.alpha432.oyvey.impl.features.modules.hud.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Management extends Module
{
    private final HashMap<EntityPlayer, UUID> list;
    private Entity enderPearl;
    private boolean flag;
    public static HashMap<String, Integer> TotemPopContainer;
    private static Management INSTANCE;
    public Setting<String> clientName;
    private Setting<Boolean> pops;
    private Setting<Boolean> lagback;
    public Setting<Boolean> notifyToggles;
    public Setting<Modes> mode;
    public Setting<Boolean> notifyFriends;
    public Setting<Boolean> notifyFriendsDelete;
    private final Map<String, Integer> popMap;
    
    public Management() {
        super("Management", "Notify you about some shit", Module.Category.CLIENT, true, false, false);
        this.clientName = (Setting<String>)this.register(new Setting("Client Name", (Object)"SomaGod.CC"));
        this.pops = (Setting<Boolean>)this.register(new Setting("Pop Counter", (Object)false));
        this.lagback = (Setting<Boolean>)this.register(new Setting("Lagback Detect", (Object)false));
        this.notifyToggles = (Setting<Boolean>)this.register(new Setting("Modules", (Object)false));
        this.mode = (Setting<Modes>)this.register(new Setting("Style", (Object)Modes.Luftwaffe));
        this.notifyFriends = (Setting<Boolean>)this.register(new Setting("Friend Notify", (Object)false));
        this.notifyFriendsDelete = (Setting<Boolean>)this.register(new Setting("Delete", (Object)false, v -> (boolean)this.notifyFriends.getValue()));
        this.popMap = new HashMap<String, Integer>();
        this.setInstance();
        this.list = new HashMap<EntityPlayer, UUID>();
    }
    
    public static Management getInstance() {
        if (Management.INSTANCE == null) {
            Management.INSTANCE = new Management();
        }
        return Management.INSTANCE;
    }
    
    private void setInstance() {
        Management.INSTANCE = this;
    }
    
    public void onEnable() {
        Management.TotemPopContainer.clear();
        this.flag = true;
    }
    
    public void onDeath(final EntityPlayer player) {
        if (Management.TotemPopContainer.containsKey(player.func_70005_c_())) {
            final int l_Count = Management.TotemPopContainer.get(player.func_70005_c_());
            Management.TotemPopContainer.remove(player.func_70005_c_());
            if (this.isOn() && (boolean)this.pops.getValue() && player.func_70005_c_() != Management.mc.field_71439_g.getDisplayNameString()) {
                String displayName = IrcNameManager.getIrcNameForPlayer(player.func_70005_c_());
                if (displayName == null) {
                    displayName = player.func_70005_c_();
                }
                Command.sendMessage(ChatFormatting.GRAY + displayName + " died aftter popping " + l_Count + this.getPopString(l_Count) + " totems");
            }
        }
    }
    
    public void onTotemPop(final EntityPlayer player) {
        if (!fullNullCheck()) {
            int l_Count = 1;
            if (Management.TotemPopContainer.containsKey(player.func_70005_c_())) {
                l_Count = Management.TotemPopContainer.get(player.func_70005_c_());
                final HashMap var10000 = Management.TotemPopContainer;
                final String var10001 = player.func_70005_c_();
                ++l_Count;
                var10000.put(var10001, l_Count);
            }
            else {
                Management.TotemPopContainer.put(player.func_70005_c_(), l_Count);
            }
            if (this.isOn() && (boolean)this.pops.getValue() && player.func_70005_c_() != Management.mc.field_71439_g.getDisplayNameString()) {
                String displayName = IrcNameManager.getIrcNameForPlayer(player.func_70005_c_());
                if (displayName == null) {
                    displayName = player.func_70005_c_();
                }
                Command.sendMessage("" + ChatFormatting.WHITE + ChatFormatting.BOLD + ChatFormatting.WHITE + displayName + ChatFormatting.RESET + TextUtil.coloredString(" popped ", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + ChatFormatting.WHITE + l_Count + this.getPopString(l_Count) + TextUtil.coloredString(" totems", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()));
            }
        }
    }
    
    public String getPopString(final int pops) {
        if (pops == 1) {
            return "st";
        }
        if (pops == 2) {
            return "nd";
        }
        if (pops == 3) {
            return "rd";
        }
        if (pops >= 4 && pops < 21) {
            return "th";
        }
        final int lastDigit = pops % 10;
        if (lastDigit == 1) {
            return "st";
        }
        if (lastDigit == 2) {
            return "nd";
        }
        return (lastDigit == 3) ? "rd" : "th";
    }
    
    public final HashMap<String, Integer> getPopMap() {
        return Management.TotemPopContainer;
    }
    
    public static boolean spawnCheck() {
        return Management.mc.field_71439_g.field_70173_aa > 15;
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent event) {
        if ((boolean)this.lagback.getValue() && !fullNullCheck() && spawnCheck() && event.getPacket() instanceof SPacketPlayerPosLook) {
            Command.sendMessage(ChatFormatting.YELLOW + "Detected Lagback");
        }
    }
    
    static {
        Management.TotemPopContainer = new HashMap<String, Integer>();
        Management.INSTANCE = new Management();
    }
    
    public enum Modes
    {
        Luftwaffe, 
        DotGod, 
        Sn0w;
    }
}
