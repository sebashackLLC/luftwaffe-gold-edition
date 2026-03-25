//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraftforge.client.event.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Watermark extends Module
{
    private static Watermark INSTANCE;
    Setting<modes> mode;
    public final Setting<Integer> y;
    public Setting<Boolean> ver;
    public Setting<Boolean> reivison;
    public Setting<Boolean> showDate;
    public Setting<Boolean> whiteText;
    public Setting<Boolean> custom;
    public Setting<String> text;
    public Setting<String> text2;
    public Setting<String> customVer;
    int darkRed;
    int gray;
    int white;
    String GIT_HASH;
    String GIT_REVISION;
    String GIT_DATE;
    
    public Watermark() {
        super("Watermark", "Draws luftwaffe logo", Module.Category.HUD, true, false, false);
        this.mode = (Setting<modes>)this.register(new Setting("ColorScheme", (Object)modes.Luftwaffe));
        this.y = (Setting<Integer>)this.register(new Setting("Y-Pos", (Object)3, (Object)0, (Object)20));
        this.ver = (Setting<Boolean>)this.register(new Setting("Show Version", (Object)false));
        this.reivison = (Setting<Boolean>)this.register(new Setting("Show Revision", (Object)false, v -> (boolean)this.ver.getValue()));
        this.showDate = (Setting<Boolean>)this.register(new Setting("Show Date", (Object)false));
        this.whiteText = (Setting<Boolean>)this.register(new Setting("White", (Object)false, v -> this.mode.getValue() == modes.Sync));
        this.custom = (Setting<Boolean>)this.register(new Setting("Custom", (Object)false));
        this.text = (Setting<String>)this.register(new Setting("Client Name", (Object)"luftwaffe", v -> (boolean)this.custom.getValue()));
        this.text2 = (Setting<String>)this.register(new Setting("Client Suffix", (Object)".xyz", v -> (boolean)this.custom.getValue() && this.mode.getValue() == modes.Luftwaffe));
        this.customVer = (Setting<String>)this.register(new Setting("Version", (Object)"2.0.0", v -> (boolean)this.custom.getValue()));
        this.darkRed = -5636096;
        this.gray = -8355712;
        this.white = -1;
        this.GIT_HASH = "58cf156";
        this.GIT_REVISION = "65";
        this.GIT_DATE = "07/01/2026 15:54";
    }
    
    public static Watermark getInstance() {
        if (Watermark.INSTANCE == null) {
            Watermark.INSTANCE = new Watermark();
        }
        return Watermark.INSTANCE;
    }
    
    private void setInstance() {
        Watermark.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Text event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        if (!(boolean)this.custom.getValue()) {
            if (this.mode.getValue() == modes.Luftwaffe) {
                this.renderer.drawStringWithGradient("luftwaffe", 3.0f, (float)(int)this.y.getValue(), true);
                this.renderer.drawString(" .xyz", (float)(3 + this.renderer.getStringWidth("luftwaffe")), (float)(int)this.y.getValue(), this.gray, true);
            }
            else {
                this.renderer.drawStringWithGradient("luftwaffe.xyz", 3.0f, (float)(int)this.y.getValue(), true);
            }
        }
        else if (this.mode.getValue() == modes.Luftwaffe) {
            this.renderer.drawStringWithGradient((String)this.text.getValue(), 3.0f, (float)(int)this.y.getValue(), true);
            this.renderer.drawString(" " + (String)this.text2.getValue(), (float)(3 + this.renderer.getStringWidth((String)this.text.getValue())), (float)(int)this.y.getValue(), this.gray, true);
        }
        else {
            this.renderer.drawStringWithGradient((String)this.text.getValue(), 3.0f, (float)(int)this.y.getValue(), true);
        }
        if (this.ver.getValue()) {
            if (this.mode.getValue() == modes.Luftwaffe) {
                if (!(boolean)this.custom.getValue()) {
                    this.renderer.drawString("  2.0.5", (float)(this.renderer.getStringWidth("luftwaffe") + this.renderer.getStringWidth(".xyz")), (float)(int)this.y.getValue(), this.white, true);
                }
                else {
                    this.renderer.drawString("  " + (String)this.customVer.getValue(), (float)(this.renderer.getStringWidth((String)this.text.getValue()) + this.renderer.getStringWidth((String)this.text2.getValue())), (float)(int)this.y.getValue(), this.white, true);
                }
            }
            else {
                final int versionColor = this.whiteText.getValue() ? this.white : Color.getInstance().syncColor();
                if (!(boolean)this.custom.getValue()) {
                    this.renderer.drawString("  2.0.5", (float)(this.renderer.getStringWidth("luftwaffe") + this.renderer.getStringWidth(".xyz")), (float)(int)this.y.getValue(), versionColor, true);
                }
                else {
                    this.renderer.drawString("  " + (String)this.customVer.getValue(), (float)this.renderer.getStringWidth((String)this.text.getValue()), (float)(int)this.y.getValue(), versionColor, true);
                }
            }
        }
        if ((boolean)this.reivison.getValue() && (boolean)this.ver.getValue()) {
            if (this.mode.getValue() == modes.Luftwaffe) {
                if (!(boolean)this.custom.getValue()) {
                    this.renderer.drawString("+" + this.GIT_HASH + this.GIT_REVISION, (float)(this.renderer.getStringWidth("+luftwaffe.xyz2.0.5") + 2), (float)(int)this.y.getValue(), this.white, true);
                }
                else {
                    this.renderer.drawString("+" + this.GIT_HASH + this.GIT_REVISION, (float)(this.renderer.getStringWidth("+" + (String)this.text.getValue() + (String)this.text2.getValue() + (String)this.customVer.getValue()) + 2), (float)(int)this.y.getValue(), this.white, true);
                }
            }
            else {
                final int revisionColor = this.whiteText.getValue() ? this.white : Color.getInstance().syncColor();
                if (!(boolean)this.custom.getValue()) {
                    this.renderer.drawString("+" + this.GIT_HASH + this.GIT_REVISION, (float)(this.renderer.getStringWidth("+luftwaffe.xyz2.0.5") + 2), (float)(int)this.y.getValue(), revisionColor, true);
                }
                else {
                    this.renderer.drawString("+" + this.GIT_HASH + this.GIT_REVISION, (float)(this.renderer.getStringWidth("+" + (String)this.text.getValue() + (String)this.customVer.getValue()) + 2), (float)(int)this.y.getValue(), revisionColor, true);
                }
            }
        }
        if (this.showDate.getValue()) {
            final String clientName = (String)(this.custom.getValue() ? this.text.getValue() : "luftwaffe");
            final String clientSuffix = this.custom.getValue() ? (" " + (String)this.text2.getValue()) : " .xyz";
            final String version = (String)(this.custom.getValue() ? this.customVer.getValue() : "2.0.5");
            final StringBuilder fullText = new StringBuilder(clientName);
            if (this.mode.getValue() == modes.Luftwaffe) {
                fullText.append(clientSuffix);
            }
            else {
                fullText.append(this.custom.getValue() ? "" : ".xyz");
            }
            if (this.ver.getValue()) {
                fullText.append("  ").append(version);
            }
            if ((boolean)this.reivison.getValue() && (boolean)this.ver.getValue()) {
                fullText.append("+").append(this.GIT_HASH).append(this.GIT_REVISION);
            }
            final int xOffset = 3 + this.renderer.getStringWidth(fullText.toString());
            this.renderer.drawString(" " + this.GIT_DATE, (float)xOffset, (float)(int)this.y.getValue(), this.gray, true);
        }
    }
    
    static {
        Watermark.INSTANCE = new Watermark();
    }
    
    public enum modes
    {
        Sync, 
        Luftwaffe;
    }
}
