//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.player;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;

public class NameProtect extends Module
{
    public static final NameProtect INSTANCE;
    public Setting<String> fakeName;
    public Setting<Boolean> fakeSkin;
    
    public NameProtect() {
        super("NameProtect", "Mixin Handler", Module.Category.PLAYER, true, false, false);
        this.fakeName = (Setting<String>)this.register(new Setting("Name", (Object)"crystal"));
        this.fakeSkin = (Setting<Boolean>)this.register(new Setting("Fake Skin", (Object)false));
    }
    
    public String getFakeName() {
        return (String)this.fakeName.getValue();
    }
    
    public boolean getFakeSkin() {
        return (boolean)this.fakeSkin.getValue();
    }
    
    static {
        INSTANCE = new NameProtect();
    }
}
