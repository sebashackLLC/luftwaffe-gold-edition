//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.movement;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.api.util.*;
import net.minecraft.network.play.server.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.fml.common.gameevent.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.network.play.client.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.util.math.*;
import me.alpha432.oyvey.mixin.mixins.*;
import io.netty.util.concurrent.*;
import me.alpha432.oyvey.api.util.math.*;

public class Phase extends Module
{
    private static Phase INSTANCE;
    Timer timer;
    Setting<Boolean> edgeEnable;
    Setting<modes> Modes;
    Setting<Integer> delay;
    Setting<Integer> attempts;
    Setting<HandleTeleport> handleTeleport;
    Setting<Boolean> onlyInBlock;
    Setting<Boolean> down;
    Setting<Boolean> noAccel;
    Setting<Boolean> hyperAccel;
    boolean cancel;
    int teleportID;
    
    public Phase() {
        super("Phase", ChatFormatting.GOLD + "Allows you to walk in walls", Module.Category.MOVEMENT, true, false, false);
        this.timer = new Timer();
        this.edgeEnable = (Setting<Boolean>)this.register(new Setting("EdgeEnable", (Object)true));
        this.Modes = (Setting<modes>)this.register(new Setting("Modes", (Object)modes.Clip));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (Object)213, (Object)0, (Object)1000));
        this.attempts = (Setting<Integer>)this.register(new Setting("Attempts", (Object)3, (Object)0, (Object)10));
        this.handleTeleport = (Setting<HandleTeleport>)this.register(new Setting("HandleTeleport", (Object)HandleTeleport.Above));
        this.onlyInBlock = (Setting<Boolean>)this.register(new Setting("Only in block", (Object)true));
        this.down = (Setting<Boolean>)this.register(new Setting("Down", (Object)true));
        this.noAccel = (Setting<Boolean>)this.register(new Setting("NoAccel", (Object)false));
        this.hyperAccel = (Setting<Boolean>)this.register(new Setting("HyperAccel", (Object)true));
        this.cancel = false;
        this.teleportID = 0;
    }
    
    public static Phase getInstance() {
        if (Phase.INSTANCE == null) {
            Phase.INSTANCE = new Phase();
        }
        return Phase.INSTANCE;
    }
    
    private void setInstance() {
        Phase.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof CPacketConfirmTeleport && ((HandleTeleport)this.handleTeleport.getValue()).equals(HandleTeleport.Cancel)) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            this.teleportID = ((SPacketPlayerPosLook)event.getPacket()).func_186965_f();
            if (((HandleTeleport)this.handleTeleport.getValue()).equals(HandleTeleport.All)) {
                Phase.mc.func_147114_u().func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportID - 1));
                Phase.mc.func_147114_u().func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportID));
                Phase.mc.func_147114_u().func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportID + 1));
            }
            if (((HandleTeleport)this.handleTeleport.getValue()).equals(HandleTeleport.Above)) {
                Phase.mc.func_147114_u().func_147297_a((Packet)new CPacketConfirmTeleport(this.teleportID + 1));
            }
        }
    }
    
    @SubscribeEvent
    public void onUpdate(final TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (((modes)this.Modes.getValue()).equals(modes.Clip)) {
            if (this.shouldPacket()) {
                if (this.timer.passedMs((long)(int)this.delay.getValue())) {
                    final double[] forward = EntityUtil.forward(this.getSpeed());
                    for (int i = 0; i < (int)this.attempts.getValue(); ++i) {
                        this.sendPackets(Phase.mc.field_71439_g.field_70165_t + forward[0], Phase.mc.field_71439_g.field_70163_u + this.getUpMovement(), Phase.mc.field_71439_g.field_70161_v + forward[1]);
                    }
                    this.timer.reset();
                }
            }
            else {
                this.cancel = false;
            }
        }
    }
    
    double getUpMovement() {
        final boolean isAtHeight1 = Phase.mc.field_71439_g.field_70163_u <= 1.0;
        if (isAtHeight1 && Phase.mc.field_71474_y.field_74311_E.func_151470_d()) {
            return 0.0;
        }
        return (Phase.mc.field_71474_y.field_74314_A.func_151470_d() ? 1 : (Phase.mc.field_71474_y.field_74311_E.func_151470_d() ? -1 : 0)) * this.getSpeed();
    }
    
    public void sendPackets(final double x, final double y, final double z) {
        this.cancel = false;
        Phase.mc.func_147114_u().func_147297_a((Packet)new CPacketPlayer.Position(x, y, z, Phase.mc.field_71439_g.field_70122_E));
        this.send(new Vec3d(Phase.mc.field_71439_g.field_70165_t, Phase.mc.field_71439_g.field_70163_u + randomBounds(), Phase.mc.field_71439_g.field_70161_v));
        this.cancel = true;
    }
    
    public void sendFiveBPackets(final double x, final double y, final double z) {
        final double sin = -Math.sin(Math.toRadians(Phase.mc.field_71439_g.field_70177_z));
        final double cos = Math.cos(Math.toRadians(Phase.mc.field_71439_g.field_70177_z));
        Phase.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Phase.mc.field_71439_g.field_70165_t + sin, Phase.mc.field_71439_g.field_70163_u, Phase.mc.field_71439_g.field_70161_v + cos, true));
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        final boolean isAtHeight1 = Phase.mc.field_71439_g.field_70163_u <= 1.0;
        if (this.shouldPacket() || (!isAtHeight1 && (boolean)this.down.getValue() && Phase.mc.field_71474_y.field_74311_E.func_151470_d() && this.isInBlock())) {
            final double[] forward = EntityUtil.forward(this.getSpeed());
            if (this.timer.passedMs((long)(int)this.delay.getValue())) {
                for (int i = 0; i < (int)this.attempts.getValue(); ++i) {
                    this.sendPackets(Phase.mc.field_71439_g.field_70165_t + forward[0], Phase.mc.field_71439_g.field_70163_u + this.getUpMovement(), Phase.mc.field_71439_g.field_70161_v + forward[1]);
                }
                this.timer.reset();
            }
        }
        if (!isAtHeight1 && (boolean)this.down.getValue() && this.shouldPacket() && Phase.mc.field_71474_y.field_74311_E.func_151470_d()) {
            event.setY(event.getY() - 0.05);
        }
        if (this.noAccel.getValue()) {
            if (!Phase.mc.field_71439_g.func_70093_af() && !Phase.mc.field_71439_g.func_70090_H() && !Phase.mc.field_71439_g.func_180799_ab() && (Phase.mc.field_71439_g.field_71158_b.field_192832_b != 0.0f || Phase.mc.field_71439_g.field_71158_b.field_78902_a != 0.0f)) {
                if (this.hyperAccel.getValue()) {
                    EntityUtil.phaseSpeed(event, EntityUtil.getStrictBaseSpeed(Double.valueOf(0.2873)));
                }
                else {
                    EntityUtil.strafe(event, EntityUtil.getStrictBaseSpeed(Double.valueOf(0.2873)));
                }
            }
            else if (Phase.mc.field_71439_g.field_71158_b.field_192832_b == 0.0f && Phase.mc.field_71439_g.field_71158_b.field_78902_a == 0.0f) {
                Phase.mc.field_71439_g.field_70179_y = 0.0;
                event.setX(Phase.mc.field_71439_g.field_70159_w = 0.0);
                event.setZ(0.0);
            }
        }
    }
    
    double getSpeed() {
        return EntityUtil.getDefaultMoveSpeed() / 10.0;
    }
    
    boolean shouldPacket() {
        return (!(boolean)this.edgeEnable.getValue() || Phase.mc.field_71439_g.field_70123_F) && (!(boolean)this.onlyInBlock.getValue() || this.isPhasing());
    }
    
    boolean isInBlock() {
        return !(boolean)this.onlyInBlock.getValue() || this.isPhasing();
    }
    
    public boolean isPhasing() {
        final AxisAlignedBB bb = Phase.mc.field_71439_g.func_174813_aQ();
        for (int x = MathHelper.func_76128_c(bb.field_72340_a); x < MathHelper.func_76128_c(bb.field_72336_d) + 1; ++x) {
            for (int y = MathHelper.func_76128_c(bb.field_72338_b); y < MathHelper.func_76128_c(bb.field_72337_e) + 1; ++y) {
                for (int z = MathHelper.func_76128_c(bb.field_72339_c); z < MathHelper.func_76128_c(bb.field_72334_f) + 1; ++z) {
                    if (Phase.mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_185904_a().func_76230_c() && bb.func_72326_a(new AxisAlignedBB((double)x, (double)y, (double)z, (double)(x + 1), (double)(y + 1), (double)(z + 1)))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public void send(final Vec3d vec) {
        ((INetworkManager)Phase.mc.field_71439_g.field_71174_a.func_147298_b()).distpatchNow((Packet<?>)new CPacketPlayer.Position(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c, true), null);
    }
    
    public static double randomBounds() {
        final int randomValue = MathUtil.random.nextInt(22) + 70;
        if (MathUtil.random.nextBoolean()) {
            return randomValue;
        }
        return -randomValue;
    }
    
    public String getDisplayInfo() {
        return "" + this.Modes.getValue();
    }
    
    static {
        Phase.INSTANCE = new Phase();
    }
    
    public enum modes
    {
        Clip, 
        Smooth;
    }
    
    public enum HandleTeleport
    {
        All, 
        Above, 
        Cancel, 
        None;
    }
}
