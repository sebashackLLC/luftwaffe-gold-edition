//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import net.minecraft.util.*;
import net.minecraft.client.settings.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import net.minecraft.client.*;
import org.lwjgl.input.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin({ MovementInputFromOptions.class })
public class MixinMovementInputFromOptions
{
    @Shadow
    @Final
    private GameSettings field_78903_e;
    
    @Inject(method = { "updatePlayerMoveState" }, at = { @At("HEAD") }, cancellable = true)
    public void updatePlayerMoveState(final CallbackInfo ci) {
        final GuiMove guiMove = GuiMove.getInstance();
        if (guiMove.isOn() && Minecraft.func_71410_x().field_71462_r != null && guiMove.shouldMove(Minecraft.func_71410_x().field_71462_r)) {
            final MovementInputFromOptions input = (MovementInputFromOptions)this;
            input.field_78902_a = 0.0f;
            input.field_192832_b = 0.0f;
            input.field_187255_c = false;
            input.field_187256_d = false;
            input.field_187257_e = false;
            input.field_187258_f = false;
            input.field_78901_c = false;
            input.field_78899_d = false;
            if (Keyboard.isKeyDown(this.field_78903_e.field_74351_w.func_151463_i())) {
                final MovementInputFromOptions movementInputFromOptions = input;
                ++movementInputFromOptions.field_192832_b;
                input.field_187255_c = true;
            }
            if (Keyboard.isKeyDown(this.field_78903_e.field_74368_y.func_151463_i())) {
                final MovementInputFromOptions movementInputFromOptions2 = input;
                --movementInputFromOptions2.field_192832_b;
                input.field_187256_d = true;
            }
            if (Keyboard.isKeyDown(this.field_78903_e.field_74370_x.func_151463_i())) {
                final MovementInputFromOptions movementInputFromOptions3 = input;
                ++movementInputFromOptions3.field_78902_a;
                input.field_187257_e = true;
            }
            if (Keyboard.isKeyDown(this.field_78903_e.field_74366_z.func_151463_i())) {
                final MovementInputFromOptions movementInputFromOptions4 = input;
                --movementInputFromOptions4.field_78902_a;
                input.field_187258_f = true;
            }
            if (Keyboard.isKeyDown(this.field_78903_e.field_74314_A.func_151463_i())) {
                input.field_78901_c = true;
            }
            if (Keyboard.isKeyDown(this.field_78903_e.field_74311_E.func_151463_i())) {
                input.field_78899_d = true;
            }
            ci.cancel();
        }
    }
}
