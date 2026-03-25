//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.renderer.entity.layers.*;
import net.minecraft.client.entity.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.alpha432.oyvey.impl.features.modules.render.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ LayerCape.class })
public class MixinLayerCape
{
    @Inject(method = { "doRenderLayer" }, at = { @At("HEAD") }, cancellable = true)
    public void onDoRenderLayer(final AbstractClientPlayer entitylivingbaseIn, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final CallbackInfo ci) {
        final NoRender module = NoRender.getInstance();
        if (module != null && module.isEnabled() && (boolean)module.nocape.getValue() && entitylivingbaseIn == Minecraft.func_71410_x().field_71439_g) {
            ci.cancel();
        }
    }
}
