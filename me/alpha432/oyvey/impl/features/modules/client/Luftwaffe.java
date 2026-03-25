//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client;

import me.alpha432.oyvey.api.features.*;
import com.mojang.realmsclient.gui.*;
import net.minecraft.init.*;

public class Luftwaffe extends Module
{
    private static Luftwaffe INSTANCE;
    
    public Luftwaffe() {
        super("Luftwaffe", ChatFormatting.RED + "Dont disable", Module.Category.CLIENT, false, false, false);
        this.setInstance();
    }
    
    public static Luftwaffe getInstance() {
        if (Luftwaffe.INSTANCE == null) {
            Luftwaffe.INSTANCE = new Luftwaffe();
        }
        return Luftwaffe.INSTANCE;
    }
    
    private void setInstance() {
        Luftwaffe.INSTANCE = this;
    }
    
    public void onDisable() {
        Luftwaffe.mc.field_71439_g.func_184185_a(SoundEvents.field_187899_gZ, 1.0f, 1.0f);
        this.enable();
    }
    
    static {
        Luftwaffe.INSTANCE = new Luftwaffe();
    }
}
