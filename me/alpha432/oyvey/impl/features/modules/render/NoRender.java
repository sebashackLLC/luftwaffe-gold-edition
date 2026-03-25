//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;

public class NoRender extends Module
{
    private static NoRender INSTANCE;
    public final Setting<Boolean> nolimbmove;
    public final Setting<Boolean> noarmor;
    public final Setting<Boolean> nocape;
    
    public NoRender() {
        super("NoRender", "Disabling trash rendering", Module.Category.RENDER, false, false, false);
        this.nolimbmove = (Setting<Boolean>)this.register(new Setting("NoLimbMove", (Object)false));
        this.noarmor = (Setting<Boolean>)this.register(new Setting("NoArmor", (Object)false));
        this.nocape = (Setting<Boolean>)this.register(new Setting("NoCape", (Object)false));
        this.setInstance();
    }
    
    public static NoRender getInstance() {
        if (NoRender.INSTANCE == null) {
            NoRender.INSTANCE = new NoRender();
        }
        return NoRender.INSTANCE;
    }
    
    private void setInstance() {
        NoRender.INSTANCE = this;
    }
    
    static {
        NoRender.INSTANCE = new NoRender();
    }
}
