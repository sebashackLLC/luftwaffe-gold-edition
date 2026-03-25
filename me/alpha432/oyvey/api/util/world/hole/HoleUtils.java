//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.world.hole;

import net.minecraft.client.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;
import net.minecraft.block.*;

public class HoleUtils
{
    public static BlockPos[] holeOffsets;
    private static final Minecraft mc;
    
    public static boolean isHole(final BlockPos pos) {
        int amount = 0;
        for (final BlockPos p : HoleUtils.holeOffsets) {
            if (!HoleUtils.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)p)).func_185904_a().func_76222_j()) {
                ++amount;
            }
        }
        return amount == 5;
    }
    
    public static boolean isObbyHole(final BlockPos pos) {
        boolean isHole = true;
        int obsidianCount = 0;
        for (final BlockPos off : HoleUtils.holeOffsets) {
            final Block block = HoleUtils.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)off)).func_177230_c();
            if (!isSafeBlock(pos.func_177971_a((Vec3i)off))) {
                isHole = false;
            }
            else if (block == Blocks.field_150343_Z || block == Blocks.field_150477_bB || block == Blocks.field_150467_bQ) {
                ++obsidianCount;
            }
        }
        if (HoleUtils.mc.field_71441_e.func_180495_p(pos.func_177981_b(1)).func_177230_c() != Blocks.field_150350_a || HoleUtils.mc.field_71441_e.func_180495_p(pos.func_177981_b(2)).func_177230_c() != Blocks.field_150350_a) {
            isHole = false;
        }
        return isHole && obsidianCount >= 1;
    }
    
    public static boolean isBedrockHoles(final BlockPos pos) {
        for (final BlockPos off : HoleUtils.holeOffsets) {
            final Block block = HoleUtils.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)off)).func_177230_c();
            if (block != Blocks.field_150357_h) {
                return false;
            }
        }
        return HoleUtils.mc.field_71441_e.func_180495_p(pos.func_177981_b(1)).func_177230_c() == Blocks.field_150350_a && HoleUtils.mc.field_71441_e.func_180495_p(pos.func_177981_b(2)).func_177230_c() == Blocks.field_150350_a;
    }
    
    public static boolean isWeb(final BlockPos pos) {
        return HoleUtils.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150321_G || HoleUtils.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150321_G || HoleUtils.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150321_G;
    }
    
    private static boolean isSafeBlock(final BlockPos pos) {
        final Block block = HoleUtils.mc.field_71441_e.func_180495_p(pos).func_177230_c();
        return block == Blocks.field_150343_Z || block == Blocks.field_150357_h || block == Blocks.field_150477_bB || block == Blocks.field_150467_bQ;
    }
    
    static {
        HoleUtils.holeOffsets = new BlockPos[] { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(0, -1, 0) };
        mc = Minecraft.func_71410_x();
    }
}
