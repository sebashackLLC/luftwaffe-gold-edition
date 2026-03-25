//Decompiled by Procyon!

package me.alpha432.oyvey.api.managers;

import me.alpha432.oyvey.api.interfaces.*;
import me.alpha432.oyvey.api.util.math.*;
import net.minecraftforge.common.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;
import me.alpha432.oyvey.api.util.entity.*;

public class BoostManager implements Minecraftable
{
    public static BoostManager INSTANCE;
    private final Timer explosionTimer;
    private final Timer longjumpTimer;
    private double boostExplosionSpeed;
    private boolean canLongjump;
    
    public BoostManager() {
        this.explosionTimer = new Timer();
        this.longjumpTimer = new Timer();
        this.boostExplosionSpeed = 0.0;
        this.canLongjump = false;
        BoostManager.INSTANCE = this;
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (BoostManager.mc.field_71439_g == null || BoostManager.mc.field_71441_e == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketExplosion) {
            final SPacketExplosion packet = event.getPacket();
            if (BoostManager.mc.field_71439_g.func_70011_f(packet.func_149148_f(), packet.func_149143_g(), packet.func_149145_h()) <= 6.0 && (packet.func_149149_c() != 0.0f || packet.func_149147_e() != 0.0f)) {
                this.explosionTimer.reset();
                this.boostExplosionSpeed = Math.hypot(packet.func_149149_c(), packet.func_149147_e());
                this.canLongjump = true;
                this.longjumpTimer.reset();
            }
        }
    }
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (BoostManager.mc.field_71439_g == null || BoostManager.mc.field_71441_e == null) {
            return;
        }
        if (!MovementUtil.isMoving()) {
            this.boostExplosionSpeed = 0.0;
            this.canLongjump = false;
        }
        if (this.explosionTimer.passedMs(400L)) {
            this.boostExplosionSpeed = 0.0;
        }
        if (this.longjumpTimer.passedMs(500L)) {
            this.canLongjump = false;
        }
    }
    
    public double getBoostSpeed(final boolean slow) {
        return (slow && this.boostExplosionSpeed != 0.0) ? 0.5 : this.boostExplosionSpeed;
    }
    
    public boolean canDoLongjump() {
        return this.canLongjump;
    }
    
    public double getExplosionSpeed() {
        return this.boostExplosionSpeed;
    }
    
    public boolean isBoostActive() {
        return this.boostExplosionSpeed > 0.0;
    }
}
