//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.player;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class Clip extends Module
{
    private static Clip INSTANCE;
    public final Setting<Integer> timeout;
    public final Setting<Boolean> disablee;
    public int disableThingy;
    
    public Clip() {
        super("Clip", "Clipping you in near wall for get less damage from explosions", Module.Category.PLAYER, true, false, false);
        this.timeout = (Setting<Integer>)this.register(new Setting("Timeout", (Object)5, (Object)1, (Object)10));
        this.disablee = (Setting<Boolean>)this.register(new Setting("AutoDisable", (Object)true));
    }
    
    public static Clip getInstance() {
        if (Clip.INSTANCE == null) {
            Clip.INSTANCE = new Clip();
        }
        return Clip.INSTANCE;
    }
    
    private void setInstance() {
        Clip.INSTANCE = this;
    }
    
    public void onUpdate() {
        if (nullCheck()) {
            return;
        }
        if (Clip.INSTANCE.movingByKeys()) {
            this.disable();
            return;
        }
        if (Clip.mc.field_71441_e.func_184144_a((Entity)Clip.mc.field_71439_g, Clip.mc.field_71439_g.func_174813_aQ().func_72314_b(0.01, 0.0, 0.01)).size() < 2) {
            Clip.mc.field_71439_g.func_70107_b(roundToClosest(Clip.mc.field_71439_g.field_70165_t, Math.floor(Clip.mc.field_71439_g.field_70165_t) + 0.301, Math.floor(Clip.mc.field_71439_g.field_70165_t) + 0.699), Clip.mc.field_71439_g.field_70163_u, roundToClosest(Clip.mc.field_71439_g.field_70161_v, Math.floor(Clip.mc.field_71439_g.field_70161_v) + 0.301, Math.floor(Clip.mc.field_71439_g.field_70161_v) + 0.699));
        }
        else if (Clip.mc.field_71439_g.field_70173_aa % (int)this.timeout.getValue() == 0) {
            Clip.mc.field_71439_g.func_70107_b(Clip.mc.field_71439_g.field_70165_t + MathHelper.func_151237_a(roundToClosest(Clip.mc.field_71439_g.field_70165_t, Math.floor(Clip.mc.field_71439_g.field_70165_t) + 0.241, Math.floor(Clip.mc.field_71439_g.field_70165_t) + 0.759) - Clip.mc.field_71439_g.field_70165_t, -0.03, 0.03), Clip.mc.field_71439_g.field_70163_u, Clip.mc.field_71439_g.field_70161_v + MathHelper.func_151237_a(roundToClosest(Clip.mc.field_71439_g.field_70161_v, Math.floor(Clip.mc.field_71439_g.field_70161_v) + 0.241, Math.floor(Clip.mc.field_71439_g.field_70161_v) + 0.759) - Clip.mc.field_71439_g.field_70161_v, -0.03, 0.03));
            Clip.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Clip.mc.field_71439_g.field_70165_t, Clip.mc.field_71439_g.field_70163_u, Clip.mc.field_71439_g.field_70161_v, true));
            Clip.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(roundToClosest(Clip.mc.field_71439_g.field_70165_t, Math.floor(Clip.mc.field_71439_g.field_70165_t) + 0.23, Math.floor(Clip.mc.field_71439_g.field_70165_t) + 0.77), Clip.mc.field_71439_g.field_70163_u, roundToClosest(Clip.mc.field_71439_g.field_70161_v, Math.floor(Clip.mc.field_71439_g.field_70161_v) + 0.23, Math.floor(Clip.mc.field_71439_g.field_70161_v) + 0.77), true));
            if (this.disablee.getValue()) {
                ++this.disableThingy;
            }
            else {
                this.disableThingy = 0;
            }
        }
        if (this.disableThingy >= 2 && (boolean)this.disablee.getValue()) {
            this.disableThingy = 0;
            this.disable();
        }
    }
    
    private boolean movingByKeys() {
        return Clip.mc.field_71474_y.field_74351_w.func_151470_d() || Clip.mc.field_71474_y.field_74368_y.func_151470_d() || Clip.mc.field_71474_y.field_74370_x.func_151470_d() || Clip.mc.field_71474_y.field_74366_z.func_151470_d();
    }
    
    public static double roundToClosest(final double num, final double low, final double high) {
        final double d1 = num - low;
        final double d2 = high - num;
        if (d2 > d1) {
            return low;
        }
        return high;
    }
    
    static {
        Clip.INSTANCE = new Clip();
    }
}
