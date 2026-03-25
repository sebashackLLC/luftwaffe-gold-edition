//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.manager.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Welcomer extends Module
{
    private static Welcomer INSTANCE;
    String hiMsg;
    String nameMsg;
    String welcomeMsg;
    int white;
    Setting<modes> mode;
    public Setting<String> text;
    public final Setting<Integer> x;
    public final Setting<Integer> y;
    
    public Welcomer() {
        super("Welcomer", "Welcomer", Module.Category.HUD, true, false, false);
        this.hiMsg = "Wassup ";
        this.nameMsg = "%name%";
        this.welcomeMsg = " welcome to luftwaffe :')";
        this.white = -1;
        this.mode = (Setting<modes>)this.register(new Setting("ColorScheme", (Object)modes.Default));
        this.text = (Setting<String>)this.register(new Setting("Text", (Object)"Welcome to luftwaffe, %name%"));
        this.x = (Setting<Integer>)this.register(new Setting("X-Pos", (Object)0, (Object)(-500), (Object)500));
        this.y = (Setting<Integer>)this.register(new Setting("Y-Pos", (Object)3, (Object)0, (Object)500));
    }
    
    public static Welcomer getInstance() {
        if (Welcomer.INSTANCE == null) {
            Welcomer.INSTANCE = new Welcomer();
        }
        return Welcomer.INSTANCE;
    }
    
    private void setInstance() {
        Welcomer.INSTANCE = this;
    }
    
    private String getPlayerDisplayName() {
        final String ircName = IrcNameManager.getIrcName();
        if (ircName != null && !ircName.isEmpty()) {
            return ircName;
        }
        if (Media.getInstance().isEnabled()) {
            return (String)Media.getInstance().nickName.getValue();
        }
        return Welcomer.mc.field_71439_g.getDisplayNameString();
    }
    
    private int centerX(final String huy) {
        return (this.renderer.scaledWidth - this.renderer.getStringWidth(huy)) / 2;
    }
    
    private String text2() {
        String text2 = (String)this.text.getValue();
        text2 = text2.replace("%name%", this.getPlayerDisplayName());
        this.nameMsg = this.nameMsg.replace("%name%", this.getPlayerDisplayName());
        return text2;
    }
    
    private String nameMsg2() {
        String nameMsg2 = this.nameMsg;
        nameMsg2 = nameMsg2.replace("%name%", this.getPlayerDisplayName());
        return nameMsg2;
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        if (this.mode.getValue() == modes.Default) {
            final String fullMessage = this.hiMsg + this.nameMsg2() + this.welcomeMsg;
            final int messageWidth = this.renderer.getStringWidth(fullMessage);
            final int startX = (this.renderer.scaledWidth - messageWidth) / 2 + (int)this.x.getValue();
            this.renderer.drawStringWithGradient(this.hiMsg, (float)startX, (float)(int)this.y.getValue(), true);
            this.renderer.drawString(this.nameMsg2(), (float)(startX + this.renderer.getStringWidth(this.hiMsg)), (float)(int)this.y.getValue(), this.white, true);
            this.renderer.drawStringWithGradient(this.welcomeMsg, (float)(startX + this.renderer.getStringWidth(this.hiMsg) + this.renderer.getStringWidth(this.nameMsg2())), (float)(int)this.y.getValue(), true);
        }
        else {
            this.renderer.drawStringWithGradient(this.text2(), (float)(this.centerX((String)this.text.getValue()) + (int)this.x.getValue()), (float)(int)this.y.getValue(), true);
        }
    }
    
    static {
        Welcomer.INSTANCE = new Welcomer();
    }
    
    public enum modes
    {
        Custom, 
        Default;
    }
}
