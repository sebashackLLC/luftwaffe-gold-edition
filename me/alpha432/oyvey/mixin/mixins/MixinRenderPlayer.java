//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import net.minecraft.client.renderer.entity.*;
import net.minecraft.util.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.*;
import me.alpha432.oyvey.impl.features.modules.player.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.alpha432.oyvey.*;
import org.spongepowered.asm.mixin.injection.*;
import me.alpha432.oyvey.impl.features.modules.render.*;
import net.minecraft.client.renderer.*;

@Mixin({ RenderPlayer.class })
public class MixinRenderPlayer
{
    @Final
    @Shadow
    private boolean field_177140_a;
    @Unique
    private static final ResourceLocation alpha432$STEVE;
    @Unique
    private static final ResourceLocation alpha432$ALEX;
    @Unique
    private static final ResourceLocation alpha432$THUNDER;
    
    @Overwrite
    public ResourceLocation func_110775_a(final AbstractClientPlayer entity) {
        if (entity == Minecraft.func_71410_x().field_71439_g && NameProtect.INSTANCE.isEnabled() && NameProtect.INSTANCE.getFakeSkin()) {
            return this.field_177140_a ? MixinRenderPlayer.alpha432$ALEX : MixinRenderPlayer.alpha432$STEVE;
        }
        final Media tweaks = Media.getInstance();
        if (entity == Minecraft.func_71410_x().field_71439_g && tweaks != null && tweaks.isEnabled()) {
            switch ((Media.Skin)tweaks.skin.getValue()) {
                case Steve: {
                    return MixinRenderPlayer.alpha432$STEVE;
                }
                case Alex: {
                    return MixinRenderPlayer.alpha432$ALEX;
                }
                case Thunder: {
                    return MixinRenderPlayer.alpha432$THUNDER;
                }
            }
        }
        return entity.func_110306_p();
    }
    
    @Inject(method = { "renderEntityName" }, at = { @At("HEAD") }, cancellable = true)
    public void onRenderEntityName(final AbstractClientPlayer entityIn, final double x, final double y, final double z, final String name, final double distanceSq, final CallbackInfo ci) {
        final Nametags nametagsModule = (Nametags)OyVey.moduleManager.getModuleByClass((Class)Nametags.class);
        if (nametagsModule != null && nametagsModule.isEnabled()) {
            ci.cancel();
        }
    }
    
    @Inject(method = { "preRenderCallback(Lnet/minecraft/client/entity/AbstractClientPlayer;F)V" }, at = { @At("RETURN") })
    public void onPreRenderCallback(final AbstractClientPlayer entity, final float partialTicks, final CallbackInfo ci) {
        final ModelModifier tweaks = ModelModifier.getINSTANCE();
        if (entity == Minecraft.func_71410_x().field_71439_g && tweaks != null && tweaks.isEnabled() && (boolean)tweaks.player.getValue()) {
            final float size = (float)tweaks.psize.getValue();
            if (size != 1.0f) {
                GlStateManager.func_179152_a(size, size, size);
            }
        }
    }
    
    @Inject(method = { "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V" }, at = { @At("HEAD") })
    public void onDoRenderPre(final AbstractClientPlayer entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo ci) {
        final ModelModifier tweaks = ModelModifier.getINSTANCE();
        if (entity == Minecraft.func_71410_x().field_71439_g && tweaks != null && tweaks.isEnabled() && (boolean)tweaks.player.getValue() && (boolean)tweaks.throughWalls.getValue()) {
            GlStateManager.func_179097_i();
        }
    }
    
    @Inject(method = { "doRender(Lnet/minecraft/client/entity/AbstractClientPlayer;DDDFF)V" }, at = { @At("RETURN") })
    public void onDoRenderPost(final AbstractClientPlayer entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks, final CallbackInfo ci) {
        final ModelModifier tweaks = ModelModifier.getINSTANCE();
        if (entity == Minecraft.func_71410_x().field_71439_g && tweaks != null && tweaks.isEnabled() && (boolean)tweaks.player.getValue() && (boolean)tweaks.throughWalls.getValue()) {
            GlStateManager.func_179126_j();
        }
    }
    
    static {
        alpha432$STEVE = new ResourceLocation("textures/entity/steve.png");
        alpha432$ALEX = new ResourceLocation("textures/entity/alex.png");
        alpha432$THUNDER = new ResourceLocation("oyvey", "textures/thunder.png");
    }
}
