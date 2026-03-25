//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.combat;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.impl.features.modules.client.*;

public class AutoMineReset extends Module
{
    private static AutoMineReset INSTANCE;
    
    public AutoMineReset() {
        super("AutoMineReset", "Resetting Future AutoMine", Module.Category.COMBAT, false, false, false);
        this.setInstance();
    }
    
    public static AutoMineReset getInstance() {
        if (AutoMineReset.INSTANCE == null) {
            AutoMineReset.INSTANCE = new AutoMineReset();
        }
        return AutoMineReset.INSTANCE;
    }
    
    private void setInstance() {
        AutoMineReset.INSTANCE = this;
    }
    
    public void onEnable() {
        AutoMineReset.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "t automine disable");
        AutoMineReset.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "t automine enable");
        this.disable();
    }
    
    static {
        AutoMineReset.INSTANCE = new AutoMineReset();
    }
}
