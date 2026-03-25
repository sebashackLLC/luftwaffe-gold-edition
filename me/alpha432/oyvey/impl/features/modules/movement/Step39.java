//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.movement;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraftforge.fml.common.gameevent.*;
import org.lwjgl.input.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class Step39 extends Module
{
    private final Setting<Float> stepHeight;
    private final Setting<Mode> mode;
    private final Setting<KeyBindMode> keyBindMode;
    private final Setting<Bind> holdBind;
    private final Setting<Boolean> timer;
    public static Step39 INSTANCE;
    private final Timer stepTimer;
    public static boolean paused;
    private float oldVal;
    private int ticks;
    private boolean wasHolding;
    
    public Step39() {
        super("39Step", "NCP step bypass", Module.Category.MOVEMENT, true, false, true);
        this.stepHeight = (Setting<Float>)this.register(new Setting("Height", (Object)1.0f, (Object)0.6f, (Object)2.5f));
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (Object)Mode.NCP));
        this.keyBindMode = (Setting<KeyBindMode>)this.register(new Setting("KeyBind Mode", (Object)KeyBindMode.Normal));
        this.holdBind = (Setting<Bind>)this.register(new Setting("Hold Bind", (Object)new Bind(0)));
        this.timer = (Setting<Boolean>)this.register(new Setting("Use Timer", (Object)false));
        this.stepTimer = new Timer();
        this.wasHolding = false;
        this.oldVal = 0.6f;
        this.ticks = 0;
        Step39.INSTANCE = this;
    }
    
    public void onEnable() {
        super.onEnable();
        this.oldVal = 0.6f;
        this.stepTimer.reset();
    }
    
    public void onDisable() {
        super.onDisable();
        if (fullNullCheck()) {
            return;
        }
        Step39.paused = false;
        Step39.mc.field_71439_g.field_70138_W = this.oldVal;
        if (this.timer.getValue()) {
            EntityUtil.resetTimer();
        }
    }
    
    public void onUpdate() {
    }
    
    private boolean shouldStep() {
        return true;
    }
    
    @SubscribeEvent
    public void onTick(final TickEvent.ClientTickEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (this.keyBindMode.getValue() == KeyBindMode.Hold && ((Bind)this.holdBind.getValue()).getKey() != 0) {
            final boolean isHolding = Keyboard.isKeyDown(((Bind)this.holdBind.getValue()).getKey());
            if (isHolding && !this.wasHolding && !this.isEnabled()) {
                this.enable();
            }
            else if (!isHolding && this.wasHolding && this.isEnabled()) {
                this.disable();
            }
            this.wasHolding = isHolding;
        }
        if (!this.isEnabled()) {
            return;
        }
        if (this.mode.getValue() == Mode.NCP && !Step39.paused && (boolean)this.timer.getValue()) {
            if (this.ticks == 0) {
                EntityUtil.resetTimer();
            }
            else {
                --this.ticks;
            }
        }
        if (!this.shouldStep()) {
            Step39.mc.field_71439_g.field_70138_W = this.oldVal;
            return;
        }
        switch ((Mode)this.mode.getValue()) {
            case Vanilla: {
                if (!Step39.paused) {
                    Step39.mc.field_71439_g.field_70138_W = (float)this.stepHeight.getValue();
                    break;
                }
                Step39.mc.field_71439_g.field_70138_W = this.oldVal;
                break;
            }
            case NCP: {
                if (Step39.paused) {
                    return;
                }
                if (this.timer.getValue()) {
                    if (this.ticks == 0) {
                        EntityUtil.resetTimer();
                    }
                    else {
                        --this.ticks;
                    }
                }
                if (Step39.mc.field_71439_g.field_70122_E && this.stepTimer.passedMs(50L)) {
                    Step39.mc.field_71439_g.field_70138_W = (float)this.stepHeight.getValue();
                    break;
                }
                Step39.mc.field_71439_g.field_70138_W = 0.6f;
                break;
            }
        }
    }
    
    @SubscribeEvent
    public void onStep(final StepEvent event) {
        if (!this.isEnabled()) {
            return;
        }
        if (this.mode.getValue() != Mode.NCP) {
            return;
        }
        if (Step39.paused) {
            return;
        }
        if (!this.shouldStep()) {
            return;
        }
        if (event.getEntity() != Step39.mc.field_71439_g) {
            return;
        }
        final double height = Step39.mc.field_71439_g.func_174813_aQ().field_72338_b - Step39.mc.field_71439_g.field_70163_u + event.getHeight();
        if (height <= 0.0 || height > (float)this.stepHeight.getValue()) {
            return;
        }
        final double[] offsets = this.getOffset(height);
        if (offsets != null && offsets.length > 1) {
            for (final double offset : offsets) {
                Step39.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Step39.mc.field_71439_g.field_70165_t, Step39.mc.field_71439_g.field_70163_u + offset, Step39.mc.field_71439_g.field_70161_v, false));
            }
            if (this.timer.getValue()) {
                if (height == 1.0) {
                    EntityUtil.setTimer(0.6f);
                    this.ticks = 1;
                }
                else if (height == 1.5) {
                    EntityUtil.setTimer(0.35f);
                    this.ticks = 1;
                }
                else if (height == 2.0) {
                    EntityUtil.setTimer(0.25f);
                    this.ticks = 2;
                }
                else if (height == 2.5) {
                    EntityUtil.setTimer(0.15f);
                    this.ticks = 2;
                }
                else {
                    EntityUtil.setTimer(1.0f / offsets.length);
                    this.ticks = 2;
                }
            }
        }
        this.stepTimer.reset();
    }
    
    private double[] getOffset(final double height) {
        if (height == 0.75) {
            return new double[] { 0.42, 0.753, 0.75 };
        }
        if (height == 0.8125) {
            return new double[] { 0.39, 0.7, 0.8125 };
        }
        if (height == 0.875) {
            return new double[] { 0.39, 0.7, 0.875 };
        }
        if (height == 1.0) {
            return new double[] { 0.41999998688698, 0.7531999805212, 1.0 };
        }
        if (height == 1.5) {
            return new double[] { 0.42, 0.75, 1.0, 1.16, 1.23, 1.2 };
        }
        if (height == 2.0) {
            return new double[] { 0.42, 0.78, 0.63, 0.51, 0.9, 1.21, 1.45, 1.43 };
        }
        if (height == 2.5) {
            return new double[] { 0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907 };
        }
        return null;
    }
    
    public String getDisplayInfo() {
        return ((Mode)this.mode.getValue()).toString();
    }
    
    public enum Mode
    {
        Vanilla, 
        NCP;
    }
    
    public enum KeyBindMode
    {
        Normal, 
        Hold;
    }
}
