//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.misc;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.network.play.server.*;
import me.alpha432.oyvey.impl.command.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Test extends Module
{
    private static Test INSTANCE;
    
    public Test() {
        super("Test", "Just a test module", Module.Category.MISC, false, false, false);
        this.setInstance();
    }
    
    public static Test getInstance() {
        if (Test.INSTANCE == null) {
            Test.INSTANCE = new Test();
        }
        return Test.INSTANCE;
    }
    
    private void setInstance() {
        Test.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent event) {
        if (event.getPacket() instanceof SPacketEntityVelocity) {
            final SPacketEntityVelocity packet = (SPacketEntityVelocity)event.getPacket();
            if (packet.field_149417_a == Test.mc.field_71439_g.field_145783_c) {
                Command.sendMessage("Detected velocity ma niga: X: " + packet.field_149415_b + ", Y: " + packet.field_149416_c + ", Z: " + packet.field_149414_d);
            }
        }
    }
    
    static {
        Test.INSTANCE = new Test();
    }
}
