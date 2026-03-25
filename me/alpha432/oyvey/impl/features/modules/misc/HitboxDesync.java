//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.misc;

import me.alpha432.oyvey.api.features.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

public class HitboxDesync extends Module
{
    private static HitboxDesync INSTANCE;
    private static final double MAGIC_OFFSET = 0.20000996883537;
    
    public HitboxDesync() {
        super("HitboxDesync", "Breaking future ca", Module.Category.MISC, false, false, false);
        this.setInstance();
    }
    
    public static HitboxDesync getInstance() {
        if (HitboxDesync.INSTANCE == null) {
            HitboxDesync.INSTANCE = new HitboxDesync();
        }
        return HitboxDesync.INSTANCE;
    }
    
    private void setInstance() {
        HitboxDesync.INSTANCE = this;
    }
    
    public void onUpdate() {
        final EnumFacing f = HitboxDesync.mc.field_71439_g.func_174811_aO();
        final AxisAlignedBB bb = HitboxDesync.mc.field_71439_g.func_174813_aQ();
        final Vec3d center = bb.func_189972_c();
        final Vec3d offset = new Vec3d(f.func_176730_m());
        final Vec3d fin = this.merge(new Vec3d((Vec3i)new BlockPos(center)).func_72441_c(0.5, 0.0, 0.5).func_178787_e(offset.func_186678_a(0.20000996883537)), f);
        HitboxDesync.mc.field_71439_g.func_70634_a((fin.field_72450_a == 0.0) ? HitboxDesync.mc.field_71439_g.field_70165_t : fin.field_72450_a, HitboxDesync.mc.field_71439_g.field_70163_u, (fin.field_72449_c == 0.0) ? HitboxDesync.mc.field_71439_g.field_70161_v : fin.field_72449_c);
        this.disable();
    }
    
    private Vec3d merge(final Vec3d a, final EnumFacing facing) {
        return new Vec3d(a.field_72450_a * Math.abs(facing.func_176730_m().func_177958_n()), a.field_72448_b * Math.abs(facing.func_176730_m().func_177956_o()), a.field_72449_c * Math.abs(facing.func_176730_m().func_177952_p()));
    }
    
    static {
        HitboxDesync.INSTANCE = new HitboxDesync();
    }
}
