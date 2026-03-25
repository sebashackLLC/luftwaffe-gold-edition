//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.world;

import me.alpha432.oyvey.api.interfaces.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import net.minecraft.client.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import java.util.*;
import net.minecraft.block.*;
import net.minecraft.block.state.*;

public class PositionUtil implements Minecraftable
{
    public static final double COLLISION_EPSILON = 1.0E-7;
    
    public static Vec3d getEyesPos() {
        return getEyesPos((Entity)PositionUtil.mc.field_71439_g);
    }
    
    public static Vec3d getEyesPos(final Entity entity) {
        return new Vec3d(entity.field_70165_t, getEyeHeight(entity), entity.field_70161_v);
    }
    
    public static double getEyeHeight(final Entity entity) {
        return entity.field_70163_u + entity.func_70047_e();
    }
    
    public static BlockPos getPosition() {
        return getPosition((Entity)PositionUtil.mc.field_71439_g);
    }
    
    public static BlockPos getPosition(final Entity entity) {
        return getPosition(entity, 0.0);
    }
    
    public static BlockPos getPosition(final Entity entity, final double yOffset) {
        double y = entity.field_70163_u + yOffset;
        if (entity.field_70163_u - Math.floor(entity.field_70163_u) > 0.5) {
            y = Math.ceil(entity.field_70163_u);
        }
        return new BlockPos(entity.field_70165_t, y, entity.field_70161_v);
    }
    
    public static Entity getPositionEntity() {
        final EntityPlayerSP player = PositionUtil.mc.field_71439_g;
        Entity ridingEntity;
        return (Entity)((player == null) ? null : (((ridingEntity = player.func_184187_bx()) != null && !(ridingEntity instanceof EntityBoat)) ? ridingEntity : player));
    }
    
    public static Set<BlockPos> getBlockedPositions(final Entity entity) {
        return getBlockedPositions(entity.func_174813_aQ());
    }
    
    public static Set<BlockPos> getBlockedPositions(final AxisAlignedBB bb) {
        return getBlockedPositions(bb, 0.5);
    }
    
    public static Set<BlockPos> getBlockedPositions(final AxisAlignedBB bb, final double offset) {
        final Set<BlockPos> positions = new HashSet<BlockPos>();
        double y = bb.field_72338_b;
        if (bb.field_72338_b - Math.floor(bb.field_72338_b) > offset) {
            y = Math.ceil(bb.field_72338_b);
        }
        final AxisAlignedBB adjustedBB = bb.func_72321_a(-1.0E-7, -1.0E-7, -1.0E-7);
        positions.add(ofFloored(adjustedBB.field_72336_d, y, adjustedBB.field_72334_f));
        positions.add(ofFloored(adjustedBB.field_72340_a, y, adjustedBB.field_72339_c));
        positions.add(ofFloored(adjustedBB.field_72336_d, y, adjustedBB.field_72339_c));
        positions.add(ofFloored(adjustedBB.field_72340_a, y, adjustedBB.field_72334_f));
        return positions;
    }
    
    public static BlockPos ofFloored(final double x, final double y, final double z) {
        return new BlockPos(MathHelper.func_76128_c(x), MathHelper.func_76128_c(y), MathHelper.func_76128_c(z));
    }
    
    public static BlockPos ofFloored(final Vec3d vec3d) {
        return ofFloored(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
    }
    
    public static Set<BlockPos> getSurroundOffsets(final Entity entity) {
        return getSurroundOffsets(entity.func_174813_aQ());
    }
    
    public static Set<BlockPos> getSurroundOffsets(final AxisAlignedBB bb) {
        final Set<BlockPos> blocked = getBlockedPositions(bb);
        final Set<BlockPos> surround = new HashSet<BlockPos>();
        for (final BlockPos pos : blocked) {
            for (final EnumFacing facing : EnumFacing.field_176754_o) {
                final BlockPos offset = pos.func_177972_a(facing);
                if (!blocked.contains(offset)) {
                    surround.add(offset);
                }
            }
        }
        return surround;
    }
    
    public static boolean isMovementBlocked() {
        final IBlockState state = findState(Block.class, MathHelper.func_76128_c(PositionUtil.mc.field_71439_g.func_174813_aQ().field_72338_b - 0.01));
        return state != null && state.func_185904_a().func_76230_c();
    }
    
    public static boolean isBoxColliding() {
        return PositionUtil.mc.field_71441_e.func_184144_a((Entity)PositionUtil.mc.field_71439_g, PositionUtil.mc.field_71439_g.func_174813_aQ().func_72321_a(0.0, 0.21, 0.0)).size() > 0;
    }
    
    private static IBlockState findState(final Class<? extends Block> block, final int y) {
        final int startX = MathHelper.func_76128_c(PositionUtil.mc.field_71439_g.func_174813_aQ().field_72340_a);
        final int startZ = MathHelper.func_76128_c(PositionUtil.mc.field_71439_g.func_174813_aQ().field_72339_c);
        final int endX = MathHelper.func_76143_f(PositionUtil.mc.field_71439_g.func_174813_aQ().field_72336_d);
        final int endZ = MathHelper.func_76143_f(PositionUtil.mc.field_71439_g.func_174813_aQ().field_72334_f);
        for (int x = startX; x < endX; ++x) {
            for (int z = startZ; z < endZ; ++z) {
                final IBlockState s = PositionUtil.mc.field_71441_e.func_180495_p(new BlockPos(x, y, z));
                if (block.isInstance(s.func_177230_c())) {
                    return s;
                }
            }
        }
        return null;
    }
    
    public static boolean voxelShapeIntersect(final AxisAlignedBB box1, final AxisAlignedBB box2) {
        return box1.field_72340_a - box2.field_72336_d < -1.0E-7 && box1.field_72336_d - box2.field_72340_a > 1.0E-7 && box1.field_72338_b - box2.field_72337_e < -1.0E-7 && box1.field_72337_e - box2.field_72338_b > 1.0E-7 && box1.field_72339_c - box2.field_72334_f < -1.0E-7 && box1.field_72334_f - box2.field_72339_c > 1.0E-7;
    }
}
