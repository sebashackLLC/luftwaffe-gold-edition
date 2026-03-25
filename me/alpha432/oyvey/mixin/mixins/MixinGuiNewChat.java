//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import java.util.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import me.alpha432.oyvey.impl.features.modules.misc.*;
import me.alpha432.oyvey.api.util.render.*;
import org.spongepowered.asm.mixin.injection.*;
import net.minecraft.client.gui.*;

@Mixin({ GuiNewChat.class })
public class MixinGuiNewChat extends Gui
{
    @Shadow
    @Final
    private List<ChatLine> field_146253_i;
    @Unique
    private static long oyvey$animationStart;
    @Unique
    private static int oyvey$drawCounter;
    @Unique
    private static final long ANIMATION_DURATION = 300L;
    
    @Inject(method = { "setChatLine" }, at = { @At("HEAD") })
    private void onSetChatLine(final CallbackInfo ci) {
        MixinGuiNewChat.oyvey$animationStart = System.currentTimeMillis();
    }
    
    @Inject(method = { "drawChat" }, at = { @At("HEAD") })
    private void onDrawChatHead(final int updateCounter, final CallbackInfo ci) {
        MixinGuiNewChat.oyvey$drawCounter = 0;
    }
    
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V"))
    private void drawRectHook(final int left, final int top, final int right, final int bottom, final int color) {
        final BetterChat cmod = BetterChat.getInstance();
        final int topColor = (cmod.isOn() && cmod.background.getValue() == BetterChat.Background.None) ? 0 : color;
        final int bottomColor = (cmod.isOn() && cmod.background.getValue() != BetterChat.Background.Default) ? 0 : color;
        Render2DMethods.drawGradientRect((float)left, (float)top, (float)right, (float)bottom, false, topColor, bottomColor);
    }
    
    @Redirect(method = { "drawChat" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawStringWithShadow(Ljava/lang/String;FFI)I"))
    private int drawStringWithShadowHook(final FontRenderer fontRenderer, final String text, final float x, final float y, final int color) {
        final BetterChat cmod = BetterChat.getInstance();
        float finalX = x;
        if (cmod.isOn() && (boolean)cmod.animatedChat.getValue() && MixinGuiNewChat.oyvey$drawCounter == 0) {
            final long elapsed = System.currentTimeMillis() - MixinGuiNewChat.oyvey$animationStart;
            if (elapsed < 300L) {
                final float progress = elapsed / 300.0f;
                final float eased = progress * progress * (3.0f - 2.0f * progress);
                finalX = x + (1.0f - eased) * -200.0f;
            }
        }
        ++MixinGuiNewChat.oyvey$drawCounter;
        return fontRenderer.func_175063_a(text, finalX, y, color);
    }
    
    @Redirect(method = { "setChatLine" }, at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 0))
    public int drawnChatLinesSize(final List<ChatLine> list) {
        return (BetterChat.getInstance().isOn() && (boolean)BetterChat.getInstance().infinite.getValue()) ? -2147483647 : list.size();
    }
    
    @Redirect(method = { "setChatLine" }, at = @At(value = "INVOKE", target = "Ljava/util/List;size()I", ordinal = 2))
    public int chatLinesSize(final List<ChatLine> list) {
        return (BetterChat.getInstance().isOn() && (boolean)BetterChat.getInstance().infinite.getValue()) ? -2147483647 : list.size();
    }
    
    static {
        MixinGuiNewChat.oyvey$animationStart = 0L;
        MixinGuiNewChat.oyvey$drawCounter = 0;
    }
}
