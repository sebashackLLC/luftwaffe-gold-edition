//Decompiled by Procyon!

package me.alpha432.oyvey.impl.command.commands;

import me.alpha432.oyvey.impl.command.*;
import me.alpha432.oyvey.*;
import com.mojang.realmsclient.gui.*;
import org.lwjgl.input.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.features.*;

public class BindCommand extends Command
{
    public BindCommand() {
        super("bind", new String[] { "<module>", "<bind>" });
    }
    
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            sendMessage("Please specify a module.");
            return;
        }
        final String rkey = commands[1];
        final String moduleName = commands[0];
        final Module module = OyVey.moduleManager.getModuleByName(moduleName);
        if (module == null) {
            sendMessage("Unknown module '" + module + "'!");
            return;
        }
        if (rkey == null) {
            sendMessage(module.getName() + " is bound to " + ChatFormatting.GRAY + module.getBind().toString());
            return;
        }
        int key = -1;
        if (rkey.equalsIgnoreCase("mouse4")) {
            key = 3;
        }
        else if (rkey.equalsIgnoreCase("mouse5")) {
            key = 4;
        }
        else if (rkey.equalsIgnoreCase("none")) {
            key = -1;
        }
        else {
            key = Keyboard.getKeyIndex(rkey.toUpperCase());
            if (key == 0) {
                sendMessage("Unknown key '" + rkey + "'!");
                return;
            }
        }
        module.bind.setValue((Object)new Bind(key));
        sendMessage("Bind for " + ChatFormatting.GREEN + module.getName() + ChatFormatting.WHITE + " set to " + ChatFormatting.GRAY + rkey.toUpperCase());
    }
}
