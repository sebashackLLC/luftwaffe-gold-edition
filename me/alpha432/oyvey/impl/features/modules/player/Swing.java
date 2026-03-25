//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.player;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;

public class Swing extends Module
{
    private static Swing INSTANCE;
    public Setting<SwingMode> swingmode;
    public Setting<SwingSode> swingStyle;
    public Setting<Float> swingspeed;
    
    public Swing() {
        super("Swing", "Customize how your swing will looks like", Module.Category.PLAYER, false, false, false);
        this.swingmode = (Setting<SwingMode>)this.register(new Setting("Mode", (Object)SwingMode.Mainhand));
        this.swingStyle = (Setting<SwingSode>)this.register(new Setting("Mode", (Object)SwingSode.Default));
        this.swingspeed = (Setting<Float>)this.register(new Setting("Speed", (Object)1.0f, (Object)0.0f, (Object)1.0f));
        this.setInstance();
    }
    
    public static Swing getINSTANCE() {
        if (Swing.INSTANCE == null) {
            Swing.INSTANCE = new Swing();
        }
        return Swing.INSTANCE;
    }
    
    private void setInstance() {
        Swing.INSTANCE = this;
    }
    
    public void onUpdate() {
        if (this.swingStyle.getValue() == SwingSode.Old && Swing.mc.field_71460_t.field_78516_c.field_187470_g >= 0.9) {
            Swing.mc.field_71460_t.field_78516_c.field_187469_f = 1.0f;
            Swing.mc.field_71460_t.field_78516_c.field_187467_d = Swing.mc.field_71439_g.func_184614_ca();
        }
        if (this.swingStyle.getValue() == SwingSode.Old && Swing.mc.field_71460_t.field_78516_c.field_187472_i >= 0.9) {
            Swing.mc.field_71460_t.field_78516_c.field_187471_h = 1.0f;
            Swing.mc.field_71460_t.field_78516_c.field_187468_e = Swing.mc.field_71439_g.func_184592_cb();
        }
    }
    
    public String getDisplayInfo() {
        return this.swingmode.currentEnumName();
    }
    
    static {
        Swing.INSTANCE = new Swing();
    }
    
    public enum SwingMode
    {
        Mainhand, 
        Offhand, 
        Offhandc, 
        None, 
        Default;
    }
    
    public enum SwingSode
    {
        Default, 
        Old;
    }
}
