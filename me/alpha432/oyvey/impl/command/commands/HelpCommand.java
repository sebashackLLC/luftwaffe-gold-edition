//Decompiled by Procyon!

package me.alpha432.oyvey.impl.command.commands;

import me.alpha432.oyvey.impl.command.*;
import me.alpha432.oyvey.*;
import com.mojang.realmsclient.gui.*;
import java.util.*;

public class HelpCommand extends Command
{
    public HelpCommand() {
        super("help");
    }
    
    public void execute(final String[] commands) {
        sendMessage("Commands: ");
        for (final Command command : OyVey.commandManager.getCommands()) {
            sendMessage(ChatFormatting.GRAY + OyVey.commandManager.getPrefix() + command.getName());
        }
    }
}
