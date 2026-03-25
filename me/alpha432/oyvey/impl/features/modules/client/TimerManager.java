//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.network.play.client.*;

public class TimerManager extends Module
{
    Setting<Integer> ticksVal;
    Setting<Float> timer;
    Setting<Boolean> instant;
    boolean canTimer;
    int tick;
    int usedTicks;
    private float tickCounter;
    
    public TimerManager() {
        super("TimerManager", "Syncs shifted ticks in timer based modules", Module.Category.CLIENT, true, false, false);
        this.ticksVal = (Setting<Integer>)this.register(new Setting("Ticks", (Object)9, (Object)1, (Object)100));
        this.timer = (Setting<Float>)this.register(new Setting("Timer", (Object)2.5f, (Object)1.0f, (Object)3.0f));
        this.instant = (Setting<Boolean>)new Setting("Instant", (Object)false);
        this.canTimer = false;
        this.tick = 0;
        this.usedTicks = 0;
        this.tickCounter = 0.0f;
    }
    
    public void onEnable() {
        this.canTimer = false;
        this.usedTicks = 0;
        this.tickCounter = 0.0f;
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
        }
        if (this.tick > 0 && ((boolean)this.instant.getValue() || EntityUtil.isEntityMoving((Entity)TimerManager.mc.field_71439_g))) {
            this.tickCounter += (float)this.timer.getValue();
            while (this.tickCounter >= 1.0f && this.tick > 0) {
                --this.tick;
                --this.tickCounter;
            }
        }
        if (!(boolean)this.instant.getValue() && !EntityUtil.isEntityMoving((Entity)TimerManager.mc.field_71439_g)) {
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
        this.usedTicks = 0;
        this.tickCounter = 0.0f;
    }
}
