//Decompiled by Procyon!

package me.alpha432.oyvey.api.features;

import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.event.bus.listener.*;
import java.util.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.api.util.*;
import me.alpha432.oyvey.impl.features.modules.hud.*;
import net.minecraft.util.text.*;
import net.minecraftforge.common.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.impl.command.*;

public class Module extends Feature
{
    private final String description;
    private final Category category;
    public Setting<Boolean> enabled;
    public Setting<Boolean> drawn;
    public Setting<Bind> bind;
    public Setting<String> displayName;
    public boolean hasListener;
    public boolean alwaysListening;
    public boolean hidden;
    public float arrayListOffset;
    public float arrayListVOffset;
    public float offset;
    public float vOffset;
    public boolean sliding;
    private final List<Listener<?>> listeners;
    
    public Module(final String name, final String description, final Category category, final boolean hasListener, final boolean hidden, final boolean alwaysListening) {
        super(name);
        this.enabled = (Setting<Boolean>)this.register(new Setting("Enabled", (T)false));
        this.drawn = (Setting<Boolean>)this.register(new Setting("Drawn", (T)false));
        this.bind = (Setting<Bind>)this.register(new Setting("Keybind", (T)new Bind(-1)));
        this.arrayListOffset = 0.0f;
        this.arrayListVOffset = 0.0f;
        this.listeners = new ArrayList<Listener<?>>();
        this.displayName = (Setting<String>)this.register(new Setting("DisplayName", (T)name));
        this.description = description;
        this.category = category;
        this.hasListener = hasListener;
        this.hidden = hidden;
        this.alwaysListening = alwaysListening;
    }
    
    public void offerListeners(final Listener<?>... listeners) {
        Collections.addAll(this.listeners, listeners);
    }
    
    public Collection<Listener<?>> getListeners() {
        return this.listeners;
    }
    
    public boolean isSliding() {
        return this.sliding;
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    public void onToggle() {
    }
    
    public void onLoad() {
    }
    
    public void onTick() {
    }
    
    public void onLogin() {
    }
    
    public void onLogout() {
    }
    
    public void onUpdate() {
    }
    
    public void onRender2D(final Render2DEvent event) {
    }
    
    public void onRender3D(final Render3DEvent event) {
    }
    
    public void onUnload() {
    }
    
    public String getDisplayInfo() {
        return null;
    }
    
    public boolean isOn() {
        return this.enabled.getValue();
    }
    
    public boolean isOff() {
        return !this.enabled.getValue();
    }
    
    public void setEnabled(final boolean enabled) {
        if (enabled) {
            this.enable();
        }
        else {
            this.disable();
        }
    }
    
    public void enable() {
        this.enabled.setValue(Boolean.TRUE);
        final String displayText = this.getFullArrayString();
        final float textWidth = (OyVey.textManager != null) ? ((float)OyVey.textManager.getStringWidth(displayText)) : 100.0f;
        this.arrayListOffset = textWidth + 5.0f;
        this.sliding = true;
        this.onToggle();
        this.onEnable();
        if (Notifications.getInstance() != null && Notifications.getInstance().isOn()) {
            Notifications.getInstance().addNotification(this.getDisplayName(), true);
        }
        if (Management.getInstance().notifyToggles.getValue() && this.getDisplayName() != "AutoCrystal") {
            if (Management.getInstance().mode.getValue() == Management.Modes.Luftwaffe) {
                final TextComponentString text = new TextComponentString(OyVey.commandManager.getClientMessage() + " " + ChatFormatting.BOLD + ChatFormatting.WHITE + this.getDisplayName() + TextUtil.coloredString(" toggled", Metrics.getInstance().commandColor.getPlannedValue()) + ChatFormatting.GREEN + " on");
                Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
            }
            if (Management.getInstance().mode.getValue() == Management.Modes.DotGod) {
                final TextComponentString text = new TextComponentString(ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + Management.getInstance().clientName.getValue() + ChatFormatting.DARK_PURPLE + "] " + ChatFormatting.DARK_AQUA + this.getDisplayName() + ChatFormatting.LIGHT_PURPLE + " was " + ChatFormatting.GREEN + "enabled");
                Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
            }
            if (Management.getInstance().mode.getValue() == Management.Modes.Sn0w) {
                final TextComponentString text = new TextComponentString(ChatFormatting.BLUE + "[" + ChatFormatting.AQUA + "\u2744" + ChatFormatting.BLUE + "] " + ChatFormatting.WHITE + ChatFormatting.BOLD + this.getDisplayName() + ChatFormatting.RESET + " was " + ChatFormatting.BLUE + "enabled" + ChatFormatting.RESET + ".");
                Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
            }
        }
        if (this.isOn() && this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.register((Object)this);
        }
    }
    
    public void disable() {
        if (this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.unregister((Object)this);
        }
        this.enabled.setValue(false);
        if (Notifications.getInstance() != null && Notifications.getInstance().isOn()) {
            Notifications.getInstance().addNotification(this.getDisplayName(), false);
        }
        if (Management.getInstance().notifyToggles.getValue() && this.getDisplayName() != "AutoCrystal") {
            if (Management.getInstance().mode.getValue() == Management.Modes.Luftwaffe) {
                final TextComponentString text = new TextComponentString(OyVey.commandManager.getClientMessage() + " " + ChatFormatting.BOLD + ChatFormatting.WHITE + this.getDisplayName() + TextUtil.coloredString(" toggled", Metrics.getInstance().commandColor.getPlannedValue()) + ChatFormatting.RED + " off");
                Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
            }
            if (Management.getInstance().mode.getValue() == Management.Modes.DotGod) {
                final TextComponentString text = new TextComponentString(ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + Management.getInstance().clientName.getValue() + ChatFormatting.DARK_PURPLE + "] " + ChatFormatting.DARK_AQUA + this.getDisplayName() + ChatFormatting.LIGHT_PURPLE + " was " + ChatFormatting.RED + "disabled");
                Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
            }
            if (Management.getInstance().mode.getValue() == Management.Modes.Sn0w) {
                final TextComponentString text = new TextComponentString(ChatFormatting.BLUE + "[" + ChatFormatting.AQUA + "\u2744" + ChatFormatting.BLUE + "] " + ChatFormatting.WHITE + ChatFormatting.BOLD + this.getDisplayName() + ChatFormatting.RESET + " was " + ChatFormatting.BLUE + "disabled" + ChatFormatting.RESET + ".");
                Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
            }
        }
        this.onToggle();
        this.onDisable();
    }
    
    public void silentDisable() {
        if (this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.unregister((Object)this);
        }
        this.enabled.setValue(false);
        if (Management.getInstance().notifyToggles.getValue()) {
            final TextComponentString text = new TextComponentString(OyVey.commandManager.getClientMessage() + " " + ChatFormatting.BOLD + ChatFormatting.WHITE + this.getDisplayName() + TextUtil.coloredString(" toggled", Metrics.getInstance().commandColor.getPlannedValue()) + ChatFormatting.RED + " off");
            Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
        }
    }
    
    public void toggle() {
        final ClientEvent event = new ClientEvent(this.isEnabled() ? 0 : 1, this);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            this.setEnabled(!this.isEnabled());
        }
    }
    
    public String getDisplayName() {
        return this.displayName.getValue();
    }
    
    public void setDisplayName(final String name) {
        final Module module = OyVey.moduleManager.getModuleByDisplayName(name);
        final Module originalModule = OyVey.moduleManager.getModuleByName(name);
        if (module == null && originalModule == null) {
            Command.sendMessage(this.getDisplayName() + ", name: " + this.getName() + ", has been renamed to: " + name);
            this.displayName.setValue(name);
            return;
        }
        Command.sendMessage(ChatFormatting.RED + "A module of this name already exists.");
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public boolean isDrawn() {
        return this.drawn.getValue();
    }
    
    public void setDrawn(final boolean drawn) {
        this.drawn.setValue(drawn);
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public String getInfo() {
        return null;
    }
    
    public Bind getBind() {
        return this.bind.getValue();
    }
    
    public void setBind(final int key) {
        this.bind.setValue(new Bind(key));
    }
    
    public boolean listening() {
        return (this.hasListener && this.isOn()) || this.alwaysListening;
    }
    
    public String getFullArrayString() {
        return this.getDisplayName() + ChatFormatting.GRAY + ((this.getDisplayInfo() != null) ? (" [" + ChatFormatting.WHITE + this.getDisplayInfo() + ChatFormatting.GRAY + "]") : "");
    }
    
    public enum Category
    {
        COMBAT("Combat"), 
        MISC("Misc"), 
        RENDER("Render"), 
        MOVEMENT("Movement"), 
        PLAYER("Player"), 
        HUD("Hud"), 
        CLIENT("Client");
        
        private final String name;
        
        private Category(final String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
    }
}
