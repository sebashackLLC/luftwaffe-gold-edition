//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.gui.*;
import net.minecraft.client.settings.*;
import me.alpha432.oyvey.api.util.render.*;
import me.alpha432.oyvey.impl.gui.components.*;
import me.alpha432.oyvey.impl.events.*;
import me.alpha432.oyvey.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.impl.command.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.client.gui.*;

public class SomaGui extends Module
{
    private static SomaGui INSTANCE;
    public Setting<String> prefix;
    public Setting<Boolean> customFov;
    public Setting<Float> fov;
    public Setting<ColorSetting> guiColor;
    public Setting<ColorSetting> enabledColor;
    public Setting<ColorSetting> sliderColor;
    public Setting<Boolean> rainbow;
    private OyVeyGui click;
    
    public SomaGui() {
        super("SomaGui", "Opens the ClickGui", Module.Category.CLIENT, true, false, false);
        this.prefix = (Setting<String>)this.register(new Setting("Prefix", (Object)"."));
        this.customFov = (Setting<Boolean>)this.register(new Setting("CustomFov", (Object)false));
        this.fov = (Setting<Float>)this.register(new Setting("Fov", (Object)150.0f, (Object)(-180.0f), (Object)180.0f));
        this.guiColor = (Setting<ColorSetting>)this.register(new Setting("GuiColor", (Object)new ColorSetting(0, 0, 0, 255)));
        this.enabledColor = (Setting<ColorSetting>)this.register(new Setting("EnabledColor", (Object)new ColorSetting(155, 175, 255, 180)));
        this.sliderColor = (Setting<ColorSetting>)this.register(new Setting("SliderColor", (Object)new ColorSetting(155, 175, 255, 180)));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", (Object)false));
        this.setInstance();
    }
    
    public static SomaGui getInstance() {
        if (SomaGui.INSTANCE == null) {
            SomaGui.INSTANCE = new SomaGui();
        }
        return SomaGui.INSTANCE;
    }
    
    private void setInstance() {
        SomaGui.INSTANCE = this;
    }
    
    public void onUpdate() {
        if (this.customFov.getValue()) {
            SomaGui.mc.field_71474_y.func_74304_a(GameSettings.Options.FOV, (float)this.fov.getValue());
        }
    }
    
    public int syncGuiColor() {
        if (!(boolean)this.rainbow.getValue()) {
            final ColorSetting gui = (ColorSetting)this.guiColor.getValue();
            return ColorUtil.toARGB(gui.getRed(), gui.getGreen(), gui.getBlue(), gui.getAlpha());
        }
        return ColorUtil.rainbow(Component.counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB();
    }
    
    public int syncModuleColor() {
        if (!(boolean)this.rainbow.getValue()) {
            final ColorSetting enabled = (ColorSetting)this.enabledColor.getValue();
            return ColorUtil.toARGB(enabled.getRed(), enabled.getGreen(), enabled.getBlue(), enabled.getAlpha());
        }
        return ColorUtil.rainbow(Component.counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB();
    }
    
    public int syncSliderColor() {
        if (!(boolean)this.rainbow.getValue()) {
            final ColorSetting slider = (ColorSetting)this.sliderColor.getValue();
            return ColorUtil.toARGB(slider.getRed(), slider.getGreen(), slider.getBlue(), slider.getAlpha());
        }
        return ColorUtil.rainbow(Component.counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB();
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && event.getSetting().getFeature().equals(this)) {
            if (event.getSetting().equals(this.prefix)) {
                OyVey.commandManager.setPrefix((String)this.prefix.getPlannedValue());
                Command.sendMessage("Prefix set to " + ChatFormatting.DARK_GRAY + OyVey.commandManager.getPrefix());
            }
            final ColorSetting mainColor = (ColorSetting)Color.getInstance().mainColor.getValue();
            OyVey.colorManager.setColor(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), mainColor.getAlpha());
        }
    }
    
    public void onEnable() {
        SomaGui.mc.func_147108_a((GuiScreen)OyVeyGui.getClickGui());
    }
    
    public void onLoad() {
        OyVey.commandManager.setPrefix((String)this.prefix.getValue());
    }
    
    public void onTick() {
        if (!(SomaGui.mc.field_71462_r instanceof OyVeyGui)) {
            this.disable();
        }
    }
    
    static {
        SomaGui.INSTANCE = new SomaGui();
    }
}
