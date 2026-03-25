//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.player;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;

public class Timer extends Module
{
    private static Timer INSTANCE;
    public Setting<Float> timerSpeed;
    
    public Timer() {
        super("Timer", "Timer", Module.Category.PLAYER, true, false, false);
        this.timerSpeed = (Setting<Float>)this.register(new Setting("Timer", (Object)1.3f, (Object)1.0f, (Object)10.0f));
    }
    
    public static Timer getInstance() {
        if (Timer.INSTANCE == null) {
            Timer.INSTANCE = new Timer();
        }
        return Timer.INSTANCE;
    }
    
    private void setInstance() {
        Timer.INSTANCE = this;
    }
    
    static {
        Timer.INSTANCE = new Timer();
    }
}
