//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client.capes;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.*;

public class CapesMain extends Module
{
    public final Setting<CapesMode> selfCape;
    
    public CapesMain() {
        super("Capes", "Custom cape system", Module.Category.CLIENT, true, false, false);
        this.selfCape = (Setting<CapesMode>)this.register(new Setting("SelfCape", (Object)CapesMode.NONE));
    }
    
    public static CapesMode getCapesMode() {
        return (CapesMode)OyVey.moduleManager.getModuleByClass(CapesMain.class).selfCape.getValue();
    }
    
    public String getSelf() {
        return ((CapesMode)this.selfCape.getValue()).getSource();
    }
    
    public enum CapesMode
    {
        NONE("None"), 
        Luftwaffe("luftwaffe"), 
        CUSTOM("Custom");
        
        private final String name;
        
        private CapesMode(final String name) {
            this.name = name;
        }
        
        public String getSource() {
            return this.name.toLowerCase();
        }
        
        @Override
        public String toString() {
            return this.name;
        }
    }
}
