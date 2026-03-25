//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.impl.events.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import me.alpha432.oyvey.api.util.render.*;
import org.lwjgl.opengl.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.*;
import java.util.*;

public class Radar extends Module
{
    public Setting<Integer> posX;
    public Setting<Integer> posY;
    int count;
    
    public Radar() {
        super("Radar", "uop.cc stuff asked by soma", Module.Category.HUD, true, false, false);
        this.posX = (Setting<Integer>)this.register(new Setting("X", (Object)2, (Object)0, (Object)1000));
        this.posY = (Setting<Integer>)this.register(new Setting("Y", (Object)11, (Object)0, (Object)1000));
        this.count = 0;
    }
    
    public void onRender2D(final Render2DEvent event) {
        this.count = 0;
        final ColorSetting mainColor = (ColorSetting)Color.getInstance().mainColor.getValue();
        java.awt.Color maincolor = new java.awt.Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), 255);
        if (Color.getInstance().rainbow.getValue()) {
            maincolor = ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue());
        }
        final int x = (int)this.posX.getValue();
        final int y = (int)this.posY.getValue();
        RenderUtil.drawRect((float)x, (float)y, (float)(x + 100), (float)(y + 100), 2147483648L);
        final int dispX = x + 50;
        final int dispY = y + 50;
        GL11.glTranslatef((float)dispX, (float)dispY, 0.0f);
        GL11.glRotatef(-Radar.mc.field_71439_g.field_70177_z, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef((float)(-dispX), (float)(-dispY), 0.0f);
        for (final EntityPlayer player : Radar.mc.field_71441_e.field_73010_i) {
            if (player != Radar.mc.field_71439_g) {
                ++this.count;
                final int differenceX = (int)Math.floor(Radar.mc.field_71439_g.field_70165_t) - (int)Math.floor(player.field_70165_t);
                final int differenceY = (int)Math.floor(Radar.mc.field_71439_g.field_70161_v) - (int)Math.floor(player.field_70161_v);
                if (Math.abs(differenceX) + Math.abs(differenceY) >= 49) {
                    continue;
                }
                RenderUtil.drawRect((float)(x + 50 + differenceX), (float)(y + 50 + differenceY), (float)(x + 51 + differenceX), (float)(y + 51 + differenceY), OyVey.friendManager.isFriend(player) ? ((long)ColorUtil.toRGBA(85, 255, 255)) : ((long)ColorUtil.toRGBA(maincolor)));
            }
        }
        GL11.glTranslatef((float)dispX, (float)dispY, 0.0f);
        GL11.glRotatef(Radar.mc.field_71439_g.field_70177_z, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef((float)(-dispX), (float)(-dispY), 0.0f);
        RenderUtil.drawRect((float)(x + 49), (float)(y + 49), (float)(x + 51), (float)(y + 51), (long)ColorUtil.toRGBA(255, 0, 0));
        RenderUtil.drawRect((float)x, (float)y, (float)(x + 101), (float)(y + 1), (long)ColorUtil.toRGBA(64, 64, 64, 255));
        RenderUtil.drawRect((float)x, (float)y, (float)(x + 1), (float)(y + 101), (long)ColorUtil.toRGBA(64, 64, 64, 255));
        RenderUtil.drawRect((float)(x + 101), (float)(y + 101), (float)(x + 100), (float)(y + 1), (long)ColorUtil.toRGBA(64, 64, 64, 255));
        RenderUtil.drawRect((float)(x + 101), (float)(y + 101), (float)(x + 1), (float)(y + 100), (long)ColorUtil.toRGBA(64, 64, 64, 255));
    }
    
    public String getDisplayInfo() {
        return String.valueOf(this.count);
    }
}
