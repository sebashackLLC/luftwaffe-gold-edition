//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.client.entity.*;
import net.minecraft.client.*;
import net.minecraft.world.*;
import net.minecraft.client.network.*;
import net.minecraft.stats.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.util.*;
import me.alpha432.oyvey.impl.features.modules.player.*;
import me.alpha432.oyvey.api.util.*;
import net.minecraft.init.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.impl.events.*;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(value = { EntityPlayerSP.class }, priority = 9998)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer
{
    public MixinEntityPlayerSP(final Minecraft p_i47378_1_, final World p_i47378_2_, final NetHandlerPlayClient p_i47378_3_, final StatisticsManager p_i47378_4_, final RecipeBook p_i47378_5_) {
        super(p_i47378_2_, p_i47378_3_.func_175105_e());
    }
    
    @Inject(method = { "sendChatMessage" }, at = { @At("HEAD") }, cancellable = true)
    public void sendChatMessage(final String message, final CallbackInfo callback) {
        final ChatEvent chatEvent = new ChatEvent(message);
        MinecraftForge.EVENT_BUS.post((Event)chatEvent);
    }
    
    @Inject(method = { "onUpdateWalkingPlayer" }, at = { @At("HEAD") })
    private void preMotion(final CallbackInfo info) {
        final UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(0);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }
    
    @Inject(method = { "onUpdateWalkingPlayer" }, at = { @At("RETURN") })
    private void postMotion(final CallbackInfo info) {
        final UpdateWalkingPlayerEvent event = new UpdateWalkingPlayerEvent(1);
        MinecraftForge.EVENT_BUS.post((Event)event);
    }
    
    @Inject(method = { "swingArm" }, at = { @At("HEAD") }, cancellable = true)
    public void swingArm(final EnumHand enumHand, final CallbackInfo info) {
        if (Swing.getINSTANCE().isEnabled()) {
            switch ((Swing.SwingMode)Swing.getINSTANCE().swingmode.getValue()) {
                case Mainhand: {
                    super.func_184609_a(EnumHand.MAIN_HAND);
                    break;
                }
                case Offhand: {
                    super.func_184609_a(EnumHand.OFF_HAND);
                    break;
                }
                case Offhandc: {
                    if (Util.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP || Util.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP) {
                        super.func_184609_a(EnumHand.OFF_HAND);
                        break;
                    }
                    super.func_184609_a(EnumHand.MAIN_HAND);
                    break;
                }
                case Default: {
                    super.func_184609_a(enumHand);
                    break;
                }
            }
            Util.mc.func_147114_u().func_147297_a((Packet)new CPacketAnimation(enumHand));
            info.cancel();
        }
    }
    
    @Redirect(method = { "move" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    public void move(final AbstractClientPlayer player, final MoverType moverType, final double x, final double y, final double z) {
        final MoveEvent event = new MoveEvent(0, moverType, x, y, z);
        MinecraftForge.EVENT_BUS.post((Event)event);
        if (!event.isCanceled()) {
            super.func_70091_d(event.getType(), event.getX(), event.getY(), event.getZ());
        }
    }
}
