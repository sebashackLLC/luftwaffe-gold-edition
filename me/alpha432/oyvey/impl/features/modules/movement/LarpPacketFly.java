//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.movement;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraft.util.math.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.network.*;
import net.minecraft.entity.*;
import net.minecraft.network.play.client.*;
import io.netty.util.concurrent.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.network.play.server.*;
import me.alpha432.oyvey.mixin.mixins.*;

public class LarpPacketFly extends Module
{
    public static LarpPacketFly INSTANCE;
    Setting<FlyMode> mode;
    Setting<Double> factor;
    Setting<PhaseMode> phase;
    Setting<BoundsMode> bounds;
    Setting<Boolean> antiKick;
    Setting<Boolean> phased;
    Setting<LimitMode> limit;
    Setting<Boolean> dupe;
    private final Map<Integer, Vec3d> predictions;
    private int tpId;
    private int lagTime;
    private static final Random random;
    private static final double CONCEAL = 0.0624;
    private static final double MOVE_FACTOR;
    float factorBuffer;
    private static final Random rand;
    
    public LarpPacketFly() {
        super("PacketFly", "Packet-based flight", Module.Category.MOVEMENT, true, false, false);
        this.mode = (Setting<FlyMode>)this.register(new Setting("Mode", (Object)FlyMode.FACTORIZE));
        this.factor = (Setting<Double>)this.register(new Setting("Factor", (Object)1.5, (Object)0.1, (Object)4.0));
        this.phase = (Setting<PhaseMode>)this.register(new Setting("Phase", (Object)PhaseMode.FULL));
        this.bounds = (Setting<BoundsMode>)this.register(new Setting("Bounds", (Object)BoundsMode.UP));
        this.antiKick = (Setting<Boolean>)this.register(new Setting("AntiKick", (Object)true));
        this.phased = (Setting<Boolean>)this.register(new Setting("Phased", (Object)true));
        this.limit = (Setting<LimitMode>)this.register(new Setting("Limit", (Object)LimitMode.NONE));
        this.dupe = (Setting<Boolean>)this.register(new Setting("Confirm", (Object)false));
        this.predictions = new HashMap<Integer, Vec3d>();
        this.tpId = 0;
        this.lagTime = 0;
        this.factorBuffer = 0.0f;
        LarpPacketFly.INSTANCE = this;
    }
    
    public static LarpPacketFly getInstance() {
        if (LarpPacketFly.INSTANCE == null) {
            LarpPacketFly.INSTANCE = new LarpPacketFly();
        }
        return LarpPacketFly.INSTANCE;
    }
    
    private void setInstance() {
        LarpPacketFly.INSTANCE = this;
    }
    
    public void onDisable() {
        super.onDisable();
        if (LarpPacketFly.mc.field_71439_g == null || LarpPacketFly.mc.field_71441_e == null) {
            return;
        }
    }
    
    public void onEnable() {
        super.onEnable();
        if (LarpPacketFly.mc.field_71439_g == null || LarpPacketFly.mc.field_71441_e == null) {
            return;
        }
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (LarpPacketFly.mc.field_71439_g == null || LarpPacketFly.mc.field_71441_e == null) {
            return;
        }
        int loops;
        if (this.mode.getValue() == FlyMode.FACTORIZE && (LarpPacketFly.mc.field_71439_g.field_70173_aa % 3 == 0 || (this.limit.getValue() != LimitMode.TICKS && this.limit.getValue() != LimitMode.ALL))) {
            final float rawFactor = ((Double)this.factor.getValue()).floatValue();
            loops = (int)Math.floor(rawFactor);
            final float extraFactor = rawFactor - loops;
            this.factorBuffer -= 0.1f;
            if (this.factorBuffer <= extraFactor) {
                this.factorBuffer = 1.0f;
                ++loops;
            }
            if (this.bounds.getValue() == BoundsMode.SOFT && this.canJitter()) {
                loops = 1;
                if (this.limit.getValue() == LimitMode.STRICT && LarpPacketFly.mc.field_71439_g.field_70173_aa % 3 == 0) {
                    loops = 0;
                }
            }
        }
        else {
            loops = 1;
            if ((this.mode.getValue() == FlyMode.FAST || (this.limit.getValue() == LimitMode.STRICT && this.canJitter())) && (this.limit.getValue() == LimitMode.TICKS || this.limit.getValue() == LimitMode.ALL || (this.limit.getValue() == LimitMode.STRICT && this.canJitter())) && LarpPacketFly.mc.field_71439_g.field_70173_aa % 3 == 0) {
                loops = 0;
            }
        }
        double moveSpeed = (!(boolean)this.phased.getValue() && --this.lagTime <= 0 && !this.isPhased()) ? 0.2873 : 0.0624;
        double motionY = 0.0;
        boolean doAntiKick = false;
        if (LarpPacketFly.mc.field_71474_y.field_74314_A.func_151470_d()) {
            if (this.limit.getValue() != LimitMode.AXIS && this.limit.getValue() != LimitMode.ALL) {
                motionY = 0.0624;
                if (this.isMoving()) {
                    moveSpeed *= LarpPacketFly.MOVE_FACTOR;
                    motionY *= LarpPacketFly.MOVE_FACTOR;
                }
            }
            else if (this.isMoving()) {
                if (LarpPacketFly.mc.field_71439_g.field_70173_aa % 2 == 0) {
                    motionY = 0.0624;
                    moveSpeed = 0.0;
                }
                else {
                    moveSpeed *= LarpPacketFly.MOVE_FACTOR;
                }
            }
            else {
                motionY = 0.0624;
            }
            doAntiKick = ((boolean)this.antiKick.getValue() && LarpPacketFly.mc.field_71439_g.field_70173_aa % 20 == 0 && !this.isPhased() && !LarpPacketFly.mc.field_71441_e.func_184143_b(LarpPacketFly.mc.field_71439_g.func_174813_aQ()));
            if (doAntiKick) {
                loops = 1;
                motionY = -0.04;
            }
        }
        else if (LarpPacketFly.mc.field_71474_y.field_74311_E.func_151470_d()) {
            if (this.limit.getValue() != LimitMode.ALL && this.limit.getValue() != LimitMode.AXIS) {
                motionY = -0.0624;
                if (this.isMoving()) {
                    moveSpeed *= LarpPacketFly.MOVE_FACTOR;
                    motionY *= LarpPacketFly.MOVE_FACTOR;
                }
            }
            else if (this.isMoving()) {
                if (LarpPacketFly.mc.field_71439_g.field_70173_aa % 2 == 0) {
                    motionY = -0.0624;
                    moveSpeed = 0.0;
                }
                else {
                    moveSpeed *= LarpPacketFly.MOVE_FACTOR;
                }
            }
            else {
                motionY = -0.0624;
            }
        }
        else {
            doAntiKick = ((boolean)this.antiKick.getValue() && LarpPacketFly.mc.field_71439_g.field_70173_aa % 20 == 0 && !this.isPhased() && !LarpPacketFly.mc.field_71441_e.func_184143_b(LarpPacketFly.mc.field_71439_g.func_174813_aQ()));
            if (doAntiKick) {
                loops = 1;
                motionY = -0.04;
            }
        }
        this.sendMovePackets(loops, moveSpeed, motionY, doAntiKick);
        event.setX(LarpPacketFly.mc.field_71439_g.field_70159_w);
        event.setY(LarpPacketFly.mc.field_71439_g.field_70181_x);
        event.setZ(LarpPacketFly.mc.field_71439_g.field_70179_y);
        this.doPhase();
    }
    
    boolean canJitter() {
        return this.isPhased() || LarpPacketFly.mc.field_71439_g.field_70123_F || LarpPacketFly.mc.field_71439_g.field_70124_G;
    }
    
    public void doPhase() {
        switch ((PhaseMode)this.phase.getValue()) {
            case FULL: {
                if (!LarpPacketFly.mc.field_71439_g.field_70145_X) {
                    LarpPacketFly.mc.field_71439_g.field_70145_X = true;
                    break;
                }
                break;
            }
            case SEMI: {
                if (this.isPhased() || LarpPacketFly.mc.field_71439_g.field_70124_G) {
                    LarpPacketFly.mc.field_71439_g.field_70145_X = true;
                    break;
                }
                if (LarpPacketFly.mc.field_71439_g.field_70145_X) {
                    LarpPacketFly.mc.field_71439_g.field_70145_X = false;
                    break;
                }
                break;
            }
            case NONE: {
                if (LarpPacketFly.mc.field_71439_g.field_70145_X) {
                    LarpPacketFly.mc.field_71439_g.field_70145_X = false;
                    break;
                }
                break;
            }
        }
    }
    
    private void sendMovePackets(final int loops, final double moveSpeed, final double motionY, final boolean antiKick) {
        if (loops == 0) {
            LarpPacketFly.mc.field_71439_g.func_70016_h(0.0, 0.0, 0.0);
        }
        else {
            final double[] strafe = this.getMoveSpeed(moveSpeed);
            for (int i = 1; i < loops + 1; ++i) {
                final double motionX = strafe[0] * i;
                final double motionZ = strafe[1] * i;
                double velY = motionY;
                if (!antiKick) {
                    velY = motionY * i;
                }
                LarpPacketFly.mc.field_71439_g.field_70159_w = motionX;
                LarpPacketFly.mc.field_71439_g.field_70181_x = velY;
                LarpPacketFly.mc.field_71439_g.field_70179_y = motionZ;
                final Vec3d posVec = LarpPacketFly.mc.field_71439_g.func_174791_d();
                final Vec3d moveVec = posVec.func_72441_c(motionX, velY, motionZ);
                this.sendMovePackets(moveVec);
                if (this.bounds.getValue() == BoundsMode.SOFT && this.canJitter()) {
                    this.sendMovePackets(new Vec3d(posVec.field_72450_a + motionX, posVec.field_72448_b + randomBounds(), posVec.field_72449_c + motionZ));
                }
                else {
                    this.sendMovePackets(this.modify(posVec));
                }
                this.doPrediction(moveVec);
            }
        }
    }
    
    public void doPrediction(final Vec3d moveVec) {
        if (this.tpId != 0 && this.mode.getValue() != FlyMode.LAGBACK) {
            this.predictions.put(++this.tpId, moveVec);
            LarpPacketFly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(this.tpId));
        }
    }
    
    private boolean isPhased() {
        return !LarpPacketFly.mc.field_71441_e.func_184144_a((Entity)LarpPacketFly.mc.field_71439_g, LarpPacketFly.mc.field_71439_g.func_174813_aQ().func_186664_h(0.0625)).isEmpty();
    }
    
    public static double randomLimitedHorizontal() {
        final int randomValue = LarpPacketFly.random.nextInt(10);
        return LarpPacketFly.random.nextBoolean() ? randomValue : ((double)(-randomValue));
    }
    
    public Vec3d modify(final Vec3d in) {
        return new Vec3d(in.field_72450_a, this.getBounds(in), in.field_72449_c);
    }
    
    public static double randomBounds() {
        final int randomValue = LarpPacketFly.random.nextInt(22) + 70;
        return LarpPacketFly.random.nextBoolean() ? randomValue : ((double)(-randomValue));
    }
    
    public double getBounds(final Vec3d in) {
        switch ((BoundsMode)this.bounds.getValue()) {
            case UP: {
                return in.field_72448_b + 1337.0;
            }
            case DOWN: {
                return in.field_72448_b - 1337.0;
            }
            case PRESERVE: {
                final int n = LarpPacketFly.rand.nextInt(29000000);
                if (LarpPacketFly.rand.nextBoolean()) {
                    return n;
                }
                return in.field_72448_b + -n;
            }
            case LIMIT: {
                final int j = LarpPacketFly.rand.nextInt(22) + 70;
                return in.field_72448_b + j;
            }
            case SOFT:
            case SNAP: {
                return 0.0;
            }
            default: {
                return in.field_72448_b - 1337.0;
            }
        }
    }
    
    public void sendMovePackets(final Vec3d vec) {
        ((INetworkManager)LarpPacketFly.mc.field_71439_g.field_71174_a.func_147298_b()).distpatchNow((Packet<?>)new CPacketPlayer.Position(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c, true), null);
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (LarpPacketFly.mc.field_71439_g == null || LarpPacketFly.mc.field_71441_e == null) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayer) {
            event.setCanceled(true);
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (LarpPacketFly.mc.field_71439_g == null || LarpPacketFly.mc.field_71441_e == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            final Vec3d prediction = this.predictions.get(packet.func_186965_f());
            if (prediction != null && prediction.field_72450_a == packet.func_148932_c() && prediction.field_72448_b == packet.func_148928_d() && prediction.field_72449_c == packet.func_148933_e()) {
                if (this.mode.getValue() != FlyMode.LAGBACK) {
                    event.setCanceled(true);
                }
                if (this.dupe.getValue()) {
                    LarpPacketFly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(packet.func_186965_f()));
                }
                this.predictions.remove(packet.func_186965_f());
                return;
            }
            ((ISPacketPlayerPosLook)packet).setYaw(LarpPacketFly.mc.field_71439_g.field_70177_z);
            ((ISPacketPlayerPosLook)packet).setPitch(LarpPacketFly.mc.field_71439_g.field_70125_A);
            LarpPacketFly.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(packet.func_186965_f()));
            this.lagTime = 10;
            this.tpId = packet.func_186965_f();
        }
    }
    
    private boolean isMoving() {
        return LarpPacketFly.mc.field_71439_g.field_191988_bg != 0.0f || LarpPacketFly.mc.field_71439_g.field_70702_br != 0.0f;
    }
    
    private double[] getMoveSpeed(final double speed) {
        float forward = LarpPacketFly.mc.field_71439_g.field_71158_b.field_192832_b;
        float strafe = LarpPacketFly.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = LarpPacketFly.mc.field_71439_g.field_70177_z;
        if (forward == 0.0f && strafe == 0.0f) {
            return new double[] { 0.0, 0.0 };
        }
        if (forward != 0.0f) {
            if (strafe > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (strafe < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            strafe = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + strafe * speed * sin;
        final double posZ = forward * speed * sin - strafe * speed * cos;
        return new double[] { posX, posZ };
    }
    
    public String getDisplayInfo() {
        return ((FlyMode)this.mode.getValue()).toString();
    }
    
    static {
        LarpPacketFly.INSTANCE = new LarpPacketFly();
        random = new Random();
        MOVE_FACTOR = 1.0 / StrictMath.sqrt(2.0);
        rand = new Random();
    }
    
    public enum FlyMode
    {
        FACTORIZE, 
        FAST, 
        LAGBACK;
    }
    
    public enum PhaseMode
    {
        FULL, 
        SEMI, 
        NONE;
    }
    
    public enum BoundsMode
    {
        UP, 
        DOWN, 
        PRESERVE, 
        LIMIT, 
        SOFT, 
        SNAP;
    }
    
    public enum LimitMode
    {
        NONE, 
        AXIS, 
        TICKS, 
        STRICT, 
        ALL;
    }
}
