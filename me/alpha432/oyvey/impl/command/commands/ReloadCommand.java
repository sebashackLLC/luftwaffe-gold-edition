//Decompiled by Procyon!

package me.alpha432.oyvey.impl.command.commands;

import me.alpha432.oyvey.impl.command.*;
import me.alpha432.oyvey.*;

public class ReloadCommand extends Command
{
    public ReloadCommand() {
        super("reload", new String[0]);
    }
    
    public void execute(final String[] commands) {
        OyVey.reload();
    }
}
