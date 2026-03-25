//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.entity;

import me.alpha432.oyvey.api.interfaces.*;
import java.util.concurrent.atomic.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.block.*;
import java.util.*;
import net.minecraft.client.*;
import net.minecraft.item.*;
import net.minecraft.init.*;
import net.minecraft.enchantment.*;
import me.alpha432.oyvey.api.util.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;

public class InventoryUtil implements Minecraftable
{
    public static int currentItem;
    
    public static void doSwap(final int currentItem) {
        InventoryUtil.mc.field_71439_g.field_71071_by.field_70461_c = currentItem;
        InventoryUtil.mc.field_71442_b.func_78765_e();
    }
    
    public static int findClassInventorySlot(final Class clazz, final boolean b) {
        final AtomicInteger atomicInteger = new AtomicInteger();
        atomicInteger.set(-1);
        for (final Map.Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
            if (clazz.isInstance(entry.getValue().func_77973_b())) {
                if (entry.getKey() == 45 && !b) {
                    continue;
                }
                atomicInteger.set(entry.getKey());
                return atomicInteger.get();
            }
        }
        return atomicInteger.get();
    }
    
    public static int switchToItem(final Item itemIn) {
        final int slot = getItemHotbar(itemIn);
        if (slot == -1) {
            return -1;
        }
        switchToHotbarSlot(slot, false);
        return slot;
    }
    
    public static int findHotbar(final Class clazz) {
        int i = 0;
        while (i < 9) {
            final ItemStack stack = InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack != ItemStack.field_190927_a) {
                if (clazz.isInstance(stack.func_77973_b())) {
                    return i;
                }
                return i;
            }
            else {
                ++i;
            }
        }
        return -1;
    }
    
    public static EnumHand getHand(final int slot) {
        return (slot == -2) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }
    
    public static EnumHand getHand(final Item item) {
        return (InventoryUtil.mc.field_71439_g.func_184614_ca().func_77973_b() == item) ? EnumHand.MAIN_HAND : ((InventoryUtil.mc.field_71439_g.func_184592_cb().func_77973_b() == item) ? EnumHand.OFF_HAND : null);
    }
    
    public static int findHotbarClass(final Class clazz) {
        final ItemStack getStackInSlot = InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(0);
        if (getStackInSlot != ItemStack.field_190927_a) {
            if (clazz.isInstance(getStackInSlot.func_77973_b())) {
                return 0;
            }
            if (getStackInSlot.func_77973_b() instanceof ItemBlock && clazz.isInstance(((ItemBlock)getStackInSlot.func_77973_b()).func_179223_d())) {
                return 0;
            }
        }
        int n = 0;
        ++n;
        return 0;
    }
    
    public static void switchToHotbarSlot(final int slot, final boolean silent) {
        if (InventoryUtil.mc.field_71439_g.field_71071_by.field_70461_c == slot || slot < 0) {
            return;
        }
        if (silent) {
            InventoryUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            InventoryUtil.mc.field_71442_b.func_78765_e();
        }
        else {
            InventoryUtil.mc.field_71439_g.field_71071_by.field_70461_c = slot;
            InventoryUtil.mc.field_71442_b.func_78765_e();
        }
    }
    
    public static int getEmptyXCarry() {
        for (int i = 1; i < 5; ++i) {
            final Slot craftingSlot = InventoryUtil.mc.field_71439_g.field_71070_bA.field_75151_b.get(i);
            final ItemStack craftingStack = craftingSlot.func_75211_c();
            if (craftingStack.func_190926_b() || craftingStack.func_77973_b() == Items.field_190931_a) {
                return i;
            }
        }
        return -1;
    }
    
    public static boolean isSlotEmpty(final int i) {
        final Slot slot = InventoryUtil.mc.field_71439_g.field_71070_bA.field_75151_b.get(i);
        final ItemStack stack = slot.func_75211_c();
        return stack.func_190926_b();
    }
    
    public static int getInventoryItemSlot(final Item item, final boolean hotbar) {
        int n;
        int i;
        for (i = (n = 0), i = (hotbar ? 0 : 9); i < 45; ++i) {
            if (InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == item) {
                return i;
            }
        }
        return -1;
    }
    
    public static void switchToHotbarSlot(final Class clazz, final boolean silent) {
        final int slot = findHotbarBlock(clazz);
        if (slot > -1) {
            switchToHotbarSlot(slot, silent);
        }
    }
    
    public static void push() {
        InventoryUtil.currentItem = InventoryUtil.mc.field_71439_g.field_71071_by.field_70461_c;
    }
    
    public static void pop() {
        InventoryUtil.mc.field_71439_g.field_71071_by.field_70461_c = InventoryUtil.currentItem;
    }
    
    public static void setSlot(final int slot) {
        if (slot > 8 || slot < 0) {
            return;
        }
        InventoryUtil.mc.field_71439_g.field_71071_by.field_70461_c = slot;
    }
    
    public static int pickItem(final int item, final boolean allowInventory) {
        final ArrayList<Object> filter = new ArrayList<Object>();
        for (int i1 = 0; i1 < (allowInventory ? InventoryUtil.mc.field_71439_g.field_71071_by.field_70462_a.size() : 9); ++i1) {
            if (Item.func_150891_b(((ItemStack)InventoryUtil.mc.field_71439_g.field_71071_by.field_70462_a.get(i1)).func_77973_b()) == item) {
                filter.add(InventoryUtil.mc.field_71439_g.field_71071_by.field_70462_a.get(i1));
            }
        }
        if (filter.size() >= 1) {
            return InventoryUtil.mc.field_71439_g.field_71071_by.field_70462_a.indexOf(filter.get(0));
        }
        return -1;
    }
    
    public static boolean isNull(final ItemStack stack) {
        return stack == null || stack.func_77973_b() instanceof ItemAir;
    }
    
    public static int findHotbarBlock(final Class clazz) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (stack != ItemStack.field_190927_a) {
                if (clazz.isInstance(stack.func_77973_b())) {
                    return i;
                }
                if (stack.func_77973_b() instanceof ItemBlock) {
                    final Block block;
                    if (clazz.isInstance(block = ((ItemBlock)stack.func_77973_b()).func_179223_d())) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }
    
    public static int findHotbarBlock(final Block blockIn) {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(i);
            final Block block;
            if (stack != ItemStack.field_190927_a && stack.func_77973_b() instanceof ItemBlock && (block = ((ItemBlock)stack.func_77973_b()).func_179223_d()) == blockIn) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getItemHotbar(final Item input) {
        for (int i = 0; i < 9; ++i) {
            final Item item = InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
            if (Item.func_150891_b(item) == Item.func_150891_b(input)) {
                return i;
            }
        }
        return -1;
    }
    
    public static void switchToSlot(final int slot) {
        if (InventoryUtil.mc.field_71439_g.field_71071_by.field_70461_c == slot || slot < 0) {
            return;
        }
        InventoryUtil.mc.field_71439_g.field_71071_by.field_70461_c = slot;
        InventoryUtil.mc.field_71442_b.func_78765_e();
    }
    
    public static int getItemHotbars(final Item input) {
        for (int i = 0; i < 36; ++i) {
            final Item item = InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
            if (Item.func_150891_b(item) == Item.func_150891_b(input)) {
                return i;
            }
        }
        return -1;
    }
    
    public static int findStackInventory(final Item input) {
        return findStackInventory(input, false);
    }
    
    public static int findStackInventory(final Item input, final boolean withHotbar) {
        int n;
        int i;
        int n2;
        Item item;
        for (i = (n = 0), i = (n2 = (withHotbar ? 0 : 9)); i < 36; ++i) {
            item = InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
            if (Item.func_150891_b(input) == Item.func_150891_b(item)) {
                return i + ((i < 9) ? 36 : 0);
            }
        }
        return -1;
    }
    
    public static int findItemInventorySlot(final Item item, final boolean offHand) {
        final AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        for (final Map.Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue().func_77973_b() == item) {
                if (entry.getKey() == 45 && !offHand) {
                    continue;
                }
                slot.set(entry.getKey());
                return slot.get();
            }
        }
        return slot.get();
    }
    
    public static int findItemInHotbar(final Item item) {
        int index = -1;
        for (int i = 0; i < 9; ++i) {
            if (InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b() == item) {
                index = i;
                break;
            }
        }
        return index;
    }
    
    public static List<Integer> getItemInventory(final Item item) {
        final ArrayList<Integer> ints = new ArrayList<Integer>();
        for (int i = 9; i < 36; ++i) {
            final Item target = InventoryUtil.mc.field_71439_g.field_71071_by.func_70301_a(i).func_77973_b();
            if (item instanceof ItemBlock) {
                if (((ItemBlock)item).func_179223_d().equals(item)) {
                    ints.add(i);
                }
            }
        }
        if (ints.size() == 0) {
            ints.add(-1);
        }
        return ints;
    }
    
    public static List<Integer> findEmptySlots(final boolean withXCarry) {
        final ArrayList<Integer> outPut = new ArrayList<Integer>();
        for (final Map.Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
            if (!entry.getValue().field_190928_g && entry.getValue().func_77973_b() != Items.field_190931_a) {
                continue;
            }
            outPut.add(entry.getKey());
        }
        if (withXCarry) {
            for (int i = 1; i < 5; ++i) {
                final Slot craftingSlot = InventoryUtil.mc.field_71439_g.field_71070_bA.field_75151_b.get(i);
                final ItemStack craftingStack = craftingSlot.func_75211_c();
                if (craftingStack.func_190926_b() || craftingStack.func_77973_b() == Items.field_190931_a) {
                    outPut.add(i);
                }
            }
        }
        return outPut;
    }
    
    public static boolean isBlock(final Item item, final Class clazz) {
        if (item instanceof ItemBlock) {
            final Block block = ((ItemBlock)item).func_179223_d();
            return clazz.isInstance(block);
        }
        return false;
    }
    
    public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        return getInventorySlots(9, 44);
    }
    
    private static Map<Integer, ItemStack> getInventorySlots(final int currentI, final int last) {
        final HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<Integer, ItemStack>();
        for (int current = currentI; current <= last; ++current) {
            fullInventorySlots.put(current, (ItemStack)InventoryUtil.mc.field_71439_g.field_71070_bA.func_75138_a().get(current));
        }
        return fullInventorySlots;
    }
    
    public static boolean holdingItem(final Class clazz) {
        boolean result = false;
        final ItemStack stack = InventoryUtil.mc.field_71439_g.func_184614_ca();
        result = isInstanceOf(stack, clazz);
        if (!result) {
            final ItemStack offhand = InventoryUtil.mc.field_71439_g.func_184592_cb();
            result = isInstanceOf(stack, clazz);
        }
        return result;
    }
    
    public static boolean isInstanceOf(final ItemStack stack, final Class clazz) {
        if (stack == null) {
            return false;
        }
        final Item item = stack.func_77973_b();
        if (clazz.isInstance(item)) {
            return true;
        }
        if (item instanceof ItemBlock) {
            final Block block = Block.func_149634_a(item);
            return clazz.isInstance(block);
        }
        return false;
    }
    
    public static int findArmorSlot(final EntityEquipmentSlot type2, final boolean binding) {
        int slot = -1;
        float damage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            final ItemStack s = Minecraft.func_71410_x().field_71439_g.field_71070_bA.func_75139_a(i).func_75211_c();
            if (s.func_77973_b() != Items.field_190931_a && s.func_77973_b() instanceof ItemArmor) {
                final ItemArmor armor;
                if ((armor = (ItemArmor)s.func_77973_b()).field_77881_a == type2) {
                    final float currentDamage = (float)(armor.field_77879_b + EnchantmentHelper.func_77506_a(Enchantments.field_180310_c, s));
                    final boolean bl;
                    final boolean cursed = bl = (binding && EnchantmentHelper.func_190938_b(s));
                    if (currentDamage > damage) {
                        if (!cursed) {
                            damage = currentDamage;
                            slot = i;
                        }
                    }
                }
            }
        }
        return slot;
    }
    
    public static int findArmorSlot(final EntityEquipmentSlot type2, final boolean binding, final boolean withXCarry) {
        int slot = findArmorSlot(type2, binding);
        if (slot == -1 && withXCarry) {
            float damage = 0.0f;
            for (int i = 1; i < 5; ++i) {
                final Slot craftingSlot = InventoryUtil.mc.field_71439_g.field_71070_bA.field_75151_b.get(i);
                final ItemStack craftingStack = craftingSlot.func_75211_c();
                if (craftingStack.func_77973_b() != Items.field_190931_a && craftingStack.func_77973_b() instanceof ItemArmor) {
                    final ItemArmor armor;
                    if ((armor = (ItemArmor)craftingStack.func_77973_b()).field_77881_a == type2) {
                        final float currentDamage = (float)(armor.field_77879_b + EnchantmentHelper.func_77506_a(Enchantments.field_180310_c, craftingStack));
                        final boolean bl;
                        final boolean cursed = bl = (binding && EnchantmentHelper.func_190938_b(craftingStack));
                        if (currentDamage > damage) {
                            if (!cursed) {
                                damage = currentDamage;
                                slot = i;
                            }
                        }
                    }
                }
            }
        }
        return slot;
    }
    
    public static int findItemInventorySlot(final Item item, final boolean offHand, final boolean withXCarry) {
        int slot = findItemInventorySlot(item, offHand);
        if (slot == -1 && withXCarry) {
            for (int i = 1; i < 5; ++i) {
                final Slot craftingSlot = InventoryUtil.mc.field_71439_g.field_71070_bA.field_75151_b.get(i);
                final ItemStack craftingStack = craftingSlot.func_75211_c();
                if (craftingStack.func_77973_b() != Items.field_190931_a) {
                    final Item craftingStackItem;
                    if ((craftingStackItem = craftingStack.func_77973_b()) == item) {
                        slot = i;
                    }
                }
            }
        }
        return slot;
    }
    
    public enum Switch
    {
        NORMAL, 
        SILENT, 
        NONE;
    }
    
    public static class Task
    {
        private final int slot;
        private final boolean update;
        private final boolean quickClick;
        
        public Task() {
            this.update = true;
            this.slot = -1;
            this.quickClick = false;
        }
        
        public Task(final int slot) {
            this.slot = slot;
            this.quickClick = false;
            this.update = false;
        }
        
        public Task(final int slot, final boolean quickClick) {
            this.slot = slot;
            this.quickClick = quickClick;
            this.update = false;
        }
        
        public void run() {
            if (this.update) {
                Util.mc.field_71442_b.func_78765_e();
            }
            if (this.slot != -1) {
                Util.mc.field_71442_b.func_78765_e();
                Util.mc.field_71442_b.func_187098_a(0, this.slot, 0, this.quickClick ? ClickType.QUICK_MOVE : ClickType.PICKUP, (EntityPlayer)Util.mc.field_71439_g);
            }
        }
    }
}
