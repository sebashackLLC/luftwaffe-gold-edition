//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.movement;

import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.util.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.mixin.mixins.*;
import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.impl.command.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import net.minecraft.util.text.*;
import net.minecraft.entity.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.init.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.network.*;
import net.minecraft.util.math.*;

public class HoleSnap extends Module
{
    private static HoleSnap INSTANCE;
    public Setting<Boolean> jump;
    public Setting<Boolean> safety;
    private final Setting<Integer> range;
    public Setting<Float> stepHeight;
    private final Setting<StepMode> stepMode;
    Setting<Boolean> enableTimer;
    Setting<Float> timerVal;
    Setting<Integer> ticksVal;
    private HoleSnapUtil.Hole hole;
    private int stuckTime;
    private int boosted;
    private final Timer stepTimer;
    private final ITimer timer;
    private float oldTimer;
    boolean instant;
    boolean onlyMovement;
    boolean canTimer;
    int tick;
    int usedTicks;
    
    public HoleSnap() {
        super("Holesnap", "Snaps into the closest hole", Module.Category.MOVEMENT, true, false, false);
        this.jump = (Setting<Boolean>)new Setting("Jump", (Object)false);
        this.safety = (Setting<Boolean>)new Setting("Safety", (Object)true);
        this.range = (Setting<Integer>)new Setting("Range", (Object)4, (Object)1, (Object)6);
        this.stepHeight = (Setting<Float>)new Setting("Step Height", (Object)2.0f, (Object)0.8f, (Object)2.0f, v -> this.stepMode.getValue() != StepMode.None);
        this.stepMode = (Setting<StepMode>)new Setting("Step Mode", (Object)StepMode.Future);
        this.enableTimer = (Setting<Boolean>)this.register(new Setting("Timer", (Object)true));
        this.timerVal = (Setting<Float>)this.register(new Setting("Timer", (Object)2.5f, (Object)1.0f, (Object)13.0f));
        this.ticksVal = (Setting<Integer>)new Setting("Ticks", (Object)100, (Object)1, (Object)100);
        this.instant = true;
        this.onlyMovement = false;
        this.canTimer = false;
        this.tick = 0;
        this.usedTicks = 0;
        this.register((Setting)this.jump);
        this.register((Setting)this.safety);
        this.register((Setting)this.range);
        this.register((Setting)this.stepHeight);
        this.register((Setting)this.stepMode);
        this.stuckTime = 0;
        this.boosted = 0;
        this.stepTimer = new Timer();
        this.timer = (ITimer)((IMinecraft)HoleSnap.mc).getTimer();
    }
    
    public static HoleSnap getInstance() {
        if (HoleSnap.INSTANCE == null) {
            HoleSnap.INSTANCE = new HoleSnap();
        }
        return HoleSnap.INSTANCE;
    }
    
    private void setInstance() {
        HoleSnap.INSTANCE = this;
    }
    
    public void onEnable() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (HoleSnap.mc.field_71439_g.func_175149_v()) {
            Command.sendSilentMessage(OyVey.commandManager.getClientMessage() + ChatFormatting.GRAY + " Unable to HoleSnap player in spectator mode.");
            this.disable();
            return;
        }
        this.stuckTime = 0;
        this.boosted = 0;
        this.oldTimer = this.timer.getTickLength();
        this.hole = HoleSnapUtil.getTargetHoleVec3D((double)(int)this.range.getValue());
        if (this.hole == null || ((boolean)this.safety.getValue() && !this.isSafeHole(this.hole))) {
            Command.sendSilentMessage(OyVey.commandManager.getClientMessage() + ChatFormatting.GRAY + " No holes in range.");
            this.disable();
            return;
        }
        if ((boolean)this.jump.getValue() && HoleSnap.mc.field_71439_g.field_70122_E) {
            HoleSnap.mc.field_71439_g.func_70664_aZ();
        }
        if (this.stepMode.getValue() == StepMode.Future) {
            HoleSnap.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "t step");
        }
        this.canTimer = false;
        this.usedTicks = 0;
        if (this.instant) {
            this.tick = (int)this.ticksVal.getValue();
        }
        else {
            this.tick = 0;
        }
    }
    
    public void onDisable() {
        if (Feature.fullNullCheck()) {
            return;
        }
        HoleSnap.mc.field_71439_g.field_70138_W = 0.6f;
        if (this.stepMode.getValue() == StepMode.Future) {
            HoleSnap.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "t step");
        }
        if (!this.onlyMovement) {
            HoleSnap.mc.field_71428_T.field_194149_e = 50.0f;
        }
        this.usedTicks = 0;
    }
    
    public void onUpdate() {
        if (Feature.fullNullCheck()) {
            return;
        }
        if (HoleSnap.mc.field_71439_g.func_70090_H()) {
            Command.sendSilentMessage(OyVey.commandManager.getClientMessage() + TextFormatting.RED + " Unable to HoleSnap in liquid!");
            this.disable();
        }
        if (this.enableTimer.getValue()) {
            ++this.usedTicks;
            if (this.instant && this.usedTicks >= (int)this.ticksVal.getValue()) {
                return;
            }
            if (this.tick <= 0) {
                this.tick = 0;
                this.canTimer = false;
                if (!this.onlyMovement) {
                    HoleSnap.mc.field_71428_T.field_194149_e = 50.0f;
                }
            }
            if (this.tick > 0 && (this.instant || EntityUtil.isEntityMoving((Entity)HoleSnap.mc.field_71439_g))) {
                --this.tick;
                if (!this.onlyMovement) {
                    HoleSnap.mc.field_71428_T.field_194149_e = 50.0f / (float)this.timerVal.getValue();
                }
            }
            if (!this.instant && !EntityUtil.isEntityMoving((Entity)HoleSnap.mc.field_71439_g)) {
                ++this.tick;
            }
            if (this.tick > (int)this.ticksVal.getValue()) {
                this.tick = (int)this.ticksVal.getValue();
            }
        }
    }
    
    public void onPacketSend(final PacketEvent.Send event) {
        if ((boolean)this.enableTimer.getValue() && event.getPacket() instanceof CPacketPlayer) {
            --this.tick;
            if (this.tick <= 0) {
                this.tick = 0;
            }
        }
    }
    
    @SubscribeEvent
    public void onLagback(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            Command.sendSilentMessage(OyVey.commandManager.getClientMessage() + TextFormatting.RED + " Rubberband detected, disabling.");
            this.disable();
        }
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (fullNullCheck()) {
            return;
        }
        if (HoleSnapUtil.isHole(HoleSnapUtil.getPlayerPos())) {
            this.disable();
            return;
        }
        if (this.hole != null && HoleSnap.mc.field_71441_e.func_180495_p(this.hole.pos1).func_177230_c() == Blocks.field_150350_a) {
            final double tolerance = 0.1;
            Vec3d targetPos;
            if (this.hole.getPos2() != null) {
                targetPos = this.getCenterOfDoubleHole(this.hole);
                if (HoleSnap.mc.field_71439_g.func_174791_d().func_72438_d(targetPos) < 0.1) {
                    this.disable();
                    return;
                }
            }
            else {
                final BlockPos pos = this.hole.pos1;
                targetPos = new Vec3d(pos.func_177958_n() + 0.5, HoleSnap.mc.field_71439_g.field_70163_u, pos.func_177952_p() + 0.5);
            }
            final Vec3d playerPos = HoleSnap.mc.field_71439_g.func_174791_d();
            final double yawRad = Math.toRadians(this.getRotationTo(targetPos, playerPos).field_189982_i);
            final double dist = playerPos.func_72438_d(targetPos);
            final double speed = HoleSnap.mc.field_71439_g.field_70122_E ? Math.min(0.2805, dist / 2.0) : (EntityUtil.getDefaultMoveSpeed() - 0.02);
            event.setX(-Math.sin(yawRad) * speed);
            event.setZ(Math.cos(yawRad) * speed);
            if (HoleSnap.mc.field_71439_g.field_70123_F && HoleSnap.mc.field_71439_g.field_70122_E) {
                ++this.stuckTime;
                if (this.stepMode.getValue() != StepMode.None && this.stepTimer.passedMs(200L)) {
                    HoleSnap.mc.field_71439_g.field_70138_W = (float)this.stepHeight.getValue();
                }
                if (((StepMode)this.stepMode.getValue()).equals(StepMode.NCP)) {
                    final double sHeight = HoleSnap.mc.field_71439_g.func_174813_aQ().field_72338_b - HoleSnap.mc.field_71439_g.field_70163_u;
                    if (sHeight <= 0.0 || sHeight > (float)this.stepHeight.getValue()) {
                        return;
                    }
                    final double[] offsets = PlayerUtil.stepOffset(sHeight);
                    if (offsets != null && offsets.length > 1) {
                        for (final double offset : offsets) {
                            HoleSnap.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(HoleSnap.mc.field_71439_g.field_70165_t, HoleSnap.mc.field_71439_g.field_70163_u + offset, HoleSnap.mc.field_71439_g.field_70161_v, false));
                        }
                        this.stepTimer.reset();
                    }
                }
                if (this.stuckTime == 10) {
                    this.disable();
                }
            }
            else {
                HoleSnap.mc.field_71439_g.field_70138_W = 0.6f;
            }
        }
        else {
            Command.sendSilentMessage(OyVey.commandManager.getClientMessage() + ChatFormatting.GRAY + " No holes in range.");
            this.disable();
        }
    }
    
    private boolean isSafeHole(final HoleSnapUtil.Hole hole) {
        return HoleSnap.mc.field_71441_e.func_180495_p(hole.pos1.func_177984_a()).func_177230_c() == Blocks.field_150350_a && HoleSnap.mc.field_71441_e.func_180495_p(hole.pos1.func_177981_b(2)).func_177230_c() == Blocks.field_150350_a;
    }
    
    private Vec3d getCenterOfDoubleHole(final HoleSnapUtil.Hole hole) {
        final BlockPos pos1 = hole.getPos1();
        final BlockPos pos2 = hole.getPos2();
        final double centerX = (pos1.func_177958_n() + pos2.func_177958_n()) / 2.0;
        final double centerZ = (pos1.func_177952_p() + pos2.func_177952_p()) / 2.0;
        return new Vec3d(centerX + 0.5, HoleSnap.mc.field_71439_g.field_70163_u, centerZ + 0.5);
    }
    
    public double normalizeAngle(final Double angleIn) {
        double angle = angleIn;
        if ((angle %= 360.0) >= 180.0) {
            angle -= 360.0;
        }
        if (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }
    
    public Vec2f getRotationTo(final Vec3d posTo, final Vec3d posFrom) {
        return this.getRotationFromVec(posTo.func_178788_d(posFrom));
    }
    
    public Vec2f getRotationFromVec(final Vec3d vec) {
        final double xz = Math.hypot(vec.field_72450_a, vec.field_72449_c);
        final float yaw = (float)this.normalizeAngle(Math.toDegrees(Math.atan2(vec.field_72449_c, vec.field_72450_a)) - 90.0);
        final float pitch = (float)this.normalizeAngle(Math.toDegrees(-Math.atan2(vec.field_72448_b, xz)));
        return new Vec2f(yaw, pitch);
    }
    
    static {
        HoleSnap.INSTANCE = new HoleSnap();
    }
    
    private enum StepMode
    {
        None, 
        Vanilla, 
        NCP, 
        Future;
    }
}
