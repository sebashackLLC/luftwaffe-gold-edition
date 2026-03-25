//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import net.minecraft.util.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraft.client.gui.*;
import java.awt.*;
import me.alpha432.oyvey.api.util.render.*;

public final class HitMarkers extends Module
{
    public final ResourceLocation image;
    private int renderTicks;
    public Setting<Integer> red;
    public Setting<Integer> green;
    public Setting<Integer> blue;
    public Setting<Integer> alpha;
    public Setting<Integer> thickness;
    public Setting<Double> time;
    
    public HitMarkers() {
        super("HitMarkers", "Shows hit markers when attacking entities", Module.Category.RENDER, true, false, false);
        this.image = new ResourceLocation("hitmarker.png");
        this.renderTicks = 100;
        this.red = (Setting<Integer>)this.register(new Setting("Red", (Object)255, (Object)0, (Object)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (Object)255, (Object)0, (Object)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (Object)255, (Object)0, (Object)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (Object)255, (Object)0, (Object)255));
        this.thickness = (Setting<Integer>)this.register(new Setting("Thickness", (Object)2, (Object)1, (Object)6));
        this.time = (Setting<Double>)this.register(new Setting("Time", (Object)20.0, (Object)1.0, (Object)50.0));
    }
    
    public void onRender2D(final Render2DEvent event) {
        if (this.renderTicks < (double)this.time.getValue()) {
            this.drawHitMarkers();
        }
    }
    
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
    }
    
    @SubscribeEvent
    public void onAttackEntity(final AttackEntityEvent event) {
        if (event.getEntity().equals((Object)HitMarkers.mc.field_71439_g)) {
            this.renderTicks = 0;
        }
    }
    
    @SubscribeEvent
    public void onTickClientTick(final TickEvent event) {
        ++this.renderTicks;
    }
    
    public void drawHitMarkers() {
        final ScaledResolution resolution = new ScaledResolution(HitMarkers.mc);
        final Color color = new Color((int)this.red.getValue(), (int)this.green.getValue(), (int)this.blue.getValue());
        final float thick = (float)this.thickness.getValue();
        final float centerX = resolution.func_78326_a() / 2.0f;
        final float centerY = resolution.func_78328_b() / 2.0f;
        RenderUtil.drawLine(centerX - 4.0f, centerY - 4.0f, centerX - 8.0f, centerY - 8.0f, thick, color.getRGB());
        RenderUtil.drawLine(centerX + 4.0f, centerY - 4.0f, centerX + 8.0f, centerY - 8.0f, thick, color.getRGB());
        RenderUtil.drawLine(centerX - 4.0f, centerY + 4.0f, centerX - 8.0f, centerY + 8.0f, thick, color.getRGB());
        RenderUtil.drawLine(centerX + 4.0f, centerY + 4.0f, centerX + 8.0f, centerY + 8.0f, thick, color.getRGB());
    }
}
