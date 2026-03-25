//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.entity.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.alpha432.oyvey.impl.features.modules.player.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ EntityLivingBase.class })
public class MixinEntityLivingBase
{
    @Inject(method = { "getArmSwingAnimationEnd" }, at = { @At("HEAD") }, cancellable = true)
    public void getArmSwingAnimationEndHook(final CallbackInfoReturnable<Integer> info) {
        final int i = Swing.getINSTANCE().isOn() ? ((int)Math.floor(6.0f / (float)Swing.getINSTANCE().swingspeed.getValue())) : 6;
        info.setReturnValue((Object)i);
    }
}
