//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.network;

import me.alpha432.oyvey.api.interfaces.*;
import net.minecraft.network.*;
import net.minecraft.client.network.*;
import io.netty.util.concurrent.*;
import me.alpha432.oyvey.mixin.mixins.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.api.util.entity.*;
import me.alpha432.oyvey.api.interfaces.ducks.*;
import net.minecraft.util.math.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;

public class PacketUtil implements Minecraftable
{
    public static void send(final Packet<?> packet) {
        final NetHandlerPlayClient connection = PacketUtil.mc.func_147114_u();
        if (connection != null) {
            connection.func_147297_a((Packet)packet);
        }
    }
    
    public static void instantSend(final Packet<?> packet) {
        if (PacketUtil.mc.field_71439_g.field_71174_a != null) {
            ((INetworkManager)PacketUtil.mc.field_71439_g.field_71174_a.func_147298_b()).sendFast((Packet)packet, (GenericFutureListener[])null);
        }
    }
    
    public static Packet<?> sendNoEvent(final Packet<?> packet) {
        final NetHandlerPlayClient connection = PacketUtil.mc.func_147114_u();
        if (connection != null) {
            final INetworkManager manager = (INetworkManager)connection.func_147298_b();
            return (Packet<?>)manager.sendPacketNoEvent((Packet)packet);
        }
        return null;
    }
    
    public static CPacketUseEntity attackPacket(final int id) {
        final CPacketUseEntity packet = new CPacketUseEntity();
        ((ICPacketUseEntity)packet).setEntityId(id);
        ((ICPacketUseEntity)packet).setAction(CPacketUseEntity.Action.ATTACK);
        return packet;
    }
    
    public static CPacketUseEntity attackPacket(final Entity entity) {
        final CPacketUseEntity packet = new CPacketUseEntity();
        ((ICPacketUseEntity)packet).setEntityId(entity.func_145782_y());
        ((ICPacketUseEntity)packet).setAction(CPacketUseEntity.Action.ATTACK);
        return packet;
    }
    
    public static void move(final double x, final double y, final double z, final boolean ground) {
        final boolean isRotating = MovementUtil.isRotating();
        final float[] rotations = { PacketUtil.mc.field_71439_g.field_70177_z, PacketUtil.mc.field_71439_g.field_70125_A };
        final CPacketPlayer packet = (CPacketPlayer)(isRotating ? new CPacketPlayer.PositionRotation(x, y, z, rotations[0], rotations[1], ground) : new CPacketPlayer.Position(x, y, z, ground));
        send((Packet<?>)packet);
        if (isRotating) {
            ((IEntityPlayerSP)PacketUtil.mc.field_71439_g).setLastReportedYaw(PacketUtil.mc.field_71439_g.field_70177_z);
            ((IEntityPlayerSP)PacketUtil.mc.field_71439_g).setLastReportedPitch(PacketUtil.mc.field_71439_g.field_70125_A);
        }
    }
    
    public static void rotate(final float[] rotations) {
        rotate(rotations, PacketUtil.mc.field_71439_g.field_70122_E);
    }
    
    public static void rotate(final float[] rotations, final boolean ground) {
        final boolean isMoving = MovementUtil.isFullMoving();
        final CPacketPlayer packet = (CPacketPlayer)(isMoving ? new CPacketPlayer.PositionRotation(PacketUtil.mc.field_71439_g.field_70165_t, PacketUtil.mc.field_71439_g.field_70163_u, PacketUtil.mc.field_71439_g.field_70161_v, rotations[0], rotations[1], ground) : new CPacketPlayer.Rotation(rotations[0], rotations[1], ground));
        send((Packet<?>)packet);
    }
    
    public static void sneak(final boolean start) {
        send((Packet<?>)sneakPacket(start));
    }
    
    public static CPacketEntityAction sneakPacket(final boolean start) {
        return new CPacketEntityAction((Entity)PacketUtil.mc.field_71439_g, start ? CPacketEntityAction.Action.START_SNEAKING : CPacketEntityAction.Action.STOP_SNEAKING);
    }
    
    public static void sprint(final boolean start) {
        send((Packet<?>)sprintPacket(start));
    }
    
    public static CPacketEntityAction sprintPacket(final boolean start) {
        return new CPacketEntityAction((Entity)PacketUtil.mc.field_71439_g, start ? CPacketEntityAction.Action.START_SPRINTING : CPacketEntityAction.Action.STOP_SPRINTING);
    }
    
    public static void drop() {
        send((Packet<?>)new CPacketPlayerDigging(CPacketPlayerDigging.Action.DROP_ITEM, BlockPos.field_177992_a, EnumFacing.DOWN));
    }
    
    public static void swing() {
        swing(EnumHand.MAIN_HAND);
    }
    
    public static void swing(final EnumHand hand) {
        send((Packet<?>)new CPacketAnimation(hand));
    }
    
    public static void manipulate(final float power) {
        final float offset = 1.0E-5f;
        send((Packet<?>)new CPacketEntityAction((Entity)PacketUtil.mc.field_71439_g, CPacketEntityAction.Action.START_SPRINTING));
        for (int i = 0; i < power * 50.0f; ++i) {
            send((Packet<?>)new CPacketPlayer.Position(PacketUtil.mc.field_71439_g.field_70165_t, PacketUtil.mc.field_71439_g.field_70163_u - offset, PacketUtil.mc.field_71439_g.field_70161_v, true));
            send((Packet<?>)new CPacketPlayer.Position(PacketUtil.mc.field_71439_g.field_70165_t, PacketUtil.mc.field_71439_g.field_70163_u + offset, PacketUtil.mc.field_71439_g.field_70161_v, false));
        }
    }
    
    public static void damage() {
        for (int index = 0; index < 81; ++index) {
            send((Packet<?>)new CPacketPlayer.Position(PacketUtil.mc.field_71439_g.field_70165_t, PacketUtil.mc.field_71439_g.field_70163_u + 0.05, PacketUtil.mc.field_71439_g.field_70161_v, false));
            send((Packet<?>)new CPacketPlayer.Position(PacketUtil.mc.field_71439_g.field_70165_t, PacketUtil.mc.field_71439_g.field_70163_u + 0.05, PacketUtil.mc.field_71439_g.field_70161_v, false));
        }
    }
}
