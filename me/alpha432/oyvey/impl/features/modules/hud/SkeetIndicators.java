//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.api.util.world.hole.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.*;
import java.util.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraftforge.client.event.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import java.util.function.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class SkeetIndicators extends Module
{
    public static Entity target;
    private static SkeetIndicators INSTANCE;
    public Setting<String> clientName;
    public Setting<Float> range;
    public Setting<Float> raytrace;
    public Setting<Boolean> players;
    public Setting<Boolean> mobs;
    public Setting<Boolean> animals;
    public Setting<Boolean> vehicles;
    public Setting<Boolean> projectiles;
    
    public SkeetIndicators() {
        super("500$", ChatFormatting.DARK_GREEN + "Skeet" + ChatFormatting.WHITE + " indicators", Module.Category.HUD, true, false, false);
        this.clientName = (Setting<String>)this.register(new Setting("Text", (Object)"SomaGod.CC"));
        this.range = (Setting<Float>)this.register(new Setting("Range", (Object)4.0f, (Object)0.1f, (Object)10.0f));
        this.raytrace = (Setting<Float>)this.register(new Setting("Raytrace", (Object)3.5f, (Object)0.1f, (Object)7.0f, "Wall Range."));
        this.players = (Setting<Boolean>)new Setting("Players", (Object)true);
        this.mobs = (Setting<Boolean>)new Setting("Mobs", (Object)false);
        this.animals = (Setting<Boolean>)new Setting("Animals", (Object)false);
        this.vehicles = (Setting<Boolean>)new Setting("Entities", (Object)false);
        this.projectiles = (Setting<Boolean>)new Setting("Projectiles", (Object)false);
        this.setInstance();
    }
    
    public static SkeetIndicators getInstance() {
        if (SkeetIndicators.INSTANCE == null) {
            SkeetIndicators.INSTANCE = new SkeetIndicators();
        }
        return SkeetIndicators.INSTANCE;
    }
    
    private void setInstance() {
        SkeetIndicators.INSTANCE = this;
    }
    
    public int getPlrColor(final Entity target) {
        if (target == null) {
            return 16711680;
        }
        final BlockPos pos = target.func_180425_c();
        if (HoleUtils.isBedrockHoles(pos)) {
            return 65280;
        }
        if (HoleUtils.isObbyHole(pos)) {
            return 16753920;
        }
        if (HoleUtils.isHole(pos)) {
            return 16776960;
        }
        return 16711680;
    }
    
    int getHtrColor() {
        boolean hasEnemyNearby = false;
        for (final Entity entity : SkeetIndicators.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityPlayer && entity != SkeetIndicators.mc.field_71439_g) {
                final EntityPlayer player = (EntityPlayer)entity;
                if (SkeetIndicators.mc.field_71439_g.func_70032_d((Entity)player) <= (float)this.range.getValue() && !OyVey.friendManager.isFriend(player.func_70005_c_())) {
                    hasEnemyNearby = true;
                    break;
                }
                continue;
            }
        }
        return hasEnemyNearby ? 65280 : 16711680;
    }
    
    private Entity getTarget() {
        Entity target = null;
        double distance = (float)this.range.getValue();
        double maxHealth = 36.0;
        for (final Entity entity : SkeetIndicators.mc.field_71441_e.field_73010_i) {
            if (entity == SkeetIndicators.mc.field_71439_g) {
                continue;
            }
            if ((!(boolean)this.players.getValue() || !(entity instanceof EntityPlayer)) && (!(boolean)this.animals.getValue() || !EntityUtil.isPassive(entity)) && (!(boolean)this.mobs.getValue() || !EntityUtil.isMobAggressive(entity)) && (!(boolean)this.vehicles.getValue() || !EntityUtil.isVehicle(entity)) && (!(boolean)this.projectiles.getValue() || !EntityUtil.isProjectile(entity))) {
                continue;
            }
            if (entity instanceof EntityLivingBase && EntityUtil.isntValid(entity, distance)) {
                continue;
            }
            if (!SkeetIndicators.mc.field_71439_g.func_70685_l(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && SkeetIndicators.mc.field_71439_g.func_70068_e(entity) > MathUtil.square((double)(float)this.raytrace.getValue())) {
                continue;
            }
            if (target == null) {
                target = entity;
                distance = SkeetIndicators.mc.field_71439_g.func_70068_e(entity);
                maxHealth = EntityUtil.getHealth(entity);
            }
            else {
                if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer)entity, 18)) {
                    target = entity;
                    break;
                }
                if (SkeetIndicators.mc.field_71439_g.func_70068_e(entity) < distance) {
                    target = entity;
                    distance = SkeetIndicators.mc.field_71439_g.func_70068_e(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
                if (EntityUtil.getHealth(entity) >= maxHealth) {
                    continue;
                }
                target = entity;
                distance = SkeetIndicators.mc.field_71439_g.func_70068_e(entity);
                maxHealth = EntityUtil.getHealth(entity);
            }
        }
        return target;
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        final int totems = SkeetIndicators.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
        final int y = this.renderer.scaledHeight / 2;
        SkeetIndicators.target = this.getTarget();
        final int plrColor = this.getPlrColor(SkeetIndicators.target);
        final int htrColor = this.getHtrColor();
        this.renderer.drawString("PLR", 1.0f, (float)(y - 4), plrColor, true);
        this.renderer.drawString("HTR", 1.0f, (float)(y - 13), htrColor, true);
        this.renderer.drawStringWithGradient((String)this.clientName.getValue(), 1.0f, (float)(y - 22), true);
        if (totems > 2) {
            this.renderer.drawString("" + totems, 1.0f, (float)(y + 4), 984193, true);
        }
        else if (totems == 2) {
            this.renderer.drawString("" + totems, 1.0f, (float)(y + 4), 1052946, true);
        }
        else if (totems == 1) {
            this.renderer.drawString("" + totems, 1.0f, (float)(y + 4), 5110769, true);
        }
        else if (totems == 0) {
            this.renderer.drawString("0", 1.0f, (float)(y + 4), 5110769, true);
        }
        else {
            this.renderer.drawString("" + totems, 1.0f, (float)(y + 4), 16711680, true);
        }
        final int pingColor = (OyVey.serverManager.getPing() > 90) ? 65280 : 16711680;
        this.renderer.drawString("" + OyVey.serverManager.getPing(), 1.0f, (float)(y + 13), pingColor, true);
        final int lbyColor = (SkeetIndicators.mc.field_71439_g.field_70163_u <= 1.0) ? 65280 : 16711680;
        this.renderer.drawString("LBY", 1.0f, (float)(y + 22), lbyColor, true);
    }
    
    static {
        SkeetIndicators.INSTANCE = new SkeetIndicators();
    }
}
