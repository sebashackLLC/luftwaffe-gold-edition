//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.world;

import me.alpha432.oyvey.api.util.*;
import net.minecraft.network.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.projectile.*;
import net.minecraft.block.state.*;
import net.minecraft.world.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.impl.command.*;
import net.minecraft.block.*;
import net.minecraft.network.play.client.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.block.material.*;
import java.util.*;

public class BlockUtil implements Util
{
    public static List<Block> rightclickableBlocks;
    public static List<Block> emptyBlocks;
    public static final List<Block> canUseList;
    public static final List<Block> noPlaceBlock;
    public static final List<Block> blackList;
    public static final List<Block> shulkerList;
    public static final List<Block> unSafeBlocks;
    public static final List<Block> cantPushBlocks;
    public static List<Block> unSolidBlocks;
    public static List<Block> godBlocks;
    
    public static boolean canBlockBeSeen(final double x, final double y, final double z) {
        return BlockUtil.mc.field_71441_e.func_147447_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d(x, y + 1.7, z), false, true, false) == null;
    }
    
    public static boolean isBlockUnSafe(final Block block) {
        return BlockUtil.unSafeBlocks.contains(block);
    }
    
    public static Boolean isPosInFov(final BlockPos pos) {
        final int dirnumber = RotationUtil.getDirection4D();
        if (dirnumber == 0 && pos.func_177952_p() - BlockUtil.mc.field_71439_g.func_174791_d().field_72449_c < 0.0) {
            return false;
        }
        if (dirnumber == 1 && pos.func_177958_n() - BlockUtil.mc.field_71439_g.func_174791_d().field_72450_a > 0.0) {
            return false;
        }
        if (dirnumber == 2 && pos.func_177952_p() - BlockUtil.mc.field_71439_g.func_174791_d().field_72449_c > 0.0) {
            return false;
        }
        return dirnumber != 3 || pos.func_177958_n() - BlockUtil.mc.field_71439_g.func_174791_d().field_72450_a >= 0.0;
    }
    
    public static void faceVectorPacketInstant(final Vec3d vec) {
        final float[] rotations = RotationUtil.getLegitRotations(vec);
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(rotations[0], rotations[1], BlockUtil.mc.field_71439_g.field_70122_E));
    }
    
    public static boolean isEnderChest(final BlockPos pos) {
        return BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150477_bB;
    }
    
    public static boolean canBlockReplace(final BlockPos pos) {
        return BlockUtil.mc.field_71441_e.func_175623_d(pos) || getBlock(pos) == Blocks.field_150324_C || getBlock(pos) == Blocks.field_150486_ae || getBlock(pos) == Blocks.field_150447_bR || getBlock(pos) == Blocks.field_150477_bB || getBlock(pos) == Blocks.field_150415_aT;
    }
    
    public static List<BlockPos> getSphere(final Entity entity, final float radius, final boolean ignoreAir) {
        final List<BlockPos> sphere = new ArrayList<BlockPos>();
        final BlockPos pos = PositionUtil.getPosition(entity);
        final int posX = pos.func_177958_n();
        final int posY = pos.func_177956_o();
        final int posZ = pos.func_177952_p();
        final int radiuss = (int)radius;
        for (int x = posX - radiuss; x <= posX + radius; ++x) {
            for (int z = posZ - radiuss; z <= posZ + radius; ++z) {
                for (int y = posY - radiuss; y < posY + radius; ++y) {
                    if ((posX - x) * (posX - x) + (posZ - z) * (posZ - z) + (posY - y) * (posY - y) < radius * radius) {
                        final BlockPos position = new BlockPos(x, y, z);
                        if (!ignoreAir || BlockUtil.mc.field_71441_e.func_180495_p(position).func_177230_c() != Blocks.field_150350_a) {
                            sphere.add(position);
                        }
                    }
                }
            }
        }
        return sphere;
    }
    
    public static boolean isSafe(final BlockPos pos) {
        return isObby(pos) || isBedrock(pos) || isEnderChest(pos);
    }
    
    public static boolean isBedrock(final BlockPos pos) {
        return BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150357_h;
    }
    
    public static boolean isObby(final BlockPos pos) {
        return BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150343_Z;
    }
    
    public static NonNullList<BlockPos> getBox(final float range, final BlockPos pos) {
        final NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)getSphere(pos, range, 0, false, true, 0));
        return (NonNullList<BlockPos>)positions;
    }
    
    public static boolean canPlaceShulker(final BlockPos pos) {
        if (BlockUtil.mc.field_71439_g.func_70092_e(pos.func_177958_n() + 0.5, pos.func_177956_o() + 0.5, pos.func_177952_p() + 0.5) > 36.0) {
            return false;
        }
        if (canBlockReplace(pos.func_177977_b())) {
            return false;
        }
        if (!canReplace(pos)) {
            return false;
        }
        if (checkEntity(pos)) {
            return false;
        }
        for (final EnumFacing side : getPlacableFacings(pos, true, true)) {
            if (canClick(pos.func_177972_a(side))) {
                if (side != EnumFacing.DOWN) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }
    
    public static boolean canPlace(final BlockPos blockPos) {
        return BlockUtil.mc.field_71439_g.func_70092_e(blockPos.func_177958_n() + 0.5, blockPos.func_177956_o() + 0.5, blockPos.func_177952_p() + 0.5) <= 36.0 && canReplace(blockPos) && !checkEntity(blockPos) && hasSupport(blockPos);
    }
    
    public static boolean canPlace(final BlockPos pos, final double distance) {
        return BlockUtil.mc.field_71439_g.func_70092_e(pos.func_177958_n() + 0.5, pos.func_177956_o() + 0.5, pos.func_177952_p() + 0.5) <= distance * distance && canBlockFacing(pos) && canReplace(pos) && !checkEntity(pos);
    }
    
    public static boolean canReplace(final BlockPos blockPos) {
        return getState(blockPos).func_185904_a().func_76222_j();
    }
    
    public static void placeCrystal(final BlockPos blockPos, final boolean faceVector) {
        final boolean b2 = BlockUtil.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP;
        final BlockPos down = blockPos.func_177977_b();
        final RayTraceResult rayTraceBlocks = BlockUtil.mc.field_71441_e.func_72933_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d(blockPos.func_177958_n() + 0.5, blockPos.func_177956_o() - 0.5, blockPos.func_177952_p() + 0.5));
        final EnumFacing enumFacing = (rayTraceBlocks == null || rayTraceBlocks.field_178784_b == null) ? EnumFacing.UP : rayTraceBlocks.field_178784_b;
        final Vec3d add = new Vec3d((Vec3i)down).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(enumFacing.func_176730_m()).func_186678_a(0.5));
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(down, enumFacing, b2 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
        BlockUtil.mc.field_71439_g.func_184609_a(b2 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
    }
    
    public static double getDistanceToCenter(final EntityPlayer entityPlayer, final BlockPos blockPos) {
        final double d = blockPos.func_177958_n() + 0.5 - entityPlayer.field_70165_t;
        final double d2 = blockPos.func_177956_o() + 0.5 - entityPlayer.field_70163_u;
        final double d3 = blockPos.func_177952_p() + 0.5 - entityPlayer.field_70161_v;
        return StrictMath.sqrt(d * d + d2 * d2 + d3 * d3);
    }
    
    public static boolean canBlockFacing(final BlockPos pos) {
        boolean airCheck = false;
        for (final EnumFacing side : EnumFacing.values()) {
            if (canClick(pos.func_177972_a(side))) {
                airCheck = true;
            }
        }
        return airCheck;
    }
    
    public static void rotatePacket(final double d, final double d2, final double d3) {
        final double d4 = d - BlockUtil.mc.field_71439_g.field_70165_t;
        final double d5 = d2 - (BlockUtil.mc.field_71439_g.field_70163_u + BlockUtil.mc.field_71439_g.func_70047_e());
        final double d6 = d3 - BlockUtil.mc.field_71439_g.field_70161_v;
        final double d7 = Math.sqrt(d4 * d4 + d6 * d6);
        final float f = (float)Math.toDegrees(Math.atan2(d6, d4)) - 90.0f;
        final float f2 = (float)(-Math.toDegrees(Math.atan2(d5, d7)));
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(f, f2, BlockUtil.mc.field_71439_g.field_70122_E));
    }
    
    public static boolean posHasCrystal(final BlockPos blockPos) {
        for (final Entity entity : BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(blockPos))) {
            if (entity instanceof EntityEnderCrystal) {
                if (!new BlockPos(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v).equals((Object)blockPos)) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }
    
    public static boolean checkEntity(final BlockPos blockPos) {
        for (final Entity entity : BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(blockPos))) {
            if (!entity.field_70128_L && !(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb) && !(entity instanceof EntityExpBottle)) {
                if (entity instanceof EntityArrow) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }
    
    public static EnumFacing getBestNeighboring(final BlockPos blockPos, final EnumFacing enumFacing) {
        for (final EnumFacing enumFacing2 : EnumFacing.field_176754_o) {
            if (enumFacing == null || !blockPos.func_177972_a(enumFacing2).equals((Object)blockPos.func_177967_a(enumFacing, -1))) {
                if (enumFacing2 != EnumFacing.DOWN) {
                    final Iterator<EnumFacing> iterator2 = getPlacableFacings(blockPos.func_177972_a(enumFacing2), true, true).iterator();
                    while (iterator2.hasNext()) {
                        if (!canClick(blockPos.func_177972_a(enumFacing2).func_177972_a((EnumFacing)iterator2.next()))) {
                            continue;
                        }
                        return enumFacing2;
                    }
                }
            }
        }
        EnumFacing enumFacing3 = null;
        double distanceSq = 0.0;
        for (final EnumFacing enumFacing4 : EnumFacing.field_176754_o) {
            if (enumFacing == null || !blockPos.func_177972_a(enumFacing4).equals((Object)blockPos.func_177967_a(enumFacing, -1))) {
                if (enumFacing4 != EnumFacing.DOWN) {
                    final Iterator<EnumFacing> iterator3 = getPlacableFacings(blockPos.func_177972_a(enumFacing4), true, false).iterator();
                    while (iterator3.hasNext()) {
                        if (canClick(blockPos.func_177972_a(enumFacing4).func_177972_a((EnumFacing)iterator3.next()))) {
                            if (enumFacing3 != null && BlockUtil.mc.field_71439_g.func_174831_c(blockPos.func_177972_a(enumFacing4)) >= distanceSq) {
                                continue;
                            }
                            enumFacing3 = enumFacing4;
                            distanceSq = BlockUtil.mc.field_71439_g.func_174831_c(blockPos.func_177972_a(enumFacing4));
                        }
                    }
                }
            }
        }
        return null;
    }
    
    public static List<EnumFacing> getPlacableFacings(final BlockPos blockPos, final boolean b, final boolean b2) {
        final ArrayList<EnumFacing> list = new ArrayList<EnumFacing>();
        for (final EnumFacing enumFacing : EnumFacing.values()) {
            if (!getRaytrace(blockPos, enumFacing)) {
                getPlaceFacing(blockPos, b, list, enumFacing);
            }
        }
        for (final EnumFacing enumFacing2 : EnumFacing.values()) {
            if (!b2 || !getRaytrace(blockPos, enumFacing2)) {
                getPlaceFacing(blockPos, b, list, enumFacing2);
            }
        }
        return list;
    }
    
    public static ArrayList<EnumFacing> checkAxis(final double n, final EnumFacing e, final EnumFacing e2, final boolean b) {
        final ArrayList<EnumFacing> list = new ArrayList<EnumFacing>();
        if (n < -0.5) {
            list.add(e);
        }
        if (n > 0.5) {
            list.add(e2);
        }
        if (b) {
            if (!list.contains(e)) {
                list.add(e);
            }
            if (!list.contains(e2)) {
                list.add(e2);
            }
        }
        return list;
    }
    
    private static void getPlaceFacing(final BlockPos blockPos, final boolean b, final ArrayList<EnumFacing> list, final EnumFacing e) {
        final BlockPos offset = blockPos.func_177972_a(e);
        if (b) {
            final Vec3d positionEyes = BlockUtil.mc.field_71439_g.func_174824_e(1.0f);
            final Vec3d vec3d = new Vec3d(offset.func_177958_n() + 0.5, offset.func_177956_o() + 0.5, offset.func_177952_p() + 0.5);
            final IBlockState blockState = BlockUtil.mc.field_71441_e.func_180495_p(offset);
            final boolean b2 = blockState.func_177230_c() == Blocks.field_150350_a || blockState.func_177230_c().func_176200_f((IBlockAccess)BlockUtil.mc.field_71441_e, offset);
            final ArrayList<EnumFacing> list2 = new ArrayList<EnumFacing>();
            list2.addAll(checkAxis(positionEyes.field_72450_a - vec3d.field_72450_a, EnumFacing.WEST, EnumFacing.EAST, !b2));
            list2.addAll(checkAxis(positionEyes.field_72448_b - vec3d.field_72448_b, EnumFacing.DOWN, EnumFacing.UP, true));
            list2.addAll(checkAxis(positionEyes.field_72449_c - vec3d.field_72449_c, EnumFacing.NORTH, EnumFacing.SOUTH, !b2));
            if (!list2.contains(e.func_176734_d())) {
                return;
            }
        }
        final IBlockState blockState2;
        if (!(blockState2 = BlockUtil.mc.field_71441_e.func_180495_p(offset)).func_177230_c().func_176209_a(blockState2, false) || blockState2.func_185904_a().func_76222_j()) {
            return;
        }
        list.add(e);
    }
    
    private static boolean getRaytrace(final BlockPos blockPos, final EnumFacing enumFacing) {
        final RayTraceResult rayTraceBlocks = BlockUtil.mc.field_71441_e.func_72933_a(BlockUtil.mc.field_71439_g.func_174824_e(1.0f), new Vec3d((Vec3i)blockPos).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(enumFacing.func_176730_m()).func_186678_a(0.5)));
        return rayTraceBlocks != null && rayTraceBlocks.field_72313_a != RayTraceResult.Type.MISS;
    }
    
    public static boolean canClick(final BlockPos blockPos) {
        return BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c().func_176209_a(BlockUtil.mc.field_71441_e.func_180495_p(blockPos), false);
    }
    
    public static boolean canBreak(final BlockPos pos, final boolean air) {
        final IBlockState blockState = BlockUtil.mc.field_71441_e.func_180495_p(pos);
        final Block block = blockState.func_177230_c();
        if (BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150350_a) {
            return air;
        }
        return BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150343_Z && BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150357_h && BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150477_bB && BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150467_bQ && BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150381_bn && BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150486_ae && BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150447_bR && block.func_180647_a(blockState, (EntityPlayer)BlockUtil.mc.field_71439_g, (World)BlockUtil.mc.field_71441_e, pos) != -1.0f;
    }
    
    public static double getPushDistance(final EntityPlayer player, final double x, final double z) {
        final double d0 = player.field_70165_t - x;
        final double d2 = player.field_70161_v - z;
        return Math.sqrt(d0 * d0 + d2 * d2);
    }
    
    public static boolean cantBlockPlace(final BlockPos blockPos) {
        return (BlockUtil.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 0, 1)).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 0, -1)).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(1, 0, 0)).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(-1, 0, 0)).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 1, 0)).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, -1, 0)).func_177230_c() == Blocks.field_150350_a) || BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150350_a || checkEntity(EntityUtil.getVarOffsets(0, 0, 0), blockPos) != null;
    }
    
    public static boolean cantBlockPlace2(final BlockPos blockPos) {
        return BlockUtil.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 0, 1)).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 0, -1)).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(1, 0, 0)).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(-1, 0, 0)).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, 1, 0)).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_180495_p(blockPos.func_177982_a(0, -1, 0)).func_177230_c() == Blocks.field_150350_a;
    }
    
    public static boolean canPlaceCrystal(final BlockPos blockPos, final boolean b, final boolean b2) {
        final BlockPos add = blockPos.func_177982_a(0, 1, 0);
        final BlockPos add2 = blockPos.func_177982_a(0, 2, 0);
        try {
            if (BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z && BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h) {
                return false;
            }
            if ((!b2 && BlockUtil.mc.field_71441_e.func_180495_p(add2).func_177230_c() != Blocks.field_150350_a) || BlockUtil.mc.field_71441_e.func_180495_p(add).func_177230_c() != Blocks.field_150350_a) {
                return false;
            }
            for (final Entity entity : BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(add))) {
                if (!entity.field_70128_L) {
                    if (b && entity instanceof EntityEnderCrystal) {
                        continue;
                    }
                    return false;
                }
            }
            if (!b2) {
                for (final Entity entity2 : BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(add2))) {
                    if (!entity2.field_70128_L) {
                        if (b && entity2 instanceof EntityEnderCrystal) {
                            continue;
                        }
                        return false;
                    }
                }
            }
        }
        catch (Exception ex) {
            return false;
        }
        return true;
    }
    
    public static boolean hasCrystal(final BlockPos pos) {
        for (final Entity entity : BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityEnderCrystal)) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    public static boolean hasPlayer(final BlockPos pos) {
        for (final Entity entity : BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityPlayer)) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    public static boolean hasEntity(final BlockPos pos) {
        for (final Entity entity : BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                if (entity == null) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }
    
    public static boolean EntityCheck(final BlockPos pos) {
        for (final Entity entity : BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                if (entity == null) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }
    
    public static boolean CanPlace(final BlockPos block) {
        for (final EnumFacing face : EnumFacing.field_176754_o) {
            if (isReplaceable(block) && !BlockUtil.noPlaceBlock.contains(getBlock(block.func_177972_a(face))) && BlockUtil.mc.field_71439_g.func_174831_c(block) <= MathUtil.square(5.0)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isReplaceable(final BlockPos pos) {
        return getState(pos).func_185904_a().func_76222_j();
    }
    
    public static boolean CrystalCheck(final BlockPos pos) {
        for (final Entity entity : BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityEnderCrystal)) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    public static boolean PlayerCheck(final BlockPos pos) {
        for (final Entity entity : BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityPlayer)) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    public static boolean hasSupport(final BlockPos pos) {
        boolean supported = false;
        for (final EnumFacing enumFacing : EnumFacing.field_176754_o) {
            if (!isAir(pos.func_177972_a(enumFacing))) {
                supported = true;
            }
        }
        return supported;
    }
    
    public static List<EnumFacing> getPossibleSides(final BlockPos pos) {
        final ArrayList<EnumFacing> facings = new ArrayList<EnumFacing>();
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.func_177972_a(side);
            if (BlockUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(BlockUtil.mc.field_71441_e.func_180495_p(neighbour), false)) {
                final IBlockState blockState;
                if (!(blockState = BlockUtil.mc.field_71441_e.func_180495_p(neighbour)).func_185904_a().func_76222_j()) {
                    facings.add(side);
                }
            }
        }
        return facings;
    }
    
    static Entity checkEntityCrystal(final Vec3d[] list, final BlockPos pos) {
        Entity test = null;
        for (final Vec3d vec3d : list) {
            final BlockPos position = new BlockPos((Vec3i)pos).func_177963_a(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
            for (final Object entity : BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(position))) {
                if (entity instanceof EntityPlayer || entity instanceof EntityEnderCrystal) {
                    if (test != null) {
                        continue;
                    }
                    test = (Entity)entity;
                }
            }
        }
        return test;
    }
    
    public static BlockPos vec3toBlockPos(final Vec3d vec3d) {
        return new BlockPos(Math.floor(vec3d.field_72450_a), (double)Math.round(vec3d.field_72448_b), Math.floor(vec3d.field_72449_c));
    }
    
    public static double distanceToXZ(final double x, final double z) {
        final double dx = BlockUtil.mc.field_71439_g.field_70165_t - x;
        final double dz = BlockUtil.mc.field_71439_g.field_70161_v - z;
        return Math.sqrt(dx * dx + dz * dz);
    }
    
    static Entity checkEntity(final Vec3d[] list, final BlockPos pos) {
        Entity test = null;
        for (final Vec3d vec3d : list) {
            final BlockPos position = new BlockPos((Vec3i)pos).func_177963_a(vec3d.field_72450_a, vec3d.field_72448_b, vec3d.field_72449_c);
            for (final Object entity : BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(position))) {
                if (entity instanceof EntityPlayer) {
                    if (test != null) {
                        continue;
                    }
                    test = (Entity)entity;
                }
            }
        }
        return test;
    }
    
    public static boolean isTrapHead(final EntityPlayer player) {
        return !isAir(new BlockPos(player.field_70165_t + 0.3, player.field_70163_u + 2.0, player.field_70161_v + 0.3)) && !isAir(new BlockPos(player.field_70165_t - 0.3, player.field_70163_u + 2.0, player.field_70161_v - 0.3)) && !isAir(new BlockPos(player.field_70165_t + 0.3, player.field_70163_u + 2.0, player.field_70161_v - 0.3)) && !isAir(new BlockPos(player.field_70165_t - 0.3, player.field_70163_u + 2.0, player.field_70161_v + 0.3));
    }
    
    public static boolean isAir(final BlockPos pos) {
        final Block block = BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        return block instanceof BlockAir;
    }
    
    public static double getMineDistance(final BlockPos to) {
        return getMineDistance((Entity)BlockUtil.mc.field_71439_g, to);
    }
    
    public static float getDistance(final BlockPos to) {
        return (float)getMineDistance((Entity)BlockUtil.mc.field_71439_g, to);
    }
    
    public static NonNullList<BlockPos> getBox(final EntityPlayer player, final float range) {
        final NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)getSphere(new BlockPos(Math.floor(player.field_70165_t), Math.floor(player.field_70163_u), Math.floor(player.field_70161_v)), range, 0, false, true, 0));
        return (NonNullList<BlockPos>)positions;
    }
    
    public static NonNullList<BlockPos> getBox(final float range) {
        final NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)getSphere(new BlockPos(Math.floor(BlockUtil.mc.field_71439_g.field_70165_t), Math.floor(BlockUtil.mc.field_71439_g.field_70163_u), Math.floor(BlockUtil.mc.field_71439_g.field_70161_v)), range, 0, false, true, 0));
        return (NonNullList<BlockPos>)positions;
    }
    
    public static boolean isIntercepted(final BlockPos blockPos) {
        for (final Entity entity : BlockUtil.mc.field_71441_e.field_72996_f) {
            final BlockPos blockPos2 = blockPos;
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityEnderCrystal)) {
                if (!new AxisAlignedBB(blockPos2).func_72326_a(entity.func_174813_aQ())) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }
    
    public static boolean isInterceptedByOther(final BlockPos blockPos) {
        for (final Entity entity : BlockUtil.mc.field_71441_e.field_72996_f) {
            if (!entity.equals((Object)BlockUtil.mc.field_71439_g)) {
                if (!new AxisAlignedBB(blockPos).func_72326_a(entity.func_174813_aQ())) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }
    
    public static boolean placeBlock(final BlockPos pos, final EnumHand hand, final boolean rotate, final boolean packet, final boolean isSneaking) {
        boolean sneaking = false;
        final EnumFacing side = getFirstFacing(pos);
        if (side == null) {
            return isSneaking;
        }
        final BlockPos neighbour = pos.func_177972_a(side);
        final EnumFacing opposite = side.func_176734_d();
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        final Block neighbourBlock = BlockUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
        if (!BlockUtil.mc.field_71439_g.func_70093_af() && (BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock))) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.field_71439_g.func_70095_a(true);
            sneaking = true;
        }
        if (rotate) {
            RotationUtil.faceVector(hitVec, true);
        }
        rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        return sneaking || isSneaking;
    }
    
    public static double getMineDistance(final Entity from, final BlockPos to) {
        final double x = from.field_70165_t - (to.func_177958_n() + 0.5);
        final double y = from.field_70163_u - (to.func_177956_o() + 0.5) + 1.5;
        final double z = from.field_70161_v - (to.func_177952_p() + 0.5);
        return x * x + y * y + z * z;
    }
    
    public static EnumFacing placeBlock(final BlockPos pos, final EnumHand hand, final boolean rotate, final boolean packet) {
        final EnumFacing side = getFirstFacing(pos);
        if (side == null) {
            return null;
        }
        final BlockPos neighbour = pos.func_177972_a(side);
        final EnumFacing opposite = side.func_176734_d();
        final Vec3d hitVec = new Vec3d((Vec3i)neighbour).func_72441_c(0.5, 0.5, 0.5).func_178787_e(new Vec3d(opposite.func_176730_m()).func_186678_a(0.5));
        final Block neighbourBlock = BlockUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c();
        if (!BlockUtil.mc.field_71439_g.func_70093_af() && (BlockUtil.blackList.contains(neighbourBlock) || BlockUtil.shulkerList.contains(neighbourBlock))) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)BlockUtil.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
            BlockUtil.mc.field_71439_g.func_70095_a(true);
        }
        if (rotate) {
            faceVectorPacketInstant(hitVec);
        }
        rightClickBlock(neighbour, hitVec, hand, opposite, packet);
        return opposite.func_176734_d();
    }
    
    public static EnumFacing getFirstFacing(final BlockPos pos) {
        final Iterator<EnumFacing> iterator2 = getPossibleSides(pos).iterator();
        if (iterator2.hasNext()) {
            final EnumFacing facing = iterator2.next();
            return facing;
        }
        return null;
    }
    
    public static EnumFacing placeEnum(final BlockPos pos) {
        for (final EnumFacing position : EnumFacing.field_176754_o) {
            if (BlockUtil.mc.field_71441_e.func_175623_d(pos.func_177972_a(position))) {
                Command.sendMessage(position.func_176610_l());
                return position;
            }
        }
        return null;
    }
    
    public static EnumFacing getFacing(final BlockPos pos) {
        for (final EnumFacing facing : EnumFacing.values()) {
            final RayTraceResult rayTraceResult = BlockUtil.mc.field_71441_e.func_147447_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n() + 0.5 + facing.func_176730_m().func_177958_n() * 1.0 / 2.0, pos.func_177956_o() + 0.5 + facing.func_176730_m().func_177956_o() * 1.0 / 2.0, pos.func_177952_p() + 0.5 + facing.func_176730_m().func_177952_p() * 1.0 / 2.0), false, true, false);
            if (rayTraceResult == null || (rayTraceResult.field_72313_a == RayTraceResult.Type.BLOCK && rayTraceResult.func_178782_a().equals((Object)pos))) {
                return facing;
            }
        }
        if (pos.func_177956_o() > BlockUtil.mc.field_71439_g.field_70163_u + BlockUtil.mc.field_71439_g.func_70047_e()) {
            return EnumFacing.DOWN;
        }
        return EnumFacing.UP;
    }
    
    public static EnumFacing getRayTraceFacing(final BlockPos pos) {
        final RayTraceResult result = BlockUtil.mc.field_71441_e.func_72933_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n() + 0.5, pos.func_177958_n() - 0.5, pos.func_177958_n() + 0.5));
        if (result == null || result.field_178784_b == null) {
            return EnumFacing.UP;
        }
        return result.field_178784_b;
    }
    
    public static void placeCrystalOnBlock(final BlockPos pos, final EnumHand hand) {
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, hand, (float)pos.func_177958_n(), (float)pos.func_177956_o(), (float)pos.func_177952_p()));
    }
    
    public static int isPositionPlaceable(final BlockPos pos, final boolean rayTrace) {
        return isPositionPlaceable(pos, rayTrace, true);
    }
    
    public static int isPositionPlaceable(final BlockPos pos, final boolean rayTrace, final boolean entityCheck) {
        final Block block = BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid) && !(block instanceof BlockTallGrass) && !(block instanceof BlockFire) && !(block instanceof BlockDeadBush) && !(block instanceof BlockSnow)) {
            return 0;
        }
        if (!rayTracePlaceCheck(pos, rayTrace, 0.0f)) {
            return -1;
        }
        if (entityCheck) {
            for (final Entity entity : BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(pos))) {
                if (!(entity instanceof EntityItem)) {
                    if (entity instanceof EntityXPOrb) {
                        continue;
                    }
                    return 1;
                }
            }
        }
        for (final EnumFacing side : getPossibleSides(pos)) {
            if (!canBeClicked(pos.func_177972_a(side))) {
                continue;
            }
            return 3;
        }
        return 2;
    }
    
    public static void rightClickBlock(final BlockPos pos, final Vec3d vec, final EnumHand hand, final EnumFacing direction, final boolean packet) {
        if (packet) {
            final float f = (float)(vec.field_72450_a - pos.func_177958_n());
            final float f2 = (float)(vec.field_72448_b - pos.func_177956_o());
            final float f3 = (float)(vec.field_72449_c - pos.func_177952_p());
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, direction, hand, f, f2, f3));
        }
        else {
            BlockUtil.mc.field_71442_b.func_187099_a(BlockUtil.mc.field_71439_g, BlockUtil.mc.field_71441_e, pos, direction, vec, hand);
        }
        BlockUtil.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
        BlockUtil.mc.field_71467_ac = 4;
    }
    
    public static void rightClickBlock(final BlockPos pos, final Vec3d vec, final EnumHand hand, final EnumFacing direction) {
        final float f = (float)(vec.field_72450_a - pos.func_177958_n());
        final float f2 = (float)(vec.field_72448_b - pos.func_177956_o());
        final float f3 = (float)(vec.field_72449_c - pos.func_177952_p());
    }
    
    public static Vec3d[] getHelpingBlocks(final Vec3d vec3d) {
        return new Vec3d[] { new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b - 1.0, vec3d.field_72449_c), new Vec3d((vec3d.field_72450_a != 0.0) ? (vec3d.field_72450_a * 2.0) : vec3d.field_72450_a, vec3d.field_72448_b, (vec3d.field_72450_a != 0.0) ? vec3d.field_72449_c : (vec3d.field_72449_c * 2.0)), new Vec3d((vec3d.field_72450_a == 0.0) ? (vec3d.field_72450_a + 1.0) : vec3d.field_72450_a, vec3d.field_72448_b, (vec3d.field_72450_a == 0.0) ? vec3d.field_72449_c : (vec3d.field_72449_c + 1.0)), new Vec3d((vec3d.field_72450_a == 0.0) ? (vec3d.field_72450_a - 1.0) : vec3d.field_72450_a, vec3d.field_72448_b, (vec3d.field_72450_a == 0.0) ? vec3d.field_72449_c : (vec3d.field_72449_c - 1.0)), new Vec3d(vec3d.field_72450_a, vec3d.field_72448_b + 1.0, vec3d.field_72449_c) };
    }
    
    public static boolean canPlaceEnum(final BlockPos blockPos) {
        return Util.mc.field_71441_e.func_175623_d(blockPos);
    }
    
    public static List<BlockPos> getSphere(final BlockPos pos, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = pos.func_177958_n();
        final int cy = pos.func_177956_o();
        final int cz = pos.func_177952_p();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                int y = sphere ? (cy - (int)r) : cy;
                while (true) {
                    final float f = (float)y;
                    final float f3;
                    final float f2 = f3 = (sphere ? (cy + r) : ((float)(cy + h)));
                    if (f >= f2) {
                        break;
                    }
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
            }
        }
        return circleblocks;
    }
    
    public static boolean canPlaceCrystal(final BlockPos blockPos) {
        final BlockPos boost = blockPos.func_177982_a(0, 1, 0);
        final BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
        try {
            return (BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150343_Z || BlockUtil.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() == Blocks.field_150357_h) && BlockUtil.mc.field_71441_e.func_180495_p(boost).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_180495_p(boost2).func_177230_c() == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public static boolean canPlaceCrystal(final BlockPos pos, final double distance) {
        if (BlockUtil.mc.field_71439_g.func_70092_e(pos.func_177958_n() + 0.5, pos.func_177956_o() + 0.5, pos.func_177952_p() + 0.5) > distance * distance) {
            return false;
        }
        final BlockPos obsPos = pos.func_177977_b();
        final BlockPos boost = obsPos.func_177984_a();
        final BlockPos boost2 = obsPos.func_177981_b(2);
        return (getBlock(obsPos) == Blocks.field_150343_Z || getBlock(obsPos) == Blocks.field_150357_h) && (getBlock(boost) == Blocks.field_150350_a || getBlock(boost) == Blocks.field_150324_C) && getBlock(boost2) == Blocks.field_150350_a && BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && BlockUtil.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return getBlock(pos).func_176209_a(getState(pos), false);
    }
    
    public static Block getBlock(final BlockPos pos) {
        return getState(pos).func_177230_c();
    }
    
    public static IBlockState getState(final BlockPos pos) {
        return BlockUtil.mc.field_71441_e.func_180495_p(pos);
    }
    
    public static void placeCrystalOnBlock(final BlockPos pos, final EnumHand hand, final boolean swing, final boolean exactHand) {
        final RayTraceResult result = BlockUtil.mc.field_71441_e.func_72933_a(new Vec3d(BlockUtil.mc.field_71439_g.field_70165_t, BlockUtil.mc.field_71439_g.field_70163_u + BlockUtil.mc.field_71439_g.func_70047_e(), BlockUtil.mc.field_71439_g.field_70161_v), new Vec3d(pos.func_177958_n() + 0.5, pos.func_177956_o() - 0.5, pos.func_177952_p() + 0.5));
        final EnumFacing facing = (result == null || result.field_178784_b == null) ? EnumFacing.UP : result.field_178784_b;
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, facing, hand, 0.0f, 0.0f, 0.0f));
        if (swing) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(exactHand ? hand : EnumHand.MAIN_HAND));
        }
    }
    
    public static void placeCrystalOnBlock(final BlockPos pos, final EnumHand hand, final boolean swing, final boolean exactHand, final boolean silent, final boolean strictFac) {
        final EnumFacing side = getFirstFacing(pos);
        if (side == null) {
            return;
        }
        final int old = BlockUtil.mc.field_71439_g.field_71071_by.field_70461_c;
        final int crystal = InventoryUtil.getItemHotbar(Items.field_185158_cP);
        if (hand != EnumHand.MAIN_HAND || !silent || crystal == -1 || crystal != BlockUtil.mc.field_71439_g.field_71071_by.field_70461_c) {}
        BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(pos, side, hand, 0.0f, 0.0f, 0.0f));
        if (swing) {
            BlockUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(exactHand ? hand : EnumHand.MAIN_HAND));
        }
    }
    
    public static BlockPos[] toBlockPos(final Vec3d[] vec3ds) {
        final BlockPos[] list = new BlockPos[vec3ds.length];
        for (int i = 0; i < vec3ds.length; ++i) {
            list[i] = new BlockPos(vec3ds[i]);
        }
        return list;
    }
    
    public static boolean isBlockUnSolid(final Block block) {
        return BlockUtil.unSolidBlocks.contains(block);
    }
    
    public static Vec3d[] convertVec3ds(final Vec3d vec3d, final Vec3d[] input) {
        final Vec3d[] output = new Vec3d[input.length];
        for (int i = 0; i < input.length; ++i) {
            output[i] = vec3d.func_178787_e(input[i]);
        }
        return output;
    }
    
    public static boolean canBreak(final BlockPos pos) {
        final IBlockState blockState = BlockUtil.mc.field_71441_e.func_180495_p(pos);
        final Block block = blockState.func_177230_c();
        return block.func_180647_a(blockState, (EntityPlayer)BlockUtil.mc.field_71439_g, (World)BlockUtil.mc.field_71441_e, pos) != -1.0f;
    }
    
    public static boolean isValidBlock(final BlockPos pos) {
        final Block block = BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        return !(block instanceof BlockLiquid) && block.func_149688_o((IBlockState)null) != Material.field_151579_a;
    }
    
    public static boolean isScaffoldPos(final BlockPos pos) {
        return BlockUtil.mc.field_71441_e.func_175623_d(pos) || BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150321_G || BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150395_bd || BlockUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() instanceof BlockLiquid;
    }
    
    public static boolean rayTracePlaceCheck(final BlockPos pos, final boolean shouldCheck, final float height) {
        return !shouldCheck || canBlockBeSeen(pos.func_177958_n() + 0.5, pos.func_177956_o() + height, pos.func_177952_p() + 0.5);
    }
    
    static {
        canUseList = Arrays.asList(Blocks.field_150467_bQ, Blocks.field_150324_C, (Block)Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150477_bB, Blocks.field_150415_aT, Blocks.field_150381_bn, Blocks.field_150460_al, (Block)Blocks.field_150438_bZ, Blocks.field_150409_cd, Blocks.field_150367_z);
        noPlaceBlock = Arrays.asList(Blocks.field_150350_a, Blocks.field_150467_bQ, (Block)Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150477_bB, Blocks.field_150415_aT);
        blackList = Arrays.asList(Blocks.field_150467_bQ, Blocks.field_150324_C, (Block)Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150477_bB, Blocks.field_150415_aT, Blocks.field_150381_bn, Blocks.field_150460_al, (Block)Blocks.field_150438_bZ, Blocks.field_150409_cd, Blocks.field_150367_z);
        shulkerList = Arrays.asList(Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA);
        unSafeBlocks = Arrays.asList(Blocks.field_150357_h, Blocks.field_150343_Z, Blocks.field_150467_bQ, Blocks.field_150477_bB);
        cantPushBlocks = Arrays.asList(Blocks.field_150357_h, Blocks.field_150343_Z, Blocks.field_150467_bQ, Blocks.field_150324_C);
        BlockUtil.unSolidBlocks = Arrays.asList((Block)Blocks.field_150486_ae, Blocks.field_150478_aa, Blocks.field_150429_aA, (Block)Blocks.field_150488_af, Blocks.field_150442_at, Blocks.field_150430_aB, Blocks.field_150471_bO, (Block)Blocks.field_150479_bC, Blocks.field_150473_bD, Blocks.field_150345_g, (Block)Blocks.field_150329_H, (Block)Blocks.field_150330_I, (Block)Blocks.field_150327_N, (Block)Blocks.field_150328_O, (Block)Blocks.field_150338_P, (Block)Blocks.field_150337_Q, Blocks.field_150350_a, Blocks.field_150457_bL, (Block)Blocks.field_150465_bP, Blocks.field_150431_aC, Blocks.field_150467_bQ, Blocks.field_150447_bR, Blocks.field_150477_bB, (Block)Blocks.field_150355_j, (Block)Blocks.field_150358_i, (Block)Blocks.field_150353_l, (Block)Blocks.field_150356_k, Blocks.field_150395_bd, Blocks.field_150468_ap, Blocks.field_150472_an, Blocks.field_150444_as, (Block)Blocks.field_150436_aH, (Block)Blocks.field_150427_aO, Blocks.field_150384_bq, Blocks.field_150378_br, Blocks.field_185775_db, Blocks.field_150321_G, Blocks.field_150404_cg, (Block)Blocks.field_150398_cm, (Block)Blocks.field_150332_K, (Block)Blocks.field_180384_M);
        BlockUtil.godBlocks = Arrays.asList(Blocks.field_150350_a, Blocks.field_150467_bQ, (Block)Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150477_bB, Blocks.field_150343_Z, Blocks.field_150324_C);
    }
}
