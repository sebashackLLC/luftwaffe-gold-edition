//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.objects;

import net.minecraft.util.math.*;

public class TimeVec3d extends Vec3d
{
    private final long time;
    
    public TimeVec3d(final Vec3d vec) {
        super(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
        this.time = System.currentTimeMillis();
    }
    
    public TimeVec3d(final double xIn, final double yIn, final double zIn, final long time) {
        super(xIn, yIn, zIn);
        this.time = time;
    }
    
    public long getTime() {
        return this.time;
    }
}
