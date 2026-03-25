//Decompiled by Procyon!

package me.alpha432.oyvey.manager;

import java.util.*;
import java.util.concurrent.atomic.*;
import net.minecraft.client.*;
import java.util.concurrent.*;
import net.minecraft.util.text.*;

public class ChatManager
{
    private final Map<Integer, Map<String, Integer>> messageIds;
    private final AtomicInteger counter;
    private final Minecraft mc;
    
    public ChatManager() {
        this.messageIds = new ConcurrentHashMap<Integer, Map<String, Integer>>();
        this.counter = new AtomicInteger(1337);
        this.mc = Minecraft.func_71410_x();
    }
    
    public void clear() {
        if (this.mc.field_71456_v != null && this.mc.field_71456_v.func_146158_b() != null) {
            this.messageIds.values().forEach(m -> m.values().forEach(id -> this.mc.field_71456_v.func_146158_b().func_146242_c((int)id)));
        }
        this.messageIds.clear();
        this.counter.set(1337);
    }
    
    public void sendDeleteMessage(final String message, final String uniqueWord, final int senderID) {
        if (this.mc.field_71456_v == null || this.mc.field_71456_v.func_146158_b() == null) {
            return;
        }
        final int id = this.messageIds.computeIfAbsent(Integer.valueOf(senderID), v -> new ConcurrentHashMap()).computeIfAbsent(uniqueWord, v -> this.counter.getAndIncrement());
        this.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)new TextComponentString(message), id);
    }
    
    public void deleteMessage(final String uniqueWord, final int senderID) {
        if (this.mc.field_71456_v == null || this.mc.field_71456_v.func_146158_b() == null) {
            return;
        }
        final Map<String, Integer> map = this.messageIds.get(senderID);
        if (map != null) {
            final Integer id = map.remove(uniqueWord);
            if (id != null) {
                this.mc.field_71456_v.func_146158_b().func_146242_c((int)id);
            }
        }
    }
    
    public void sendDeleteComponent(final ITextComponent component, final String uniqueWord, final int senderID) {
        if (this.mc.field_71456_v == null || this.mc.field_71456_v.func_146158_b() == null) {
            return;
        }
        final int id = this.messageIds.computeIfAbsent(Integer.valueOf(senderID), v -> new ConcurrentHashMap()).computeIfAbsent(uniqueWord, v -> this.counter.getAndIncrement());
        this.mc.field_71456_v.func_146158_b().func_146234_a(component, id);
    }
    
    public int getId(final String uniqueWord, final int senderID) {
        final Map<String, Integer> map = this.messageIds.get(senderID);
        if (map != null) {
            final Integer id = map.get(uniqueWord);
            if (id != null) {
                return id;
            }
        }
        return -1;
    }
    
    public void replace(final ITextComponent component, final String uniqueWord, final int senderID, final boolean sendIfAbsent) {
        if (this.mc.field_71456_v == null || this.mc.field_71456_v.func_146158_b() == null) {
            return;
        }
        final Map<String, Integer> map = this.messageIds.get(senderID);
        if (map != null) {
            final Integer id = map.get(uniqueWord);
            if (id != null) {
                this.mc.field_71456_v.func_146158_b().func_146242_c((int)id);
                this.mc.field_71456_v.func_146158_b().func_146234_a(component, (int)id);
                return;
            }
        }
        if (sendIfAbsent) {
            this.sendDeleteComponent(component, uniqueWord, senderID);
        }
    }
}
