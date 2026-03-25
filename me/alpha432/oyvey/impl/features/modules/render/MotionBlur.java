//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import java.util.*;
import net.minecraft.client.resources.*;
import me.alpha432.oyvey.impl.features.modules.render.motionblur.*;
import java.lang.reflect.*;
import net.minecraft.util.*;

public class MotionBlur extends Module
{
    public static MotionBlur INSTANCE;
    private final Setting<Float> amount;
    private Map<String, Object> domainResourceManager;
    private float lastAmount;
    
    public MotionBlur() {
        super("MotionBlur", "Adds motion blur effect using shaders", Module.Category.RENDER, false, false, false);
        this.amount = (Setting<Float>)this.register(new Setting("Amount", (Object)0.5f, (Object)0.1f, (Object)0.99f));
        this.lastAmount = -1.0f;
        MotionBlur.INSTANCE = this;
    }
    
    public void onEnable() {
        if (MotionBlur.mc.field_71441_e == null || MotionBlur.mc.field_71439_g == null) {
            return;
        }
        this.initResourceManager();
        this.loadShader();
    }
    
    public void onDisable() {
        if (MotionBlur.mc.field_71460_t != null && MotionBlur.mc.field_71460_t.func_147702_a()) {
            MotionBlur.mc.field_71460_t.func_181022_b();
        }
    }
    
    public void onUpdate() {
        if (MotionBlur.mc.field_71441_e == null || MotionBlur.mc.field_71439_g == null) {
            return;
        }
        this.initResourceManager();
        if (this.lastAmount != (float)this.amount.getValue()) {
            this.lastAmount = (float)this.amount.getValue();
            this.loadShader();
        }
        if (!MotionBlur.mc.field_71460_t.func_147702_a() && MotionBlur.mc.field_71415_G) {
            this.loadShader();
        }
    }
    
    private void initResourceManager() {
        if (this.domainResourceManager == null) {
            try {
                for (final Field field : SimpleReloadableResourceManager.class.getDeclaredFields()) {
                    if (field.getType() == Map.class) {
                        field.setAccessible(true);
                        this.domainResourceManager = (Map<String, Object>)field.get(MotionBlur.mc.func_110442_L());
                        break;
                    }
                }
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        if (this.domainResourceManager != null && !this.domainResourceManager.containsKey("motionblur")) {
            this.domainResourceManager.put("motionblur", new MotionBlurResourceManager());
        }
    }
    
    private void loadShader() {
        if (MotionBlur.mc.field_71460_t != null) {
            MotionBlur.mc.field_71460_t.func_175069_a(new ResourceLocation("motionblur", "motionblur"));
        }
    }
    
    public float getBlurAmount() {
        return (float)this.amount.getValue();
    }
    
    public String getDisplayInfo() {
        return String.format("%.0f%%", (float)this.amount.getValue() * 100.0f);
    }
}
