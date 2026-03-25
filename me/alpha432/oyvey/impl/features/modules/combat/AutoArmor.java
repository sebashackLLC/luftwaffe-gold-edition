//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.combat;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.api.util.entity.*;
import java.util.concurrent.*;
import net.minecraft.client.gui.inventory.*;
import me.alpha432.oyvey.*;
import net.minecraft.entity.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import java.util.*;
import net.minecraft.entity.player.*;

public class AutoArmor extends Module
{
    private final Setting<Integer> delay;
    private final Setting<Boolean> curse;
    private final Setting<Boolean> mendingTakeOff;
    private final Setting<Integer> closestEnemy;
    private final Setting<Integer> repair;
    private final Setting<Integer> actions;
    private final Timer timer;
    private final Queue<InventoryUtil.Task> taskList;
    private final List<Integer> doneSlots;
    boolean flag;
    
    public AutoArmor() {
        super("AutoArmor", "Puts Armor on for you.", Module.Category.COMBAT, true, false, false);
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (Object)50, (Object)0, (Object)500));
        this.curse = (Setting<Boolean>)this.register(new Setting("Vanishing", (Object)false));
        this.mendingTakeOff = (Setting<Boolean>)this.register(new Setting("AutoMend", (Object)false));
        this.closestEnemy = (Setting<Integer>)this.register(new Setting("Enemy", (Object)8, (Object)1, (Object)20, v -> (boolean)this.mendingTakeOff.getValue()));
        this.repair = (Setting<Integer>)this.register(new Setting("Repair%", (Object)80, (Object)1, (Object)100, v -> (boolean)this.mendingTakeOff.getValue()));
        this.actions = (Setting<Integer>)this.register(new Setting("Packets", (Object)3, (Object)1, (Object)12));
        this.timer = new Timer();
        this.taskList = new ConcurrentLinkedQueue<InventoryUtil.Task>();
        this.doneSlots = new ArrayList<Integer>();
    }
    
    public void onLogin() {
        this.timer.reset();
    }
    
    public void onDisable() {
        this.taskList.clear();
        this.doneSlots.clear();
        this.flag = false;
    }
    
    public void onLogout() {
        this.taskList.clear();
        this.doneSlots.clear();
    }
    
    public void onTick() {
        if (fullNullCheck() || (AutoArmor.mc.field_71462_r instanceof GuiContainer && !(AutoArmor.mc.field_71462_r instanceof GuiInventory))) {
            return;
        }
        if (this.taskList.isEmpty()) {
            if ((boolean)this.mendingTakeOff.getValue() && InventoryUtil.holdingItem((Class)ItemExpBottle.class) && AutoArmor.mc.field_71474_y.field_74313_G.func_151470_d() && AutoArmor.mc.field_71441_e.field_73010_i.stream().noneMatch(e -> e != AutoArmor.mc.field_71439_g && !OyVey.friendManager.isFriend(((EntityPlayer)e).func_70005_c_()) && AutoArmor.mc.field_71439_g.func_70032_d((Entity)e) <= (int)this.closestEnemy.getValue()) && !this.flag) {
                int takeOff = 0;
                for (final Map.Entry<Integer, ItemStack> armorSlot : this.getArmor().entrySet()) {
                    final ItemStack stack = armorSlot.getValue();
                    final float percent = (int)this.repair.getValue() / 100.0f;
                    final int dam = Math.round(stack.func_77958_k() * percent);
                    final int goods;
                    if (dam >= (goods = stack.func_77958_k() - stack.func_77952_i())) {
                        continue;
                    }
                    ++takeOff;
                }
                if (takeOff == 4) {
                    this.flag = true;
                }
                if (!this.flag) {
                    final ItemStack itemStack1 = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(5).func_75211_c();
                    if (!itemStack1.field_190928_g) {
                        final float percent2 = (int)this.repair.getValue() / 100.0f;
                        final int dam2 = Math.round(itemStack1.func_77958_k() * percent2);
                        final int goods2;
                        if (dam2 < (goods2 = itemStack1.func_77958_k() - itemStack1.func_77952_i())) {
                            this.takeOffSlot(5);
                        }
                    }
                    final ItemStack itemStack2 = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(6).func_75211_c();
                    if (!itemStack2.field_190928_g) {
                        final float percent = (int)this.repair.getValue() / 100.0f;
                        final int dam3 = Math.round(itemStack2.func_77958_k() * percent);
                        final int goods3;
                        if (dam3 < (goods3 = itemStack2.func_77958_k() - itemStack2.func_77952_i())) {
                            this.takeOffSlot(6);
                        }
                    }
                    final ItemStack itemStack3 = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(7).func_75211_c();
                    if (!itemStack3.field_190928_g) {
                        final float percent = (int)this.repair.getValue() / 100.0f;
                        final int dam = Math.round(itemStack3.func_77958_k() * percent);
                        final int goods;
                        if (dam < (goods = itemStack3.func_77958_k() - itemStack3.func_77952_i())) {
                            this.takeOffSlot(7);
                        }
                    }
                    final ItemStack itemStack4 = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(8).func_75211_c();
                    if (!itemStack4.field_190928_g) {
                        final float percent3 = (int)this.repair.getValue() / 100.0f;
                        final int dam4 = Math.round(itemStack4.func_77958_k() * percent3);
                        final int goods4;
                        if (dam4 < (goods4 = itemStack4.func_77958_k() - itemStack4.func_77952_i())) {
                            this.takeOffSlot(8);
                        }
                    }
                }
                return;
            }
            this.flag = false;
            final ItemStack helm = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(5).func_75211_c();
            final int slot4;
            if (helm.func_77973_b() == Items.field_190931_a && (slot4 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.HEAD, (boolean)this.curse.getValue(), true)) != -1) {
                this.getSlotOn(5, slot4);
            }
            final ItemStack chest;
            final int slot5;
            if ((chest = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(6).func_75211_c()).func_77973_b() == Items.field_190931_a && (slot5 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.CHEST, (boolean)this.curse.getValue(), true)) != -1) {
                this.getSlotOn(6, slot5);
            }
            final ItemStack legging;
            final int slot6;
            if ((legging = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(7).func_75211_c()).func_77973_b() == Items.field_190931_a && (slot6 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.LEGS, (boolean)this.curse.getValue(), true)) != -1) {
                this.getSlotOn(7, slot6);
            }
            final ItemStack feet;
            final int slot7;
            if ((feet = AutoArmor.mc.field_71439_g.field_71069_bz.func_75139_a(8).func_75211_c()).func_77973_b() == Items.field_190931_a && (slot7 = InventoryUtil.findArmorSlot(EntityEquipmentSlot.FEET, (boolean)this.curse.getValue(), true)) != -1) {
                this.getSlotOn(8, slot7);
            }
        }
        if (this.timer.passedMs((long)(int)((int)this.delay.getValue() * OyVey.serverManager.getTpsFactor()))) {
            if (!this.taskList.isEmpty()) {
                for (int i = 0; i < (int)this.actions.getValue(); ++i) {
                    final InventoryUtil.Task task = this.taskList.poll();
                    if (task != null) {
                        task.run();
                    }
                }
            }
            this.timer.reset();
        }
    }
    
    private void takeOffSlot(final int slot) {
        if (this.taskList.isEmpty()) {
            int target = -1;
            for (final int i : InventoryUtil.findEmptySlots(true)) {
                if (this.doneSlots.contains(target)) {
                    continue;
                }
                target = i;
                this.doneSlots.add(i);
            }
            if (target != -1) {
                this.taskList.add(new InventoryUtil.Task(slot));
                this.taskList.add(new InventoryUtil.Task(target));
                this.taskList.add(new InventoryUtil.Task());
            }
        }
    }
    
    private void getSlotOn(final int slot, final int target) {
        if (this.taskList.isEmpty()) {
            this.doneSlots.remove((Object)target);
            this.taskList.add(new InventoryUtil.Task(target));
            this.taskList.add(new InventoryUtil.Task(slot));
            this.taskList.add(new InventoryUtil.Task());
        }
    }
    
    private Map<Integer, ItemStack> getArmor() {
        return this.getInventorySlots(5, 8);
    }
    
    private Map<Integer, ItemStack> getInventorySlots(int current, final int last) {
        final HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        while (current <= last) {
            fullInventorySlots.put(current, (ItemStack)AutoArmor.mc.field_71439_g.field_71069_bz.func_75138_a().get(current));
            ++current;
        }
        return fullInventorySlots;
    }
}
