//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.movement;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.mixin.mixins.*;
import net.minecraftforge.fml.common.gameevent.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.network.play.server.*;
import net.minecraft.init.*;
import me.alpha432.oyvey.api.managers.*;
import net.minecraft.entity.*;
import java.util.*;
import net.minecraft.util.math.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.client.entity.*;

public class Speed extends Module
{
    public static Speed INSTANCE;
    public Setting<Mode> speedMode;
    public Setting<TimerMode> timerMode;
    public Setting<Boolean> lowHop;
    public Setting<Boolean> buffer;
    public Setting<Boolean> boost;
    public Setting<Boolean> always;
    public Setting<AlwaysMode> alwaysMode;
    public Setting<Boolean> potionEffects;
    public Setting<Double> speed;
    public Setting<Double> yportSpeed;
    private int strafeStage;
    private double ncpPrevMotion;
    private double horizontal;
    private final double BUNNY_DIV_FRICTION = 159.077;
    private double currentSpeed;
    private double prevMotion;
    private boolean accelerate;
    private int state;
    private int aacCounter;
    private float oldTickLength;
    private ITimer timer;
    
    public Speed() {
        super("Speed", "Move around at rapid speeds", Module.Category.MOVEMENT, true, false, false);
        this.speedMode = (Setting<Mode>)this.register(new Setting("Mode", (Object)Mode.STRAFE));
        this.timerMode = (Setting<TimerMode>)this.register(new Setting("Timer", (Object)TimerMode.NONE));
        this.lowHop = (Setting<Boolean>)this.register(new Setting("LowHop", (Object)false));
        this.buffer = (Setting<Boolean>)this.register(new Setting("Buffer", (Object)false));
        this.boost = (Setting<Boolean>)this.register(new Setting("Boost", (Object)false));
        this.always = (Setting<Boolean>)this.register(new Setting("Always", (Object)false));
        this.alwaysMode = (Setting<AlwaysMode>)new Setting("AlwaysMode", (Object)AlwaysMode.NEW, v -> (boolean)this.always.getValue());
        this.potionEffects = (Setting<Boolean>)new Setting("Potion Effects", (Object)true);
        this.speed = (Setting<Double>)this.register(new Setting("Speed", (Object)1.0, (Object)0.01, (Object)2.0));
        this.yportSpeed = (Setting<Double>)this.register(new Setting("YPortSpeed", (Object)0.06, (Object)0.01, (Object)0.15, v -> this.speedMode.getValue() == Mode.STRAFE_STRICT));
        this.strafeStage = 1;
        this.ncpPrevMotion = 0.0;
        this.horizontal = 0.0;
        this.currentSpeed = 0.0;
        this.prevMotion = 0.0;
        this.accelerate = false;
        this.state = 4;
        this.aacCounter = 0;
        this.oldTickLength = 50.0f;
        Speed.INSTANCE = this;
    }
    
    public void onEnable() {
        if (Speed.mc.field_71439_g == null) {
            return;
        }
        this.timer = (ITimer)Speed.mc.field_71428_T;
        this.oldTickLength = 50.0f;
        this.reset();
    }
    
    public void onDisable() {
        if (this.timerMode.getValue() != TimerMode.NONE && this.timer != null) {
            this.timer.setTickLength(this.oldTickLength);
        }
        this.reset();
    }
    
    public void onTick() {
        if (Speed.mc.field_71439_g == null || Speed.mc.field_71441_e == null) {
            return;
        }
        if ((boolean)this.always.getValue() && this.alwaysMode.getValue() == AlwaysMode.OLD) {
            if (!this.isOn() && Speed.mc.field_71474_y.field_74314_A.func_151470_d()) {
                this.enable();
            }
            else if (this.isOn() && !Speed.mc.field_71474_y.field_74314_A.func_151470_d()) {
                this.disable();
            }
        }
    }
    
    public void onUpdate() {
        if (Speed.mc.field_71439_g == null || Speed.mc.field_71441_e == null) {
            return;
        }
        if ((boolean)this.always.getValue() && this.alwaysMode.getValue() == AlwaysMode.NEW) {
            if (!this.isOn() && Speed.mc.field_71474_y.field_74314_A.func_151470_d()) {
                this.enable();
            }
            else if (this.isOn() && !Speed.mc.field_71474_y.field_74314_A.func_151470_d()) {
                this.disable();
            }
        }
    }
    
    public void reset() {
        this.ncpPrevMotion = 0.0;
        this.currentSpeed = 0.0;
        this.horizontal = 0.0;
        this.state = 4;
        this.prevMotion = 0.0;
        this.aacCounter = 0;
        this.accelerate = false;
    }
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (Speed.mc.field_71439_g == null || Speed.mc.field_71441_e == null) {
            return;
        }
        if (Speed.mc.field_71439_g.func_184613_cA()) {
            return;
        }
        if (this.speedMode.getValue() == Mode.NONE) {
            return;
        }
        if (LongJump.INSTANCE != null && LongJump.INSTANCE.isOn() && (LongJump.INSTANCE.mode.getValue() == LongJump.Mode.STRICT || LongJump.INSTANCE.mode.getValue() == LongJump.Mode.STRICT_HIGH)) {
            this.reset();
            return;
        }
        if (this.isStrafeSpeed()) {
            if (this.timer != null && this.timerMode.getValue() != TimerMode.NONE) {
                final float timerSpeed = (this.timerMode.getValue() == TimerMode.FAST) ? 1.0985f : 1.0888f;
                this.timer.setTickLength(this.oldTickLength / timerSpeed);
            }
            else if (this.timer != null) {
                this.timer.setTickLength(this.oldTickLength);
            }
            if (MovementUtil.isMoving()) {
                Speed.mc.field_71439_g.func_70031_b(true);
            }
            this.ncpPrevMotion = Math.sqrt((Speed.mc.field_71439_g.field_70165_t - Speed.mc.field_71439_g.field_70169_q) * (Speed.mc.field_71439_g.field_70165_t - Speed.mc.field_71439_g.field_70169_q) + (Speed.mc.field_71439_g.field_70161_v - Speed.mc.field_71439_g.field_70166_s) * (Speed.mc.field_71439_g.field_70161_v - Speed.mc.field_71439_g.field_70166_s));
        }
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (Speed.mc.field_71439_g == null || Speed.mc.field_71441_e == null) {
            return;
        }
        if (event.getStage() != 0) {
            return;
        }
        if (Speed.mc.field_71439_g.func_184613_cA()) {
            return;
        }
        if (this.speedMode.getValue() == Mode.NONE) {
            return;
        }
        if (LongJump.INSTANCE != null && LongJump.INSTANCE.isOn() && (LongJump.INSTANCE.mode.getValue() == LongJump.Mode.STRICT || LongJump.INSTANCE.mode.getValue() == LongJump.Mode.STRICT_HIGH)) {
            this.reset();
            return;
        }
        if (this.isStrafeSpeed()) {
            final double dX = Speed.mc.field_71439_g.field_70165_t - Speed.mc.field_71439_g.field_70169_q;
            final double dZ = Speed.mc.field_71439_g.field_70161_v - Speed.mc.field_71439_g.field_70166_s;
            this.prevMotion = Math.sqrt(dX * dX + dZ * dZ);
        }
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (Speed.mc.field_71439_g == null || Speed.mc.field_71441_e == null) {
            return;
        }
        if (Speed.mc.field_71439_g.func_184613_cA()) {
            return;
        }
        if (this.speedMode.getValue() == Mode.NONE) {
            return;
        }
        if (LongJump.INSTANCE != null && LongJump.INSTANCE.isOn() && (LongJump.INSTANCE.mode.getValue() == LongJump.Mode.STRICT || LongJump.INSTANCE.mode.getValue() == LongJump.Mode.STRICT_HIGH)) {
            this.reset();
            return;
        }
        switch ((Mode)this.speedMode.getValue()) {
            case STRAFE: {
                this.handleStrafeSpeed(event);
                break;
            }
            case STRAFE_STRICT: {
                this.doRestrictedSpeed(event, 0.465, 0.44, false);
                break;
            }
            case STRICT_FAST: {
                this.doRestrictedSpeed(event, 0.465, 0.44, true);
                break;
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            this.reset();
        }
    }
    
    private void handleStrafeSpeed(final MoveEvent event) {
        if (this.state == 1 && Speed.mc.field_71439_g.field_191988_bg != 0.0f && Speed.mc.field_71439_g.field_70702_br != 0.0f) {
            this.currentSpeed = 1.35 * this.getEffectiveBaseSpeed() - 0.01;
        }
        else if (this.state == 2 && (Speed.mc.field_71439_g.field_191988_bg != 0.0f || Speed.mc.field_71439_g.field_70702_br != 0.0f) && Speed.mc.field_71439_g.field_70122_E) {
            double jumpBoost = 0.0;
            if (Speed.mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
                jumpBoost = (Speed.mc.field_71439_g.func_70660_b(MobEffects.field_76430_j).func_76458_c() + 1) * 0.1;
            }
            event.setY(Speed.mc.field_71439_g.field_70181_x = (this.lowHop.getValue() ? 0.3999 : 0.3999999463558197) + jumpBoost);
            if (this.buffer.getValue()) {
                this.currentSpeed *= (this.accelerate ? 1.6835 : 1.395);
            }
            else {
                this.currentSpeed *= 1.535;
            }
            if ((boolean)this.boost.getValue() && BoostManager.INSTANCE != null) {
                this.currentSpeed = Math.max(BoostManager.INSTANCE.getBoostSpeed(true), this.currentSpeed);
            }
        }
        else if (this.state == 3) {
            final double decrease = 0.66 * (this.prevMotion - this.getEffectiveBaseSpeed());
            this.currentSpeed = this.prevMotion - decrease;
            this.accelerate = !this.accelerate;
            if ((boolean)this.boost.getValue() && BoostManager.INSTANCE != null) {
                this.currentSpeed = Math.max(BoostManager.INSTANCE.getBoostSpeed(true), this.currentSpeed);
            }
        }
        else {
            final List<AxisAlignedBB> collisionBoxes = (List<AxisAlignedBB>)Speed.mc.field_71441_e.func_184144_a((Entity)Speed.mc.field_71439_g, Speed.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, Speed.mc.field_71439_g.field_70181_x, 0.0));
            if ((collisionBoxes.size() > 0 || Speed.mc.field_71439_g.field_70124_G) && this.state > 0) {
                this.state = ((Speed.mc.field_71439_g.field_191988_bg != 0.0f || Speed.mc.field_71439_g.field_70702_br != 0.0f) ? 1 : 0);
            }
            this.currentSpeed = this.prevMotion - this.prevMotion / 159.0;
        }
        this.currentSpeed = Math.max(this.currentSpeed, this.getEffectiveBaseSpeed());
        double forward = Speed.mc.field_71439_g.field_71158_b.field_192832_b;
        double strafe = Speed.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = Speed.mc.field_71439_g.field_70177_z;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                forward = ((forward > 0.0) ? 1.0 : -1.0);
            }
            final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
            final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
            event.setX(forward * this.currentSpeed * cos + strafe * this.currentSpeed * sin);
            event.setZ(forward * this.currentSpeed * sin - strafe * this.currentSpeed * cos);
        }
        if (Speed.mc.field_71439_g.field_191988_bg != 0.0f || Speed.mc.field_71439_g.field_70702_br != 0.0f) {
            ++this.state;
        }
    }
    
    private void handleStrafeInsano(final MoveEvent event) {
        if (!Speed.mc.field_71439_g.func_70051_ag()) {
            Speed.mc.field_71439_g.func_70031_b(true);
            Speed.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)Speed.mc.field_71439_g, CPacketEntityAction.Action.START_SPRINTING));
        }
        final double speedMultiplier = (double)this.speed.getValue() * 0.99;
        switch (this.strafeStage) {
            case 0: {
                ++this.strafeStage;
                this.ncpPrevMotion = 0.0;
                break;
            }
            default: {
                this.horizontal = this.ncpPrevMotion - this.ncpPrevMotion / 159.0;
                final List<AxisAlignedBB> collisionBoxes = (List<AxisAlignedBB>)Speed.mc.field_71441_e.func_184144_a((Entity)Speed.mc.field_71439_g, Speed.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, Speed.mc.field_71439_g.field_70181_x, 0.0));
                if ((collisionBoxes.size() > 0 || Speed.mc.field_71439_g.field_70124_G) && this.strafeStage > 0) {
                    this.strafeStage = ((Speed.mc.field_71439_g.field_191988_bg != 0.0f || Speed.mc.field_71439_g.field_70702_br != 0.0f) ? 1 : 0);
                    break;
                }
                break;
            }
            case 2: {
                final double jumpHeight = this.lowHop.getValue() ? 0.3999999463558197 : 0.40123128;
                if ((Speed.mc.field_71439_g.field_191988_bg != 0.0f || Speed.mc.field_71439_g.field_70702_br != 0.0f) && Speed.mc.field_71439_g.field_70122_E) {
                    event.setY(Speed.mc.field_71439_g.field_70181_x = jumpHeight);
                    this.horizontal *= 2.149;
                    break;
                }
                break;
            }
            case 3: {
                this.horizontal = this.ncpPrevMotion - 0.66 * (this.ncpPrevMotion - this.getEffectiveBaseSpeed());
                break;
            }
        }
        this.horizontal = Math.max(this.horizontal, this.getEffectiveBaseSpeed());
        float forward = Speed.mc.field_71439_g.field_71158_b.field_192832_b;
        float strafe = Speed.mc.field_71439_g.field_71158_b.field_78902_a;
        if (forward == 0.0f && strafe == 0.0f) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else if (forward != 0.0 && strafe != 0.0) {
            forward *= (float)Math.sin(0.7853981633974483);
            strafe *= (float)Math.cos(0.7853981633974483);
        }
        event.setX((forward * this.horizontal * -Math.sin(Math.toRadians(Speed.mc.field_71439_g.field_70177_z)) + strafe * this.horizontal * Math.cos(Math.toRadians(Speed.mc.field_71439_g.field_70177_z))) * speedMultiplier);
        event.setZ((forward * this.horizontal * Math.cos(Math.toRadians(Speed.mc.field_71439_g.field_70177_z)) - strafe * this.horizontal * -Math.sin(Math.toRadians(Speed.mc.field_71439_g.field_70177_z))) * speedMultiplier);
        ++this.strafeStage;
    }
    
    private void doRestrictedSpeed(final MoveEvent event, final double baseRestriction, final double actualRestriction, final boolean fast) {
        if (!MovementUtil.isMoving()) {
            event.setX(0.0);
            event.setZ(0.0);
            return;
        }
        if (fast && this.round(Speed.mc.field_71439_g.field_70163_u - (int)Speed.mc.field_71439_g.field_70163_u, 3) == this.round(0.138, 3)) {
            final EntityPlayerSP field_71439_g = Speed.mc.field_71439_g;
            field_71439_g.field_70181_x -= 0.08;
            event.setY(event.getY() - 0.09316090325960147);
            final EntityPlayerSP field_71439_g2 = Speed.mc.field_71439_g;
            field_71439_g2.field_70163_u -= 0.09316090325960147;
        }
        if (this.strafeStage == 1) {
            this.currentSpeed = (fast ? 1.38 : 1.35) * this.getEffectiveBaseSpeed() - 0.01;
        }
        else if (this.strafeStage == 2 && Speed.mc.field_71439_g.field_70122_E) {
            double jumpSpeed = 0.3999999463558197;
            if (!(boolean)this.lowHop.getValue()) {
                jumpSpeed = 0.41999998688697815;
            }
            if (Speed.mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
                final double amplifier = Speed.mc.field_71439_g.func_70660_b(MobEffects.field_76430_j).func_76458_c();
                jumpSpeed += (amplifier + 1.0) * 0.1;
            }
            event.setY(Speed.mc.field_71439_g.field_70181_x = jumpSpeed);
            this.currentSpeed *= 2.149;
            if ((boolean)this.boost.getValue() && BoostManager.INSTANCE != null) {
                this.currentSpeed += BoostManager.INSTANCE.getBoostSpeed(false);
            }
        }
        else if (this.strafeStage == 3) {
            final double decrease = 0.66 * (this.prevMotion - this.getEffectiveBaseSpeed());
            this.currentSpeed = this.prevMotion - decrease;
            if ((boolean)this.boost.getValue() && BoostManager.INSTANCE != null) {
                this.currentSpeed += BoostManager.INSTANCE.getBoostSpeed(false);
            }
        }
        else {
            final List<AxisAlignedBB> collisionBoxes = (List<AxisAlignedBB>)Speed.mc.field_71441_e.func_184144_a((Entity)Speed.mc.field_71439_g, Speed.mc.field_71439_g.func_174813_aQ().func_72317_d(0.0, Speed.mc.field_71439_g.field_70181_x, 0.0));
            if ((collisionBoxes.size() > 0 || Speed.mc.field_71439_g.field_70124_G) && this.strafeStage > 0) {
                this.strafeStage = (MovementUtil.isMoving() ? 1 : 0);
            }
            this.currentSpeed = this.prevMotion - this.prevMotion / 159.077;
        }
        this.currentSpeed = Math.max(this.currentSpeed, this.getEffectiveBaseSpeed());
        double maxSpeed = baseRestriction;
        double actualMax = actualRestriction;
        if (Speed.mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
            final double amplifier2 = Speed.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c).func_76458_c();
            maxSpeed = baseRestriction * (1.0 + 0.2 * (amplifier2 + 1.0));
            actualMax = actualRestriction * (1.0 + 0.2 * (amplifier2 + 1.0));
        }
        if (Speed.mc.field_71439_g.func_70644_a(MobEffects.field_76421_d)) {
            final double amplifier2 = Speed.mc.field_71439_g.func_70660_b(MobEffects.field_76421_d).func_76458_c();
            maxSpeed /= 1.0 + 0.2 * (amplifier2 + 1.0);
            actualMax /= 1.0 + 0.2 * (amplifier2 + 1.0);
        }
        this.currentSpeed = Math.min(this.currentSpeed, (this.aacCounter > 25) ? maxSpeed : actualMax);
        ++this.aacCounter;
        if (this.aacCounter > 50) {
            this.aacCounter = 0;
        }
        float forward = Speed.mc.field_71439_g.field_71158_b.field_192832_b;
        float strafe = Speed.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = Speed.mc.field_71439_g.field_70126_B + (Speed.mc.field_71439_g.field_70177_z - Speed.mc.field_71439_g.field_70126_B) * Speed.mc.func_184121_ak();
        if (Speed.mc.field_71439_g.func_70093_af()) {
            strafe = 0.0f;
        }
        if (!MovementUtil.isMoving()) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else if (forward != 0.0f) {
            if (strafe >= 1.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
                strafe = 0.0f;
            }
            else if (strafe <= -1.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
                strafe = 0.0f;
            }
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double cos = Math.cos(Math.toRadians(yaw));
        final double sin = -Math.sin(Math.toRadians(yaw));
        event.setX(forward * this.currentSpeed * sin + strafe * this.currentSpeed * cos);
        event.setZ(forward * this.currentSpeed * cos - strafe * this.currentSpeed * sin);
        ++this.strafeStage;
    }
    
    private void handleYPortSpeed() {
        if (!MovementUtil.isMoving() || Speed.mc.field_71439_g.func_70090_H() || Speed.mc.field_71439_g.func_180799_ab()) {
            return;
        }
        if (Speed.mc.field_71439_g.field_70122_E) {
            Speed.mc.field_71439_g.func_70664_aZ();
            final double[] dir = MovementUtil.strafe(MovementUtil.getSpeed() + (double)this.yportSpeed.getValue());
            Speed.mc.field_71439_g.field_70159_w = dir[0];
            Speed.mc.field_71439_g.field_70179_y = dir[1];
        }
        else {
            Speed.mc.field_71439_g.field_70181_x = -1.0;
        }
    }
    
    private boolean isStrafeSpeed() {
        final Mode mode = (Mode)this.speedMode.getValue();
        return mode == Mode.STRAFE || mode == Mode.STRAFE_STRICT || mode == Mode.STRICT_FAST;
    }
    
    private double getBaseMotionSpeed() {
        double baseSpeed = 0.2873;
        if (Speed.mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
            final int amplifier = Speed.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c).func_76458_c();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1.0);
        }
        if (Speed.mc.field_71439_g.func_70644_a(MobEffects.field_76421_d)) {
            final int amplifier = Speed.mc.field_71439_g.func_70660_b(MobEffects.field_76421_d).func_76458_c();
            baseSpeed /= 1.0 + 0.2 * (amplifier + 1.0);
        }
        return baseSpeed;
    }
    
    private double getBaseMotionSpeedNoPot() {
        return 0.2873;
    }
    
    private double getBaseMotionSpeedWithPotions() {
        double baseSpeed = 0.2873;
        if (this.potionEffects.getValue()) {
            if (Speed.mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
                final int amplifier = Speed.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c).func_76458_c();
                baseSpeed *= 1.0 + 0.2 * (amplifier + 1.0);
            }
            if (Speed.mc.field_71439_g.func_70644_a(MobEffects.field_76421_d)) {
                final int amplifier = Speed.mc.field_71439_g.func_70660_b(MobEffects.field_76421_d).func_76458_c();
                baseSpeed *= 1.0 - 0.15 * (amplifier + 1.0);
                baseSpeed = Math.max(baseSpeed, 0.05);
            }
        }
        return baseSpeed;
    }
    
    private double getEffectiveBaseSpeed() {
        return this.potionEffects.getValue() ? this.getBaseMotionSpeedWithPotions() : this.getBaseMotionSpeedNoPot();
    }
    
    private double round(final double value, final int places) {
        final double scale = Math.pow(10.0, places);
        return Math.round(value * scale) / scale;
    }
    
    public String getDisplayInfo() {
        return ((Mode)this.speedMode.getValue()).toString();
    }
    
    public enum Mode
    {
        NONE, 
        STRAFE, 
        STRAFE_STRICT, 
        STRICT_FAST;
    }
    
    public enum TimerMode
    {
        NONE, 
        TIMER_0888, 
        FAST;
    }
    
    public enum AlwaysMode
    {
        OLD, 
        NEW;
    }
}
