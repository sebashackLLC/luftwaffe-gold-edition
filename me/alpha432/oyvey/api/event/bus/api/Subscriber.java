//Decompiled by Procyon!

package me.alpha432.oyvey.api.event.bus.api;

import java.util.*;
import me.alpha432.oyvey.api.event.bus.listener.*;

public interface Subscriber
{
    Collection<Listener<?>> getListeners();
}
