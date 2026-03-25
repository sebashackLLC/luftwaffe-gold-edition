//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;

public class Resolver extends Module
{
    private static Resolver INSTANCE;
    public Setting<Float> range;
    public Setting<Float> raytrace;
    public Setting<Boolean> players;
    public Setting<Boolean> mobs;
    public Setting<Boolean> animals;
    public Setting<Boolean> vehicles;
    public Setting<Boolean> projectiles;
    
    public Resolver() {
        super("Resolver", "Settings for resolving target", Module.Category.CLIENT, false, false, false);
        this.range = (Setting<Float>)this.register(new Setting("Range", (Object)9.0f, (Object)1.0f, (Object)15.0f));
        this.raytrace = (Setting<Float>)this.register(new Setting("Raytrace", (Object)6.0f, (Object)0.1f, (Object)7.0f, "Wall Range."));
        this.players = (Setting<Boolean>)this.register(new Setting("Players", (Object)true));
        this.mobs = (Setting<Boolean>)this.register(new Setting("Mobs", (Object)false));
        this.animals = (Setting<Boolean>)this.register(new Setting("Animals", (Object)false));
        this.vehicles = (Setting<Boolean>)this.register(new Setting("Entities", (Object)false));
        this.projectiles = (Setting<Boolean>)this.register(new Setting("Projectiles", (Object)false));
        this.setInstance();
    }
    
    public static Resolver getInstance() {
        if (Resolver.INSTANCE == null) {
            Resolver.INSTANCE = new Resolver();
        }
        return Resolver.INSTANCE;
    }
    
    private void setInstance() {
        Resolver.INSTANCE = this;
    }
    
    static {
        Resolver.INSTANCE = new Resolver();
    }
}
