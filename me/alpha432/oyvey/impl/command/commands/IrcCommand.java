//Decompiled by Procyon!

package me.alpha432.oyvey.impl.command.commands;

import me.alpha432.oyvey.impl.command.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.manager.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import me.alpha432.oyvey.*;
import java.util.*;

public class IrcCommand extends Command
{
    private static boolean chatEnabled;
    private static final Set<String> mutedUsers;
    
    public IrcCommand() {
        super("irc", new String[] { "<add/list/chat/send/mute/unmute>", "<all/name/on/off/message>" });
        String formatted;
        IrcNameManager.addMessageListener(msg -> {
            if (IrcCommand.chatEnabled && IrcCommand.mc.field_71439_g != null) {
                if (!IrcCommand.mutedUsers.contains(msg.senderIrcName.toLowerCase()) && !IrcCommand.mutedUsers.contains(msg.senderIgn.toLowerCase())) {
                    formatted = ChatFormatting.GRAY + "[IRC] " + ChatFormatting.AQUA + msg.senderIrcName + ChatFormatting.DARK_GRAY + " (" + msg.senderIgn + ")" + ChatFormatting.WHITE + ": " + msg.message;
                    sendMessage(formatted);
                }
            }
        });
    }
    
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            sendMessage("Usage: .irc list | .irc add all | .irc chat on/off | .irc send <message> | .irc mute/unmute <name>");
            return;
        }
        final String lowerCase;
        final String action = lowerCase = commands[1].toLowerCase();
        switch (lowerCase) {
            case "mute": {
                if (commands.length < 3) {
                    sendMessage("Usage: .irc mute <ircname or ign>");
                    return;
                }
                final String target = commands[2].toLowerCase();
                if (IrcCommand.mutedUsers.contains(target)) {
                    sendMessage(ChatFormatting.YELLOW + target + " is already muted.");
                }
                else {
                    IrcCommand.mutedUsers.add(target);
                    sendMessage(ChatFormatting.GREEN + "Muted " + target + " in IRC chat.");
                }
            }
            case "unmute": {
                if (commands.length < 3) {
                    sendMessage("Usage: .irc unmute <ircname or ign>");
                    return;
                }
                final String target = commands[2].toLowerCase();
                if (IrcCommand.mutedUsers.remove(target)) {
                    sendMessage(ChatFormatting.GREEN + "Unmuted " + target + " in IRC chat.");
                }
                else {
                    sendMessage(ChatFormatting.YELLOW + target + " is not muted.");
                }
            }
            case "muted": {
                if (IrcCommand.mutedUsers.isEmpty()) {
                    sendMessage("No muted users.");
                }
                else {
                    sendMessage(ChatFormatting.GREEN + "Muted users (" + IrcCommand.mutedUsers.size() + "):");
                    for (final String user : IrcCommand.mutedUsers) {
                        sendMessage(ChatFormatting.GRAY + "- " + user);
                    }
                }
            }
            case "chat": {
                if (commands.length < 3) {
                    sendMessage("IRC chat is currently " + (IrcCommand.chatEnabled ? (ChatFormatting.GREEN + "ON") : (ChatFormatting.RED + "OFF")));
                    sendMessage("Usage: .irc chat on/off");
                    return;
                }
                final String toggle = commands[2].toLowerCase();
                if (toggle.equals("on")) {
                    IrcCommand.chatEnabled = true;
                    sendMessage(ChatFormatting.GREEN + "IRC chat enabled. You'll see messages from #luftwaffe");
                }
                else if (toggle.equals("off")) {
                    IrcCommand.chatEnabled = false;
                    sendMessage(ChatFormatting.RED + "IRC chat disabled.");
                }
                else {
                    sendMessage("Usage: .irc chat on/off");
                }
            }
            case "send":
            case "say":
            case "s": {
                if (commands.length < 3) {
                    sendMessage("Usage: .irc send <message>");
                    return;
                }
                if (!IrcNameManager.isConnected()) {
                    sendMessage(ChatFormatting.RED + "Not connected to IRC.");
                    return;
                }
                final StringBuilder msg = new StringBuilder();
                for (int i = 2; i < commands.length; ++i) {
                    if (i > 2) {
                        msg.append(" ");
                    }
                    msg.append(commands[i]);
                }
                IrcNameManager.sendMessage(msg.toString());
                sendMessage(ChatFormatting.GRAY + "[IRC] " + ChatFormatting.GREEN + "You" + ChatFormatting.WHITE + ": " + (Object)msg);
            }
            case "list": {
                if (!IrcNameManager.isConnected()) {
                    sendMessage(ChatFormatting.RED + "Not connected to IRC.");
                    return;
                }
                final Map<String, String> onlineUsers = IrcNameManager.getOnlineIrcUsers();
                if (onlineUsers.isEmpty()) {
                    sendMessage("No IRC users online.");
                }
                else {
                    sendMessage(ChatFormatting.GREEN + "Online IRC users (" + onlineUsers.size() + "):");
                    for (final Map.Entry<String, String> entry : onlineUsers.entrySet()) {
                        final String muteStatus = (IrcCommand.mutedUsers.contains(entry.getValue().toLowerCase()) || IrcCommand.mutedUsers.contains(entry.getKey().toLowerCase())) ? (ChatFormatting.RED + " [MUTED]") : "";
                        sendMessage(ChatFormatting.GRAY + entry.getKey() + ChatFormatting.WHITE + " -> " + ChatFormatting.AQUA + entry.getValue() + muteStatus);
                    }
                }
            }
            case "add": {
                if (commands.length < 3) {
                    sendMessage("Usage: .irc add all | .irc add <ign>");
                    return;
                }
                final String target = commands[2].toLowerCase();
                if (target.equals("all")) {
                    final Map<String, String> onlineUsers2 = IrcNameManager.getOnlineIrcUsers();
                    if (onlineUsers2.isEmpty()) {
                        sendMessage("No IRC users online to add.");
                        return;
                    }
                    int added = 0;
                    int skipped = 0;
                    final boolean futureSync = (boolean)Sync.getInstance().friendSync.getValue() && Sync.getInstance().isEnabled();
                    final String prefix = (String)Sync.getInstance().futurePrefix.getValue();
                    final boolean notifyFriends = (boolean)Management.getInstance().notifyFriends.getValue();
                    for (final String ign : onlineUsers2.keySet()) {
                        if (OyVey.friendManager.isFriend(ign)) {
                            ++skipped;
                        }
                        else {
                            if (IrcCommand.mc.field_71439_g != null && ign.equalsIgnoreCase(IrcCommand.mc.field_71439_g.func_70005_c_())) {
                                continue;
                            }
                            OyVey.friendManager.addFriend(ign);
                            if (futureSync) {
                                IrcCommand.mc.field_71439_g.func_71165_d(prefix + "friend add " + ign);
                            }
                            if (notifyFriends) {
                                IrcCommand.mc.field_71439_g.func_71165_d("/w " + ign + " I just added you in friends thanks to luftwaffe.xyz!");
                            }
                            ++added;
                        }
                    }
                    final String syncMsg = futureSync ? (ChatFormatting.GRAY + " (synced with " + Sync.getInstance().Mode.getValue() + ")") : "";
                    sendMessage(ChatFormatting.GREEN + "Added " + added + " IRC users as friends" + ((skipped > 0) ? (ChatFormatting.GRAY + " (" + skipped + " already friended)") : "") + syncMsg);
                }
                else {
                    final Map<String, String> onlineUsers2 = IrcNameManager.getOnlineIrcUsers();
                    String foundIgn = null;
                    for (final Map.Entry<String, String> entry2 : onlineUsers2.entrySet()) {
                        if (entry2.getKey().equalsIgnoreCase(commands[2]) || entry2.getValue().equalsIgnoreCase(commands[2])) {
                            foundIgn = entry2.getKey();
                            break;
                        }
                    }
                    if (foundIgn == null) {
                        sendMessage(ChatFormatting.RED + "User not found in IRC.");
                        return;
                    }
                    if (OyVey.friendManager.isFriend(foundIgn)) {
                        sendMessage(ChatFormatting.YELLOW + foundIgn + " is already a friend.");
                        return;
                    }
                    OyVey.friendManager.addFriend(foundIgn);
                    final boolean futureSync2 = (boolean)Sync.getInstance().friendSync.getValue() && Sync.getInstance().isEnabled();
                    if (futureSync2) {
                        final String prefix2 = (String)Sync.getInstance().futurePrefix.getValue();
                        IrcCommand.mc.field_71439_g.func_71165_d(prefix2 + "friend add " + foundIgn);
                    }
                    if (Management.getInstance().notifyFriends.getValue()) {
                        IrcCommand.mc.field_71439_g.func_71165_d("/w " + foundIgn + " I just added you in friends thanks to luftwaffe.xyz!");
                    }
                    final String ircName = onlineUsers2.get(foundIgn);
                    final String syncMsg2 = futureSync2 ? (ChatFormatting.GRAY + " (synced with " + Sync.getInstance().Mode.getValue() + ")") : "";
                    sendMessage(ChatFormatting.GREEN + "Added " + foundIgn + ChatFormatting.GRAY + " (" + ircName + ")" + ChatFormatting.GREEN + " as friend." + syncMsg2);
                }
            }
            case "status": {
                if (IrcNameManager.isConnected()) {
                    sendMessage(ChatFormatting.GREEN + "Connected to IRC");
                }
                else {
                    sendMessage(ChatFormatting.RED + "Not connected to IRC");
                }
            }
            default: {
                sendMessage("Unknown subcommand. Usage: .irc list | .irc add all | .irc chat on/off | .irc mute/unmute <name>");
            }
        }
    }
    
    static {
        IrcCommand.chatEnabled = false;
        mutedUsers = new HashSet<String>();
    }
}
