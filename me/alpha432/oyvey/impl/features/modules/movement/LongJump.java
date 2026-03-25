//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.movement;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.api.managers.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.*;
import java.util.*;

public class LongJump extends Module
{
    public static LongJump INSTANCE;
    public Setting<Mode> mode;
    public Setting<Double> boost;
    public Setting<Boolean> autoDisable;
    public Setting<Boolean> onGround;
    private boolean hasBeenGrounded;
    private boolean hasBoosted;
    private int stage;
    private double moveSpeed;
    private double lastDist;
    
    public LongJump() {
        super("LongJump", "Jumps but long and fast", Module.Category.MOVEMENT, true, false, false);
        this.mode = (Setting<Mode>)this.register(new Setting("Mode", (Object)Mode.BOOST));
        this.boost = (Setting<Double>)this.register(new Setting("Boost", (Object)0.4, (Object)1.0, (Object)10.0));
        this.autoDisable = (Setting<Boolean>)this.register(new Setting("AutoDisable", (Object)true));
        this.onGround = (Setting<Boolean>)this.register(new Setting("OnGround", (Object)true));
        this.hasBeenGrounded = false;
        this.hasBoosted = false;
        this.stage = 0;
        this.moveSpeed = 0.0;
        this.lastDist = 0.0;
        LongJump.INSTANCE = this;
    }
    
    public void onEnable() {
        if (LongJump.mc.field_71439_g == null) {
            return;
        }
        this.hasBeenGrounded = false;
        this.hasBoosted = false;
        this.stage = 0;
        this.moveSpeed = 0.0;
        this.lastDist = 0.0;
    }
    
    public void onDisable() {
    }
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (LongJump.mc.field_71439_g == null || LongJump.mc.field_71441_e == null) {
            return;
        }
        if (!this.hasBeenGrounded) {
            this.hasBeenGrounded = LongJump.mc.field_71439_g.field_70122_E;
        }
        if (this.hasBeenGrounded || !(boolean)this.onGround.getValue()) {
            this.lastDist = Math.sqrt((LongJump.mc.field_71439_g.field_70165_t - LongJump.mc.field_71439_g.field_70169_q) * (LongJump.mc.field_71439_g.field_70165_t - LongJump.mc.field_71439_g.field_70169_q) + (LongJump.mc.field_71439_g.field_70161_v - LongJump.mc.field_71439_g.field_70166_s) * (LongJump.mc.field_71439_g.field_70161_v - LongJump.mc.field_71439_g.field_70166_s));
            if (this.canSprint()) {
                LongJump.mc.field_71439_g.func_70031_b(true);
            }
        }
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (LongJump.mc.field_71439_g == null || LongJump.mc.field_71441_e == null) {
            return;
        }
        if (!this.hasBeenGrounded && (boolean)this.onGround.getValue()) {
            return;
        }
        if ((this.mode.getValue() == Mode.STRICT || this.mode.getValue() == Mode.STRICT_HIGH) && (BoostManager.INSTANCE == null || !BoostManager.INSTANCE.canDoLongjump())) {
            return;
        }
        if (LongJump.mc.field_71439_g.func_70093_af() || LongJump.mc.field_71439_g.func_70090_H() || LongJump.mc.field_71439_g.func_180799_ab()) {
            return;
        }
        switch (this.stage) {
            case 0: {
                ++this.stage;
                this.lastDist = 0.0;
                break;
            }
            default: {
                if ((LongJump.mc.field_71441_e.func_184144_a((Entity)LongJump.mc.field_71439_g, LongJump.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, LongJump.mc.field_71439_g.field_70181_x, 0.0)).size() > 0 || LongJump.mc.field_71439_g.field_70124_G) && this.stage > 0) {
                    this.stage = ((LongJump.mc.field_71439_g.field_191988_bg != 0.0f || LongJump.mc.field_71439_g.field_70702_br != 0.0f) ? 1 : 0);
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                break;
            }
            case 2: {
                double jumpHeight = 0.40123128;
                if ((LongJump.mc.field_71439_g.field_191988_bg != 0.0f || LongJump.mc.field_71439_g.field_70702_br != 0.0f) && LongJump.mc.field_71439_g.field_70122_E) {
                    if (LongJump.mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
                        jumpHeight += (LongJump.mc.field_71439_g.func_70660_b(MobEffects.field_76430_j).func_76458_c() + 1) * 0.1f;
                    }
                    event.setY(LongJump.mc.field_71439_g.field_70181_x = jumpHeight);
                    this.moveSpeed *= 2.149;
                    break;
                }
                break;
            }
            case 3: {
                this.moveSpeed = this.lastDist - 0.76 * (this.lastDist - this.getBaseMoveSpeed());
                break;
            }
        }
        this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
        double forward = LongJump.mc.field_71439_g.field_71158_b.field_192832_b;
        double strafe = LongJump.mc.field_71439_g.field_71158_b.field_78902_a;
        final double yaw = LongJump.mc.field_71439_g.field_70177_z;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else {
            if (forward != 0.0 && strafe != 0.0) {
                forward *= Math.sin(0.7853981633974483);
                strafe *= Math.cos(0.7853981633974483);
            }
            event.setX((forward * this.moveSpeed * -Math.sin(Math.toRadians(yaw)) + strafe * this.moveSpeed * Math.cos(Math.toRadians(yaw))) * 0.99);
            event.setZ((forward * this.moveSpeed * Math.cos(Math.toRadians(yaw)) - strafe * this.moveSpeed * -Math.sin(Math.toRadians(yaw))) * 0.99);
        }
        ++this.stage;
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook && (boolean)this.autoDisable.getValue()) {
            this.disable();
        }
    }
    
    private double getBaseMoveSpeed() {
        double baseSpeed = 0.272;
        if (LongJump.mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
            final int amplifier = Objects.requireNonNull(LongJump.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c)).func_76458_c();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed * (double)this.boost.getValue();
    }
    
    private boolean canSprint() {
        return (LongJump.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f || LongJump.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f) && !LongJump.mc.field_71439_g.func_70093_af() && !LongJump.mc.field_71439_g.func_70090_H() && !LongJump.mc.field_71439_g.func_180799_ab() && !LongJump.mc.field_71439_g.func_184613_cA() && LongJump.mc.field_71439_g.func_71024_bL().func_75116_a() > 6;
    }
    
    public String getDisplayInfo() {
        return ((Mode)this.mode.getValue()).toString();
    }
    
    public enum Mode
    {
        BOOST, 
        STRICT, 
        STRICT_HIGH;
    }
}
