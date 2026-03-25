//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.item.*;
import net.minecraft.client.renderer.block.model.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.renderer.*;
import me.alpha432.oyvey.impl.features.modules.render.*;
import java.awt.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.client.*;

@Mixin({ RenderItem.class })
public class MixinRenderItem
{
    @Inject(method = { "renderItemModel" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderItem;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V", shift = At.Shift.BEFORE) })
    private void renderItemModel(final ItemStack stack, final IBakedModel bakedModel, final ItemCameraTransforms.TransformType transform, final boolean leftHanded, final CallbackInfo ci) {
        if (ViewModel.getINSTANCE().isOn()) {
            GlStateManager.func_179152_a((float)ViewModel.getINSTANCE().X.getValue(), (float)ViewModel.getINSTANCE().Y.getValue(), (float)ViewModel.getINSTANCE().Z.getValue());
        }
    }
    
    @Inject(method = { "renderEffect" }, at = { @At("HEAD") })
    private void renderEffectPre(final IBakedModel model, final CallbackInfo ci) {
        GlStateManager.func_179123_a();
        GlStateManager.func_179126_j();
        GlStateManager.func_179132_a(true);
        GlStateManager.func_179143_c(515);
        GlStateManager.func_179147_l();
        GlStateManager.func_187401_a(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);
    }
    
    @Inject(method = { "renderEffect" }, at = { @At("RETURN") })
    private void renderEffectPost(final IBakedModel model, final CallbackInfo ci) {
        GlStateManager.func_179099_b();
    }
    
    @ModifyArg(method = { "renderEffect" }, at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/RenderItem.renderModel(Lnet/minecraft/client/renderer/block/model/IBakedModel;I)V"), index = 1)
    private int glintColor(final int color) {
        if (GlintColorizer.getINSTANCE().isOn()) {
            return new Color(GlintColorizer.getINSTANCE().getRed(), GlintColorizer.getINSTANCE().getGreen(), GlintColorizer.getINSTANCE().getBlue()).getRGB();
        }
        return color;
    }
    
    @ModifyArg(method = { "renderEffect" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V"), index = 0)
    private float glintSpeed(final float f) {
        if (!GlintColorizer.getINSTANCE().isOn()) {
            return f;
        }
        final double speed = (double)GlintColorizer.getINSTANCE().speed.getValue();
        if (f > 0.0f) {
            return (float)(Minecraft.func_71386_F() * speed % 3000.0) / 3000.0f / 8.0f;
        }
        return (float)(Minecraft.func_71386_F() * speed % 4873.0) / 4873.0f / 8.0f * -1.0f;
    }
}
