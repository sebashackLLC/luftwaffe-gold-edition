//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.player;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.util.*;
import net.minecraft.init.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import net.minecraft.network.play.client.*;
import net.minecraft.entity.*;
import net.minecraft.network.*;
import java.util.*;
import com.mojang.realmsclient.gui.*;

public class FastProjectile extends Module
{
    private static FastProjectile INSTANCE;
    private boolean shooting;
    private long lastShootTime;
    private final Setting<Boolean> bows;
    private final Setting<Boolean> pearls;
    Setting<modes> Mode;
    private final Setting<Integer> Packets;
    private final Setting<Boolean> rotate;
    private final Setting<Boolean> StopMotion;
    private final Setting<Boolean> cancelStupidShit;
    private final Setting<Boolean> noRotate;
    private final Setting<Boolean> Bypass;
    boolean cancel;
    
    public FastProjectile() {
        super("FastProjectile", "An exploit that uses packets to greatly increase the speed of a projectile", Module.Category.COMBAT, true, false, false);
        this.bows = (Setting<Boolean>)this.register(new Setting("Bows", (Object)false));
        this.pearls = (Setting<Boolean>)this.register(new Setting("Pearls", (Object)true));
        this.Mode = (Setting<modes>)new Setting("Mode", (Object)modes.Future);
        this.Packets = (Setting<Integer>)this.register(new Setting("Charge", (Object)20, (Object)1, (Object)300));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (Object)false));
        this.StopMotion = (Setting<Boolean>)this.register(new Setting("StopMotion", (Object)false));
        this.cancelStupidShit = (Setting<Boolean>)this.register(new Setting("CancelPacket", (Object)false));
        this.noRotate = (Setting<Boolean>)this.register(new Setting("NoRotate", (Object)false));
        this.Bypass = (Setting<Boolean>)this.register(new Setting("Manipulate", (Object)false));
        this.cancel = false;
        FastProjectile.INSTANCE = this;
    }
    
    public static FastProjectile getInstance() {
        if (FastProjectile.INSTANCE == null) {
            FastProjectile.INSTANCE = new FastProjectile();
        }
        return FastProjectile.INSTANCE;
    }
    
    public void onEnable() {
        super.onEnable();
        if (NullUtils.nullCheck()) {
            return;
        }
        this.shooting = false;
        this.lastShootTime = System.currentTimeMillis();
        this.cancel = false;
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if ((boolean)this.StopMotion.getValue() && FastProjectile.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151031_f && FastProjectile.mc.field_71439_g.func_184587_cr()) {
            event.setX(0.0);
            event.setZ(0.0);
        }
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        final CPacketPlayerTryUseItem packet;
        final ItemStack handStack;
        if (event.getPacket() instanceof CPacketPlayerTryUseItem && (packet = (CPacketPlayerTryUseItem)event.getPacket()).func_187028_a() == EnumHand.MAIN_HAND && !(handStack = FastProjectile.mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND)).func_190926_b() && handStack.func_77973_b() != null && handStack.func_77973_b() instanceof ItemEnderPearl && (boolean)this.pearls.getValue()) {
            this.doTheWork();
        }
        if (event.getPacket() instanceof CPacketPlayer && FastProjectile.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151031_f && FastProjectile.mc.field_71439_g.func_184587_cr() && this.cancel && (boolean)this.cancelStupidShit.getValue()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer.Rotation && FastProjectile.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151031_f && FastProjectile.mc.field_71439_g.func_184587_cr() && this.cancel && (boolean)this.noRotate.getValue()) {
            event.setCanceled(true);
        }
        final CPacketPlayerDigging packet2;
        if (event.getPacket() instanceof CPacketPlayerDigging && (packet2 = (CPacketPlayerDigging)event.getPacket()).func_180762_c() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM && FastProjectile.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_151031_f && FastProjectile.mc.field_71439_g.func_184587_cr() && (boolean)this.bows.getValue()) {
            this.doTheWork();
        }
    }
    
    public void doTheWork() {
        FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)FastProjectile.mc.field_71439_g, CPacketEntityAction.Action.START_SPRINTING));
        this.cancel = false;
        for (int i = 0; i < (int)this.Packets.getValue(); ++i) {
            switch ((modes)this.Mode.getValue()) {
                case Down: {
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u - 9.999999960041972E-12, FastProjectile.mc.field_71439_g.field_70161_v, !(boolean)this.Bypass.getValue()));
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u + 9.999999960041972E-12, FastProjectile.mc.field_71439_g.field_70161_v, (boolean)this.Bypass.getValue()));
                    break;
                }
                case Massive: {
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u - 1.0E-10, FastProjectile.mc.field_71439_g.field_70161_v, !(boolean)this.Bypass.getValue()));
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u + 1.0E-10, FastProjectile.mc.field_71439_g.field_70161_v, (boolean)this.Bypass.getValue()));
                    break;
                }
                case Beam: {
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u - 1.0E-10, FastProjectile.mc.field_71439_g.field_70161_v, !(boolean)this.Bypass.getValue()));
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u + 0.6, FastProjectile.mc.field_71439_g.field_70161_v, (boolean)this.Bypass.getValue()));
                    break;
                }
                case Test: {
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u - 9.99999993922529E-9, FastProjectile.mc.field_71439_g.field_70161_v, !(boolean)this.Bypass.getValue()));
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u + 9.99999993922529E-9, FastProjectile.mc.field_71439_g.field_70161_v, (boolean)this.Bypass.getValue()));
                    break;
                }
                case Direction: {
                    final double sin = -Math.sin(Math.toRadians(FastProjectile.mc.field_71439_g.field_70177_z));
                    final double cos = Math.cos(Math.toRadians(FastProjectile.mc.field_71439_g.field_70177_z));
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u - 0.1, FastProjectile.mc.field_71439_g.field_70161_v, false));
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t + sin * 100.0, FastProjectile.mc.field_71439_g.field_70163_u, FastProjectile.mc.field_71439_g.field_70161_v + cos * 100.0, true));
                    break;
                }
                case Gradual: {
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u - this.methodY(FastProjectile.mc.field_71439_g.field_70163_u, i), FastProjectile.mc.field_71439_g.field_70161_v, !(boolean)this.Bypass.getValue()));
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u + this.methodY(FastProjectile.mc.field_71439_g.field_70163_u, i), FastProjectile.mc.field_71439_g.field_70161_v, (boolean)this.Bypass.getValue()));
                    break;
                }
                case NCP: {
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u + 9.999999960041972E-12, FastProjectile.mc.field_71439_g.field_70161_v, false));
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u + 9.999999960041972E-12, FastProjectile.mc.field_71439_g.field_70161_v, false));
                    break;
                }
                case OPP: {
                    final Random projectileRandom = new Random();
                    final double sin2 = -Math.sin(Math.toRadians(FastProjectile.mc.field_71439_g.field_70177_z));
                    final double cos2 = Math.cos(Math.toRadians(FastProjectile.mc.field_71439_g.field_70177_z));
                    if (projectileRandom.nextBoolean()) {
                        FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t + sin2 * 100.0, FastProjectile.mc.field_71439_g.field_70163_u, FastProjectile.mc.field_71439_g.field_70161_v + cos2 * 100.0, false));
                        FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t - sin2 * 100.0, FastProjectile.mc.field_71439_g.field_70163_u, FastProjectile.mc.field_71439_g.field_70161_v - cos2 * 100.0, true));
                        break;
                    }
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t - sin2 * 100.0, FastProjectile.mc.field_71439_g.field_70163_u, FastProjectile.mc.field_71439_g.field_70161_v - cos2 * 100.0, true));
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t + sin2 * 100.0, FastProjectile.mc.field_71439_g.field_70163_u, FastProjectile.mc.field_71439_g.field_70161_v + cos2 * 100.0, false));
                    break;
                }
                case Future: {
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u, FastProjectile.mc.field_71439_g.field_70161_v, true));
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u + 1.0E-11, FastProjectile.mc.field_71439_g.field_70161_v, false));
                    break;
                }
                case Stonian: {
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u, FastProjectile.mc.field_71439_g.field_70161_v, FastProjectile.mc.field_71439_g.field_70177_z, FastProjectile.mc.field_71439_g.field_70125_A, true));
                    FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.PositionRotation(FastProjectile.mc.field_71439_g.field_70165_t, FastProjectile.mc.field_71439_g.field_70163_u + 1.0E-10, FastProjectile.mc.field_71439_g.field_70161_v, FastProjectile.mc.field_71439_g.field_70177_z, FastProjectile.mc.field_71439_g.field_70125_A, false));
                    break;
                }
            }
            if (this.rotate.getValue()) {
                FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(FastProjectile.mc.field_71439_g.field_70177_z, FastProjectile.mc.field_71439_g.field_70125_A, true));
            }
        }
        this.cancel = true;
        FastProjectile.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)FastProjectile.mc.field_71439_g, CPacketEntityAction.Action.STOP_SPRINTING));
    }
    
    public double methodY(final double y, final int x) {
        final double yf = Math.pow(2.3853, -10.0) * x;
        return yf;
    }
    
    public String getDescription() {
        return "FastProjectile: An " + ChatFormatting.RED + "exploit" + ChatFormatting.WHITE + " that uses packets to greatly increase the speed of a projectile";
    }
    
    public enum modes
    {
        Down, 
        Massive, 
        Weird, 
        Beam, 
        Test, 
        Direction, 
        Gradual, 
        NCP, 
        OPP, 
        Future, 
        Stonian;
    }
}
