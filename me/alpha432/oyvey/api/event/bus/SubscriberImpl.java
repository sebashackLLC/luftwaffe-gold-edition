//Decompiled by Procyon!

package me.alpha432.oyvey.api.event.bus;

import me.alpha432.oyvey.api.event.bus.api.*;
import me.alpha432.oyvey.api.event.bus.listener.*;
import java.util.*;

public class SubscriberImpl implements Subscriber
{
    protected final List<Listener<?>> listeners;
    
    public SubscriberImpl() {
        this.listeners = new ArrayList<Listener<?>>();
    }
    
    public Collection<Listener<?>> getListeners() {
        return this.listeners;
    }
}
