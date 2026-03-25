//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.player;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.api.util.math.*;
import java.util.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.network.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.item.*;
import me.alpha432.oyvey.api.util.*;
import me.alpha432.oyvey.api.features.settings.*;

public class AutoMend extends Module
{
    public static AutoMend INSTANCE;
    Setting<Mode> mode;
    private final Setting<Integer> delay;
    private final Setting<Integer> safetyRange;
    public static Entity target;
    private final Timer delayTimer;
    
    public AutoMend() {
        super("AutoMend", "Automatically throwing exp", Module.Category.PLAYER, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Modes", (Object)Mode.KEY));
        this.delay = (Setting<Integer>)new Setting("Delay", (Object)1, (Object)0, (Object)5);
        this.safetyRange = (Setting<Integer>)new Setting("Range", (Object)7, (Object)0, (Object)20);
        this.delayTimer = new Timer();
    }
    
    public String getDisplayInfo() {
        return "" + this.mode.getValue();
    }
    
    private Entity getTarget() {
        Entity target = null;
        double distance = (float)Resolver.getInstance().range.getValue();
        double maxHealth = 36.0;
        for (final Entity entity : AutoMend.mc.field_71441_e.field_73010_i) {
            if (((boolean)Resolver.getInstance().players.getValue() && entity instanceof EntityPlayer) || ((boolean)Resolver.getInstance().animals.getValue() && EntityUtil.isPassive(entity)) || ((boolean)Resolver.getInstance().mobs.getValue() && EntityUtil.isMobAggressive(entity)) || ((boolean)Resolver.getInstance().vehicles.getValue() && EntityUtil.isVehicle(entity)) || ((boolean)Resolver.getInstance().projectiles.getValue() && EntityUtil.isProjectile(entity))) {
                if (entity instanceof EntityLivingBase && EntityUtil.isntValid(entity, distance)) {
                    continue;
                }
                if (!AutoMend.mc.field_71439_g.func_70685_l(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && AutoMend.mc.field_71439_g.func_70068_e(entity) > MathUtil.square((double)(float)Resolver.getInstance().raytrace.getValue())) {
                    continue;
                }
                if (target == null) {
                    target = entity;
                    distance = AutoMend.mc.field_71439_g.func_70068_e(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
                else {
                    if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer)entity, 18)) {
                        target = entity;
                        break;
                    }
                    if (AutoMend.mc.field_71439_g.func_70068_e(entity) < distance) {
                        target = entity;
                        distance = AutoMend.mc.field_71439_g.func_70068_e(entity);
                        maxHealth = EntityUtil.getHealth(entity);
                    }
                    if (EntityUtil.getHealth(entity) >= maxHealth) {
                        continue;
                    }
                    target = entity;
                    distance = AutoMend.mc.field_71439_g.func_70068_e(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
            }
        }
        return target;
    }
    
    public void onTick() {
        this.throwExp();
    }
    
    private void throwExp() {
        final int oldSlot = AutoMend.mc.field_71439_g.field_71071_by.field_70461_c;
        final int newSlot = InventoryUtil.findHotbarBlock((Class)ItemExpBottle.class);
        if (newSlot != -1 && this.delayTimer.passedMs((long)((int)this.delay.getValue() * 20))) {
            AutoMend.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(newSlot));
            AutoMend.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            AutoMend.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(oldSlot));
            this.delayTimer.reset();
        }
    }
    
    private float getDurabilityPercent(final ItemStack stack) {
        final float maxDurability = (float)stack.func_77958_k();
        final float currentDamage = (float)stack.func_77952_i();
        return 100.0f - currentDamage / maxDurability * 100.0f;
    }
    
    private boolean check() {
        return !NullUtils.nullCheck() && ((Bind)this.bind.getValue()).getKey() != -1;
    }
    
    protected enum Mode
    {
        KEY, 
        MIDDLECLICK;
    }
}
