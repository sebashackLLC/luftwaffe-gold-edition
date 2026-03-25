//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.entity;

import me.alpha432.oyvey.api.interfaces.*;
import me.alpha432.oyvey.api.util.math.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.item.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import java.util.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import net.minecraft.entity.player.*;

public class CombatUtil implements Minecraftable
{
    public static final Timer breakTimer;
    public static Timer attackTimer;
    
    public static boolean rayTraceRangeCheck(final Entity target, final double range) {
        final boolean isVisible = CombatUtil.mc.field_71439_g.func_70685_l(target);
        return !isVisible || CombatUtil.mc.field_71439_g.func_70068_e(target) <= range * range;
    }
    
    public static boolean rayTraceRangeCheck(final BlockPos pos, final double range, final double height) {
        final RayTraceResult result = CombatUtil.mc.field_71441_e.func_147447_a(new Vec3d(CombatUtil.mc.field_71439_g.field_70165_t, CombatUtil.mc.field_71439_g.field_70163_u + CombatUtil.mc.field_71439_g.func_70047_e(), CombatUtil.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n(), pos.func_177956_o() + height, (double)pos.func_177952_p()), false, true, false);
        return result == null || CombatUtil.mc.field_71439_g.func_174818_b(pos) <= range * range;
    }
    
    public static void attackCrystal(final BlockPos pos) {
        if (CombatUtil.attackTimer.passedMs(0L)) {
            for (final Entity entity : CombatUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(pos))) {
                if (!(entity instanceof EntityEnderCrystal)) {
                    continue;
                }
                CombatUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(entity));
                CombatUtil.attackTimer.reset();
                break;
            }
        }
    }
    
    public static void attackCrystal(final BlockPos pos, final boolean rotate, final boolean eatingPause) {
        if (!eatingPause || !EntityUtil.isEating()) {
            for (final Entity entity : CombatUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(pos))) {
                if (!(entity instanceof EntityEnderCrystal)) {
                    continue;
                }
                CombatUtil.breakTimer.reset();
                CombatUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(entity));
                CombatUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                if (!rotate) {
                    break;
                }
                EntityUtil.faceXYZ(entity.field_70165_t, entity.field_70163_u + 0.25, entity.field_70161_v);
                break;
            }
        }
    }
    
    public static void attackCrystal(final Entity entity, final boolean rotate, final boolean eatingPause) {
        if ((!eatingPause || !EntityUtil.isEating()) && entity != null) {
            CombatUtil.breakTimer.reset();
            CombatUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(entity));
            CombatUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
            if (rotate) {
                EntityUtil.faceXYZ(entity.field_70165_t, entity.field_70163_u + 0.25, entity.field_70161_v);
            }
        }
    }
    
    public static boolean isHole(final BlockPos pos, final boolean anyBlock, final int blocks, final boolean onlyCanStand) {
        int blockProgress = 0;
        if (anyBlock) {
            if (!canBlockReplace(pos.func_177982_a(0, 0, 1)) || (!canBlockReplace(pos.func_177982_a(0, 0, 2)) && !canBlockReplace(pos.func_177982_a(0, 1, 1)) && !canBlockReplace(pos.func_177982_a(1, 0, 1)) && !canBlockReplace(pos.func_177982_a(-1, 0, 1)))) {
                ++blockProgress;
            }
            if (!canBlockReplace(pos.func_177982_a(0, 0, -1)) || (!canBlockReplace(pos.func_177982_a(0, 0, -2)) && !canBlockReplace(pos.func_177982_a(0, 1, -1)) && !canBlockReplace(pos.func_177982_a(1, 0, -1)) && !canBlockReplace(pos.func_177982_a(-1, 0, -1)))) {
                ++blockProgress;
            }
            if (!canBlockReplace(pos.func_177982_a(1, 0, 0)) || (!canBlockReplace(pos.func_177982_a(2, 0, 0)) && !canBlockReplace(pos.func_177982_a(1, 1, 0)) && !canBlockReplace(pos.func_177982_a(1, 0, 1)) && !canBlockReplace(pos.func_177982_a(1, 0, -1)))) {
                ++blockProgress;
            }
            if (!canBlockReplace(pos.func_177982_a(-1, 0, 0)) || (!canBlockReplace(pos.func_177982_a(-2, 0, 0)) && !canBlockReplace(pos.func_177982_a(-1, 1, 0)) && !canBlockReplace(pos.func_177982_a(-1, 0, 1)) && !canBlockReplace(pos.func_177982_a(-1, 0, -1)))) {
                ++blockProgress;
            }
        }
        else {
            if (getBlock(pos.func_177982_a(0, 0, 1)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(0, 0, 1)) == Blocks.field_150357_h || ((getBlock(pos.func_177982_a(0, 0, 2)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(0, 0, 2)) == Blocks.field_150357_h) && (getBlock(pos.func_177982_a(0, 1, 1)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(0, 1, 1)) == Blocks.field_150357_h) && (getBlock(pos.func_177982_a(1, 0, 1)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(1, 0, 1)) == Blocks.field_150357_h) && (getBlock(pos.func_177982_a(-1, 0, 1)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(-1, 0, 1)) == Blocks.field_150357_h))) {
                ++blockProgress;
            }
            if (getBlock(pos.func_177982_a(0, 0, -1)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(0, 0, -1)) == Blocks.field_150357_h || ((getBlock(pos.func_177982_a(0, 0, -2)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(0, 0, -2)) == Blocks.field_150357_h) && (getBlock(pos.func_177982_a(0, 1, -1)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(0, 1, -1)) == Blocks.field_150357_h) && (getBlock(pos.func_177982_a(1, 0, -1)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(1, 0, -1)) == Blocks.field_150357_h) && (getBlock(pos.func_177982_a(-1, 0, -1)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(-1, 0, -1)) == Blocks.field_150357_h))) {
                ++blockProgress;
            }
            if (getBlock(pos.func_177982_a(1, 0, 0)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(1, 0, 0)) == Blocks.field_150357_h || ((getBlock(pos.func_177982_a(2, 0, 0)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(2, 0, 0)) == Blocks.field_150357_h) && (getBlock(pos.func_177982_a(1, 1, 0)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(1, 1, 0)) == Blocks.field_150357_h) && (getBlock(pos.func_177982_a(1, 0, 1)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(1, 0, 1)) == Blocks.field_150357_h) && (getBlock(pos.func_177982_a(1, 0, -1)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(1, 0, -1)) == Blocks.field_150357_h))) {
                ++blockProgress;
            }
            if (getBlock(pos.func_177982_a(-1, 0, 0)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(-1, 0, 0)) == Blocks.field_150357_h || ((getBlock(pos.func_177982_a(-2, 0, 0)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(-2, 0, 0)) == Blocks.field_150357_h) && (getBlock(pos.func_177982_a(-1, 1, 0)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(-1, 1, 0)) == Blocks.field_150357_h) && (getBlock(pos.func_177982_a(-1, 0, 1)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(-1, 0, 1)) == Blocks.field_150357_h) && (getBlock(pos.func_177982_a(-1, 0, -1)) == Blocks.field_150343_Z || getBlock(pos.func_177982_a(-1, 0, -1)) == Blocks.field_150357_h))) {
                ++blockProgress;
            }
        }
        return getBlock(pos) == Blocks.field_150350_a && getBlock(pos.func_177982_a(0, 1, 0)) == Blocks.field_150350_a && (getBlock(pos.func_177982_a(0, -1, 0)) != Blocks.field_150350_a || !onlyCanStand) && getBlock(pos.func_177982_a(0, 2, 0)) == Blocks.field_150350_a && blockProgress > blocks - 1;
    }
    
    public static Block getBlock(final BlockPos pos) {
        return CombatUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c();
    }
    
    public static boolean canBlockReplace(final BlockPos pos) {
        return CombatUtil.mc.field_71441_e.func_175623_d(pos) || getBlock(pos) == Blocks.field_150480_ab || getBlock(pos) == Blocks.field_150329_H || getBlock(pos) == Blocks.field_150395_bd || getBlock(pos) == Blocks.field_150431_aC || getBlock(pos) == Blocks.field_150392_bi;
    }
    
    public static EntityPlayer getTarget(final double range) {
        EntityPlayer target = null;
        double distance = range;
        for (final EntityPlayer player : CombatUtil.mc.field_71441_e.field_73010_i) {
            if (EntityUtil.isntValid((Entity)player, range)) {
                continue;
            }
            if (target == null) {
                target = player;
                distance = EntityUtil.mc.field_71439_g.func_70068_e((Entity)player);
            }
            else {
                if (EntityUtil.mc.field_71439_g.func_70068_e((Entity)player) >= distance) {
                    continue;
                }
                target = player;
                distance = EntityUtil.mc.field_71439_g.func_70068_e((Entity)player);
            }
        }
        return target;
    }
    
    static {
        breakTimer = new Timer();
        CombatUtil.attackTimer = new Timer();
    }
}
