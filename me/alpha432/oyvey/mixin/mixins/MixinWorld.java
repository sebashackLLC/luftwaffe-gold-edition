//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.*;
import java.util.*;
import com.google.common.base.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.impl.features.modules.render.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.util.math.*;
import java.awt.*;

@Mixin({ World.class })
public class MixinWorld
{
    @Redirect(method = { "getEntitiesWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Lcom/google/common/base/Predicate;)Ljava/util/List;" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;getEntitiesOfTypeWithinAABB(Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/List;Lcom/google/common/base/Predicate;)V"))
    public <T extends Entity> void getEntitiesOfTypeWithinAABBHook(final Chunk chunk, final Class<? extends T> entityClass, final AxisAlignedBB aabb, final List<T> listToFill, final Predicate<? super T> filter) {
        try {
            chunk.func_177430_a((Class)entityClass, aabb, (List)listToFill, (Predicate)filter);
        }
        catch (Exception ex) {}
    }
    
    @Inject(method = { "getWorldTime" }, at = { @At("HEAD") }, cancellable = true)
    public void getWorldTime(final CallbackInfoReturnable<Long> cir) {
        if (OyVey.moduleManager != null) {
            final SkyBox skyBox = (SkyBox)OyVey.moduleManager.getModuleByClass((Class)SkyBox.class);
            if (skyBox != null && skyBox.isEnabled() && skyBox.getTime() != 0) {
                cir.setReturnValue((Object)(long)skyBox.getTime());
            }
        }
    }
    
    @Inject(method = { "getCelestialAngle" }, at = { @At("HEAD") }, cancellable = true)
    public void getCelestialAngle(final float partialTicks, final CallbackInfoReturnable<Float> cir) {
        if (OyVey.moduleManager != null) {
            final SkyBox skyBox = (SkyBox)OyVey.moduleManager.getModuleByClass((Class)SkyBox.class);
            if (skyBox != null && skyBox.isEnabled() && skyBox.getTime() != 0) {
                final int time = skyBox.getTime();
                float angle = time % 24000 / 24000.0f - 0.25f;
                if (angle < 0.0f) {
                    ++angle;
                }
                if (angle > 1.0f) {
                    --angle;
                }
                final float oldAngle = angle;
                angle = 1.0f - (float)((Math.cos(angle * 3.141592653589793) + 1.0) / 2.0);
                angle = oldAngle + (angle - oldAngle) / 3.0f;
                cir.setReturnValue((Object)angle);
            }
        }
    }
    
    @Inject(method = { "getSkyColor" }, at = { @At("RETURN") }, cancellable = true)
    public void getSkyColor(final Entity entityIn, final float partialTicks, final CallbackInfoReturnable<Vec3d> cir) {
        if (OyVey.moduleManager != null) {
            final SkyBox CUSTOM_SKY = (SkyBox)OyVey.moduleManager.getModuleByClass((Class)SkyBox.class);
            if (CUSTOM_SKY != null && CUSTOM_SKY.isEnabled() && (CUSTOM_SKY.mode() == SkyBox.Mode.BOTH || CUSTOM_SKY.mode() == SkyBox.Mode.SKY) && CUSTOM_SKY.mode() != SkyBox.Mode.NONE) {
                final Color color = CUSTOM_SKY.getSkyColor();
                cir.setReturnValue((Object)new Vec3d((double)(color.getRed() / 255.0f), (double)(color.getGreen() / 255.0f), (double)(color.getBlue() / 255.0f)));
            }
        }
    }
}
