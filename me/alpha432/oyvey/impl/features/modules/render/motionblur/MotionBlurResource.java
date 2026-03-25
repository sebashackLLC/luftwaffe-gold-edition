//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render.motionblur;

import net.minecraft.client.resources.*;
import net.minecraft.util.*;
import me.alpha432.oyvey.impl.features.modules.render.*;
import java.nio.charset.*;
import java.io.*;
import net.minecraft.client.resources.data.*;
import javax.annotation.*;

public class MotionBlurResource implements IResource
{
    public ResourceLocation func_177241_a() {
        return null;
    }
    
    public InputStream func_110527_b() {
        final float blurAmount = (MotionBlur.INSTANCE != null) ? MotionBlur.INSTANCE.getBlurAmount() : 0.5f;
        final String json = "{\n    \"blend\": {\n        \"func\": \"add\",\n        \"srcrgb\": \"one\",\n        \"dstrgb\": \"zero\"\n    },\n    \"vertex\": \"sobel\",\n    \"fragment\": \"phosphor\",\n    \"attributes\": [\"Position\"],\n    \"samplers\": [\n        {\"name\": \"DiffuseSampler\"},\n        {\"name\": \"PrevSampler\"}\n    ],\n    \"uniforms\": [\n        {\n            \"name\": \"ProjMat\",\n            \"type\": \"matrix4x4\",\n            \"count\": 16,\n            \"values\": [1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0,0.0,0.0,0.0,0.0,1.0]\n        },\n        {\n            \"name\": \"InSize\",\n            \"type\": \"float\",\n            \"count\": 2,\n            \"values\": [1.0, 1.0]\n        },\n        {\n            \"name\": \"OutSize\",\n            \"type\": \"float\",\n            \"count\": 2,\n            \"values\": [1.0, 1.0]\n        },\n        {\n            \"name\": \"Phosphor\",\n            \"type\": \"float\",\n            \"count\": 3,\n            \"values\": [" + blurAmount + ", " + blurAmount + ", " + blurAmount + "]\n        }\n    ]\n}";
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
