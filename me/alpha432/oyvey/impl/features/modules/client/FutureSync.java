//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.*;

public class FutureSync extends Module
{
    private static FutureSync INSTANCE;
    public Setting<Boolean> syncSpeed;
    public Setting<Boolean> syncAutoCrystal;
    public Setting<Boolean> syncStep;
    public Setting<Boolean> syncKillaura;
    
    public FutureSync() {
        super("FutureSync", "Click to sync module states with Future client", Module.Category.CLIENT, false, false, false);
        this.syncSpeed = (Setting<Boolean>)this.register(new Setting("Speed", (Object)true));
        this.syncAutoCrystal = (Setting<Boolean>)this.register(new Setting("AutoCrystal", (Object)true));
        this.syncStep = (Setting<Boolean>)this.register(new Setting("Step", (Object)true));
        this.syncKillaura = (Setting<Boolean>)this.register(new Setting("Killaura", (Object)true));
        this.setInstance();
    }
    
    public static FutureSync getInstance() {
        if (FutureSync.INSTANCE == null) {
            FutureSync.INSTANCE = new FutureSync();
        }
        return FutureSync.INSTANCE;
    }
    
    private void setInstance() {
        FutureSync.INSTANCE = this;
    }
    
    public void onEnable() {
        if (FutureSync.mc.field_71439_g == null || FutureSync.mc.field_71441_e == null) {
            this.disable();
            return;
        }
        final String prefix = (String)Sync.getInstance().futurePrefix.getValue();
        if (this.syncSpeed.getValue()) {
            final boolean enabled = this.isModuleEnabled("Speed");
            FutureSync.mc.field_71439_g.func_71165_d(prefix + "toggle speed " + (enabled ? "on" : "off"));
        }
        if (this.syncAutoCrystal.getValue()) {
            final boolean enabled = this.isModuleEnabled("AutoCrystal");
            FutureSync.mc.field_71439_g.func_71165_d(prefix + "toggle autocrystal " + (enabled ? "on" : "off"));
        }
        if (this.syncStep.getValue()) {
            final boolean enabled = this.isModuleEnabled("Step");
            FutureSync.mc.field_71439_g.func_71165_d(prefix + "toggle step " + (enabled ? "on" : "off"));
        }
        if (this.syncKillaura.getValue()) {
            final boolean enabled = this.isModuleEnabled("Killaura");
            FutureSync.mc.field_71439_g.func_71165_d(prefix + "toggle aura " + (enabled ? "on" : "off"));
        }
        this.disable();
    }
    
    private boolean isModuleEnabled(final String name) {
        final Module module = OyVey.moduleManager.getModuleByName(name);
        return module != null && module.isOn();
    }
    
    static {
        FutureSync.INSTANCE = new FutureSync();
    }
}
