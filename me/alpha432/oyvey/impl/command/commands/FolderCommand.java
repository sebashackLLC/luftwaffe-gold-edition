//Decompiled by Procyon!

package me.alpha432.oyvey.impl.command.commands;

import me.alpha432.oyvey.impl.command.*;
import java.awt.*;
import java.nio.file.*;
import com.mojang.realmsclient.gui.*;
import java.io.*;

public class FolderCommand extends Command
{
    public FolderCommand() {
        super("folder", new String[0]);
    }
    
    public void execute(final String[] args) {
        try {
            Desktop.getDesktop().open(Paths.get("oyvey", new String[0]).toFile());
        }
        catch (IOException e) {
            Command.sendMessage(ChatFormatting.YELLOW + "[WARNING]" + ChatFormatting.WHITE + " Failed to open client folder");
            e.printStackTrace();
        }
    }
}
