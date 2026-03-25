//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.world;

import me.alpha432.oyvey.api.util.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.block.state.*;
import net.minecraft.util.math.*;
import net.minecraft.init.*;
import java.util.*;

public class WorldUtil implements Util
{
    public static final ArrayList<Block> empty;
    public static final List<Block> blackList;
    
    public static Block getBlock(final BlockPos block) {
        return WorldUtil.mc.field_71441_e.func_180495_p(block).func_177230_c();
    }
    
    public static EnumFacing getPlaceableSide(final BlockPos pos) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.func_177972_a(side);
            final IBlockState blockState;
            if (WorldUtil.mc.field_71441_e.func_180495_p(neighbour).func_177230_c().func_176209_a(WorldUtil.mc.field_71441_e.func_180495_p(neighbour), false) && !(blockState = WorldUtil.mc.field_71441_e.func_180495_p(neighbour)).func_177230_c().func_176200_f((IBlockAccess)WorldUtil.mc.field_71441_e, neighbour)) {
                return side;
            }
        }
        return null;
    }
    
    public static double getRange(final Vec3d vec) {
        return WorldUtil.mc.field_71439_g.func_174791_d().func_72441_c(0.0, (double)WorldUtil.mc.field_71439_g.func_70047_e(), 0.0).func_72438_d(vec);
    }
    
    public static EnumFacing getEnumFacing(final boolean rayTrace, final BlockPos placePosition2) {
        final RayTraceResult result = WorldUtil.mc.field_71441_e.func_72933_a(new Vec3d(WorldUtil.mc.field_71439_g.field_70165_t, WorldUtil.mc.field_71439_g.field_70163_u + WorldUtil.mc.field_71439_g.func_70047_e(), WorldUtil.mc.field_71439_g.field_70161_v), new Vec3d(placePosition2.func_177958_n() + 0.5, placePosition2.func_177956_o() - 0.5, placePosition2.func_177952_p() + 0.5));
        if (placePosition2.func_177956_o() == 255) {
            return EnumFacing.DOWN;
        }
        if (rayTrace) {
            return (result == null || result.field_178784_b == null) ? EnumFacing.UP : result.field_178784_b;
        }
        return EnumFacing.UP;
    }
    
    public static boolean canBeClicked(final BlockPos pos) {
        return WorldUtil.mc.field_71441_e.func_180495_p(pos).func_177230_c().func_176209_a(WorldUtil.mc.field_71441_e.func_180495_p(pos), false);
    }
    
    public static boolean isWithin(final double distance, final Vec3d vec, final Vec3d vec2) {
        return vec.func_72436_e(vec2) <= distance * distance;
    }
    
    public static boolean isOutside(final double distance, final Vec3d vec, final Vec3d vec2) {
        return vec.func_72436_e(vec2) > distance * distance;
    }
    
    static {
        empty = new ArrayList<Block>(Arrays.asList(Blocks.field_150350_a, Blocks.field_150472_an, Blocks.field_150444_as, Blocks.field_150456_au, Blocks.field_150452_aw, Blocks.field_150445_bS, Blocks.field_150443_bT, Blocks.field_150473_bD, (Block)Blocks.field_150479_bC));
        blackList = Arrays.asList(Blocks.field_150477_bB, (Block)Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150324_C, Blocks.field_150460_al, Blocks.field_150470_am, Blocks.field_150367_z, Blocks.field_150409_cd, (Block)Blocks.field_150438_bZ, (Block)Blocks.field_150461_bJ, (Block)Blocks.field_150413_aR, (Block)Blocks.field_150416_aS);
    }
}
