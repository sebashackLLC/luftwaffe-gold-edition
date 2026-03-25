//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.player;

import me.alpha432.oyvey.api.features.*;
import net.minecraft.client.entity.*;
import java.net.*;
import java.nio.charset.*;
import org.apache.commons.io.*;
import com.google.gson.*;
import java.util.*;
import com.mojang.authlib.*;
import net.minecraft.world.*;
import me.alpha432.oyvey.impl.command.*;
import net.minecraft.entity.*;
import net.minecraft.client.multiplayer.*;

public class FakePlayer extends Module
{
    private final String name = "opp";
    private EntityOtherPlayerMP _fakePlayer;
    
    public FakePlayer() {
        super("FakePlayer", "Spawns a FakePlayer for testing", Module.Category.MISC, false, false, false);
    }
    
    public static String getUuid(final String name) {
        final JsonParser parser = new JsonParser();
        final String url = "https://api.mojang.com/users/profiles/minecraft/" + name;
        try {
            final String UUIDJson = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
            if (UUIDJson.isEmpty()) {
                return "invalid name";
            }
            final JsonObject UUIDObject = (JsonObject)parser.parse(UUIDJson);
            return reformatUuid(UUIDObject.get("id").toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
    
    private static String reformatUuid(final String uuid) {
        String longUuid = "";
        longUuid = longUuid + uuid.substring(1, 9) + "-";
        longUuid = longUuid + uuid.substring(9, 13) + "-";
        longUuid = longUuid + uuid.substring(13, 17) + "-";
        longUuid = longUuid + uuid.substring(17, 21) + "-";
        longUuid += uuid.substring(21, 33);
        return longUuid;
    }
    
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
            return;
        }
        this.cleanupFakePlayer();
        if (FakePlayer.mc.field_71439_g != null && FakePlayer.mc.field_71441_e != null) {
            try {
                final WorldClient field_71441_e = FakePlayer.mc.field_71441_e;
                this.getClass();
                final UUID fromString = UUID.fromString(getUuid("opp"));
                this.getClass();
                this._fakePlayer = new EntityOtherPlayerMP((World)field_71441_e, new GameProfile(fromString, "opp"));
            }
            catch (Exception e3) {
                try {
                    final WorldClient field_71441_e2 = FakePlayer.mc.field_71441_e;
                    final UUID fromString2 = UUID.fromString("70ee432d-0a96-4137-a2c0-37cc9df67f03");
                    this.getClass();
                    this._fakePlayer = new EntityOtherPlayerMP((World)field_71441_e2, new GameProfile(fromString2, "opp"));
                    Command.sendMessage("Failed to load uuid, setting another one.");
                }
                catch (Exception e2) {
                    Command.sendMessage("Failed to create fake player: " + e2.getMessage());
                    this.disable();
                    return;
                }
            }
            if (this._fakePlayer != null) {
                final String s = "%s has been spawned.";
                final Object[] array = { null };
                final int n = 0;
                this.getClass();
                array[n] = "opp";
                Command.sendMessage(String.format(s, array));
                this._fakePlayer.func_82149_j((Entity)FakePlayer.mc.field_71439_g);
                this._fakePlayer.field_70759_as = FakePlayer.mc.field_71439_g.field_70759_as;
                FakePlayer.mc.field_71441_e.func_73027_a(-100, (Entity)this._fakePlayer);
            }
        }
    }
    
    public void onDisable() {
        this.cleanupFakePlayer();
        super.onDisable();
    }
    
    private void cleanupFakePlayer() {
        if (this._fakePlayer != null) {
            try {
                if (FakePlayer.mc.field_71441_e != null && FakePlayer.mc.field_71441_e.func_72910_y().contains(this._fakePlayer)) {
                    FakePlayer.mc.field_71441_e.func_72900_e((Entity)this._fakePlayer);
                }
            }
            catch (Exception e) {
                System.out.println("FakePlayer cleanup error (non-critical): " + e.getMessage());
            }
            finally {
                this._fakePlayer = null;
            }
        }
    }
    
    public void onLoad() {
        this._fakePlayer = null;
        super.onLoad();
    }
    
    public void onUnload() {
        this.cleanupFakePlayer();
        super.onUnload();
    }
}
