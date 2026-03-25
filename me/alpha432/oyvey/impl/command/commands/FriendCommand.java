//Decompiled by Procyon!

package me.alpha432.oyvey.impl.command.commands;

import me.alpha432.oyvey.impl.command.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.manager.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.api.util.*;
import me.alpha432.oyvey.impl.features.modules.hud.*;
import me.alpha432.oyvey.api.features.*;
import net.minecraft.util.text.*;
import java.util.*;

public class FriendCommand extends Command
{
    public FriendCommand() {
        super("friend", new String[] { "<add/del/name/clear>", "<name>" });
    }
    
    public void execute(final String[] commands) {
        if (commands.length == 1) {
            if (OyVey.friendManager.getFriends().isEmpty()) {
                sendMessage("Friend list empty D:.");
            }
            else {
                String f = "Friends: ";
                for (final FriendManager.Friend friend : OyVey.friendManager.getFriends()) {
                    try {
                        f = f + friend.getUsername() + ", ";
                    }
                    catch (Exception ex) {}
                }
                sendMessage(f);
            }
            return;
        }
        if (commands.length != 2) {
            if (commands.length >= 2) {
                final String s = commands[0];
                switch (s) {
                    case "add": {
                        if (Sync.getInstance().Mode.getValue() == Sync.clients.Future) {
                            if (Sync.getInstance().friendSync.getValue()) {
                                FriendCommand.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "friend add " + commands[1]);
                            }
                            if (Management.getInstance().notifyFriends.getValue()) {
                                FriendCommand.mc.field_71439_g.func_71165_d("/w " + commands[1] + " I just added you in friends thanks to luftwaffe.xyz!");
                            }
                            OyVey.friendManager.addFriend(commands[1]);
                            if ((boolean)Sync.getInstance().friendSync.getValue() && Sync.getInstance().isEnabled()) {
                                Command.sendMessage(ChatFormatting.RED + "[" + Sync.getInstance().Mode.getValue() + "] " + ChatFormatting.WHITE + commands[1] + TextUtil.coloredString(" has been friended", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()));
                            }
                            else {
                                sendMessage(ChatFormatting.WHITE + commands[1] + TextUtil.coloredString(" has been friended", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()));
                            }
                            return;
                        }
                        if ((boolean)Sync.getInstance().friendSync.getValue() && Sync.getInstance().isEnabled()) {
                            FriendCommand.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "friend add " + commands[1]);
                        }
                        if (Management.getInstance().notifyFriends.getValue()) {
                            FriendCommand.mc.field_71439_g.func_71165_d("/w " + commands[1] + " I just added you in friends thanks to luftwaffe.xyz!");
                        }
                        OyVey.friendManager.addFriend(commands[1]);
                        if ((boolean)Sync.getInstance().friendSync.getValue() && Sync.getInstance().isEnabled()) {
                            Command.sendMessage(ChatFormatting.BLUE + "[" + Sync.getInstance().Mode.getValue() + "] " + ChatFormatting.WHITE + commands[1] + TextUtil.coloredString(" has been friended", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()));
                        }
                        else {
                            sendMessage(ChatFormatting.WHITE + commands[1] + TextUtil.coloredString(" has been friended", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()));
                        }
                    }
                    case "del": {
                        if (Sync.getInstance().Mode.getValue() == Sync.clients.Future) {
                            if ((boolean)Sync.getInstance().friendSync.getValue() && Sync.getInstance().isEnabled()) {
                                FriendCommand.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "friend del " + commands[1]);
                                final TextComponentString text = new TextComponentString(ChatFormatting.RED + "[" + Sync.getInstance().Mode.getValue() + "] " + ChatFormatting.GRAY + "Added friend with alias " + commands[1] + ".");
                                Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
                            }
                            if ((boolean)Management.getInstance().notifyFriends.getValue() && (boolean)Management.getInstance().notifyFriendsDelete.getValue()) {
                                FriendCommand.mc.field_71439_g.func_71165_d("/w " + commands[1] + " I just deleted you from friends thanks to luftwaffe.xyz!");
                            }
                            OyVey.friendManager.removeFriend(commands[1]);
                            if ((boolean)Sync.getInstance().friendSync.getValue() && Sync.getInstance().isEnabled()) {
                                Command.sendMessage(ChatFormatting.RED + "[" + Sync.getInstance().Mode.getValue() + "] " + ChatFormatting.WHITE + commands[1] + TextUtil.coloredString(" has been unfriended", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()));
                            }
                            else {
                                sendMessage(ChatFormatting.WHITE + commands[1] + TextUtil.coloredString(" has been unfriended", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()));
                            }
                            return;
                        }
                        if ((boolean)Sync.getInstance().friendSync.getValue() && Sync.getInstance().isEnabled()) {
                            FriendCommand.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "friend del " + commands[1]);
                            final TextComponentString text = new TextComponentString(ChatFormatting.BLUE + "[" + Sync.getInstance().Mode.getValue() + "] " + ChatFormatting.GRAY + "Added friend with alias " + commands[1] + ".");
                            Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
                        }
                        if ((boolean)Management.getInstance().notifyFriends.getValue() && (boolean)Management.getInstance().notifyFriendsDelete.getValue()) {
                            FriendCommand.mc.field_71439_g.func_71165_d("/w " + commands[1] + " I just deleted you from friends thanks to luftwaffe.xyz!");
                        }
                        OyVey.friendManager.removeFriend(commands[1]);
                        if ((boolean)Sync.getInstance().friendSync.getValue() && Sync.getInstance().isEnabled()) {
                            Command.sendMessage(ChatFormatting.BLUE + "[" + Sync.getInstance().Mode.getValue() + "] " + ChatFormatting.WHITE + commands[1] + TextUtil.coloredString(" has been unfriended", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()));
                        }
                        else {
                            sendMessage(ChatFormatting.WHITE + commands[1] + TextUtil.coloredString(" has been unfriended", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()));
                        }
                    }
                    default: {
                        sendMessage("Unknown Command, try friend add/del (name)");
                        break;
                    }
                }
            }
            return;
        }
        final String s2 = commands[0];
        switch (s2) {
            case "reset": {
                OyVey.friendManager.onLoad();
                sendMessage("Friends got reset.");
            }
            default: {
                sendMessage(commands[0] + (OyVey.friendManager.isFriend(commands[0]) ? " is friended." : " isn't friended."));
            }
        }
    }
}
