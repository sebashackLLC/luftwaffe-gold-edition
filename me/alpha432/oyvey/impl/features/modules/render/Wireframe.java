//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Wireframe extends Module
{
    private static Wireframe INSTANCE;
    public final Setting<Float> alpha;
    public final Setting<Float> cAlpha;
    public final Setting<Float> lineWidth;
    public final Setting<Float> crystalLineWidth;
    public Setting<RenderMode> mode;
    public Setting<RenderMode> cMode;
    public Setting<Boolean> players;
    public Setting<Boolean> playerModel;
    public Setting<Boolean> crystals;
    public Setting<Boolean> crystalModel;
    
    public Wireframe() {
        super("Wireframe", "Draws a wireframe esp around other players.", Module.Category.RENDER, false, false, false);
        this.alpha = (Setting<Float>)this.register(new Setting("PAlpha", (Object)255.0f, (Object)0.1f, (Object)255.0f));
        this.cAlpha = (Setting<Float>)this.register(new Setting("CAlpha", (Object)255.0f, (Object)0.1f, (Object)255.0f));
        this.lineWidth = (Setting<Float>)this.register(new Setting("PLineWidth", (Object)1.0f, (Object)0.1f, (Object)3.0f));
        this.crystalLineWidth = (Setting<Float>)this.register(new Setting("CLineWidth", (Object)1.0f, (Object)0.1f, (Object)3.0f));
        this.mode = (Setting<RenderMode>)this.register(new Setting("PMode", (Object)RenderMode.SOLID));
        this.cMode = (Setting<RenderMode>)this.register(new Setting("CMode", (Object)RenderMode.SOLID));
        this.players = (Setting<Boolean>)this.register(new Setting("Players", (Object)Boolean.FALSE));
        this.playerModel = (Setting<Boolean>)this.register(new Setting("PlayerModel", (Object)Boolean.FALSE));
        this.crystals = (Setting<Boolean>)this.register(new Setting("Crystals", (Object)Boolean.FALSE));
        this.crystalModel = (Setting<Boolean>)this.register(new Setting("CrystalModel", (Object)Boolean.FALSE));
        this.setInstance();
    }
    
    public static Wireframe getINSTANCE() {
        if (Wireframe.INSTANCE == null) {
            Wireframe.INSTANCE = new Wireframe();
        }
        return Wireframe.INSTANCE;
    }
    
    private void setInstance() {
        Wireframe.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onRenderPlayerEvent(final RenderPlayerEvent.Pre event) {
        event.getEntityPlayer().field_70737_aN = 0;
    }
    
    static {
        Wireframe.INSTANCE = new Wireframe();
    }
    
    public enum RenderMode
    {
        SOLID, 
        WIREFRAME;
    }
}
