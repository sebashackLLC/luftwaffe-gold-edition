//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;

public class CustomMainScreen extends Module
{
    public static CustomMainScreen INSTANCE;
    public Setting<Boolean> mainScreen;
    
    public CustomMainScreen() {
        super("Screens", "Controls custom screens used by the client", Module.Category.CLIENT, true, false, false);
        this.mainScreen = (Setting<Boolean>)this.register(new Setting("MainScreen", (Object)true));
        CustomMainScreen.INSTANCE = this;
    }
    
    public void onTick() {
    }
}
