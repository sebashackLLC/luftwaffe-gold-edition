//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.model.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.impl.features.modules.render.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ ModelPlayer.class })
public class MixinModelPlayer
{
    @Inject(method = { "setRotationAngles" }, at = { @At("RETURN") })
    public void setRotationAngles(final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scaleFactor, final Entity entityIn, final CallbackInfo ci) {
        if (Minecraft.func_71410_x().field_71441_e != null && Minecraft.func_71410_x().field_71439_g != null && entityIn instanceof EntityPlayer) {
            final NoRender module = NoRender.getInstance();
            if (module != null && module.isEnabled() && entityIn == Minecraft.func_71410_x().field_71439_g) {
                final ModelPlayer self = (ModelPlayer)this;
                if (module.nolimbmove.getValue()) {
                    self.field_178724_i.field_78795_f = 0.0f;
                    self.field_178724_i.field_78808_h = 0.0f;
                    self.field_178723_h.field_78795_f = 0.0f;
                    self.field_178723_h.field_78808_h = 0.0f;
                    self.field_178722_k.field_78795_f = 0.0f;
                    self.field_178721_j.field_78795_f = 0.0f;
                }
                if ((boolean)ModelModifier.getINSTANCE().sneak.getValue() && ModelModifier.getINSTANCE().isEnabled() && (boolean)ModelModifier.getINSTANCE().player.getValue()) {
                    self.field_78115_e.field_78795_f = 0.5f;
                    self.field_178721_j.field_78798_e = 4.0f;
                    self.field_178722_k.field_78798_e = 4.0f;
                    self.field_78116_c.field_78797_d = 4.2f;
                    self.field_78115_e.field_78797_d = 3.2f;
                    self.field_178724_i.field_78797_d = 5.2f;
                    self.field_178723_h.field_78797_d = 5.2f;
                }
            }
        }
    }
}
