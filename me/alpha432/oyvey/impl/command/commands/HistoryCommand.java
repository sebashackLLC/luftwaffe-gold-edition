//Decompiled by Procyon!

package me.alpha432.oyvey.impl.command.commands;

import me.alpha432.oyvey.impl.command.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.api.util.entity.*;
import java.util.*;

public class HistoryCommand extends Command
{
    public HistoryCommand() {
        super("history", new String[] { "<player>" });
    }
    
    public void execute(final String[] commands) {
        if (commands.length == 1 || commands.length == 0) {
            sendMessage(ChatFormatting.RED + "Please specify a player.");
        }
        UUID uuid;
        try {
            uuid = PlayerUtil.getUUIDFromName(commands[0]);
        }
        catch (Exception e) {
            sendMessage("An error occured.");
            return;
        }
        List<String> names;
        try {
            names = (List<String>)PlayerUtil.getHistoryOfNames(uuid);
        }
        catch (Exception e) {
            sendMessage("An error occured.");
            return;
        }
        if (names != null) {
            sendMessage(commands[0] + "\u00c2´s name history:");
            for (final String name : names) {
                sendMessage(name);
            }
        }
        else {
            sendMessage("No names found.");
        }
    }
}
