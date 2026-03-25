//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.impl.command.*;

public class govno extends Module
{
    private static govno INSTANCE;
    
    public govno() {
        super("govno", "PENIS", Module.Category.CLIENT, false, false, false);
        this.setInstance();
    }
    
    public static govno getInstance() {
        if (govno.INSTANCE == null) {
            govno.INSTANCE = new govno();
        }
        return govno.INSTANCE;
    }
    
    private void setInstance() {
        govno.INSTANCE = this;
    }
    
    public void onEnable() {
        Command.sendMessage("GOVNO");
    }
    
    static {
        govno.INSTANCE = new govno();
    }
}
