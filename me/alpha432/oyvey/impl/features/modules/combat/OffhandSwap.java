//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.combat;

import me.alpha432.oyvey.api.features.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraft.init.*;
import me.alpha432.oyvey.impl.features.modules.client.*;

public class OffhandSwap extends Module
{
    private static OffhandSwap INSTANCE;
    public static Entity target;
    public Setting<Boolean> auto;
    private boolean wasWeakness;
    private boolean autoEnabled;
    
    public OffhandSwap() {
        super("OffhandSwap", "Switches your offhand between crystal and totem", Module.Category.COMBAT, false, false, false);
        this.auto = (Setting<Boolean>)this.register(new Setting("Auto", (Object)false, "Automatically swap to crystal when weakness is detected"));
        this.wasWeakness = false;
        this.autoEnabled = false;
        this.setInstance();
        this.setDrawn(false);
    }
    
    public static OffhandSwap getInstance() {
        if (OffhandSwap.INSTANCE == null) {
            OffhandSwap.INSTANCE = new OffhandSwap();
        }
        return OffhandSwap.INSTANCE;
    }
    
    private void setInstance() {
        OffhandSwap.INSTANCE = this;
    }
    
    public void onUpdate() {
        if (fullNullCheck()) {
            return;
        }
        if (this.auto.getValue()) {
            final boolean hasWeakness = OffhandSwap.mc.field_71439_g.func_70644_a(MobEffects.field_76437_t);
            if (hasWeakness && !this.wasWeakness && !this.autoEnabled) {
                this.enableCrystalOffhand();
                this.autoEnabled = true;
            }
            else if (!hasWeakness && this.wasWeakness && this.autoEnabled) {
                this.disableCrystalOffhand();
                this.autoEnabled = false;
            }
            this.wasWeakness = hasWeakness;
        }
    }
    
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
            return;
        }
        this.wasWeakness = false;
        this.autoEnabled = false;
        if (this.auto.getValue()) {
            if (OffhandSwap.mc.field_71439_g.func_70644_a(MobEffects.field_76437_t)) {
                this.enableCrystalOffhand();
                this.wasWeakness = true;
                this.autoEnabled = true;
            }
            return;
        }
        this.enableCrystalOffhand();
    }
    
    public void onDisable() {
        if (fullNullCheck()) {
            return;
        }
        if ((boolean)this.auto.getValue() && this.autoEnabled) {
            this.disableCrystalOffhand();
            this.autoEnabled = false;
        }
        else if (!(boolean)this.auto.getValue()) {
            this.disableCrystalOffhand();
        }
        this.wasWeakness = false;
    }
    
    private void enableCrystalOffhand() {
        if (Sync.getInstance().Mode.getValue() == Sync.clients.Future) {
            OffhandSwap.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "autototem health 14");
            OffhandSwap.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "autototem offhand crystal");
        }
        else {
            OffhandSwap.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "crystal");
        }
    }
    
    private void disableCrystalOffhand() {
        if (Sync.getInstance().Mode.getValue() == Sync.clients.Future) {
            OffhandSwap.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "autototem offhand totem");
            OffhandSwap.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "autototem health 3");
        }
        else {
            OffhandSwap.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "totem");
        }
    }
    
    static {
        OffhandSwap.INSTANCE = new OffhandSwap();
    }
}
