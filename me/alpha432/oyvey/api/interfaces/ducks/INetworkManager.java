//Decompiled by Procyon!

package me.alpha432.oyvey.api.interfaces.ducks;

import net.minecraft.network.*;
import io.netty.util.concurrent.*;
import javax.annotation.*;

public interface INetworkManager
{
    Packet<?> sendPacketNoEvent(final Packet<?> p0);
    
    void sendFast(final Packet<?> p0, @Nullable final GenericFutureListener<? extends Future<? super Void>>[] p1);
}
