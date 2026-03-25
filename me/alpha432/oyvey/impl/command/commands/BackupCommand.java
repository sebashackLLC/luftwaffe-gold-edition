//Decompiled by Procyon!

package me.alpha432.oyvey.impl.command.commands;

import me.alpha432.oyvey.impl.command.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import me.alpha432.oyvey.*;

public class BackupCommand extends Command
{
    public BackupCommand() {
        super("backup", new String[] { "b", "ping" });
    }
    
    public void execute(final String[] commands) {
        final IRC ircModule = (IRC)OyVey.moduleManager.getModuleByName("IRC");
        if (ircModule == null) {
            sendMessage("IRC module not found!");
            return;
        }
        if (!ircModule.isEnabled()) {
            sendMessage("IRC module is not enabled!");
            return;
        }
        sendMessage("Use the ping key (bind IRC module) to call for backup!");
    }
}
