//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.combat;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraft.util.math.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.manager.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.impl.features.modules.movement.*;
import net.minecraft.item.*;
import me.alpha432.oyvey.impl.command.*;
import net.minecraft.block.*;
import me.alpha432.oyvey.api.util.world.hole.holeesp.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.network.*;
import me.alpha432.oyvey.*;
import net.minecraft.network.play.client.*;
import java.util.*;

public class AutoSelect extends Module
{
    public Setting<Boolean> smart;
    public Setting<Boolean> rotate;
    public Setting<Boolean> burrow;
    public Setting<Boolean> NoSwing;
    public Setting<Boolean> move;
    public Setting<Boolean> holeCheck;
    public Setting<Boolean> pickCheck;
    public Setting<Float> targetRange;
    public Setting<Float> resetRange;
    public Setting<Boolean> players;
    public Setting<Boolean> mobs;
    public Setting<Boolean> animals;
    public Setting<Boolean> vehicles;
    public Setting<Boolean> projectiles;
    public Setting<Float> raytrace;
    protected final Setting<RotationType> type;
    BlockPos mining;
    long startTime;
    int old;
    boolean swapBack;
    
    public AutoSelect() {
        super("AutoSelect", "Autoselecting for future automine", Module.Category.COMBAT, true, false, false);
        this.smart = (Setting<Boolean>)new Setting("Smart", (Object)true);
        this.rotate = (Setting<Boolean>)new Setting("Rotate", (Object)true);
        this.burrow = (Setting<Boolean>)new Setting("Burrow", (Object)true);
        this.NoSwing = (Setting<Boolean>)new Setting("No Swing", (Object)false);
        this.move = (Setting<Boolean>)new Setting("MovementCheck", (Object)false);
        this.holeCheck = (Setting<Boolean>)new Setting("Hole Check", (Object)true);
        this.pickCheck = (Setting<Boolean>)new Setting("PickCheck", (Object)false);
        this.targetRange = (Setting<Float>)new Setting("Target Range", (Object)10.0f, (Object)2.0f, (Object)15.0f);
        this.resetRange = (Setting<Float>)new Setting("Reset Range", (Object)4.0f, (Object)1.0f, (Object)6.0f);
        this.players = (Setting<Boolean>)new Setting("Players", (Object)true);
        this.mobs = (Setting<Boolean>)new Setting("Mobs", (Object)false);
        this.animals = (Setting<Boolean>)new Setting("Animals", (Object)false);
        this.vehicles = (Setting<Boolean>)new Setting("Entities", (Object)false);
        this.projectiles = (Setting<Boolean>)new Setting("Projectiles", (Object)false);
        this.raytrace = (Setting<Float>)new Setting("Raytrace", (Object)6.0f, (Object)0.1f, (Object)7.0f, "Wall Range.");
        this.type = (Setting<RotationType>)new Setting("Rotation Mode", (Object)RotationType.Packet, v -> (boolean)this.rotate.getValue());
    }
    
    public void onEnable() {
        this.startTime = 0L;
        this.old = 1;
        this.swapBack = false;
    }
    
    public String getDisplayInfo() {
        if (AutoSelect.mc.field_71439_g == null) {
            return "";
        }
        if (this.getTarget() == null) {
            return ChatFormatting.RED + "none" + ChatFormatting.RESET;
        }
        final String targetName = this.getTarget().func_70005_c_();
        final String ircName = IrcNameManager.getIrcNameForPlayer(targetName);
        if (ircName != null) {
            return ircName.toLowerCase();
        }
        return targetName.toLowerCase();
    }
    
    private EntityPlayer getTarget() {
        Entity target = null;
        double distance = (float)this.targetRange.getValue();
        double maxHealth = 36.0;
        for (final EntityPlayer entity : AutoSelect.mc.field_71441_e.field_73010_i) {
            if (((boolean)this.players.getValue() && entity instanceof EntityPlayer) || ((boolean)this.animals.getValue() && EntityUtil.isPassive((Entity)entity)) || ((boolean)this.mobs.getValue() && EntityUtil.isMobAggressive((Entity)entity)) || ((boolean)this.vehicles.getValue() && EntityUtil.isVehicle((Entity)entity)) || ((boolean)this.projectiles.getValue() && EntityUtil.isProjectile((Entity)entity))) {
                if (entity instanceof EntityLivingBase && EntityUtil.isntValid((Entity)entity, distance)) {
                    continue;
                }
                if (!AutoSelect.mc.field_71439_g.func_70685_l((Entity)entity) && !EntityUtil.canEntityFeetBeSeen((Entity)entity) && AutoSelect.mc.field_71439_g.func_70068_e((Entity)entity) > MathUtil.square((double)(float)this.raytrace.getValue())) {
                    continue;
                }
                if (target == null) {
                    target = (Entity)entity;
                    distance = AutoSelect.mc.field_71439_g.func_70068_e((Entity)entity);
                    maxHealth = EntityUtil.getHealth((Entity)entity);
                }
                else {
                    if (entity instanceof EntityPlayer && DamageUtil.isArmorLow(entity, 18)) {
                        target = (Entity)entity;
                        break;
                    }
                    if (AutoSelect.mc.field_71439_g.func_70068_e((Entity)entity) < distance) {
                        target = (Entity)entity;
                        distance = AutoSelect.mc.field_71439_g.func_70068_e((Entity)entity);
                        maxHealth = EntityUtil.getHealth((Entity)entity);
                    }
                    if (EntityUtil.getHealth((Entity)entity) >= maxHealth) {
                        continue;
                    }
                    target = (Entity)entity;
                    distance = AutoSelect.mc.field_71439_g.func_70068_e((Entity)entity);
                    maxHealth = EntityUtil.getHealth((Entity)entity);
                }
            }
        }
        return (EntityPlayer)target;
    }
    
    public void onUpdate() {
        if (FakeLag.getInstance().isEnabled() || PhaseWalk.getInstance().isPhasing()) {
            return;
        }
        if (AutoSelect.mc.field_71439_g != null && AutoSelect.mc.field_71441_e != null) {
            if (this.pickCheck.getValue()) {
                final int i = InventoryUtil.findHotbar((Class)ItemPickaxe.class);
                if (i == -1) {
                    Command.sendSilentMessage(ChatFormatting.BOLD + "No pickaxe found! Disabling AutoCity...");
                    this.disable();
                }
            }
            if ((!(boolean)this.move.getValue() || !MovementUtil.anyMovementKeys()) && this.getTarget() != null) {
                if (this.mining != null) {
                    if (AutoSelect.mc.field_71441_e.func_180495_p(this.mining).func_177230_c() instanceof BlockAir) {
                        this.mining = null;
                        return;
                    }
                    if ((boolean)this.holeCheck.getValue() && !HoleUtil.isHole(EntityUtil.getOtherPlayerPos(this.getTarget())) && !EntityUtil.isBurrow(this.getTarget())) {
                        this.mining = null;
                        return;
                    }
                    if (AutoSelect.mc.field_71439_g.func_174818_b(this.mining) > MathUtil.square((double)((Float)this.resetRange.getValue()).intValue())) {
                        this.mining = null;
                    }
                }
                if (this.mining == null && this.getBurrowBlock(this.getTarget()) != null && (boolean)this.burrow.getValue()) {
                    this.mine(this.getBurrowBlock(this.getTarget()));
                }
                else if (this.mining == null && HoleUtil.isHole(EntityUtil.getOtherPlayerPos(this.getTarget())) && getCityBlockSurround(this.getTarget()) != null) {
                    this.mine(getCityBlockSurround(this.getTarget()));
                }
            }
        }
    }
    
    private void mine(final BlockPos blockPos) {
        if (FakeLag.getInstance().isEnabled() || PhaseWalk.getInstance().isPhasing()) {
            return;
        }
        if (AutoSelect.mc.field_71439_g.func_174818_b(blockPos) <= MathUtil.square((double)(float)this.resetRange.getValue())) {
            if (this.rotate.getValue()) {
                final float[] rotations = RotationUtil.getRotations(blockPos);
                doRotation((RotationType)this.type.getValue(), rotations);
            }
            this.checkAndBreakBlock(blockPos, (boolean)this.smart.getValue());
            this.mining = blockPos;
            this.startTime = System.currentTimeMillis();
        }
    }
    
    public void checkAndBreakBlock(final BlockPos blockPos, final boolean smart) {
        if (FakeLag.getInstance().isEnabled()) {
            return;
        }
        if (smart) {
            boolean shouldBreak = false;
            for (final EnumFacing direction : EnumFacing.values()) {
                if (direction != EnumFacing.UP && direction != EnumFacing.DOWN) {
                    final BlockPos adjacentPos = blockPos.func_177972_a(direction);
                    final BlockPos belowAirPos = adjacentPos.func_177977_b();
                    if (AutoSelect.mc.field_71441_e.func_180495_p(adjacentPos).func_177230_c() == Blocks.field_150350_a && (AutoSelect.mc.field_71441_e.func_180495_p(belowAirPos).func_177230_c() == Blocks.field_150343_Z || AutoSelect.mc.field_71441_e.func_180495_p(belowAirPos).func_177230_c() == Blocks.field_150357_h)) {
                        shouldBreak = true;
                        break;
                    }
                }
            }
            if (shouldBreak) {
                AutoSelect.mc.field_71442_b.func_180512_c(blockPos, EnumFacing.UP);
                if (!(boolean)this.NoSwing.getValue()) {
                    AutoSelect.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                }
            }
        }
        else {
            AutoSelect.mc.field_71442_b.func_180512_c(blockPos, EnumFacing.UP);
            if (!(boolean)this.NoSwing.getValue()) {
                AutoSelect.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            }
        }
    }
    
    public static void doRotation(final RotationType rotation, final float[] angles) {
        switch (rotation) {
            case Packet: {
                AutoSelect.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(angles[0], angles[1], AutoSelect.mc.field_71439_g.field_70122_E));
                break;
            }
            case Normal: {
                OyVey.rotationManager.setRotations(angles[0], angles[1]);
                break;
            }
        }
    }
    
    public static void swapToHotbarSlot(final int slot) {
        if (slot >= 0 && slot < 9) {
            AutoSelect.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
            AutoSelect.mc.field_71439_g.field_71071_by.field_70461_c = slot;
        }
    }
    
    public static void swapItemInHand(final int targetSlot) {
        final int currentSlot = AutoSelect.mc.field_71439_g.field_71071_by.field_70461_c;
        swapToHotbarSlot(targetSlot);
        swapToHotbarSlot(currentSlot);
    }
    
    public void onDisable() {
        this.mining = null;
        this.swapBack = false;
    }
    
    public static List<BlockPos> getSurroundBlocks(final EntityPlayer player) {
        if (player == null) {
            return null;
        }
        final List<BlockPos> positions = new ArrayList<BlockPos>();
        final BlockPos targetPos = EntityUtil.getOtherPlayerPos(player);
        final boolean burrowed = EntityUtil.isBurrow(player);
        if (burrowed && (AutoSelect.mc.field_71441_e.func_180495_p(targetPos).func_177230_c() == Blocks.field_150343_Z || AutoSelect.mc.field_71441_e.func_180495_p(targetPos).func_177230_c() == Blocks.field_150477_bB)) {
            positions.add(targetPos);
        }
        for (final EnumFacing direction : EnumFacing.field_176754_o) {
            final BlockPos pos = targetPos.func_177972_a(direction);
            if (AutoSelect.mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150350_a && (canPlaceCrystalBelow(pos.func_177977_b()) || canPlaceCrystalBelow(pos.func_177972_a(direction).func_177977_b())) && canCityBlock(pos, direction)) {
                positions.add(pos);
            }
        }
        return positions;
    }
    
    public BlockPos getBurrowBlock(final EntityPlayer player) {
        if (player == null) {
            return null;
        }
        final BlockPos blockPos = new BlockPos(player.field_70165_t, player.field_70163_u, player.field_70161_v);
        return (!AutoSelect.mc.field_71441_e.func_180495_p(blockPos).func_177230_c().equals(Blocks.field_150343_Z) && !AutoSelect.mc.field_71441_e.func_180495_p(blockPos).func_177230_c().equals(Blocks.field_150477_bB)) ? null : blockPos;
    }
    
    public static BlockPos getCityBlockSurround(final EntityPlayer player) {
        final List<BlockPos> posList = getSurroundBlocks(player);
        if (posList == null || posList.isEmpty()) {
            return null;
        }
        BlockPos bestPos = null;
        double bestRange = Double.MAX_VALUE;
        final boolean targetBurrowed = EntityUtil.isBurrow(player);
        final BlockPos targetPos = EntityUtil.getOtherPlayerPos(player);
        for (final BlockPos pos : posList) {
            if (AutoSelect.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150350_a) {
                continue;
            }
            final double currentRange = AutoSelect.mc.field_71439_g.func_174818_b(pos);
            final boolean isBurrowBlock = targetBurrowed && pos.equals((Object)targetPos);
            if (isBurrowBlock) {
                return pos;
            }
            if (currentRange >= bestRange) {
                continue;
            }
            bestPos = pos;
            bestRange = currentRange;
        }
        return bestPos;
    }
    
    private static boolean canPlaceCrystalBelow(final BlockPos pos) {
        return (AutoSelect.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150343_Z || AutoSelect.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150357_h) && AutoSelect.mc.field_71441_e.func_180495_p(pos.func_177984_a()).func_177230_c() == Blocks.field_150350_a && AutoSelect.mc.field_71441_e.func_180495_p(pos.func_177981_b(2)).func_177230_c() == Blocks.field_150350_a;
    }
    
    public static boolean canCityBlock(final BlockPos blockPos, final EnumFacing direction) {
        return AutoSelect.mc.field_71441_e.func_180495_p(blockPos.func_177984_a()).func_177230_c() == Blocks.field_150350_a || (AutoSelect.mc.field_71441_e.func_180495_p(blockPos.func_177972_a(direction)).func_177230_c() == Blocks.field_150350_a && AutoSelect.mc.field_71441_e.func_180495_p(blockPos.func_177972_a(direction).func_177984_a()).func_177230_c() == Blocks.field_150350_a && (AutoSelect.mc.field_71441_e.func_180495_p(blockPos.func_177972_a(direction).func_177977_b()).func_177230_c() == Blocks.field_150343_Z || AutoSelect.mc.field_71441_e.func_180495_p(blockPos.func_177972_a(direction).func_177977_b()).func_177230_c() == Blocks.field_150357_h));
    }
    
    public enum RotationType
    {
        Packet, 
        Normal;
    }
}
