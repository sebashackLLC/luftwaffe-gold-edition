//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.*;

public class DiscordRPC extends Module
{
    private static DiscordRPC INSTANCE;
    public Setting<String> rpcText;
    public Setting<Boolean> rpcIgn;
    
    public DiscordRPC() {
        super("DiscordRPC", "flex on discord with Luftwaffe", Module.Category.CLIENT, false, false, false);
        this.rpcText = (Setting<String>)this.register(new Setting("Text", (Object)"CRYSTALOPIUM = KING"));
        this.rpcIgn = (Setting<Boolean>)this.register(new Setting("ShowUrIgn", (Object)false));
        this.setInstance();
    }
    
    public static DiscordRPC getInstance() {
        if (DiscordRPC.INSTANCE == null) {
            DiscordRPC.INSTANCE = new DiscordRPC();
        }
        return DiscordRPC.INSTANCE;
    }
    
    private void setInstance() {
        DiscordRPC.INSTANCE = this;
    }
    
    public void onEnable() {
        OyVey.discordManager.start(true);
    }
    
    public void onDisable() {
        OyVey.discordManager.shutdown();
    }
    
    static {
        DiscordRPC.INSTANCE = new DiscordRPC();
    }
}
