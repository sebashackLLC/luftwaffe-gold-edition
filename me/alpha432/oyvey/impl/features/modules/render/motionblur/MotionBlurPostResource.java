//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render.motionblur;

import net.minecraft.client.resources.*;
import net.minecraft.util.*;
import me.alpha432.oyvey.impl.features.modules.render.*;
import java.nio.charset.*;
import java.io.*;
import net.minecraft.client.resources.data.*;
import javax.annotation.*;

public class MotionBlurPostResource implements IResource
{
    public ResourceLocation func_177241_a() {
        return null;
    }
    
    public InputStream func_110527_b() {
        final float blurAmount = (MotionBlur.INSTANCE != null) ? MotionBlur.INSTANCE.getBlurAmount() : 0.5f;
        final String json = "{\n    \"targets\": [\"swap\", \"previous\"],\n    \"passes\": [\n        {\n            \"name\": \"phosphor\",\n            \"intarget\": \"minecraft:main\",\n            \"outtarget\": \"swap\",\n            \"auxtargets\": [\n                {\n                    \"name\": \"PrevSampler\",\n                    \"id\": \"previous\"\n                }\n            ],\n            \"uniforms\": [\n                {\n                    \"name\": \"Phosphor\",\n                    \"values\": [" + blurAmount + ", " + blurAmount + ", " + blurAmount + "]\n                }\n            ]\n        },\n        {\n            \"name\": \"blit\",\n            \"intarget\": \"swap\",\n            \"outtarget\": \"previous\"\n        },\n        {\n            \"name\": \"blit\",\n            \"intarget\": \"swap\",\n            \"outtarget\": \"minecraft:main\"\n        }\n    ]\n}";
        return new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
    }
    
    public boolean func_110528_c() {
        return false;
    }
    
    @Nullable
    public <T extends IMetadataSection> T func_110526_a(final String sectionName) {
        return null;
    }
    
    public String func_177240_d() {
        return null;
    }
    
    public void close() {
    }
}
