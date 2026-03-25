//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.discord;

import me.alpha432.oyvey.api.util.discord.connection.*;
import java.util.function.*;
import com.google.gson.*;
import java.lang.management.*;

public class DiscordIPC
{
    private static final Gson GSON;
    private static BiConsumer<Integer, String> onError;
    private static Connection c;
    private static Runnable onReady;
    private static boolean receivedDispatch;
    private static JsonObject queuedActivity;
    private static IPCUser user;
    
    public static IPCUser getUser() {
        return DiscordIPC.user;
    }
    
    public static void setOnError(final BiConsumer<Integer, String> onError) {
        DiscordIPC.onError = onError;
    }
    
    public static boolean start(final long appId, final Runnable onReady) {
        DiscordIPC.c = Connection.open((Consumer)DiscordIPC::onPacket);
        if (DiscordIPC.c == null) {
            return false;
        }
        DiscordIPC.onReady = onReady;
        final JsonObject o = new JsonObject();
        o.addProperty("v", (Number)1);
        o.addProperty("client_id", Long.toString(appId));
        DiscordIPC.c.write(Opcode.Handshake, o);
        return true;
    }
    
    public static boolean isConnected() {
        return DiscordIPC.c != null;
    }
    
    public static void setActivity(final RichPresence presence) {
        if (DiscordIPC.c == null) {
            return;
        }
        DiscordIPC.queuedActivity = presence.toJson();
        if (DiscordIPC.receivedDispatch) {
            sendActivity();
        }
    }
    
    public static void stop() {
        if (DiscordIPC.c != null) {
            DiscordIPC.c.close();
            DiscordIPC.c = null;
            DiscordIPC.onReady = null;
            DiscordIPC.receivedDispatch = false;
            DiscordIPC.queuedActivity = null;
            DiscordIPC.user = null;
        }
    }
    
    private static void sendActivity() {
        final JsonObject args = new JsonObject();
        args.addProperty("pid", (Number)getPID());
        args.add("activity", (JsonElement)DiscordIPC.queuedActivity);
        final JsonObject o = new JsonObject();
        o.addProperty("cmd", "SET_ACTIVITY");
        o.add("args", (JsonElement)args);
        DiscordIPC.c.write(Opcode.Frame, o);
        DiscordIPC.queuedActivity = null;
    }
    
    private static void onPacket(final Packet packet) {
        if (packet.opcode() == Opcode.Close) {
            if (DiscordIPC.onError != null) {
                DiscordIPC.onError.accept(packet.data().get("code").getAsInt(), packet.data().get("message").getAsString());
            }
            stop();
        }
        else if (packet.opcode() == Opcode.Frame) {
            if (packet.data().has("evt") && packet.data().get("evt").getAsString().equals("ERROR")) {
                final JsonObject d = packet.data().getAsJsonObject("data");
                if (DiscordIPC.onError != null) {
                    DiscordIPC.onError.accept(d.get("code").getAsInt(), d.get("message").getAsString());
                }
            }
            else if (packet.data().has("cmd") && packet.data().get("cmd").getAsString().equals("DISPATCH")) {
                DiscordIPC.receivedDispatch = true;
                DiscordIPC.user = (IPCUser)DiscordIPC.GSON.fromJson((JsonElement)packet.data().getAsJsonObject("data").getAsJsonObject("user"), (Class)IPCUser.class);
                if (DiscordIPC.onReady != null) {
                    DiscordIPC.onReady.run();
                }
                if (DiscordIPC.queuedActivity != null) {
                    sendActivity();
                }
            }
        }
    }
    
    private static int getPID() {
        final String pr = ManagementFactory.getRuntimeMXBean().getName();
        return Integer.parseInt(pr.substring(0, pr.indexOf(64)));
    }
    
    private static void defaultErrorCallback(final int code, final String message) {
        System.err.println("Discord IPC error " + code + " with message: " + message);
    }
    
    static {
        GSON = new Gson();
        DiscordIPC.onError = DiscordIPC::defaultErrorCallback;
    }
}
