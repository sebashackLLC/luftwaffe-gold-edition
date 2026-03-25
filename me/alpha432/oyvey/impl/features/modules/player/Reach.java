//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.player;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;

public class Reach extends Module
{
    private static Reach INSTANCE;
    public Setting<Float> reach;
    
    public Reach() {
        super("Reach", "Allows you to change reach for attack", Module.Category.PLAYER, false, false, false);
        this.reach = (Setting<Float>)this.register(new Setting("Reach", (Object)3.0f, (Object)0.1f, (Object)7.0f));
        this.setInstance();
    }
    
    public static Reach getInstance() {
        if (Reach.INSTANCE == null) {
            Reach.INSTANCE = new Reach();
        }
        return Reach.INSTANCE;
    }
    
    private void setInstance() {
        Reach.INSTANCE = this;
    }
    
    static {
        Reach.INSTANCE = new Reach();
    }
}
