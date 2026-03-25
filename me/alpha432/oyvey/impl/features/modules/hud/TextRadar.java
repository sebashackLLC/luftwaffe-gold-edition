//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.entity.player.*;
import java.text.*;
import java.math.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import net.minecraft.entity.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.manager.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.api.util.render.*;
import java.util.*;

public class TextRadar extends Module
{
    Setting<Integer> maxAmount;
    Setting<Integer> offset;
    
    public TextRadar() {
        super("TextRadar", "Shows players in render distance on hud", Module.Category.HUD, true, false, false);
        this.maxAmount = (Setting<Integer>)this.register(new Setting("MaxAmount", (Object)10, (Object)1, (Object)100));
        this.offset = (Setting<Integer>)this.register(new Setting("Offset", (Object)5, (Object)0, (Object)50));
    }
    
    public void onRender2D(final Render2DEvent event) {
        int i = 0;
        for (final Object o : TextRadar.mc.field_71441_e.field_72996_f) {
            if (o instanceof EntityPlayer && o != TextRadar.mc.field_71439_g) {
                if (++i > (int)this.maxAmount.getValue()) {
                    return;
                }
                final EntityPlayer entity = (EntityPlayer)o;
                final float health = (float)(Math.round(entity.func_110143_aJ()) + Math.round(entity.func_110139_bj()));
                final DecimalFormat dfDistance = new DecimalFormat("#.#");
                dfDistance.setRoundingMode(RoundingMode.CEILING);
                final StringBuilder distanceSB = new StringBuilder();
                final int color = Color.getInstance().syncColor(i);
                String health_str = "" + health;
                health_str = health_str.replace(".0", "");
                final int distanceInt = (int)TextRadar.mc.field_71439_g.func_70032_d((Entity)entity);
                final String distance = dfDistance.format(distanceInt);
                if (distanceInt >= 35) {
                    distanceSB.append(ChatFormatting.GREEN);
                }
                else if (distanceInt > 10) {
                    distanceSB.append(ChatFormatting.GOLD);
                }
                else {
                    distanceSB.append(ChatFormatting.RED);
                }
                distanceSB.append(distance);
                String heal;
                if (health >= 12.0) {
                    heal = " " + ChatFormatting.GREEN + health_str + "";
                }
                else if (health >= 5.0) {
                    heal = " " + ChatFormatting.YELLOW + health_str + "";
                }
                else {
                    heal = " " + ChatFormatting.RED + health_str + "";
                }
                String name = entity.func_145748_c_().func_150254_d();
                final String ircName = IrcNameManager.getIrcNameForPlayer(entity.func_70005_c_());
                if (ircName != null) {
                    name = name.replace(entity.func_70005_c_(), ircName);
                }
                final String str = heal + " " + ChatFormatting.RESET;
                if (OyVey.friendManager.isFriend(entity.func_70005_c_())) {
                    OyVey.textManager.drawString(str + ChatFormatting.AQUA + name + " " + (Object)distanceSB + ChatFormatting.WHITE, -2.0f, (float)((int)this.offset.getValue() + i * 10), ColorUtil.toRGBA(0, 255, 255), true);
                }
                else {
                    final float yPos = (float)((int)this.offset.getValue() + i * 10);
                    float xPos = -2.0f;
                    OyVey.textManager.drawString(str, xPos, yPos, -1, true);
                    xPos += OyVey.textManager.getStringWidth(str);
                    if (Color.getInstance().rainbow.getValue()) {
                        if (Color.getInstance().rainbowModeHud.getValue() == Color.rainbowMode.Sideway) {
                            OyVey.textManager.drawStringWithGradient(name, xPos, yPos, true);
                        }
                        else {
                            OyVey.textManager.drawString(name, xPos, yPos, ColorUtil.rainbow(i * (int)Color.getInstance().rainbowHue.getValue()).getRGB(), true);
                        }
                    }
                    else if (Color.getInstance().sidewaysGradient.getValue()) {
                        OyVey.textManager.drawStringWithGradient(name, xPos, yPos, true);
                    }
                    else if (Color.getInstance().gradient.getValue()) {
                        OyVey.textManager.drawStringWithGradient(name, xPos, yPos, true, i);
                    }
                    else {
                        OyVey.textManager.drawString(name, xPos, yPos, color, true);
                    }
                    xPos += OyVey.textManager.getStringWidth(name);
                    OyVey.textManager.drawString(" " + (Object)distanceSB + ChatFormatting.GRAY, xPos, yPos, -1, true);
                }
            }
        }
    }
}
