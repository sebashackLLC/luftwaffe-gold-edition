//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render.motionblur;

import net.minecraft.util.*;
import net.minecraft.client.resources.*;
import java.io.*;
import java.util.*;

public class MotionBlurResourceManager implements IResourceManager
{
    public Set<String> func_135055_a() {
        return null;
    }
    
    public IResource func_110536_a(final ResourceLocation location) throws IOException {
        final String path = location.func_110623_a();
        if (path.contains("shaders/program/")) {
            return (IResource)new MotionBlurResource();
        }
        return (IResource)new MotionBlurPostResource();
    }
    
    public List<IResource> func_135056_b(final ResourceLocation location) throws IOException {
        return null;
    }
}
