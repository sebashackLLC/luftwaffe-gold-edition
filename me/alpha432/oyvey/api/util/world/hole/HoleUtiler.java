//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.world.hole;

import me.alpha432.oyvey.api.util.*;
import net.minecraft.util.math.*;
import net.minecraft.block.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import me.alpha432.oyvey.api.util.entity.*;
import me.alpha432.oyvey.api.util.world.*;
import java.util.*;

public class HoleUtiler implements Util
{
    public static Hole getHole(final BlockPos pos, final int height) {
        boolean hasObstruction = false;
        for (int yOffset = 0; yOffset < height; ++yOffset) {
            if (!WorldUtil.empty.contains(WorldUtil.getBlock(pos.func_177981_b(yOffset + 1)))) {
                hasObstruction = true;
            }
        }
        if (hasObstruction) {
            return null;
        }
        if (WorldUtil.empty.contains(WorldUtil.getBlock(pos)) && !WorldUtil.empty.contains(WorldUtil.getBlock(pos.func_177977_b()))) {
            if ((WorldUtil.getBlock(pos.func_177978_c()) instanceof BlockObsidian || WorldUtil.getBlock(pos.func_177978_c()) == Blocks.field_150357_h) && (WorldUtil.getBlock(pos.func_177968_d()) instanceof BlockObsidian || WorldUtil.getBlock(pos.func_177968_d()) == Blocks.field_150357_h) && (WorldUtil.getBlock(pos.func_177974_f()) instanceof BlockObsidian || WorldUtil.getBlock(pos.func_177974_f()) == Blocks.field_150357_h) && (WorldUtil.getBlock(pos.func_177976_e()) instanceof BlockObsidian || WorldUtil.getBlock(pos.func_177976_e()) == Blocks.field_150357_h)) {
                if (WorldUtil.getBlock(pos.func_177978_c()) instanceof BlockObsidian || WorldUtil.getBlock(pos.func_177974_f()) instanceof BlockObsidian || WorldUtil.getBlock(pos.func_177968_d()) instanceof BlockObsidian || WorldUtil.getBlock(pos.func_177976_e()) instanceof BlockObsidian) {
                    return new SingleHole(pos, material.OBSIDIAN);
                }
                return new SingleHole(pos, material.BEDROCK);
            }
            else {
                final BlockPos[] directions = { pos.func_177976_e(), pos.func_177978_c(), pos.func_177974_f(), pos.func_177968_d() };
                BlockPos adjacentPos = null;
                for (final BlockPos direction : directions) {
                    if (WorldUtil.empty.contains(WorldUtil.getBlock(direction))) {
                        adjacentPos = direction;
                        break;
                    }
                }
                if (adjacentPos == null || WorldUtil.empty.contains(WorldUtil.getBlock(adjacentPos.func_177977_b()))) {
                    return null;
                }
                final BlockPos[] adjacentDirections = { adjacentPos.func_177976_e(), adjacentPos.func_177978_c(), adjacentPos.func_177974_f(), adjacentPos.func_177968_d() };
                int checked = 0;
                boolean hasObsidian = false;
                EnumFacing facing = null;
                for (final BlockPos blockPos : adjacentDirections) {
                    if (!blockPos.equals((Object)pos)) {
                        if (WorldUtil.getBlock(blockPos) instanceof BlockObsidian) {
                            hasObsidian = true;
                            ++checked;
                        }
                        if (WorldUtil.getBlock(blockPos) == Blocks.field_150357_h) {
                            ++checked;
                        }
                    }
                }
                for (final BlockPos blockPos : directions) {
                    if (!blockPos.equals((Object)adjacentPos)) {
                        if (WorldUtil.getBlock(blockPos) instanceof BlockObsidian) {
                            hasObsidian = true;
                            ++checked;
                        }
                        if (WorldUtil.getBlock(blockPos) == Blocks.field_150357_h) {
                            ++checked;
                        }
                    }
                }
                for (final EnumFacing enumFacing : EnumFacing.values()) {
                    if (pos.func_177972_a(enumFacing).equals((Object)adjacentPos)) {
                        facing = enumFacing;
                    }
                }
                if (checked >= 6) {
                    return new DoubleHole(pos, adjacentPos, hasObsidian ? material.OBSIDIAN : material.BEDROCK, facing);
                }
            }
        }
        return null;
    }
    
    public static ArrayList<Hole> holes(final float r, final int height) {
        final ArrayList<Hole> holes = new ArrayList<Hole>();
        for (final BlockPos pos : BlockUtil.getSphere(PlayerUtil.getPlayerPosFloored(), r, (int)r, false, true, 0)) {
            final Hole hole = getHole(pos, height);
            if (hole instanceof QuadHole) {
                boolean alreadyExists = false;
                for (final Hole hole2 : holes) {
                    if (hole2 instanceof QuadHole) {
                        if (!((QuadHole)hole2).contains((QuadHole)hole)) {
                            continue;
                        }
                        alreadyExists = true;
                        break;
                    }
                }
                if (alreadyExists) {
                    continue;
                }
            }
            if (hole instanceof DoubleHole) {
                boolean alreadyExists = false;
                for (final Hole hole2 : holes) {
                    if (hole2 instanceof DoubleHole) {
                        if (!((DoubleHole)hole2).contains((DoubleHole)hole)) {
                            continue;
                        }
                        alreadyExists = true;
                        break;
                    }
                }
                if (alreadyExists) {
                    continue;
                }
            }
            if (hole == null) {
                continue;
            }
            holes.add(hole);
        }
        return holes;
    }
    
    public enum type
    {
        DOUBLE, 
        SINGLE, 
        QUAD;
    }
    
    public enum material
    {
        BEDROCK, 
        OBSIDIAN;
    }
    
    public static class Hole
    {
        public type type;
        public material mat;
        
        public Hole(final type type2, final material mat) {
            this.type = type2;
            this.mat = mat;
        }
    }
    
    public static final class SingleHole extends Hole
    {
        public BlockPos pos;
        
        public SingleHole(final BlockPos pos, final material mat) {
            super(type.SINGLE, mat);
            this.pos = pos;
        }
    }
    
    public static final class DoubleHole extends Hole
    {
        public BlockPos pos;
        public BlockPos pos1;
        public EnumFacing dir;
        
        public DoubleHole(final BlockPos pos, final BlockPos pos1, final material mat, final EnumFacing dir) {
            super(type.DOUBLE, mat);
            this.pos = pos;
            this.pos1 = pos1;
            this.dir = dir;
        }
        
        public boolean contains(final BlockPos pos) {
            return this.pos.equals((Object)pos) || this.pos1.equals((Object)pos);
        }
        
        public boolean contains(final DoubleHole pos) {
            return pos.pos.equals((Object)this.pos) || pos.pos.equals((Object)this.pos1) || pos.pos1.equals((Object)this.pos) || pos.pos1.equals((Object)this.pos1);
        }
        
        public boolean equals(final DoubleHole pos) {
            return (pos.pos1.equals((Object)this.pos) || pos.pos1.equals((Object)this.pos1)) && (pos.pos.equals((Object)this.pos) || pos.pos.equals((Object)this.pos1));
        }
    }
    
    public static final class QuadHole extends Hole
    {
        public BlockPos pos;
        public BlockPos pos1;
        public BlockPos pos2;
        public BlockPos pos3;
        public EnumFacing dir;
        
        public QuadHole(final BlockPos pos, final BlockPos pos1, final BlockPos pos2, final BlockPos pos3, final material mat, final EnumFacing dir) {
            super(type.QUAD, mat);
            this.pos = pos;
            this.pos1 = pos1;
            this.pos2 = pos2;
            this.pos3 = pos3;
            this.dir = dir;
        }
        
        public boolean contains(final BlockPos pos) {
            return !this.pos.equals((Object)pos) || true;
        }
        
        public boolean contains(final QuadHole pos) {
            return pos.pos.equals((Object)this.pos) || pos.pos.equals((Object)this.pos1) || pos.pos.equals((Object)this.pos2) || pos.pos3.equals((Object)this.pos) || pos.pos1.equals((Object)this.pos3);
        }
        
        public boolean equals(final QuadHole pos) {
            return pos.pos3.equals((Object)this.pos) || pos.pos3.equals((Object)this.pos3) || pos.pos2.equals((Object)this.pos) || pos.pos2.equals((Object)this.pos2) || pos.pos1.equals((Object)this.pos) || (pos.pos1.equals((Object)this.pos1) && (pos.pos.equals((Object)this.pos) || pos.pos.equals((Object)this.pos3) || pos.pos.equals((Object)this.pos2) || pos.pos.equals((Object)this.pos1)));
        }
    }
}
