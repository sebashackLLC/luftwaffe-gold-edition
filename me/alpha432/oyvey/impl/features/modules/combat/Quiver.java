//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.combat;

import me.alpha432.oyvey.api.features.*;
import net.minecraft.util.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.init.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.potion.*;
import java.util.*;
import net.minecraft.item.*;

public final class Quiver extends Module
{
    public final ResourceLocation image;
    private final Setting<Integer> tickDelay;
    private final Setting<Boolean> autoLookUp;
    private final Setting<Boolean> preferPotion;
    private final Setting<Boolean> preferStrength;
    private final Setting<Boolean> preferSpeed;
    
    public Quiver() {
        super("Quiver", "Rotates and shoots yourself with good potion effects", Module.Category.COMBAT, true, false, false);
        this.image = new ResourceLocation("hitmarker.png");
        this.tickDelay = (Setting<Integer>)this.register(new Setting("TickDelay", (Object)3, (Object)0, (Object)8));
        this.autoLookUp = (Setting<Boolean>)this.register(new Setting("AutoLookUp", (Object)true));
        this.preferPotion = (Setting<Boolean>)this.register(new Setting("PreferPotion", (Object)true));
        this.preferStrength = (Setting<Boolean>)this.register(new Setting("PreferStrength", (Object)true));
        this.preferSpeed = (Setting<Boolean>)this.register(new Setting("PreferSpeed", (Object)false));
    }
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || Quiver.mc.field_71439_g == null) {
            return;
        }
        if (Quiver.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() instanceof ItemBow && Quiver.mc.field_71439_g.func_184587_cr() && Quiver.mc.field_71439_g.func_184612_cw() >= (int)this.tickDelay.getValue()) {
            if (this.autoLookUp.getValue()) {
                Quiver.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(Quiver.mc.field_71439_g.field_70177_z, -90.0f, Quiver.mc.field_71439_g.field_70122_E));
            }
            Quiver.mc.field_71442_b.func_78766_c((EntityPlayer)Quiver.mc.field_71439_g);
        }
        if (this.preferPotion.getValue()) {
            this.selectBestArrow();
        }
    }
    
    public void onEnable() {
        super.onEnable();
    }
    
    private int findBow() {
        return InventoryUtil.getItemHotbar((Item)Items.field_151031_f);
    }
    
    private void selectBestArrow() {
        final List<Integer> arrowSlots = (List<Integer>)InventoryUtil.getItemInventory(Items.field_185167_i);
        if (arrowSlots.isEmpty() || arrowSlots.get(0) == -1) {
            return;
        }
        int speedSlot = -1;
        int strengthSlot = -1;
        for (final Integer slot : arrowSlots) {
            final ItemStack stack = Quiver.mc.field_71439_g.field_71071_by.func_70301_a((int)slot);
            if (stack.func_190926_b()) {
                continue;
            }
            final ResourceLocation potionName = PotionUtils.func_185191_c(stack).getRegistryName();
            if (potionName == null) {
                continue;
            }
            final String name = potionName.func_110623_a();
            if (name.contains("swiftness") && speedSlot == -1 && (boolean)this.preferSpeed.getValue()) {
                speedSlot = slot;
            }
            else {
                if (!name.contains("strength") || strengthSlot != -1 || !(boolean)this.preferStrength.getValue()) {
                    continue;
                }
                strengthSlot = slot;
            }
        }
        int targetSlot = -1;
        if ((boolean)this.preferStrength.getValue() && strengthSlot != -1) {
            targetSlot = strengthSlot;
        }
        else if ((boolean)this.preferSpeed.getValue() && speedSlot != -1) {
            targetSlot = speedSlot;
        }
        if (targetSlot != -1 && targetSlot != Quiver.mc.field_71439_g.field_71071_by.field_70461_c && targetSlot < 9) {
            Quiver.mc.field_71439_g.field_71071_by.field_70461_c = targetSlot;
        }
    }
}
