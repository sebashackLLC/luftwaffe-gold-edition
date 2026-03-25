//Decompiled by Procyon!

package me.alpha432.oyvey.impl.events;

import me.alpha432.oyvey.api.event.*;

public class KeyEvent extends EventStage
{
    private final int key;
    
    public KeyEvent(final int key) {
        this.key = key;
    }
    
    public int getKey() {
        return this.key;
    }
}
