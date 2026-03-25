//Decompiled by Procyon!

package me.alpha432.oyvey.manager;

import me.alpha432.oyvey.api.features.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.api.features.settings.*;
import java.util.function.*;
import me.alpha432.oyvey.api.util.entity.*;
import java.util.*;

public class FriendManager extends Feature
{
    private List<Friend> friends;
    
    public FriendManager() {
        super("Friends");
        this.friends = new ArrayList<Friend>();
    }
    
    public boolean isFriend(final String name) {
        this.cleanFriends();
        return this.friends.stream().anyMatch(friend -> friend.username.equalsIgnoreCase(name));
    }
    
    public boolean isFriend(final EntityPlayer player) {
        return this.isFriend(player.func_70005_c_());
    }
    
    public void addFriend(final String name) {
        final Friend friend = this.getFriendByName(name);
        if (friend != null) {
            this.friends.add(friend);
        }
        this.cleanFriends();
    }
    
    public void removeFriend(final String name) {
        this.cleanFriends();
        for (final Friend friend : this.friends) {
            if (!friend.getUsername().equalsIgnoreCase(name)) {
                continue;
            }
            this.friends.remove(friend);
            break;
        }
    }
    
    public Friend getFriend(final String label) {
        for (final Friend friend : this.friends) {
            if (friend.getUsername().equalsIgnoreCase(label)) {
                return friend;
            }
        }
        return null;
    }
    
    public boolean isAliasSameAsLabel(final String aliasOrLabel) {
        for (final Friend friend : this.friends) {
            if (aliasOrLabel.equalsIgnoreCase(friend.getUsername()) && aliasOrLabel.equalsIgnoreCase(friend.getUsername())) {
                return true;
            }
        }
        return false;
    }
    
    public void onLoad() {
        this.friends = new ArrayList<Friend>();
        this.clearSettings();
    }
    
    public void saveFriends() {
        this.clearSettings();
        this.cleanFriends();
        for (final Friend friend : this.friends) {
            this.register(new Setting(friend.getUuid().toString(), (Object)friend.getUsername()));
        }
    }
    
    public void cleanFriends() {
        this.friends.stream().filter(Objects::nonNull).filter(friend -> friend.getUsername() != null);
    }
    
    public List<Friend> getFriends() {
        this.cleanFriends();
        return this.friends;
    }
    
    public Friend getFriendByName(final String input) {
        final UUID uuid = PlayerUtil.getUUIDFromName(input);
        if (uuid != null) {
            final Friend friend = new Friend(input, uuid);
            return friend;
        }
        return null;
    }
    
    public void addFriend(final Friend friend) {
        this.friends.add(friend);
    }
    
    public static class Friend
    {
        private final String username;
        private final UUID uuid;
        
        public Friend(final String username, final UUID uuid) {
            this.username = username;
            this.uuid = uuid;
        }
        
        public String getUsername() {
            return this.username;
        }
        
        public UUID getUuid() {
            return this.uuid;
        }
    }
}
