//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.misc;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.network.play.server.*;
import net.minecraft.util.text.event.*;
import me.alpha432.oyvey.api.util.*;
import me.alpha432.oyvey.impl.features.modules.hud.*;
import com.mojang.realmsclient.gui.*;
import net.minecraft.util.text.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class AntiUnicode extends Module
{
    private static AntiUnicode INSTANCE;
    String smartText;
    Setting<modes> Modes;
    
    public AntiUnicode() {
        super("AntiUnicode", "Prevents you from poplag", Module.Category.MISC, true, false, false);
        this.smartText = "\u0410\u0410\u0410\u0410 \u042e\u041d\u0418\u041a\u041e\u0414 \u0429\u042b\u0422";
        this.Modes = (Setting<modes>)this.register(new Setting("Mode", (Object)modes.Strict));
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (this.isEnabled() && event.getPacket() instanceof SPacketChat) {
            final SPacketChat packet = (SPacketChat)event.getPacket();
            final String text = packet.func_148915_c().func_150260_c();
            int flag = 0;
            for (final char currentChar : text.toCharArray()) {
                if (Character.UnicodeBlock.of(currentChar) != Character.UnicodeBlock.BASIC_LATIN) {
                    ++flag;
                }
            }
            if (this.Modes.getValue() == modes.Strict) {
                if (flag > 20) {
                    final Style style = new Style().func_150241_a((ClickEvent)new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, text) {
                        public ClickEvent.Action func_150669_a() {
                            return ClickEvent.Action.SUGGEST_COMMAND;
                        }
                    });
                    AntiUnicode.mc.field_71456_v.func_146158_b().func_146227_a(new TextComponentString(TextUtil.coloredString("[", (TextUtil.Color)Metrics.getInstance().bracketColor.getPlannedValue()) + TextUtil.coloredString("luftwaffe", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + TextUtil.coloredString("] Blocked message ", (TextUtil.Color)Metrics.getInstance().bracketColor.getPlannedValue()) + flag + TextUtil.coloredString(" flags", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + ChatFormatting.WHITE + " [" + ChatFormatting.GRAY + "Click to view" + ChatFormatting.WHITE + "]").func_150255_a(style));
                    event.setCanceled(true);
                }
            }
            else {
                final Style style = new Style().func_150241_a((ClickEvent)new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, text) {
                    public ClickEvent.Action func_150669_a() {
                        return ClickEvent.Action.SUGGEST_COMMAND;
                    }
                });
                if (flag == 76) {
                    AntiUnicode.mc.field_71456_v.func_146158_b().func_146227_a(new TextComponentString(TextUtil.coloredString("[", (TextUtil.Color)Metrics.getInstance().bracketColor.getPlannedValue()) + TextUtil.coloredString("luftwaffe", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + TextUtil.coloredString("] Blocked message ", (TextUtil.Color)Metrics.getInstance().bracketColor.getPlannedValue()) + flag + TextUtil.coloredString(" flags", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + ChatFormatting.WHITE + " [" + ChatFormatting.GRAY + "Click to view" + ChatFormatting.WHITE + "]").func_150255_a(style));
                    event.setCanceled(true);
                }
            }
        }
    }
    
    static {
        AntiUnicode.INSTANCE = new AntiUnicode();
    }
    
    public enum modes
    {
        Smart, 
        Strict;
    }
}
