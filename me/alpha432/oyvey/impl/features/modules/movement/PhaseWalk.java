//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.movement;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraft.util.text.*;
import net.minecraft.network.*;
import net.minecraft.entity.*;
import net.minecraft.init.*;
import me.alpha432.oyvey.impl.features.modules.combat.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.impl.events.*;
import me.alpha432.oyvey.mixin.mixins.*;
import net.minecraft.network.play.server.*;
import net.minecraft.network.play.client.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.client.*;
import net.minecraft.util.math.*;
import java.util.*;

public class PhaseWalk extends Module
{
    Timer timer;
    Timer packetTimer;
    Setting<PhaseMode> phaseMode;
    Setting<Integer> delay;
    Setting<TeleportMode> handleTeleport;
    Setting<Boolean> onlyInBlock;
    Setting<InhibitMode> inhibit;
    Setting<SpeedMode> motion;
    Setting<Double> factor;
    Setting<Boolean> down;
    Setting<Boolean> noVoid;
    Setting<Boolean> boost;
    Setting<BoostMode> boostMode;
    Setting<LimitMode> limit;
    Setting<Integer> packetMax;
    Setting<Integer> resetDelay;
    public static PhaseWalk INSTANCE;
    private final Map<Integer, Vec3d> predictions;
    boolean extra;
    boolean dontTouchRots;
    int tpId;
    int packets;
    private boolean disabledFutureCA;
    
    public static PhaseWalk getInstance() {
        if (PhaseWalk.INSTANCE == null) {
            PhaseWalk.INSTANCE = new PhaseWalk();
        }
        return PhaseWalk.INSTANCE;
    }
    
    public PhaseWalk() {
        super("Phase", "Phase through blocks", Module.Category.MOVEMENT, true, false, false);
        this.timer = new Timer();
        this.packetTimer = new Timer();
        this.phaseMode = (Setting<PhaseMode>)this.register(new Setting("Phase", (Object)PhaseMode.CLIP));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (Object)200, (Object)0, (Object)1000));
        this.handleTeleport = (Setting<TeleportMode>)this.register(new Setting("HandleTeleport", (Object)TeleportMode.ALL));
        this.onlyInBlock = (Setting<Boolean>)this.register(new Setting("OnlyInBlock", (Object)false));
        this.inhibit = (Setting<InhibitMode>)this.register(new Setting("Inhibit", (Object)InhibitMode.NONE));
        this.motion = (Setting<SpeedMode>)this.register(new Setting("Speed", (Object)SpeedMode.SLOW));
        this.factor = (Setting<Double>)this.register(new Setting("Factor", (Object)1.0, (Object)0.0, (Object)5.0));
        this.down = (Setting<Boolean>)this.register(new Setting("Down", (Object)true));
        this.noVoid = (Setting<Boolean>)this.register(new Setting("AntiVoid", (Object)false));
        this.boost = (Setting<Boolean>)this.register(new Setting("Boost", (Object)false));
        this.boostMode = (Setting<BoostMode>)this.register(new Setting("BoostMode", (Object)BoostMode.NEW, v -> (boolean)this.boost.getValue()));
        this.limit = (Setting<LimitMode>)this.register(new Setting("Limit", (Object)LimitMode.NONE));
        this.packetMax = (Setting<Integer>)this.register(new Setting("MaxPackets", (Object)1, (Object)1, (Object)100));
        this.resetDelay = (Setting<Integer>)this.register(new Setting("Reset", (Object)300, (Object)1, (Object)1000));
        this.predictions = new HashMap<Integer, Vec3d>();
        this.extra = false;
        this.dontTouchRots = false;
        this.tpId = 0;
        this.packets = 0;
        this.disabledFutureCA = false;
        PhaseWalk.INSTANCE = this;
    }
    
    public void onEnable() {
        super.onEnable();
        if (PhaseWalk.mc.field_71439_g == null || PhaseWalk.mc.field_71441_e == null) {
            return;
        }
        this.timer.reset();
        this.packetTimer.reset();
        if (PhaseWalk.mc.field_71439_g.func_145748_c_().equals("DarkHours")) {
            PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new SPacketDisconnect((ITextComponent)new TextComponentString("nice iq robot go play grim")));
            this.disable();
        }
    }
    
    public void onUpdate() {
        if (PhaseWalk.mc.field_71439_g == null || PhaseWalk.mc.field_71441_e == null) {
            return;
        }
        if (LarpPacketFly.INSTANCE.isEnabled()) {
            return;
        }
        if (this.extra) {
            if (this.boostMode.getValue() == BoostMode.NEW) {
                this.invokeMovementTick();
            }
            this.extra = false;
        }
        if (this.packetTimer.passedMs((long)this.resetDelay.getValue())) {
            this.packets = 0;
            this.packetTimer.reset();
        }
    }
    
    private void invokeMovementTick() {
        if (PhaseWalk.mc.field_71439_g == null) {
            return;
        }
        final double motionX = PhaseWalk.mc.field_71439_g.field_70159_w;
        final double motionY = PhaseWalk.mc.field_71439_g.field_70181_x;
        final double motionZ = PhaseWalk.mc.field_71439_g.field_70179_y;
        PhaseWalk.mc.field_71439_g.func_70091_d(MoverType.SELF, motionX, motionY, motionZ);
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (PhaseWalk.mc.field_71439_g == null || PhaseWalk.mc.field_71441_e == null) {
            return;
        }
        if (LarpPacketFly.INSTANCE.isEnabled()) {
            return;
        }
        if (this.phaseMode.getValue() == PhaseMode.CLIP) {
            this.doPhase(event);
        }
        if (this.motion.getValue() != SpeedMode.SLOW) {
            if (!PhaseWalk.mc.field_71439_g.func_70090_H() && !PhaseWalk.mc.field_71439_g.func_180799_ab()) {
                final double baseSpeed = this.getBaseSpeed();
                if (this.motion.getValue() == SpeedMode.FACTOR && PhaseWalk.mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
                    final double[] dir = MovementUtil.directionSpeed(baseSpeed * (double)this.factor.getValue());
                    event.setX(dir[0]);
                    event.setZ(dir[1]);
                }
                else {
                    MovementUtil.strafe(event, baseSpeed);
                }
            }
            else if (PhaseWalk.mc.field_71439_g.field_71158_b.field_78902_a == 0.0f && PhaseWalk.mc.field_71439_g.field_71158_b.field_192832_b == 0.0f) {
                PhaseWalk.mc.field_71439_g.field_70159_w = 0.0;
                event.setX(PhaseWalk.mc.field_71439_g.field_70179_y = 0.0);
                event.setZ(0.0);
            }
        }
        if (this.phaseMode.getValue() == PhaseMode.SMOOTH) {
            this.doPhase(event);
        }
    }
    
    @SubscribeEvent
    public void onUpdatePhasing(final UpdateWalkingPlayerEvent event) {
        if (PhaseWalk.mc.field_71439_g == null || PhaseWalk.mc.field_71441_e == null) {
            return;
        }
        if (this.isPhasing() && FutureCA.getInstance().isEnabled() && !(boolean)FutureCA.getInstance().antiFuture.getValue()) {
            FutureCA.getInstance().disable();
            this.disabledFutureCA = true;
        }
        else if (!this.isPhasing() && this.disabledFutureCA) {
            FutureCA.getInstance().enable();
            this.disabledFutureCA = false;
        }
    }
    
    public void doPhase(final MoveEvent event) {
        if (this.limit.getValue() == LimitMode.PACKETS && this.packets > (int)this.packetMax.getValue()) {
            return;
        }
        switch ((PhaseMode)this.phaseMode.getValue()) {
            case CLIP: {
                boolean flag = false;
                if (this.shouldPacket() || (flag = ((boolean)this.down.getValue() && PhaseWalk.mc.field_71474_y.field_74311_E.func_151470_d() && this.isPhasing2()))) {
                    final double[] forward = this.getForward(0.001);
                    if (!flag || !(boolean)this.noVoid.getValue() || PhaseWalk.mc.field_71439_g.field_70163_u > 1.0) {
                        if (flag && !PhaseWalk.mc.field_71439_g.field_70122_E) {
                            return;
                        }
                        if (this.timer.passedMs((long)this.delay.getValue())) {
                            this.sendPackets(PhaseWalk.mc.field_71439_g.field_70165_t + forward[0], PhaseWalk.mc.field_71439_g.field_70163_u + this.getUpMovement(), PhaseWalk.mc.field_71439_g.field_70161_v + forward[1]);
                            this.timer.reset();
                            this.extra = (boolean)this.boost.getValue();
                        }
                    }
                    break;
                }
                break;
            }
            case SMOOTH: {
                if (!this.isInBlock()) {
                    break;
                }
                final List<BlockPos> currentIntersections = this.getMovementStoppingBlocksInPlayer(new Vec3d(0.0, 0.0, 0.0));
                final List<BlockPos> intersections = this.getMovementStoppingBlocksInPlayer(new Vec3d(event.getX(), this.getUpMovement(), event.getZ()));
                boolean changedBlock = false;
                for (final BlockPos pos : intersections) {
                    if (!currentIntersections.contains(pos)) {
                        changedBlock = true;
                        break;
                    }
                }
                if (changedBlock && this.timer.passedMs((long)this.delay.getValue())) {
                    final double[] forward2 = this.getForward(0.001);
                    this.sendPackets(PhaseWalk.mc.field_71439_g.field_70165_t + forward2[0], PhaseWalk.mc.field_71439_g.field_70163_u + this.getUpMovement(), PhaseWalk.mc.field_71439_g.field_70161_v + forward2[1]);
                    event.setCanceled(true);
                    this.timer.reset();
                    this.extra = (boolean)this.boost.getValue();
                    break;
                }
                break;
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onUpdateWalkingPlayer(final UpdateWalkingPlayerEvent event) {
        if (PhaseWalk.mc.field_71439_g == null || PhaseWalk.mc.field_71441_e == null) {
            return;
        }
        if (LarpPacketFly.INSTANCE.isEnabled()) {
            return;
        }
        this.dontTouchRots = false;
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent.Send event) {
        if (PhaseWalk.mc.field_71439_g == null || PhaseWalk.mc.field_71441_e == null) {
            return;
        }
        if (LarpPacketFly.INSTANCE.isEnabled()) {
            return;
        }
        if ((this.inhibit.getValue() == InhibitMode.BOTH || this.inhibit.getValue() == InhibitMode.ROTATE) && this.isInBlock()) {
            if (event.getPacket() instanceof CPacketPlayer.Rotation && !this.dontTouchRots) {
                event.setCanceled(true);
            }
            if (event.getPacket() instanceof CPacketPlayer.PositionRotation && !this.dontTouchRots) {
                event.setCanceled(true);
                final ICPacketPlayer packet = (ICPacketPlayer)event.getPacket();
                PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(packet.getX(), packet.getY(), packet.getZ(), ((CPacketPlayer.PositionRotation)event.getPacket()).func_149465_i()));
            }
        }
        if ((this.inhibit.getValue() == InhibitMode.BOTH || this.inhibit.getValue() == InhibitMode.NECESSARY) && event.getPacket() instanceof CPacketEntityAction) {
            final CPacketEntityAction packet2 = (CPacketEntityAction)event.getPacket();
            if (packet2.func_180764_b().equals((Object)CPacketEntityAction.Action.START_SPRINTING)) {
                event.setCanceled(true);
            }
            else if (packet2.func_180764_b().equals((Object)CPacketEntityAction.Action.STOP_SPRINTING)) {
                event.setCanceled(true);
            }
        }
        if (!event.isCanceled()) {
            ++this.packets;
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (PhaseWalk.mc.field_71439_g == null || PhaseWalk.mc.field_71441_e == null) {
            return;
        }
        if (LarpPacketFly.INSTANCE.isEnabled()) {
            return;
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            if (this.handleTeleport.getValue() == TeleportMode.PREDICT) {
                final SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
                final Vec3d prediction = this.predictions.get(packet.func_186965_f());
                if (prediction != null && prediction.field_72450_a == packet.func_148932_c() && prediction.field_72448_b == packet.func_148928_d() && prediction.field_72449_c == packet.func_148933_e()) {
                    event.setCanceled(true);
                    this.predictions.remove(packet.func_186965_f());
                    PhaseWalk.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketConfirmTeleport(packet.func_186965_f()));
                    return;
                }
                this.tpId = packet.func_186965_f();
            }
            else {
                if (this.handleTeleport.getValue() == TeleportMode.ALL) {
                    PhaseWalk.mc.func_147114_u().func_147297_a((Packet)new CPacketConfirmTeleport(this.tpId - 1));
                    PhaseWalk.mc.func_147114_u().func_147297_a((Packet)new CPacketConfirmTeleport(this.tpId));
                    PhaseWalk.mc.func_147114_u().func_147297_a((Packet)new CPacketConfirmTeleport(this.tpId + 1));
                }
                if (this.handleTeleport.getValue() == TeleportMode.ABOVE) {
                    PhaseWalk.mc.func_147114_u().func_147297_a((Packet)new CPacketConfirmTeleport(this.tpId + 1));
                }
            }
        }
    }
    
    double getUpMovement() {
        if ((boolean)this.noVoid.getValue() && PhaseWalk.mc.field_71439_g.field_70163_u <= 1.0) {
            return (PhaseWalk.mc.field_71474_y.field_74314_A.func_151470_d() ? 1 : 0) * this.getSpeed();
        }
        return (PhaseWalk.mc.field_71474_y.field_74314_A.func_151470_d() ? 1 : (PhaseWalk.mc.field_71474_y.field_74311_E.func_151470_d() ? -1 : 0)) * this.getSpeed();
    }
    
    public void sendPackets(final double x, final double y, final double z) {
        PhaseWalk.mc.func_147114_u().func_147297_a((Packet)new CPacketPlayer.Position(x, y, z, PhaseWalk.mc.field_71439_g.field_70122_E));
        PhaseWalk.mc.func_147114_u().func_147297_a((Packet)new CPacketPlayer.Position(PhaseWalk.mc.field_71439_g.field_70165_t, PhaseWalk.mc.field_71439_g.field_70163_u - 85.0, PhaseWalk.mc.field_71439_g.field_70161_v, PhaseWalk.mc.field_71439_g.field_70122_E));
        if (this.handleTeleport.getValue() == TeleportMode.PREDICT) {
            PhaseWalk.mc.field_71439_g.func_70107_b(x, y, z);
            this.predictions.put(++this.tpId, new Vec3d(x, y, z));
            PhaseWalk.mc.func_147114_u().func_147297_a((Packet)new CPacketConfirmTeleport(this.tpId));
        }
    }
    
    public void onDisable() {
        super.onDisable();
        this.predictions.clear();
        if (this.disabledFutureCA) {
            FutureCA.getInstance().enable();
            this.disabledFutureCA = false;
        }
    }
    
    double getSpeed() {
        return this.getBaseSpeed() / 10.0;
    }
    
    double getBaseSpeed() {
        return 0.2873;
    }
    
    double[] getForward(final double speed) {
        final float yaw = PhaseWalk.mc.field_71439_g.field_70177_z;
        final double sin = -Math.sin(Math.toRadians(yaw));
        final double cos = Math.cos(Math.toRadians(yaw));
        return new double[] { speed * sin, speed * cos };
    }
    
    boolean shouldPacket() {
        return PhaseWalk.mc.field_71439_g.field_70123_F && (!(boolean)this.onlyInBlock.getValue() || this.isPhasing());
    }
    
    boolean isInBlock() {
        return !(boolean)this.onlyInBlock.getValue() || this.isPhasing();
    }
    
    public boolean isPhasing() {
        if (getInstance().isEnabled() && EntityUtil.isMoving()) {
            final AxisAlignedBB bb = Minecraft.func_71410_x().field_71439_g.func_174813_aQ();
            for (int x = MathHelper.func_76128_c(bb.field_72340_a); x < MathHelper.func_76128_c(bb.field_72336_d) + 1; ++x) {
                for (int y = MathHelper.func_76128_c(bb.field_72338_b); y < MathHelper.func_76128_c(bb.field_72337_e) + 1; ++y) {
                    for (int z = MathHelper.func_76128_c(bb.field_72339_c); z < MathHelper.func_76128_c(bb.field_72334_f) + 1; ++z) {
                        if (Minecraft.func_71410_x().field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_185904_a().func_76230_c() && bb.func_72326_a(new AxisAlignedBB((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1)))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isPhasing2() {
        final AxisAlignedBB bb = Minecraft.func_71410_x().field_71439_g.func_174813_aQ();
        for (int x = MathHelper.func_76128_c(bb.field_72340_a); x < MathHelper.func_76128_c(bb.field_72336_d) + 1; ++x) {
            for (int y = MathHelper.func_76128_c(bb.field_72338_b); y < MathHelper.func_76128_c(bb.field_72337_e) + 1; ++y) {
                for (int z = MathHelper.func_76128_c(bb.field_72339_c); z < MathHelper.func_76128_c(bb.field_72334_f) + 1; ++z) {
                    if (Minecraft.func_71410_x().field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_185904_a().func_76230_c() && bb.func_72326_a(new AxisAlignedBB((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1)))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public List<BlockPos> getMovementStoppingBlocksInPlayer() {
        final List<BlockPos> intersections = new ArrayList<BlockPos>();
        final AxisAlignedBB bb = Minecraft.func_71410_x().field_71439_g.func_174813_aQ();
        for (int x = MathHelper.func_76128_c(bb.field_72340_a); x < MathHelper.func_76128_c(bb.field_72336_d) + 1; ++x) {
            for (int y = MathHelper.func_76128_c(bb.field_72338_b); y < MathHelper.func_76128_c(bb.field_72337_e) + 1; ++y) {
                for (int z = MathHelper.func_76128_c(bb.field_72339_c); z < MathHelper.func_76128_c(bb.field_72334_f) + 1; ++z) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    if (Minecraft.func_71410_x().field_71441_e.func_180495_p(pos).func_185904_a().func_76230_c() && bb.func_72326_a(new AxisAlignedBB((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1))) && !intersections.contains(pos)) {
                        intersections.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        return intersections;
    }
    
    public List<BlockPos> getMovementStoppingBlocksInPlayer(final Vec3d offset) {
        final List<BlockPos> intersections = new ArrayList<BlockPos>();
        final AxisAlignedBB bb = Minecraft.func_71410_x().field_71439_g.func_174813_aQ().func_191194_a(offset);
        for (int x = MathHelper.func_76128_c(bb.field_72340_a); x < MathHelper.func_76128_c(bb.field_72336_d) + 1; ++x) {
            for (int y = MathHelper.func_76128_c(bb.field_72338_b); y < MathHelper.func_76128_c(bb.field_72337_e) + 1; ++y) {
                for (int z = MathHelper.func_76128_c(bb.field_72339_c); z < MathHelper.func_76128_c(bb.field_72334_f) + 1; ++z) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    if (Minecraft.func_71410_x().field_71441_e.func_180495_p(pos).func_185904_a().func_76230_c() && bb.func_72326_a(new AxisAlignedBB((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1))) && !intersections.contains(pos)) {
                        intersections.add(new BlockPos(x, y, z));
                    }
                }
            }
        }
        return intersections;
    }
    
    public String getDisplayInfo() {
        return ((SpeedMode)this.motion.getValue()).toString();
    }
    
    static {
        PhaseWalk.INSTANCE = new PhaseWalk();
    }
    
    public enum PhaseMode
    {
        CLIP, 
        SMOOTH;
    }
    
    public enum TeleportMode
    {
        ALL, 
        ABOVE, 
        PREDICT, 
        NONE;
    }
    
    public enum InhibitMode
    {
        NONE, 
        ROTATE, 
        NECESSARY, 
        BOTH;
    }
    
    public enum SpeedMode
    {
        SLOW, 
        SOFT, 
        FACTOR;
    }
    
    public enum LimitMode
    {
        NONE, 
        PACKETS;
    }
    
    public enum BoostMode
    {
        OLD, 
        NEW;
    }
}
