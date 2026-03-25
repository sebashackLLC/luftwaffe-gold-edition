//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.movement;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import java.text.*;
import com.mojang.realmsclient.gui.*;

public class Step extends Module
{
    public Setting<Mode> mode;
    public Setting<Integer> height;
    public Setting<Boolean> reverse;
    public Setting<Boolean> timer;
    private int ticks;
    private static Step INSTANCE;
    
    public Step() {
        super("Step", "Walk up blocks quickly.", Module.Category.MOVEMENT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (Object)Mode.VANILLA));
        this.setInstance();
        this.height = (Setting<Integer>)this.register(new Setting("Height", (Object)1, (Object)1, (Object)2));
        this.reverse = (Setting<Boolean>)this.register(new Setting("Reverse", (Object)false));
        this.timer = (Setting<Boolean>)this.register(new Setting("Timer", (Object)false));
        this.ticks = 0;
    }
    
    public static Step getInstance() {
        if (Step.INSTANCE == null) {
            Step.INSTANCE = new Step();
        }
        return Step.INSTANCE;
    }
    
    private void setInstance() {
        Step.INSTANCE = this;
    }
    
    public void onUpdate() {
        if (Step.mc.field_71441_e != null && Step.mc.field_71439_g != null) {
            if (this.mode.getValue() == Mode.NONE) {
                return;
            }
            if (!Step.mc.field_71439_g.func_70090_H() && !Step.mc.field_71439_g.func_180799_ab() && !Step.mc.field_71439_g.func_70617_f_() && !Step.mc.field_71474_y.field_74311_E.func_151470_d()) {
                if (this.mode.currentEnumName().equalsIgnoreCase("Normal")) {
                    if (this.timer.getValue()) {
                        if (this.ticks == 0) {
                            EntityUtil.resetTimer();
                        }
                        else {
                            --this.ticks;
                        }
                    }
                    if (Step.mc.field_71439_g != null && Step.mc.field_71439_g.field_70122_E && !Step.mc.field_71439_g.func_70090_H() && !Step.mc.field_71439_g.func_70617_f_() && (boolean)this.reverse.getValue()) {
                        for (double y = 0.0; y < (int)this.height.getValue() + 0.5; y += 0.01) {
                            if (!Step.mc.field_71441_e.func_184144_a((Entity)Step.mc.field_71439_g, Step.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, -y, 0.0)).isEmpty()) {
                                Step.mc.field_71439_g.field_70181_x = -10.0;
                                break;
                            }
                        }
                    }
                    final double[] dir = PlayerUtil.forward(0.1);
                    boolean twofive = false;
                    boolean two = false;
                    boolean onefive = false;
                    boolean one = false;
                    if (Step.mc.field_71441_e.func_184144_a((Entity)Step.mc.field_71439_g, Step.mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 2.6, dir[1])).isEmpty() && !Step.mc.field_71441_e.func_184144_a((Entity)Step.mc.field_71439_g, Step.mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 2.4, dir[1])).isEmpty()) {
                        twofive = true;
                    }
                    if (Step.mc.field_71441_e.func_184144_a((Entity)Step.mc.field_71439_g, Step.mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 2.1, dir[1])).isEmpty() && !Step.mc.field_71441_e.func_184144_a((Entity)Step.mc.field_71439_g, Step.mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 1.9, dir[1])).isEmpty()) {
                        two = true;
                    }
                    if (Step.mc.field_71441_e.func_184144_a((Entity)Step.mc.field_71439_g, Step.mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 1.6, dir[1])).isEmpty() && !Step.mc.field_71441_e.func_184144_a((Entity)Step.mc.field_71439_g, Step.mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 1.4, dir[1])).isEmpty()) {
                        onefive = true;
                    }
                    if (Step.mc.field_71441_e.func_184144_a((Entity)Step.mc.field_71439_g, Step.mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 1.0, dir[1])).isEmpty() && !Step.mc.field_71441_e.func_184144_a((Entity)Step.mc.field_71439_g, Step.mc.field_71439_g.func_174813_aQ().func_72317_d(dir[0], 0.6, dir[1])).isEmpty()) {
                        one = true;
                    }
                    if (Step.mc.field_71439_g.field_70123_F && (Step.mc.field_71439_g.field_191988_bg != 0.0f || Step.mc.field_71439_g.field_70702_br != 0.0f) && Step.mc.field_71439_g.field_70122_E) {
                        if (one && (int)this.height.getValue() >= 1.0) {
                            final double[] twoFiveOffset = { 0.42, 0.753 };
                            for (int i = 0; i < twoFiveOffset.length; ++i) {
                                Step.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Step.mc.field_71439_g.field_70165_t, Step.mc.field_71439_g.field_70163_u + twoFiveOffset[i], Step.mc.field_71439_g.field_70161_v, Step.mc.field_71439_g.field_70122_E));
                            }
                            if (this.timer.getValue()) {
                                EntityUtil.setTimer(0.6f);
                            }
                            Step.mc.field_71439_g.func_70107_b(Step.mc.field_71439_g.field_70165_t, Step.mc.field_71439_g.field_70163_u + 1.0, Step.mc.field_71439_g.field_70161_v);
                            this.ticks = 1;
                        }
                        if (onefive && (int)this.height.getValue() >= 1.5) {
                            final double[] twoFiveOffset = { 0.42, 0.75, 1.0, 1.16, 1.23, 1.2 };
                            for (int i = 0; i < twoFiveOffset.length; ++i) {
                                Step.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Step.mc.field_71439_g.field_70165_t, Step.mc.field_71439_g.field_70163_u + twoFiveOffset[i], Step.mc.field_71439_g.field_70161_v, Step.mc.field_71439_g.field_70122_E));
                            }
                            if (this.timer.getValue()) {
                                EntityUtil.setTimer(0.1f);
                            }
                            Step.mc.field_71439_g.func_70107_b(Step.mc.field_71439_g.field_70165_t, Step.mc.field_71439_g.field_70163_u + 1.5, Step.mc.field_71439_g.field_70161_v);
                            this.ticks = 1;
                        }
                        if (two && (int)this.height.getValue() >= 2.0) {
                            final double[] twoFiveOffset = { 0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43 };
                            for (int i = 0; i < twoFiveOffset.length; ++i) {
                                Step.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Step.mc.field_71439_g.field_70165_t, Step.mc.field_71439_g.field_70163_u + twoFiveOffset[i], Step.mc.field_71439_g.field_70161_v, Step.mc.field_71439_g.field_70122_E));
                            }
                            if (this.timer.getValue()) {
                                EntityUtil.setTimer(0.1f);
                            }
                            Step.mc.field_71439_g.func_70107_b(Step.mc.field_71439_g.field_70165_t, Step.mc.field_71439_g.field_70163_u + 2.0, Step.mc.field_71439_g.field_70161_v);
                            this.ticks = 2;
                        }
                        if (twofive && (int)this.height.getValue() >= 2.5) {
                            final double[] twoFiveOffset = { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907 };
                            for (int i = 0; i < twoFiveOffset.length; ++i) {
                                Step.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Step.mc.field_71439_g.field_70165_t, Step.mc.field_71439_g.field_70163_u + twoFiveOffset[i], Step.mc.field_71439_g.field_70161_v, Step.mc.field_71439_g.field_70122_E));
                            }
                            if (this.timer.getValue()) {
                                EntityUtil.setTimer(0.1f);
                            }
                            Step.mc.field_71439_g.func_70107_b(Step.mc.field_71439_g.field_70165_t, Step.mc.field_71439_g.field_70163_u + 2.5, Step.mc.field_71439_g.field_70161_v);
                            this.ticks = 2;
                        }
                    }
                }
                if (this.mode.currentEnumName().equalsIgnoreCase("Vanilla")) {
                    final DecimalFormat df = new DecimalFormat("#");
                    Step.mc.field_71439_g.field_70138_W = Float.parseFloat(df.format(this.height.getValue()));
                }
            }
        }
    }
    
    public void onDisable() {
        if (this.mode.getValue() != Mode.NONE) {
            Step.mc.field_71439_g.field_70138_W = 0.5f;
        }
    }
    
    public String getHudInfo() {
        String t = "";
        if (this.mode.currentEnumName().equalsIgnoreCase("Normal")) {
            t = "[" + ChatFormatting.WHITE + "Normal" + ChatFormatting.GRAY + "]";
        }
        if (this.mode.currentEnumName().equalsIgnoreCase("Vanilla")) {
            t = "[" + ChatFormatting.WHITE + "Vanilla" + ChatFormatting.GRAY + "]";
        }
        return t;
    }
    
    static {
        Step.INSTANCE = new Step();
    }
    
    public enum Mode
    {
        NONE, 
        VANILLA, 
        NORMAL;
    }
}
