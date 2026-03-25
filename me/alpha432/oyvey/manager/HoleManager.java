//Decompiled by Procyon!

package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.api.util.world.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import java.util.*;
import net.minecraft.block.*;

public class HoleManager extends Feature
{
    private static final BlockPos[] surroundOffset;
    private final List<BlockPos> midSafety;
    private List<BlockPos> holes;
    
    public HoleManager() {
        this.midSafety = new ArrayList<BlockPos>();
        this.holes = new ArrayList<BlockPos>();
    }
    
    public void update() {
        if (!fullNullCheck()) {
            this.holes = this.calcHoles();
        }
    }
    
    public List<BlockPos> getHoles() {
        return this.holes;
    }
    
    public List<BlockPos> getMidSafety() {
        return this.midSafety;
    }
    
    public List<BlockPos> getSortedHoles() {
        this.holes.sort(Comparator.comparingDouble(hole -> HoleManager.mc.field_71439_g.func_174818_b(hole)));
        return this.getHoles();
    }
    
    public List<BlockPos> calcHoles() {
        final ArrayList<BlockPos> safeSpots = new ArrayList<BlockPos>();
        this.midSafety.clear();
        final List<BlockPos> positions = (List<BlockPos>)BlockUtil.getSphere(EntityUtil.getPlayerPos((EntityPlayer)HoleManager.mc.field_71439_g), 6.0f, 6, false, true, 0);
        for (final BlockPos pos : positions) {
            if (HoleManager.mc.field_71441_e.func_180495_p(pos).func_177230_c().equals(Blocks.field_150350_a) && HoleManager.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 1, 0)).func_177230_c().equals(Blocks.field_150350_a)) {
                if (!HoleManager.mc.field_71441_e.func_180495_p(pos.func_177982_a(0, 2, 0)).func_177230_c().equals(Blocks.field_150350_a)) {
                    continue;
                }
                boolean isSafe = true;
                boolean midSafe = true;
                for (final BlockPos offset : HoleManager.surroundOffset) {
                    final Block block = HoleManager.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)offset)).func_177230_c();
                    if (BlockUtil.isBlockUnSolid(block)) {
                        midSafe = false;
                    }
                    if (block != Blocks.field_150357_h && block != Blocks.field_150343_Z && block != Blocks.field_150477_bB) {
                        if (block != Blocks.field_150467_bQ) {
                            isSafe = false;
                        }
                    }
                }
                if (isSafe) {
                    safeSpots.add(pos);
                }
                if (!midSafe) {
                    continue;
                }
                this.midSafety.add(pos);
            }
        }
        return safeSpots;
    }
    
    public boolean isSafe(final BlockPos pos) {
        boolean isSafe = true;
        for (final BlockPos offset : HoleManager.surroundOffset) {
            final Block block = HoleManager.mc.field_71441_e.func_180495_p(pos.func_177971_a((Vec3i)offset)).func_177230_c();
            if (block != Blocks.field_150357_h) {
                isSafe = false;
                break;
            }
        }
        return isSafe;
    }
    
    static {
        surroundOffset = BlockUtil.toBlockPos(EntityUtil.getOffsets(0, true));
    }
}
