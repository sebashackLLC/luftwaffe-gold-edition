//Decompiled by Procyon!

package me.alpha432.oyvey.mixin.mixins;

import org.spongepowered.asm.mixin.*;
import net.minecraft.network.*;
import io.netty.util.concurrent.*;
import org.spongepowered.asm.mixin.gen.*;

@Mixin({ NetworkManager.class })
public interface INetworkManager
{
    @Invoker("dispatchPacket")
    void distpatchNow(final Packet<?> p0, final GenericFutureListener<? extends Future<? super Void>>[] p1);
}
