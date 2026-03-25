//Decompiled by Procyon!

package me.alpha432.oyvey.impl.events;

import net.minecraft.client.gui.*;
import me.alpha432.oyvey.api.event.*;

public class GuiScreenEvent<T extends GuiScreen> extends EventStage
{
    private final T screen;
    
    public GuiScreenEvent(final T screen) {
        this.screen = screen;
    }
    
    public T getScreen() {
        return this.screen;
    }
}
