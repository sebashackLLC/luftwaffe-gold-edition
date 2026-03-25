//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.movement;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.network.play.client.*;

public class TickShift extends Module
{
    Setting<Integer> ticksVal;
    Setting<Float> timer;
    Setting<Boolean> instant;
    Setting<Boolean> blink;
    Setting<Boolean> onlyMovement;
    boolean canTimer;
    int tick;
    int usedTicks;
    
    public TickShift() {
        super("TickShift", "Making ticks in minecraft faster", Module.Category.MOVEMENT, true, false, false);
        this.ticksVal = (Setting<Integer>)this.register(new Setting("Ticks", (Object)9, (Object)1, (Object)100));
        this.timer = (Setting<Float>)this.register(new Setting("Timer", (Object)2.5f, (Object)1.0f, (Object)3.0f));
        this.instant = (Setting<Boolean>)this.register(new Setting("Instant", (Object)true));
        this.blink = (Setting<Boolean>)this.register(new Setting("Blink", (Object)true));
        this.onlyMovement = (Setting<Boolean>)new Setting("Physics", (Object)false);
        this.canTimer = false;
        this.tick = 0;
        this.usedTicks = 0;
    }
    
    public void onEnable() {
        if (this.blink.getValue()) {
            FakeLag.getInstance().enable();
        }
        this.canTimer = false;
        this.usedTicks = 0;
        if (this.instant.getValue()) {
            this.tick = (int)this.ticksVal.getValue();
        }
        else {
            this.tick = 0;
        }
    }
    
    public void onUpdate() {
        ++this.usedTicks;
        if ((boolean)this.instant.getValue() && this.usedTicks >= (int)this.ticksVal.getValue()) {
            this.disable();
            return;
        }
        if (this.tick <= 0) {
            this.tick = 0;
            this.canTimer = false;
            if (!(boolean)this.onlyMovement.getValue()) {
                TickShift.mc.field_71428_T.field_194149_e = 50.0f;
            }
        }
        if (this.tick > 0 && ((boolean)this.instant.getValue() || EntityUtil.isEntityMoving((Entity)TickShift.mc.field_71439_g))) {
            --this.tick;
            if (!(boolean)this.onlyMovement.getValue()) {
                TickShift.mc.field_71428_T.field_194149_e = 50.0f / (float)this.timer.getValue();
            }
        }
        if (!(boolean)this.instant.getValue() && !EntityUtil.isEntityMoving((Entity)TickShift.mc.field_71439_g)) {
            ++this.tick;
        }
        if (this.tick > (int)this.ticksVal.getValue()) {
            this.tick = (int)this.ticksVal.getValue();
        }
    }
    
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayer) {
            --this.tick;
            if (this.tick <= 0) {
                this.tick = 0;
            }
        }
    }
    
    public String getDisplayInfo() {
        return String.valueOf(this.tick);
    }
    
    public void onDisable() {
        if (this.blink.getValue()) {
            FakeLag.getInstance().disable();
        }
        if (!(boolean)this.onlyMovement.getValue()) {
            TickShift.mc.field_71428_T.field_194149_e = 50.0f;
        }
        this.usedTicks = 0;
    }
}
