//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.events.*;
import me.alpha432.oyvey.api.util.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.util.text.*;

public class Media extends Module
{
    private static Media INSTANCE;
    public Setting<String> nickName;
    public Setting<Skin> skin;
    
    public Media() {
        super("Media", "Replaces your IGN with a custom name in chat", Module.Category.CLIENT, true, false, false);
        this.nickName = (Setting<String>)this.register(new Setting("Name", (Object)"YourName"));
        this.skin = (Setting<Skin>)this.register(new Setting("Skin", (Object)Skin.Off));
        this.setInstance();
    }
    
    public static Media getInstance() {
        if (Media.INSTANCE == null) {
            Media.INSTANCE = new Media();
        }
        return Media.INSTANCE;
    }
    
    private void setInstance() {
        Media.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent event) {
        if (NullUtils.nullCheck() || !this.isEnabled()) {
            return;
        }
        if (event.getPacket() instanceof SPacketChat) {
            final SPacketChat packet = (SPacketChat)event.getPacket();
            final String originalText = packet.func_148915_c().func_150260_c();
            final String originalFormatted = packet.func_148915_c().func_150254_d();
            final String realName = Media.mc.field_71439_g.func_70005_c_();
            final String displayName = Media.mc.field_71439_g.getDisplayNameString();
            boolean modified = false;
            String newText = originalText;
            String newFormatted = originalFormatted;
            if (newText.contains(realName)) {
                newText = newText.replace(realName, (CharSequence)this.nickName.getValue());
                modified = true;
            }
            if (!displayName.equals(realName) && newText.contains(displayName)) {
                newText = newText.replace(displayName, (CharSequence)this.nickName.getValue());
                modified = true;
            }
            if (newFormatted.contains(realName)) {
                newFormatted = newFormatted.replace(realName, (CharSequence)this.nickName.getValue());
                modified = true;
            }
            if (!displayName.equals(realName) && newFormatted.contains(displayName)) {
                newFormatted = newFormatted.replace(displayName, (CharSequence)this.nickName.getValue());
                modified = true;
            }
            if (modified) {
                if (!newFormatted.equals(originalFormatted)) {
                    packet.field_148919_a = (ITextComponent)new TextComponentString(newFormatted);
                }
                else {
                    packet.field_148919_a = (ITextComponent)new TextComponentString(newText);
                }
            }
        }
    }
    
    static {
        Media.INSTANCE = new Media();
    }
    
    public enum Skin
    {
        Off, 
        Alex, 
        Steve, 
        Thunder;
    }
}
