//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;

public class SmallShield extends Module
{
    private static SmallShield INSTANCE;
    public Setting<Float> offX;
    public Setting<Float> offY;
    public Setting<Float> mainX;
    public Setting<Float> mainY;
    
    public SmallShield() {
        super("SmallShield", "Makes you offhand lower.", Module.Category.RENDER, false, false, false);
        this.offX = (Setting<Float>)this.register(new Setting("OffHandX", (Object)0.0f, (Object)(-1.0f), (Object)1.0f));
        this.offY = (Setting<Float>)this.register(new Setting("OffHandY", (Object)0.0f, (Object)(-1.0f), (Object)1.0f));
        this.mainX = (Setting<Float>)this.register(new Setting("MainHandX", (Object)0.0f, (Object)(-1.0f), (Object)1.0f));
        this.mainY = (Setting<Float>)this.register(new Setting("MainHandY", (Object)0.0f, (Object)(-1.0f), (Object)1.0f));
        this.setInstance();
    }
    
    public static SmallShield getINSTANCE() {
        if (SmallShield.INSTANCE == null) {
            SmallShield.INSTANCE = new SmallShield();
        }
        return SmallShield.INSTANCE;
    }
    
    private void setInstance() {
        SmallShield.INSTANCE = this;
    }
    
    static {
        SmallShield.INSTANCE = new SmallShield();
    }
}
