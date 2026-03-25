//Decompiled by Procyon!

package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.api.event.bus.*;
import me.alpha432.oyvey.api.interfaces.*;
import me.alpha432.oyvey.impl.events.*;
import me.alpha432.oyvey.api.event.bus.listener.*;
import me.alpha432.oyvey.mixin.mixins.*;
import me.alpha432.oyvey.api.interfaces.ducks.*;
import me.alpha432.oyvey.impl.features.modules.player.*;

public class TimerManager extends SubscriberImpl implements Minecraftable
{
    private float length;
    private int ticks;
    
    public TimerManager() {
        this.ticks = 0;
        this.listeners.add(new Listener<TickEvent>(TickEvent.class) {
            public void call(final TickEvent event) {
                if (Minecraftable.mc.field_71439_g == null) {
                    TimerManager.this.reset();
                }
                else {
                    if (TimerManager.this.ticks != 0) {
                        ++TimerManager.this.ticks;
                    }
                    final ITimer timer = (ITimer)((IMinecraft)Minecraftable.mc).getTimer();
                    if (Timer.getInstance().isOn()) {
                        timer.setTickLength(50.0f / (float)Timer.getInstance().timerSpeed.getValue());
                    }
                    else {
                        timer.setTickLength(50.0f / TimerManager.this.length);
                    }
                }
            }
        });
    }
    
    public void set(final double timer) {
        this.set((float)timer);
    }
    
    public void set(final float timer) {
        this.length = ((timer <= 0.0f) ? 0.1f : timer);
    }
    
    public void reset() {
        this.set(1.0f);
    }
    
    public float getLength() {
        return this.length;
    }
}
