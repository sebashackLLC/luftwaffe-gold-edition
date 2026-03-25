//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraft.util.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.gui.*;

public class MentalSupport extends Module
{
    private static MentalSupport INSTANCE;
    Setting<modes> mode;
    Setting<Integer> x;
    Setting<Integer> y;
    Setting<Integer> width;
    Setting<Integer> height;
    Setting<Float> scale;
    Setting<Float> alpha;
    private final ResourceLocation CRYSTAL_IMAGE;
    private final ResourceLocation DOICK_IMAGE;
    
    public static MentalSupport getInstance() {
        if (MentalSupport.INSTANCE == null) {
            MentalSupport.INSTANCE = new MentalSupport();
        }
        return MentalSupport.INSTANCE;
    }
    
    public MentalSupport() {
        super("MentalSupport", "u will not be alone while playing crystalpvp", Module.Category.HUD, true, false, false);
        this.mode = (Setting<modes>)this.register(new Setting("mode", (Object)modes.crystalopium));
        this.x = (Setting<Integer>)this.register(new Setting("X", (Object)100, (Object)0, (Object)1000));
        this.y = (Setting<Integer>)this.register(new Setting("Y", (Object)100, (Object)0, (Object)1000));
        this.width = (Setting<Integer>)this.register(new Setting("Width", (Object)50, (Object)10, (Object)200));
        this.height = (Setting<Integer>)this.register(new Setting("Height", (Object)50, (Object)10, (Object)200));
        this.scale = (Setting<Float>)this.register(new Setting("Scale", (Object)1.0f, (Object)0.1f, (Object)5.0f));
        this.alpha = (Setting<Float>)this.register(new Setting("Alpha", (Object)1.0f, (Object)0.1f, (Object)1.0f));
        this.CRYSTAL_IMAGE = new ResourceLocation("oyvey", "textures/crystalopium.png");
        this.DOICK_IMAGE = new ResourceLocation("oyvey", "textures/doickswag.png");
    }
    
    @SubscribeEvent
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        if (MentalSupport.mc.field_71441_e == null) {
            return;
        }
        this.drawSupportImage();
    }
    
    private void drawSupportImage() {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(3008);
        GL11.glBlendFunc(770, 771);
        final ResourceLocation image = (this.mode.getValue() == modes.crystalopium) ? this.CRYSTAL_IMAGE : this.DOICK_IMAGE;
        MentalSupport.mc.func_110434_K().func_110577_a(image);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        Gui.func_146110_a((int)this.x.getValue(), (int)this.y.getValue(), 0.0f, 0.0f, (int)this.width.getValue(), (int)this.height.getValue(), (float)(int)this.width.getValue(), (float)(int)this.height.getValue());
        GL11.glDisable(3008);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    private void drawSimpleImage() {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        final ResourceLocation image = (this.mode.getValue() == modes.crystalopium) ? this.CRYSTAL_IMAGE : this.DOICK_IMAGE;
        MentalSupport.mc.func_110434_K().func_110577_a(image);
        Gui.func_146110_a((int)this.x.getValue(), (int)this.y.getValue(), 0.0f, 0.0f, (int)this.width.getValue(), (int)this.height.getValue(), (float)(int)this.width.getValue(), (float)(int)this.height.getValue());
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    static {
        MentalSupport.INSTANCE = new MentalSupport();
    }
    
    enum modes
    {
        crystalopium, 
        doickswag;
    }
}
