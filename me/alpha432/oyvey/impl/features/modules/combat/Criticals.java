//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.combat;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.impl.events.*;
import me.alpha432.oyvey.api.util.entity.*;
import me.alpha432.oyvey.impl.features.modules.movement.*;
import net.minecraft.entity.*;
import net.minecraft.world.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Criticals extends Module
{
    Setting<Boolean> pauseMove;
    Setting<Boolean> onlyPhase;
    Setting<modes> packets;
    private final Timer timer;
    private final boolean resetTimer = false;
    
    public Criticals() {
        super("Criticals", "Allows you to crit without jumping", Module.Category.COMBAT, true, false, false);
        this.pauseMove = (Setting<Boolean>)this.register(new Setting("MovePause", (Object)true));
        this.onlyPhase = (Setting<Boolean>)this.register(new Setting("OnlyPhase", (Object)true));
        this.packets = (Setting<modes>)this.register(new Setting("Mode", (Object)modes.Strict));
        this.timer = new Timer();
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if ((boolean)this.pauseMove.getValue() && EntityUtil.isMoving()) {
            return;
        }
        if ((boolean)this.onlyPhase.getValue() && !Phase.getInstance().isPhasing()) {
            return;
        }
        final CPacketUseEntity packet;
        if (event.getPacket() instanceof CPacketUseEntity && (packet = (CPacketUseEntity)event.getPacket()).func_149565_c() == CPacketUseEntity.Action.ATTACK) {
            this.getClass();
            if (!this.timer.passedMs(0L)) {
                return;
            }
            if (Criticals.mc.field_71439_g.field_70122_E && !Criticals.mc.field_71474_y.field_74314_A.func_151470_d() && packet.func_149564_a((World)Criticals.mc.field_71441_e) instanceof EntityLivingBase && !Criticals.mc.field_71439_g.func_70090_H() && !Criticals.mc.field_71439_g.func_180799_ab()) {
                switch ((modes)this.packets.getValue()) {
                    case Packet: {
                        Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.0625101, Criticals.mc.field_71439_g.field_70161_v, false));
                        Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                        Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.0125, Criticals.mc.field_71439_g.field_70161_v, false));
                        Criticals.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u, Criticals.mc.field_71439_g.field_70161_v, false));
                        break;
                    }
                    case Strict: {
                        Criticals.mc.func_147114_u().func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.170001501788139, Criticals.mc.field_71439_g.field_70161_v, false));
                        Criticals.mc.func_147114_u().func_147297_a((Packet)new CPacketPlayer.Position(Criticals.mc.field_71439_g.field_70165_t, Criticals.mc.field_71439_g.field_70163_u + 0.0700018752980234, Criticals.mc.field_71439_g.field_70161_v, false));
                        break;
                    }
                }
                this.timer.reset();
            }
        }
    }
    
    public String getDisplayInfo() {
        return "" + this.packets.getValue();
    }
    
    public enum modes
    {
        Packet, 
        Strict;
    }
}
