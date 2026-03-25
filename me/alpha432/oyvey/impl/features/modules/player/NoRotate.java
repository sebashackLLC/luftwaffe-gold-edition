//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.player;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class NoRotate extends Module
{
    private final Timer timer;
    private boolean cancelPackets;
    private boolean timerReset;
    
    public NoRotate() {
        super("NoRotate", "Prevents you from force rotations by server", Module.Category.PLAYER, true, false, false);
        this.timer = new Timer();
        this.cancelPackets = true;
        this.timerReset = false;
    }
    
    public void onLogout() {
        this.cancelPackets = false;
    }
    
    public void onLogin() {
        this.timer.reset();
        this.timerReset = true;
    }
    
    public void onUpdate() {
        if (this.timerReset && !this.cancelPackets) {
            this.cancelPackets = true;
            this.timerReset = false;
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (fullNullCheck()) {
            return;
        }
        if (event.getStage() == 0 && this.cancelPackets && event.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            packet.field_148936_d = NoRotate.mc.field_71439_g.field_70177_z;
            packet.field_148937_e = NoRotate.mc.field_71439_g.field_70125_A;
        }
    }
}
