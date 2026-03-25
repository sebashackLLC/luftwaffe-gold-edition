//Decompiled by Procyon!

package me.alpha432.oyvey.api.event.bus.listener;

import me.alpha432.oyvey.api.event.bus.api.*;

public abstract class Listener<E> implements IListener<E>
{
    private final Class<? super E> event;
    private final Class<?> type;
    private final int priority;
    
    public Listener(final Class<? super E> event) {
        this(event, 10, null);
    }
    
    public Listener(final Class<? super E> event, final Class<?> type) {
        this(event, 10, type);
    }
    
    public Listener(final Class<? super E> event, final int priority) {
        this(event, priority, null);
    }
    
    public Listener(final Class<? super E> target, final int priority, final Class<?> type) {
        this.priority = priority;
        this.event = target;
        this.type = type;
    }
    
    public int getPriority() {
        return this.priority;
    }
    
    public Class<? super E> getTarget() {
        return this.event;
    }
    
    public Class<?> getType() {
        return this.type;
    }
}
