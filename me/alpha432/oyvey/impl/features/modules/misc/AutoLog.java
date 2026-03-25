//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.misc;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.util.*;
import net.minecraft.network.play.client.*;
import me.alpha432.oyvey.api.util.network.*;
import net.minecraft.network.*;
import net.minecraft.network.play.server.*;
import java.util.*;
import org.apache.commons.lang3.*;

public class AutoLog extends Module
{
    private static AutoLog INSTANCE;
    Setting<modes> method;
    Setting<Integer> minHealth;
    Setting<Boolean> noTotem;
    Setting<Boolean> disable;
    
    public AutoLog() {
        super("AutoLog", "/kill out of render bug aka alberto method", Module.Category.MISC, false, false, false);
        this.method = (Setting<modes>)this.register(new Setting("Method", (Object)modes.InvalidSlot));
        this.minHealth = (Setting<Integer>)this.register(new Setting("MinHealth", (Object)14.0, (Object)1.0, (Object)36.0));
        this.noTotem = (Setting<Boolean>)this.register(new Setting("NoTotem", (Object)true));
        this.disable = (Setting<Boolean>)this.register(new Setting("AutoDisable", (Object)true));
        this.setInstance();
    }
    
    public static AutoLog getInstance() {
        if (AutoLog.INSTANCE == null) {
            AutoLog.INSTANCE = new AutoLog();
        }
        return AutoLog.INSTANCE;
    }
    
    private void setInstance() {
        AutoLog.INSTANCE = this;
    }
    
    public void onEnable() {
    }
    
    public void onUpdate() {
        if (NullUtils.nullCheck() || !this.isEnabled()) {
            return;
        }
        switch ((modes)this.method.getValue()) {
            case InvalidSlot: {
                PacketUtil.send((Packet)new CPacketHeldItemChange(-1));
                break;
            }
            case SelfHurt: {
                PacketUtil.send((Packet)new SPacketPlayerPosLook(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, (Set)Collections.singleton(SPacketPlayerPosLook.EnumFlags.X_ROT), 67676767));
                PacketUtil.send((Packet)new SPacketPlayerPosLook(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, (Set)Collections.singleton(SPacketPlayerPosLook.EnumFlags.X_ROT), 67676767));
                PacketUtil.send((Packet)new SPacketPlayerPosLook(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, (Set)Collections.singleton(SPacketPlayerPosLook.EnumFlags.X_ROT), 67676767));
                PacketUtil.send((Packet)new SPacketPlayerPosLook(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, (Set)Collections.singleton(SPacketPlayerPosLook.EnumFlags.X_ROT), 67676767));
                break;
            }
            case IllegalChat: {
                AutoLog.mc.field_71439_g.func_71165_d(RandomUtils.nextInt() + "§§§" + RandomUtils.nextInt());
                break;
            }
            case Disonnect: {
                AutoLog.mc.field_71441_e.func_72882_A();
                break;
            }
        }
        if (this.disable.getValue()) {
            this.disable();
        }
    }
    
    static {
        AutoLog.INSTANCE = new AutoLog();
    }
    
    public enum modes
    {
        InvalidSlot, 
        SelfHurt, 
        IllegalChat, 
        Disonnect;
    }
}
