//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;

public class Sync extends Module
{
    private static Sync INSTANCE;
    public Setting<clients> Mode;
    public Setting<String> futurePrefix;
    public Setting<Boolean> friendSync;
    
    public Sync() {
        super("Sync", "Sync with another clients", Module.Category.CLIENT, false, false, false);
        this.Mode = (Setting<clients>)this.register(new Setting("Client", (Object)clients.Future));
        this.futurePrefix = (Setting<String>)this.register(new Setting("Prefix", (Object)"."));
        this.friendSync = (Setting<Boolean>)this.register(new Setting("Friend Sync", (Object)true));
        this.setInstance();
    }
    
    public static Sync getInstance() {
        if (Sync.INSTANCE == null) {
            Sync.INSTANCE = new Sync();
        }
        return Sync.INSTANCE;
    }
    
    private void setInstance() {
        Sync.INSTANCE = this;
    }
    
    static {
        Sync.INSTANCE = new Sync();
    }
    
    public enum clients
    {
        Earthhack, 
        Future;
    }
}
