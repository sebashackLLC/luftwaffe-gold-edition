//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.world.hole.holeesp;

import me.alpha432.oyvey.api.util.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.api.util.world.*;
import net.minecraft.init.*;
import net.minecraft.block.*;
import me.alpha432.oyvey.api.util.world.hole.holeesp.Enums.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.*;
import java.util.*;

public class HoleUtil implements Util
{
    public static BlockPos[] HOLE_OFFSETS;
    public static BlockPos[] AROUND_OFFSETS;
    
    public static boolean isHole(final BlockPos pos) {
        return isMixedHole(pos) || isBedrockHole(pos) || isObbyHole(pos);
    }
    
    public static boolean isInHole(final EntityPlayer player) {
        return isHole(player.func_180425_c());
    }
    
    public static boolean isWebHole(final BlockPos pos) {
        if (!BlockUtil.isAir(pos.func_177984_a()) || !BlockUtil.isAir(pos.func_177984_a().func_177984_a())) {
            return false;
        }
        for (final BlockPos off : HoleUtil.HOLE_OFFSETS) {
            if (!isWeb(pos.func_177971_a((Vec3i)off))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isWeb(final BlockPos pos) {
        return HoleUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150321_G || HoleUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150343_Z || HoleUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150357_h;
    }
    
    public static boolean isObbyHole(final BlockPos pos) {
        assert HoleUtil.mc.field_71441_e != null;
        if (!BlockUtil.isAir(pos.func_177984_a()) || !BlockUtil.isAir(pos.func_177981_b(2))) {
            return false;
        }
        for (final BlockPos off : HoleUtil.HOLE_OFFSETS) {
            if (!BlockUtil.isObby(pos.func_177971_a((Vec3i)off))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isMixedHole(final BlockPos pos) {
        assert HoleUtil.mc.field_71441_e != null;
        if (isBedrockHole(pos)) {
            return false;
        }
        if (!BlockUtil.isAir(pos.func_177984_a()) || !BlockUtil.isAir(pos.func_177981_b(2))) {
            return false;
        }
        for (final BlockPos off : HoleUtil.HOLE_OFFSETS) {
            if (!BlockUtil.isSafe(pos.func_177971_a((Vec3i)off))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isBedrockHole(final BlockPos pos) {
        if (!BlockUtil.isAir(pos.func_177984_a()) || !BlockUtil.isAir(pos.func_177981_b(2))) {
            return false;
        }
        for (final BlockPos off : HoleUtil.HOLE_OFFSETS) {
            if (!BlockUtil.isBedrock(pos.func_177971_a((Vec3i)off))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isTrapHole(final BlockPos pos) {
        if (isHole(pos) || !BlockUtil.isAir(pos.func_177984_a()) || BlockUtil.isAir(pos.func_177981_b(2)) || !BlockUtil.isSafe(pos.func_177977_b())) {
            return false;
        }
        if (!BlockUtil.isSafe(pos.func_177977_b())) {
            return false;
        }
        for (final BlockPos off : HoleUtil.AROUND_OFFSETS) {
            if (!BlockUtil.isSafe(pos.func_177984_a().func_177971_a((Vec3i)off))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isTerrainHole(final BlockPos pos) {
        if (isHole(pos)) {
            return false;
        }
        if (!BlockUtil.isAir(pos.func_177984_a()) || !BlockUtil.isAir(pos.func_177981_b(2))) {
            return false;
        }
        for (final BlockPos off : HoleUtil.HOLE_OFFSETS) {
            final Block block = HoleUtil.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)off)).func_177230_c();
            if (block != Blocks.field_150348_b && block != Blocks.field_150346_d && block != Blocks.field_150322_A && block != Blocks.field_150347_e && block != Blocks.field_150424_aL && block != Blocks.field_150377_bs && !BlockUtil.isSafe(pos.func_177971_a((Vec3i)off))) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isDoubleHole(final BlockPos pos) {
        final Hole hole = getDoubleHole(pos);
        return hole != null && BlockUtil.isAir(hole.getFirst().func_177984_a()) && BlockUtil.isAir(hole.getSecond().func_177984_a()) && BlockUtil.isAir(hole.getFirst().func_177981_b(2)) && BlockUtil.isAir(hole.getFirst().func_177981_b(2));
    }
    
    public static Hole getDoubleHole(final BlockPos pos) {
        if (isBedrock(pos, 0, 1)) {
            return new Hole(pos, pos.func_177982_a(0, 0, 1), SafetyEnum.BEDROCK);
        }
        if (isBedrock(pos, 1, 0)) {
            return new Hole(pos, pos.func_177982_a(1, 0, 0), SafetyEnum.BEDROCK);
        }
        if (isObby(pos, 0, 1)) {
            return new Hole(pos, pos.func_177982_a(0, 0, 1), SafetyEnum.OBBY);
        }
        if (isObby(pos, 1, 0)) {
            return new Hole(pos, pos.func_177982_a(1, 0, 0), SafetyEnum.OBBY);
        }
        if (isMixed(pos, 0, 1)) {
            return new Hole(pos, pos.func_177982_a(0, 0, 1), SafetyEnum.MIXED);
        }
        if (isMixed(pos, 1, 0)) {
            return new Hole(pos, pos.func_177982_a(1, 0, 0), SafetyEnum.MIXED);
        }
        return null;
    }
    
    public static Vec3d getCenter(final Hole hole) {
        double x = hole.getFirst().func_177958_n() + 0.5;
        double z = hole.getFirst().func_177952_p() + 0.5;
        if (hole.getSecond() != null) {
            x = (x + hole.getSecond().func_177958_n() + 0.5) / 2.0;
            z = (z + hole.getSecond().func_177952_p() + 0.5) / 2.0;
        }
        return new Vec3d(x, (double)hole.getFirst().func_177956_o(), z);
    }
    
    public static boolean isObby(final BlockPos pos, final int offX, final int offZ) {
        return BlockUtil.isAir(pos) && BlockUtil.isAir(pos.func_177982_a(offX, 0, offZ)) && BlockUtil.isObby(pos.func_177982_a(0, -1, 0)) && BlockUtil.isObby(pos.func_177982_a(offX, -1, offZ)) && BlockUtil.isObby(pos.func_177982_a(offX * 2, 0, offZ * 2)) && BlockUtil.isObby(pos.func_177982_a(-offX, 0, -offZ)) && BlockUtil.isObby(pos.func_177982_a(offZ, 0, offX)) && BlockUtil.isObby(pos.func_177982_a(-offZ, 0, -offX)) && BlockUtil.isObby(pos.func_177982_a(offX, 0, offZ).func_177982_a(offZ, 0, offX)) && BlockUtil.isObby(pos.func_177982_a(offX, 0, offZ).func_177982_a(-offZ, 0, -offX));
    }
    
    public static boolean isMixed(final BlockPos pos, final int offX, final int offZ) {
        return BlockUtil.isAir(pos) && BlockUtil.isAir(pos.func_177982_a(offX, 0, offZ)) && BlockUtil.isSafe(pos.func_177982_a(0, -1, 0)) && BlockUtil.isSafe(pos.func_177982_a(offX, -1, offZ)) && BlockUtil.isSafe(pos.func_177982_a(offX * 2, 0, offZ * 2)) && BlockUtil.isSafe(pos.func_177982_a(-offX, 0, -offZ)) && BlockUtil.isSafe(pos.func_177982_a(offZ, 0, offX)) && BlockUtil.isSafe(pos.func_177982_a(-offZ, 0, -offX)) && BlockUtil.isSafe(pos.func_177982_a(offX, 0, offZ).func_177982_a(offZ, 0, offX)) && BlockUtil.isSafe(pos.func_177982_a(offX, 0, offZ).func_177982_a(-offZ, 0, -offX));
    }
    
    public static boolean isBedrock(final BlockPos pos, final int offX, final int offZ) {
        return BlockUtil.isAir(pos) && BlockUtil.isAir(pos.func_177982_a(offX, 0, offZ)) && BlockUtil.isBedrock(pos.func_177982_a(0, -1, 0)) && BlockUtil.isBedrock(pos.func_177982_a(offX, -1, offZ)) && BlockUtil.isBedrock(pos.func_177982_a(offX * 2, 0, offZ * 2)) && BlockUtil.isBedrock(pos.func_177982_a(-offX, 0, -offZ)) && BlockUtil.isBedrock(pos.func_177982_a(offZ, 0, offX)) && BlockUtil.isBedrock(pos.func_177982_a(-offZ, 0, -offX)) && BlockUtil.isBedrock(pos.func_177982_a(offX, 0, offZ).func_177982_a(offZ, 0, offX)) && BlockUtil.isBedrock(pos.func_177982_a(offX, 0, offZ).func_177982_a(-offZ, 0, -offX));
    }
    
    public static Hole getHole(final BlockPos pos, final boolean doubles) {
        return getHole(pos, doubles, false);
    }
    
    public static Hole getHole(final BlockPos pos, final boolean doubles, final boolean terrain) {
        if (!BlockUtil.isAir(pos)) {
            return null;
        }
        Hole hole = null;
        if (isBedrockHole(pos)) {
            hole = new Hole(pos, SafetyEnum.BEDROCK);
        }
        else if (isObbyHole(pos)) {
            hole = new Hole(pos, SafetyEnum.OBBY);
        }
        else if (isMixedHole(pos)) {
            hole = new Hole(pos, SafetyEnum.MIXED);
        }
        else if (terrain && isTerrainHole(pos)) {
            hole = new Hole(pos, SafetyEnum.TERRAIN);
        }
        if (doubles && isDoubleHole(pos)) {
            hole = getDoubleHole(pos);
        }
        return hole;
    }
    
    public static List<Hole> getHoles(final float range, final boolean doubles, final boolean webs) {
        return getHoles((Entity)HoleUtil.mc.field_71439_g, range, doubles, webs, false, false);
    }
    
    public static List<Hole> getHoles(final float range, final boolean doubles, final boolean webs, final boolean trap, final boolean terrain) {
        return getHoles((Entity)HoleUtil.mc.field_71439_g, range, doubles, webs, trap, terrain);
    }
    
    public static List<Hole> getHoles(final Entity player, final float range, final boolean doubles, final boolean webs, final boolean trap, final boolean terrain) {
        final List<Hole> holes = new ArrayList<Hole>();
        for (final BlockPos pos : BlockUtil.getSphere(player, range, false)) {
            if (!BlockUtil.isAir(pos)) {
                continue;
            }
            if (webs && isWebHole(pos)) {
                holes.add(new Hole(pos, SafetyEnum.MIXED));
            }
            else if (isBedrockHole(pos)) {
                holes.add(new Hole(pos, SafetyEnum.BEDROCK));
            }
            else if (isObbyHole(pos)) {
                holes.add(new Hole(pos, SafetyEnum.OBBY));
            }
            else if (isMixedHole(pos)) {
                holes.add(new Hole(pos, SafetyEnum.MIXED));
            }
            else if (trap && isTrapHole(pos)) {
                holes.add(new Hole(pos, SafetyEnum.TRAPPED));
            }
            else if (terrain && isTerrainHole(pos)) {
                holes.add(new Hole(pos, SafetyEnum.TERRAIN));
            }
            else {
                if (!doubles) {
                    continue;
                }
                if (!isDoubleHole(pos)) {
                    continue;
                }
                holes.add(getDoubleHole(pos));
            }
        }
        return holes;
    }
    
    static {
        HoleUtil.HOLE_OFFSETS = new BlockPos[] { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(0, -1, 0) };
        HoleUtil.AROUND_OFFSETS = new BlockPos[] { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
    }
}
