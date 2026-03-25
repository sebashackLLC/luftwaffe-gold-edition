//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;

public class ModelModifier extends Module
{
    private static ModelModifier INSTANCE;
    public final Setting<Boolean> crystal;
    public final Setting<Float> size;
    public final Setting<Float> speed;
    public final Setting<Float> oscillate;
    public final Setting<Boolean> player;
    public final Setting<Float> psize;
    public final Setting<Boolean> sneak;
    public final Setting<Boolean> throughWalls;
    
    public ModelModifier() {
        super("ModelModifier", "Customize how minecraft models will looks like", Module.Category.RENDER, false, false, false);
        this.crystal = (Setting<Boolean>)this.register(new Setting("CModificators", (Object)false));
        this.size = (Setting<Float>)this.register(new Setting("Size", (Object)1.0f, (Object)0.0f, (Object)1.0f, v -> (boolean)this.crystal.getValue()));
        this.speed = (Setting<Float>)this.register(new Setting("Speed", (Object)1.0f, (Object)0.0f, (Object)7.0f, v -> (boolean)this.crystal.getValue()));
        this.oscillate = (Setting<Float>)this.register(new Setting("Bounce", (Object)1.0f, (Object)0.0f, (Object)3.0f, v -> (boolean)this.crystal.getValue()));
        this.player = (Setting<Boolean>)this.register(new Setting("PModificators", (Object)false));
        this.psize = (Setting<Float>)this.register(new Setting("Size", (Object)1.0f, (Object)0.0f, (Object)1.0f));
        this.sneak = (Setting<Boolean>)this.register(new Setting("Sneak", (Object)false));
        this.throughWalls = (Setting<Boolean>)this.register(new Setting("ThroughWalls", (Object)false));
        this.setInstance();
    }
    
    public static ModelModifier getINSTANCE() {
        if (ModelModifier.INSTANCE == null) {
            ModelModifier.INSTANCE = new ModelModifier();
        }
        return ModelModifier.INSTANCE;
    }
    
    private void setInstance() {
        ModelModifier.INSTANCE = this;
    }
    
    static {
        ModelModifier.INSTANCE = new ModelModifier();
    }
}
