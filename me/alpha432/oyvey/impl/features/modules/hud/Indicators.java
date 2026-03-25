//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.api.util.entity.*;
import java.util.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Indicators extends Module
{
    private static Indicators INSTANCE;
    private final Setting<Boolean> yourHealth;
    private final Setting<Boolean> enemyHealth;
    
    public Indicators() {
        super("Indicators", "Draws some info under crosshair", Module.Category.HUD, true, false, false);
        this.yourHealth = (Setting<Boolean>)this.register(new Setting("YourHealth", (Object)false));
        this.enemyHealth = (Setting<Boolean>)this.register(new Setting("EnemyHealth", (Object)false));
        this.setInstance();
    }
    
    public static Indicators getInstance() {
        if (Indicators.INSTANCE == null) {
            Indicators.INSTANCE = new Indicators();
        }
        return Indicators.INSTANCE;
    }
    
    private void setInstance() {
        Indicators.INSTANCE = this;
    }
    
    public int intHealth() {
        if (Indicators.mc.field_71439_g == null) {
            return 0;
        }
        final float health = Indicators.mc.field_71439_g.func_110143_aJ() + Indicators.mc.field_71439_g.func_110139_bj();
        final int intHealth2 = (int)Math.floor(health);
        return intHealth2;
    }
    
    private Entity getTarget() {
        Entity target = null;
        double distance = (float)Resolver.getInstance().range.getValue();
        double maxHealth = 36.0;
        for (final Entity entity : Indicators.mc.field_71441_e.field_73010_i) {
            if (((boolean)Resolver.getInstance().players.getValue() && entity instanceof EntityPlayer) || ((boolean)Resolver.getInstance().animals.getValue() && EntityUtil.isPassive(entity)) || ((boolean)Resolver.getInstance().mobs.getValue() && EntityUtil.isMobAggressive(entity)) || ((boolean)Resolver.getInstance().vehicles.getValue() && EntityUtil.isVehicle(entity)) || ((boolean)Resolver.getInstance().projectiles.getValue() && EntityUtil.isProjectile(entity))) {
                if (entity instanceof EntityLivingBase && EntityUtil.isntValid(entity, distance)) {
                    continue;
                }
                if (!Indicators.mc.field_71439_g.func_70685_l(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && Indicators.mc.field_71439_g.func_70068_e(entity) > MathUtil.square((double)(float)Resolver.getInstance().raytrace.getValue())) {
                    continue;
                }
                if (target == null) {
                    target = entity;
                    distance = Indicators.mc.field_71439_g.func_70068_e(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
                else {
                    if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer)entity, 18)) {
                        target = entity;
                        break;
                    }
                    if (Indicators.mc.field_71439_g.func_70068_e(entity) < distance) {
                        target = entity;
                        distance = Indicators.mc.field_71439_g.func_70068_e(entity);
                        maxHealth = EntityUtil.getHealth(entity);
                    }
                    if (EntityUtil.getHealth(entity) >= maxHealth) {
                        continue;
                    }
                    target = entity;
                    distance = Indicators.mc.field_71439_g.func_70068_e(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
            }
        }
        return target;
    }
    
    @SubscribeEvent
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        if (Indicators.mc.field_71439_g == null || this.renderer == null) {
            return;
        }
        final int health = this.intHealth();
        final int textWidth = this.renderer.getStringWidth("" + health + "");
        final int xPos = (this.renderer.scaledWidth - textWidth) / 2;
        final int yPos = this.renderer.scaledHeight / 2 + 10;
        final int red = -65536;
        final int orange = -23296;
        final int green = -16711936;
        if (Indicators.mc.field_71439_g == null) {
            return;
        }
        if (this.yourHealth.getValue()) {
            if (health > 19) {
                this.renderer.drawString("" + this.intHealth(), (float)(xPos + 1), (float)yPos, green, true);
            }
            if (health < 20) {
                this.renderer.drawString("" + this.intHealth(), (float)(xPos + 1), (float)yPos, orange, true);
            }
            if (health < 10) {
                this.renderer.drawString("" + this.intHealth(), (float)(xPos + 1), (float)yPos, red, true);
            }
        }
    }
    
    static {
        Indicators.INSTANCE = new Indicators();
    }
}
