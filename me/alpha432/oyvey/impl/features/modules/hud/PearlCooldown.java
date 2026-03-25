//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraft.entity.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import me.alpha432.oyvey.api.util.render.*;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.*;
import java.util.*;

public class PearlCooldown extends Module
{
    private static PearlCooldown INSTANCE;
    Setting<ColorMode> colorMode;
    public final Setting<Integer> x;
    public final Setting<Integer> y;
    private final Setting<ColorSetting> customColor;
    private Entity enderPearl;
    private boolean flag;
    private Entity lastProcessedPearl;
    boolean inCooldown;
    private int cooldownTimer;
    private Timer timer;
    
    public PearlCooldown() {
        super("PearlCooldown", "Draw delay to next pearl", Module.Category.HUD, true, false, false);
        this.colorMode = (Setting<ColorMode>)this.register(new Setting("ColorScheme", (Object)ColorMode.Default));
        this.x = (Setting<Integer>)this.register(new Setting("X-Pos", (Object)580, (Object)0, (Object)1000));
        this.y = (Setting<Integer>)this.register(new Setting("Y-Pos", (Object)70, (Object)0, (Object)1000));
        this.customColor = (Setting<ColorSetting>)this.register(new Setting("CustomColor", (Object)new ColorSetting(255, 255, 255, 255), v -> this.colorMode.getValue() == ColorMode.Custom));
        this.inCooldown = false;
        this.cooldownTimer = 0;
        this.timer = new Timer();
    }
    
    public static PearlCooldown getInstance() {
        if (PearlCooldown.INSTANCE == null) {
            PearlCooldown.INSTANCE = new PearlCooldown();
        }
        return PearlCooldown.INSTANCE;
    }
    
    private void setInstance() {
        PearlCooldown.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT && this.inCooldown) {
            this.renderCooldownText();
        }
    }
    
    private void renderCooldownText() {
        if (this.cooldownTimer > 0) {
            if (this.colorMode.getValue() == ColorMode.Sync) {
                this.renderer.drawString("ender pearl cooldown (" + this.cooldownTimer + "s)", (float)(int)this.x.getValue(), (float)(int)this.y.getValue(), Color.getInstance().syncColor(), true);
            }
            else {
                this.renderer.drawString("ender pearl cooldown (" + this.cooldownTimer + "s)", (float)(int)this.x.getValue(), (float)(int)this.y.getValue(), this.getColor(), true);
            }
        }
    }
    
    int getColor() {
        int colorValue = 0;
        if (this.colorMode.getValue() == ColorMode.Default) {
            colorValue = ColorUtil.toRGBA(255, 85, 255);
        }
        if (this.colorMode.getValue() == ColorMode.Custom) {
            final ColorSetting custom = (ColorSetting)this.customColor.getValue();
            colorValue = ColorUtil.toRGBA(custom.getRed(), custom.getGreen(), custom.getBlue());
        }
        return colorValue;
    }
    
    public void onUpdate() {
        if (PearlCooldown.mc.field_71441_e == null || PearlCooldown.mc.field_71439_g == null) {
            return;
        }
        this.enderPearl = null;
        for (final Entity e : PearlCooldown.mc.field_71441_e.field_72996_f) {
            if (e instanceof EntityEnderPearl) {
                this.enderPearl = e;
                break;
            }
        }
        if (this.enderPearl == null) {
            this.flag = true;
            this.lastProcessedPearl = null;
            return;
        }
        if (this.enderPearl == this.lastProcessedPearl) {
            return;
        }
        EntityPlayer closestPlayer = null;
        for (final EntityPlayer entity : PearlCooldown.mc.field_71441_e.field_73010_i) {
            if (closestPlayer == null) {
                closestPlayer = entity;
            }
            else {
                if (closestPlayer.func_70032_d(this.enderPearl) <= entity.func_70032_d(this.enderPearl)) {
                    continue;
                }
                closestPlayer = entity;
            }
        }
        if (closestPlayer == PearlCooldown.mc.field_71439_g) {
            this.flag = true;
        }
        else {
            this.flag = false;
        }
        if (closestPlayer != null && this.flag) {
            String faceing = this.enderPearl.func_174811_aO().toString();
            if (faceing.equals("west")) {
                faceing = "+x";
            }
            else if (faceing.equals("east")) {
                faceing = "-x";
            }
            else if (faceing.equals("north")) {
                faceing = "-z";
            }
            else if (faceing.equals("south")) {
                faceing = "+z";
            }
            if (this.isOn()) {
                if (!this.inCooldown) {
                    this.startCooldown();
                }
                this.flag = false;
                this.lastProcessedPearl = this.enderPearl;
            }
        }
    }
    
    private void startCooldown() {
        this.inCooldown = true;
        this.cooldownTimer = 15;
        if (this.timer != null) {
            this.timer.cancel();
        }
        (this.timer = new Timer()).scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (PearlCooldown.this.cooldownTimer > 0) {
                    PearlCooldown.this.cooldownTimer--;
                }
                else {
                    PearlCooldown.this.inCooldown = false;
                    this.cancel();
                }
            }
        }, 1000L, 1000L);
    }
    
    public void onDisable() {
        super.onDisable();
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
        this.inCooldown = false;
        this.cooldownTimer = 0;
    }
    
    static {
        PearlCooldown.INSTANCE = new PearlCooldown();
    }
    
    public enum ColorMode
    {
        Default, 
        Custom, 
        Sync;
    }
}
