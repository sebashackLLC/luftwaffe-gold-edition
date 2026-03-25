//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.util.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import me.alpha432.oyvey.api.util.render.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class WatermarkRewrite extends Module
{
    private static WatermarkRewrite INSTANCE;
    private final Setting<Boolean> waterMark;
    public Setting<Integer> waterMarkY;
    public Setting<TextUtil.Color> bracketColor;
    public Setting<TextUtil.Color> commandColor;
    public String commandBracket;
    public String commandBracket2;
    public String command;
    private int color;
    
    public WatermarkRewrite() {
        super("WatermarkRewrite", "Renders watermark on your screen", Module.Category.HUD, true, false, false);
        this.waterMark = (Setting<Boolean>)this.register(new Setting("Watermark", (Object)false, "Displays watermark"));
        this.waterMarkY = (Setting<Integer>)this.register(new Setting("WatermarkPosY", (Object)2, (Object)0, (Object)20, v -> (boolean)this.waterMark.getValue()));
        this.bracketColor = (Setting<TextUtil.Color>)this.register(new Setting("BracketColor", (Object)TextUtil.Color.BLUE));
        this.commandColor = (Setting<TextUtil.Color>)this.register(new Setting("NameColor", (Object)TextUtil.Color.BLUE));
        this.commandBracket = "[";
        this.commandBracket2 = "]";
        this.command = "luftwaffe";
        this.setInstance();
    }
    
    public static WatermarkRewrite getInstance() {
        if (WatermarkRewrite.INSTANCE == null) {
            WatermarkRewrite.INSTANCE = new WatermarkRewrite();
        }
        return WatermarkRewrite.INSTANCE;
    }
    
    private void setInstance() {
        WatermarkRewrite.INSTANCE = this;
    }
    
    public void onRender2D(final Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        final ColorSetting mainColor = (ColorSetting)Color.getInstance().mainColor.getValue();
        this.color = ColorUtil.toRGBA(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue());
        if (this.waterMark.getValue()) {
            final String string = this.command + " v0.0.3";
            if (Color.getInstance().rainbow.getValue()) {
                if (Color.getInstance().rainbowModeHud.getValue() == Color.rainbowMode.Static) {
                    this.renderer.drawString(string, 2.0f, (float)(int)this.waterMarkY.getValue(), ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] counter = { 1 };
                    final char[] stringToCharArray = string.toCharArray();
                    float offset = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + offset, (float)(int)this.waterMarkY.getValue(), ColorUtil.rainbow(counter[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB(), true);
                        offset += this.renderer.getStringWidth(String.valueOf(c));
                        ++counter[0];
                    }
                }
            }
            else {
                this.renderer.drawString(string, 2.0f, (float)(int)this.waterMarkY.getValue(), this.color, true);
            }
        }
    }
    
    public void onLoad() {
        OyVey.commandManager.setClientMessage(this.getCommandMessage());
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && this.equals(event.getSetting().getFeature())) {
            OyVey.commandManager.setClientMessage(this.getCommandMessage());
        }
    }
    
    public String getCommandMessage() {
        return TextUtil.coloredString(this.commandBracket, (TextUtil.Color)this.bracketColor.getPlannedValue()) + TextUtil.coloredString(this.command, (TextUtil.Color)this.commandColor.getPlannedValue()) + TextUtil.coloredString(this.commandBracket2, (TextUtil.Color)this.bracketColor.getPlannedValue());
    }
    
    static {
        WatermarkRewrite.INSTANCE = new WatermarkRewrite();
    }
}
