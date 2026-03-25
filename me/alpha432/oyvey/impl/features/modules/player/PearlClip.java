//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.player;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.init.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.impl.command.*;
import me.alpha432.oyvey.api.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;
import net.minecraft.client.network.*;

public class PearlClip extends Module
{
    private static PearlClip INSTANCE;
    Setting<Boolean> bypass;
    
    public PearlClip() {
        super("PearlClip", "Allows you to eat while mining", Module.Category.PLAYER, false, false, false);
        this.bypass = (Setting<Boolean>)this.register(new Setting("Bypass", (Object)true));
        this.setInstance();
    }
    
    public static PearlClip getInstance() {
        if (PearlClip.INSTANCE == null) {
            PearlClip.INSTANCE = new PearlClip();
        }
        return PearlClip.INSTANCE;
    }
    
    private void setInstance() {
        PearlClip.INSTANCE = this;
    }
    
    public void onEnable() {
        super.onEnable();
        if (PearlClip.mc.field_71439_g == null || PearlClip.mc.field_71441_e == null) {
            this.toggle();
            return;
        }
        final int slot = InventoryUtil.findItemInHotbar(Items.field_151079_bi);
        final int slot2 = InventoryUtil.findHotbarBlock(Blocks.field_150343_Z);
        if (slot != -1) {
            if (slot2 == -1 && (boolean)this.bypass.getValue()) {
                Command.sendMessage(ChatFormatting.RED + "[PearlClip] Disabled, no Obsidian");
            }
            else {
                final int lastSlot;
                final float aimYaw;
                final float aimPitch;
                final int n;
                int x;
                int z;
                NetHandlerPlayClient field_71174_a;
                final CPacketPlayerTryUseItemOnBlock cPacketPlayerTryUseItemOnBlock;
                final int n2;
                Locks.acquire(Locks.PLACE_SWITCH_LOCK, () -> {
                    lastSlot = PearlClip.mc.field_71439_g.field_71071_by.field_70461_c;
                    aimYaw = PearlClip.mc.field_71439_g.field_70177_z;
                    aimPitch = 80.0f;
                    if (this.bypass.getValue()) {
                        InventoryUtil.switchToSlot(n);
                        x = (int)Math.floor(PearlClip.mc.field_71439_g.field_70165_t);
                        z = (int)Math.floor(PearlClip.mc.field_71439_g.field_70161_v);
                        field_71174_a = PearlClip.mc.field_71439_g.field_71174_a;
                        new CPacketPlayerTryUseItemOnBlock(new BlockPos(x, 0, z), EnumFacing.DOWN, EnumHand.MAIN_HAND, (float)x, -1.0f, (float)z);
                        field_71174_a.func_147297_a((Packet)cPacketPlayerTryUseItemOnBlock);
                        InventoryUtil.switchToSlot(lastSlot);
                    }
                    InventoryUtil.switchToSlot(n2);
                    PearlClip.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(aimYaw, aimPitch, PearlClip.mc.field_71439_g.field_70122_E));
                    PearlClip.mc.field_71442_b.func_187101_a((EntityPlayer)PearlClip.mc.field_71439_g, (World)PearlClip.mc.field_71441_e, InventoryUtil.getHand(n2));
                    InventoryUtil.switchToSlot(lastSlot);
                    this.toggle();
                });
            }
        }
        else if (slot == -1) {
            if (slot2 == -1 && (boolean)this.bypass.getValue()) {
                Command.sendMessage(ChatFormatting.RED + "[PearlClip] Disabled, no Obsidian and Ender Pearl");
            }
            else {
                Command.sendMessage(ChatFormatting.RED + "[PearlClip] Disabled, no Ender Pearl");
            }
        }
    }
    
    public String getDescription() {
        return "Allows you to clip in wall only by pressing one button";
    }
    
    static {
        PearlClip.INSTANCE = new PearlClip();
    }
}
