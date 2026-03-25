//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;

public class ViewModel extends Module
{
    private static ViewModel INSTANCE;
    public Setting<Float> X;
    public Setting<Float> Y;
    public Setting<Float> Z;
    public Setting<Boolean> nosway;
    
    public ViewModel() {
        super("ViewModel", "Customize how your view model will looks like", Module.Category.RENDER, false, false, false);
        this.X = (Setting<Float>)this.register(new Setting("X-Scale", (Object)1.0f, (Object)0.0f, (Object)2.0f));
        this.Y = (Setting<Float>)this.register(new Setting("Y-Scale", (Object)1.0f, (Object)0.0f, (Object)2.0f));
        this.Z = (Setting<Float>)this.register(new Setting("Z-Scale", (Object)1.0f, (Object)0.0f, (Object)2.0f));
        this.nosway = (Setting<Boolean>)this.register(new Setting("NoSway", (Object)true));
        this.setInstance();
    }
    
    public static ViewModel getINSTANCE() {
        if (ViewModel.INSTANCE == null) {
            ViewModel.INSTANCE = new ViewModel();
        }
        return ViewModel.INSTANCE;
    }
    
    private void setInstance() {
        ViewModel.INSTANCE = this;
    }
    
    static {
        ViewModel.INSTANCE = new ViewModel();
    }
}
