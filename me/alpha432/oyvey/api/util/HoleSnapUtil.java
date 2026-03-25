//Decompiled by Procyon!

package me.alpha432.oyvey.api.util;

import net.minecraft.block.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;
import java.util.*;

public class HoleSnapUtil
{
    public static BlockPos[] holeOffsets;
    public static final Block[] TERRAIN_BLOCKS;
    
    public static boolean isInHole(final Entity entity) {
        final BlockPos pos = PlayerUtil.getPlayerPos(entity);
        return isObbyHole(pos) || isBedrockHoles(pos) || isMixedHole(pos);
    }
    
    public static BlockPos getPlayerPos() {
        final double decimalPoint = Util.mc.field_71439_g.field_70163_u - Math.floor(Util.mc.field_71439_g.field_70163_u);
        return new BlockPos(Util.mc.field_71439_g.field_70165_t, (decimalPoint > 0.8) ? (Math.floor(Util.mc.field_71439_g.field_70163_u) + 1.0) : Math.floor(Util.mc.field_71439_g.field_70163_u), Util.mc.field_71439_g.field_70161_v);
    }
    
    public static Hole getTargetHoleVec3D(final double targetRange) {
        return getHoles(targetRange, getPlayerPos(), true).stream().filter(hole -> Util.mc.field_71439_g.func_174791_d().func_72438_d(new Vec3d(hole.pos1.func_177958_n() + 0.5, Util.mc.field_71439_g.field_70163_u, hole.pos1.func_177952_p() + 0.5)) <= targetRange).min(Comparator.comparingDouble(hole -> Util.mc.field_71439_g.func_174791_d().func_72438_d(new Vec3d(hole.pos1.func_177958_n() + 0.5, Util.mc.field_71439_g.field_70163_u, hole.pos1.func_177952_p() + 0.5)))).orElse(null);
    }
    
    public static boolean isHole(final BlockPos pos) {
        int amount = 0;
        for (final BlockPos p : HoleSnapUtil.holeOffsets) {
            if (!Util.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)p)).func_185904_a().func_76222_j()) {
                ++amount;
            }
        }
        return amount == 5;
    }
    
    public static boolean isObsidian(final BlockPos pos) {
        final Block block = Util.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        return block == Blocks.field_150343_Z || block == Blocks.field_150477_bB || block == Blocks.field_150467_bQ;
    }
    
    public static boolean isTerrainBlock(final Block block) {
        for (final Block terrainBlock : HoleSnapUtil.TERRAIN_BLOCKS) {
            if (block == terrainBlock) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isPureTerrainHole(final BlockPos pos) {
        boolean isHole = true;
        for (final BlockPos off : HoleSnapUtil.holeOffsets) {
            final Block block = Util.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)off)).func_177230_c();
            if (!isTerrainBlock(block)) {
                isHole = false;
                break;
            }
        }
        return isHole && Util.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 2, 0)).func_177230_c() == Blocks.field_150350_a && Util.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c() == Blocks.field_150350_a;
    }
    
    public static boolean isObbyHole(final BlockPos pos) {
        boolean isHole = true;
        int obsidianCount = 0;
        for (final BlockPos off : HoleSnapUtil.holeOffsets) {
            final Block block = Util.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)off)).func_177230_c();
            if (!isSafeBlock(pos.func_177971_a((Vec3i)off))) {
                isHole = false;
            }
            else if (isObsidian(pos.func_177971_a((Vec3i)off))) {
                ++obsidianCount;
            }
        }
        if (Util.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 2, 0)).func_177230_c() != Blocks.field_150350_a || Util.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c() != Blocks.field_150350_a || obsidianCount < 1) {
            isHole = false;
        }
        return isHole;
    }
    
    public static boolean isBedrockHoles(final BlockPos pos) {
        boolean isHole = true;
        for (final BlockPos off : HoleSnapUtil.holeOffsets) {
            final Block block = Util.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)off)).func_177230_c();
            if (block != Blocks.field_150357_h) {
                isHole = false;
                break;
            }
        }
        return isHole && Util.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 2, 0)).func_177230_c() == Blocks.field_150350_a && Util.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c() == Blocks.field_150350_a;
    }
    
    public static boolean isMixedHole(final BlockPos pos) {
        boolean isHole = true;
        boolean hasBedrock = false;
        boolean hasObsidian = false;
        for (final BlockPos off : HoleSnapUtil.holeOffsets) {
            final Block block = Util.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)off)).func_177230_c();
            if (block == Blocks.field_150357_h) {
                hasBedrock = true;
            }
            else if (isObsidian(pos.func_177971_a((Vec3i)off))) {
                hasObsidian = true;
            }
            else if (!isSafeBlock(pos.func_177971_a((Vec3i)off))) {
                isHole = false;
            }
        }
        return isHole && hasBedrock && hasObsidian && Util.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 2, 0)).func_177230_c() == Blocks.field_150350_a && Util.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c() == Blocks.field_150350_a;
    }
    
    public static Hole isDoubleHole(final BlockPos pos) {
        boolean bedrock = false;
        boolean obsidian = false;
        if (checkOffset(pos, 1, 0)) {
            bedrock = (Util.mc.field_71441_e.func_180495_p(pos.func_177982_a(1, 0, 0)).func_177230_c() == Blocks.field_150357_h);
            obsidian = isObsidian(pos.func_177982_a(1, 0, 0));
            return new Hole(bedrock, obsidian, pos, pos.func_177982_a(1, 0, 0));
        }
        if (checkOffset(pos, 0, 1)) {
            bedrock = (Util.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 0, 1)).func_177230_c() == Blocks.field_150357_h);
            obsidian = isObsidian(pos.func_177982_a(0, 0, 1));
            return new Hole(bedrock, obsidian, pos, pos.func_177982_a(0, 0, 1));
        }
        return null;
    }
    
    public static boolean checkOffset(final BlockPos pos, final int offX, final int offZ) {
        return Util.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150350_a && Util.mc.field_71441_e.func_180495_p(pos.func_177982_a(offX, 0, offZ)).func_177230_c() == Blocks.field_150350_a && isSafeBlock(pos.func_177982_a(0, -1, 0)) && isSafeBlock(pos.func_177982_a(offX, -1, offZ)) && isSafeBlock(pos.func_177982_a(offX * 2, 0, offZ * 2)) && isSafeBlock(pos.func_177982_a(-offX, 0, -offZ)) && isSafeBlock(pos.func_177982_a(offZ, 0, offX)) && isSafeBlock(pos.func_177982_a(-offZ, 0, -offX)) && isSafeBlock(pos.func_177982_a(offX, 0, offZ).func_177982_a(offZ, 0, offX)) && isSafeBlock(pos.func_177982_a(offX, 0, offZ).func_177982_a(-offZ, 0, -offX));
    }
    
    public static boolean isSafeBlock(final BlockPos pos) {
        final Block block = Util.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        return block == Blocks.field_150343_Z || block == Blocks.field_150357_h || block == Blocks.field_150477_bB;
    }
    
    public static List<Hole> getHoles(final double range, final BlockPos playerPos, final boolean doubles) {
        final ArrayList<Hole> holes = new ArrayList<Hole>();
        final List<BlockPos> circle = getSphere(range, playerPos, true, false);
        for (final BlockPos pos : circle) {
            if (Util.mc.field_71441_e.func_180495_p(pos).func_177230_c() != Blocks.field_150350_a) {
                continue;
            }
            if (isObbyHole(pos)) {
                holes.add(new Hole(false, true, pos));
            }
            else if (isBedrockHoles(pos)) {
                holes.add(new Hole(true, false, pos));
            }
            else if (isPureTerrainHole(pos)) {
                holes.add(new Hole(false, false, pos));
            }
            else if (isMixedHole(pos)) {
                holes.add(new Hole(true, true, pos));
            }
            else {
                final Hole doubleHole;
                if (!doubles || (doubleHole = isDoubleHole(pos)) == null || Util.mc.field_71441_e.func_180495_p(doubleHole.pos1.func_177982_a(0, 1, 0)).func_177230_c() != Blocks.field_150350_a) {
                    continue;
                }
                if (Util.mc.field_71441_e.func_180495_p(doubleHole.pos2.func_177982_a(0, 1, 0)).func_177230_c() != Blocks.field_150350_a) {
                    continue;
                }
                holes.add(doubleHole);
            }
        }
        return holes;
    }
    
    public static List<BlockPos> getSphere(final double range, final BlockPos pos, final boolean sphere, final boolean hollow) {
        final ArrayList<BlockPos> circleBlocks = new ArrayList<BlockPos>();
        final int cx = pos.func_177958_n();
        final int cy = pos.func_177956_o();
        final int cz = pos.func_177952_p();
        for (int x = cx - (int)range; x <= cx + range; ++x) {
            for (int z = cz - (int)range; z <= cz + range; ++z) {
                for (int y = sphere ? (cy - (int)range) : cy; y < (sphere ? (cy + range) : (cy + range)); ++y) {
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < range * range && (!hollow || dist >= (range - 1.0) * (range - 1.0))) {
                        circleBlocks.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        return circleBlocks;
    }
    
    static {
        HoleSnapUtil.holeOffsets = new BlockPos[] { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(0, -1, 0) };
        TERRAIN_BLOCKS = new Block[] { Blocks.field_150348_b, Blocks.field_150424_aL, Blocks.field_150347_e, Blocks.field_150346_d, (Block)Blocks.field_150349_c, Blocks.field_150351_n, (Block)Blocks.field_150354_m, (Block)Blocks.field_150376_bx, Blocks.field_150325_L, Blocks.field_150406_ce, Blocks.field_150322_A, Blocks.field_150377_bs };
    }
    
    public static class Hole
    {
        public boolean bedrock;
        public boolean obsidian;
        public boolean doubleHole;
        public boolean terrain;
        public BlockPos pos1;
        public BlockPos pos2;
        
        public Hole(final boolean bedrock, final boolean obsidian, final BlockPos pos1, final BlockPos pos2) {
            this.bedrock = bedrock;
            this.obsidian = obsidian;
            this.doubleHole = (pos2 != null);
            this.terrain = (!bedrock && !obsidian);
            this.pos1 = pos1;
            this.pos2 = pos2;
        }
        
        public Hole(final boolean bedrock, final boolean obsidian, final BlockPos pos1) {
            this(bedrock, obsidian, pos1, null);
        }
        
        public BlockPos getPos1() {
            return this.pos1;
        }
        
        public BlockPos getPos2() {
            return this.pos2;
        }
    }
}
