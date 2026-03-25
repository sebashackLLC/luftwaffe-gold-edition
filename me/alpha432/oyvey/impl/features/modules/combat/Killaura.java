//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.combat;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.api.util.entity.*;
import me.alpha432.oyvey.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.api.util.math.*;
import java.util.*;

public class Killaura extends Module
{
    public static Entity target;
    private final Timer timer;
    public Setting<Mode> mode;
    public Setting<Float> range;
    public Setting<Boolean> delay;
    public Setting<Boolean> rotate;
    public Setting<Boolean> onlySharp;
    public Setting<Float> raytrace;
    public Setting<Boolean> players;
    public Setting<Boolean> mobs;
    public Setting<Boolean> animals;
    public Setting<Boolean> vehicles;
    public Setting<Boolean> projectiles;
    public Setting<Boolean> tps;
    public Setting<Boolean> packet;
    
    public Killaura() {
        super("Killaura", "Kills aura.", Module.Category.COMBAT, true, false, false);
        this.timer = new Timer();
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (Object)Mode.NORMAL));
        this.range = (Setting<Float>)this.register(new Setting("Range", (Object)6.0f, (Object)0.1f, (Object)7.0f));
        this.delay = (Setting<Boolean>)this.register(new Setting("HitDelay", (Object)true));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (Object)true));
        this.onlySharp = (Setting<Boolean>)this.register(new Setting("SwordOnly", (Object)true));
        this.raytrace = (Setting<Float>)this.register(new Setting("Raytrace", (Object)6.0f, (Object)0.1f, (Object)7.0f, "Wall Range."));
        this.players = (Setting<Boolean>)this.register(new Setting("Players", (Object)true));
        this.mobs = (Setting<Boolean>)this.register(new Setting("Mobs", (Object)false));
        this.animals = (Setting<Boolean>)this.register(new Setting("Animals", (Object)false));
        this.vehicles = (Setting<Boolean>)this.register(new Setting("Entities", (Object)false));
        this.projectiles = (Setting<Boolean>)this.register(new Setting("Projectiles", (Object)false));
        this.tps = (Setting<Boolean>)this.register(new Setting("TpsSync", (Object)true));
        this.packet = (Setting<Boolean>)this.register(new Setting("Packet", (Object)false));
    }
    
    public void onTick() {
        if (!(boolean)this.rotate.getValue()) {
            this.doKillaura();
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayerEvent(final UpdateWalkingPlayerEvent event) {
        if (event.getStage() == 0 && (boolean)this.rotate.getValue()) {
            this.doKillaura();
        }
    }
    
    private void doKillaura() {
        if (this.mode.getValue() == Mode.NONE) {
            return;
        }
        if ((boolean)this.onlySharp.getValue() && !EntityUtil.holdingWeapon((EntityPlayer)Killaura.mc.field_71439_g)) {
            Killaura.target = null;
            return;
        }
        final int wait = this.delay.getValue() ? ((int)(DamageUtil.getCooldownByWeapon((EntityPlayer)Killaura.mc.field_71439_g) * (this.tps.getValue() ? OyVey.serverManager.getTpsFactor() : 1.0f))) : 0;
        if (!this.timer.passedMs((long)wait)) {
            return;
        }
        Killaura.target = this.getTarget();
        if (Killaura.target == null) {
            return;
        }
        if (this.rotate.getValue()) {
            OyVey.rotationManager.lookAtEntity(Killaura.target);
        }
        EntityUtil.attackEntity(Killaura.target, (boolean)this.packet.getValue(), true);
        this.timer.reset();
    }
    
    private Entity getTarget() {
        Entity target = null;
        double distance = (float)this.range.getValue();
        double maxHealth = 36.0;
        for (final Entity entity : Killaura.mc.field_71441_e.field_73010_i) {
            if (((boolean)this.players.getValue() && entity instanceof EntityPlayer) || ((boolean)this.animals.getValue() && EntityUtil.isPassive(entity)) || ((boolean)this.mobs.getValue() && EntityUtil.isMobAggressive(entity)) || ((boolean)this.vehicles.getValue() && EntityUtil.isVehicle(entity)) || ((boolean)this.projectiles.getValue() && EntityUtil.isProjectile(entity))) {
                if (entity instanceof EntityLivingBase && EntityUtil.isntValid(entity, distance)) {
                    continue;
                }
                if (!Killaura.mc.field_71439_g.func_70685_l(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && Killaura.mc.field_71439_g.func_70068_e(entity) > MathUtil.square((double)(float)this.raytrace.getValue())) {
                    continue;
                }
                if (target == null) {
                    target = entity;
                    distance = Killaura.mc.field_71439_g.func_70068_e(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
                else {
                    if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer)entity, 18)) {
                        target = entity;
                        break;
                    }
                    if (Killaura.mc.field_71439_g.func_70068_e(entity) < distance) {
                        target = entity;
                        distance = Killaura.mc.field_71439_g.func_70068_e(entity);
                        maxHealth = EntityUtil.getHealth(entity);
                    }
                    if (EntityUtil.getHealth(entity) >= maxHealth) {
                        continue;
                    }
                    target = entity;
                    distance = Killaura.mc.field_71439_g.func_70068_e(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
            }
        }
        return target;
    }
    
    public String getDisplayInfo() {
        if (Killaura.target instanceof EntityPlayer) {
            return Killaura.target.func_70005_c_();
        }
        return null;
    }
    
    public enum Mode
    {
        NONE, 
        NORMAL;
    }
}
