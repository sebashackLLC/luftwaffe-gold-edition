//Decompiled by Procyon!

package me.alpha432.oyvey.manager;

import java.net.*;
import java.util.function.*;
import net.minecraftforge.common.*;
import me.alpha432.oyvey.*;
import net.minecraft.client.*;
import java.nio.charset.*;
import java.io.*;
import net.minecraftforge.fml.common.gameevent.*;
import me.alpha432.oyvey.impl.gui.*;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.common.eventhandler.*;
import java.nio.file.attribute.*;
import java.nio.file.*;
import com.google.gson.*;
import java.util.concurrent.*;
import java.util.*;

public class IrcNameManager
{
    private static final String OYVEY_FOLDER = "oyvey";
    private static final String IRC_NAME_FILE = "ircname.json";
    private static final String IRC_SERVER = "irc.rizon.net";
    private static final int IRC_PORT = 6667;
    private static final String IRC_CHANNEL = "#luftwaffe";
    private static final String IRC_CHANNEL_KEY = "LuftWaffeKings12367";
    private static final Gson GSON;
    private static String cachedIrcName;
    private static boolean initialized;
    private static final Map<String, String> onlineIrcUsers;
    private static Socket ircSocket;
    private static BufferedWriter ircWriter;
    private static BufferedReader ircReader;
    private static ExecutorService ircExecutor;
    private static volatile boolean connected;
    private static String currentNick;
    private static final List<Consumer<IrcMessage>> messageListeners;
    
    public static void init() {
        if (IrcNameManager.initialized) {
            return;
        }
        IrcNameManager.initialized = true;
        if (!hasIrcName()) {
            MinecraftForge.EVENT_BUS.register((Object)new IrcNameManager());
        }
        else {
            connectToIrc();
        }
    }
    
    private static void connectToIrc() {
        if (IrcNameManager.ircExecutor != null) {
            IrcNameManager.ircExecutor.shutdownNow();
        }
        (IrcNameManager.ircExecutor = Executors.newSingleThreadExecutor()).submit(IrcNameManager::ircConnectionLoop);
    }
    
    private static void ircConnectionLoop() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                doConnect();
                readLoop();
            }
            catch (Exception e) {
                OyVey.LOGGER.error("IRC connection error", (Throwable)e);
                IrcNameManager.connected = false;
            }
            try {
                Thread.sleep(10000L);
                continue;
            }
            catch (InterruptedException e2) {}
            break;
        }
    }
    
    private static void doConnect() throws IOException {
        final String ircName = getIrcName();
        if (ircName == null) {
            return;
        }
        final Minecraft mc = Minecraft.func_71410_x();
        final String ign = (mc.func_110432_I() != null) ? mc.func_110432_I().func_111285_a() : "Unknown";
        IrcNameManager.currentNick = sanitizeNick(ign + "_" + ircName);
        OyVey.LOGGER.info("Connecting to IRC: irc.rizon.net");
        (IrcNameManager.ircSocket = new Socket("irc.rizon.net", 6667)).setSoTimeout(300000);
        IrcNameManager.ircWriter = new BufferedWriter(new OutputStreamWriter(IrcNameManager.ircSocket.getOutputStream(), StandardCharsets.UTF_8));
        IrcNameManager.ircReader = new BufferedReader(new InputStreamReader(IrcNameManager.ircSocket.getInputStream(), StandardCharsets.UTF_8));
        sendRaw("NICK " + IrcNameManager.currentNick);
        sendRaw("USER " + IrcNameManager.currentNick + " 0 * :" + ircName);
    }
    
    private static void readLoop() throws IOException {
        String line;
        while ((line = IrcNameManager.ircReader.readLine()) != null) {
            handleIrcMessage(line);
        }
    }
    
    private static void handleIrcMessage(final String line) {
        OyVey.LOGGER.info("IRC RAW: " + line);
        if (line.startsWith("PING")) {
            sendRaw("PONG" + line.substring(4));
            OyVey.LOGGER.info("IRC: Responded to PING");
            return;
        }
        final String[] parts = line.split(" ");
        if (parts.length < 2) {
            return;
        }
        final String s;
        final String command = s = ((parts.length > 1) ? parts[1] : "");
        switch (s) {
            case "001": {
                IrcNameManager.connected = true;
                sendRaw("JOIN #luftwaffe LuftWaffeKings12367");
                OyVey.LOGGER.info("Connected to IRC, joining #luftwaffe");
                break;
            }
            case "353": {
                final int channelIdx = line.indexOf("#luftwaffe");
                if (channelIdx != -1) {
                    final int colonIdx = line.indexOf(58, channelIdx);
                    if (colonIdx != -1) {
                        final String userList = line.substring(colonIdx + 1);
                        parseUserList(userList);
                    }
                    break;
                }
                break;
            }
            case "366": {
                OyVey.LOGGER.info("IRC online users: " + IrcNameManager.onlineIrcUsers.size());
                break;
            }
            case "JOIN": {
                if (parts.length >= 3) {
                    final String nick = extractNick(parts[0]);
                    if (!nick.equals(IrcNameManager.currentNick)) {
                        addUserFromNick(nick);
                    }
                    break;
                }
                break;
            }
            case "PART":
            case "QUIT": {
                final String nick = extractNick(parts[0]);
                removeUserByNick(nick);
                break;
            }
            case "NICK": {
                if (parts.length >= 3) {
                    final String oldNick = extractNick(parts[0]);
                    final String newNick = parts[2].startsWith(":") ? parts[2].substring(1) : parts[2];
                    removeUserByNick(oldNick);
                    addUserFromNick(newNick);
                    break;
                }
                break;
            }
            case "433": {
                IrcNameManager.currentNick += "_";
                sendRaw("NICK " + IrcNameManager.currentNick);
                break;
            }
            case "PRIVMSG": {
                if (parts.length < 4) {
                    break;
                }
                final String senderNick = extractNick(parts[0]);
                final String target = parts[2];
                if (!target.equalsIgnoreCase("#luftwaffe")) {
                    break;
                }
                final int msgStart = line.indexOf(58, 1);
                if (msgStart != -1) {
                    final String msg = line.substring(msgStart + 1);
                    final IrcMessage ircMsg = new IrcMessage(senderNick, msg);
                    final Iterator<Consumer<IrcMessage>> iterator;
                    Consumer<IrcMessage> listener;
                    final IrcMessage ircMessage;
                    Minecraft.func_71410_x().func_152344_a(() -> {
                        IrcNameManager.messageListeners.iterator();
                        while (iterator.hasNext()) {
                            listener = iterator.next();
                            listener.accept(ircMessage);
                        }
                        return;
                    });
                    break;
                }
                break;
            }
        }
    }
    
    private static void parseUserList(final String userList) {
        IrcNameManager.onlineIrcUsers.clear();
        final String[] split;
        final String[] users = split = userList.trim().split("\\s+");
        for (final String user : split) {
            final String nick = user.replaceAll("^[@+%&~]", "");
            addUserFromNick(nick);
        }
    }
    
    private static void addUserFromNick(final String nick) {
        final int underscoreIdx = nick.lastIndexOf(95);
        if (underscoreIdx > 0 && underscoreIdx < nick.length() - 1) {
            final String ign = nick.substring(0, underscoreIdx);
            final String ircName = nick.substring(underscoreIdx + 1);
            IrcNameManager.onlineIrcUsers.put(ign, ircName);
        }
    }
    
    private static void removeUserByNick(final String nick) {
        final int underscoreIdx = nick.lastIndexOf(95);
        if (underscoreIdx > 0) {
            final String ign = nick.substring(0, underscoreIdx);
            IrcNameManager.onlineIrcUsers.remove(ign);
        }
    }
    
    private static String extractNick(String prefix) {
        if (prefix.startsWith(":")) {
            prefix = prefix.substring(1);
        }
        final int exclamIdx = prefix.indexOf(33);
        return (exclamIdx > 0) ? prefix.substring(0, exclamIdx) : prefix;
    }
    
    private static String sanitizeNick(final String nick) {
        return nick.replaceAll("[^a-zA-Z0-9_\\-\\[\\]\\\\`^{}]", "").substring(0, Math.min(nick.length(), 30));
    }
    
    private static void sendRaw(final String message) {
        try {
            if (IrcNameManager.ircWriter != null) {
                IrcNameManager.ircWriter.write(message + "\r\n");
                IrcNameManager.ircWriter.flush();
            }
        }
        catch (IOException e) {
            OyVey.LOGGER.error("Failed to send IRC message", (Throwable)e);
        }
    }
    
    public static void disconnect() {
        IrcNameManager.connected = false;
        if (IrcNameManager.ircExecutor != null) {
            IrcNameManager.ircExecutor.shutdownNow();
            IrcNameManager.ircExecutor = null;
        }
        try {
            if (IrcNameManager.ircWriter != null) {
                sendRaw("QUIT :Closing");
                IrcNameManager.ircWriter.close();
            }
            if (IrcNameManager.ircReader != null) {
                IrcNameManager.ircReader.close();
            }
            if (IrcNameManager.ircSocket != null) {
                IrcNameManager.ircSocket.close();
            }
        }
        catch (IOException ex) {}
        IrcNameManager.ircWriter = null;
        IrcNameManager.ircReader = null;
        IrcNameManager.ircSocket = null;
    }
    
    public static String getIrcNameForPlayer(final String ign) {
        return IrcNameManager.onlineIrcUsers.get(ign);
    }
    
    public static boolean hasIrcNameForPlayer(final String ign) {
        return IrcNameManager.onlineIrcUsers.containsKey(ign);
    }
    
    public static Map<String, String> getOnlineIrcUsers() {
        return IrcNameManager.onlineIrcUsers;
    }
    
    public static boolean isConnected() {
        return IrcNameManager.connected;
    }
    
    public static void sendMessage(final String message) {
        if (!IrcNameManager.connected) {
            OyVey.LOGGER.warn("IRC: Cannot send message - not connected");
            return;
        }
        OyVey.LOGGER.info("IRC: Sending message to #luftwaffe: " + message);
        sendRaw("PRIVMSG #luftwaffe :" + message);
    }
    
    public static void addMessageListener(final Consumer<IrcMessage> listener) {
        IrcNameManager.messageListeners.add(listener);
    }
    
    public static void removeMessageListener(final Consumer<IrcMessage> listener) {
        IrcNameManager.messageListeners.remove(listener);
    }
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        if (hasIrcName()) {
            MinecraftForge.EVENT_BUS.unregister((Object)this);
            connectToIrc();
            return;
        }
        final Minecraft mc = Minecraft.func_71410_x();
        if (mc.field_71439_g != null && mc.field_71441_e != null && !(mc.field_71462_r instanceof IrcNamePopup)) {
            mc.func_147108_a((GuiScreen)new IrcNamePopup());
        }
    }
    
    public static boolean hasIrcName() {
        final Path filePath = getIrcNameFilePath();
        return Files.exists(filePath, new LinkOption[0]);
    }
    
    public static String getIrcName() {
        if (IrcNameManager.cachedIrcName != null) {
            return IrcNameManager.cachedIrcName;
        }
        final Path filePath = getIrcNameFilePath();
        if (!Files.exists(filePath, new LinkOption[0])) {
            return null;
        }
        try {
            final String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            final JsonObject json = (JsonObject)IrcNameManager.GSON.fromJson(content, (Class)JsonObject.class);
            if (json != null && json.has("ircName")) {
                return IrcNameManager.cachedIrcName = json.get("ircName").getAsString();
            }
        }
        catch (Exception e) {
            OyVey.LOGGER.error("Failed to read IRC name", (Throwable)e);
        }
        return null;
    }
    
    public static void saveIrcName(final String ircName) {
        final Path filePath = getIrcNameFilePath();
        try {
            Files.createDirectories(filePath.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
            final JsonObject json = new JsonObject();
            json.addProperty("ircName", ircName);
            Files.write(filePath, IrcNameManager.GSON.toJson((JsonElement)json).getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
            IrcNameManager.cachedIrcName = ircName;
            OyVey.LOGGER.info("IRC name saved: " + ircName);
        }
        catch (Exception e) {
            OyVey.LOGGER.error("Failed to save IRC name", (Throwable)e);
        }
    }
    
    private static Path getIrcNameFilePath() {
        return Paths.get("oyvey", "ircname.json");
    }
    
    public static void sendLaunchNotification() {
        connectToIrc();
    }
    
    public static void sendCloseNotification() {
        disconnect();
    }
    
    public static void stopPolling() {
        disconnect();
    }
    
    static {
        GSON = new GsonBuilder().setPrettyPrinting().create();
        IrcNameManager.cachedIrcName = null;
        IrcNameManager.initialized = false;
        onlineIrcUsers = new ConcurrentHashMap<String, String>();
        IrcNameManager.connected = false;
        messageListeners = new ArrayList<Consumer<IrcMessage>>();
    }
    
    public static class IrcMessage
    {
        public final String senderNick;
        public final String senderIgn;
        public final String senderIrcName;
        public final String message;
        
        public IrcMessage(final String senderNick, final String message) {
            this.senderNick = senderNick;
            this.message = message;
            final int idx = senderNick.lastIndexOf(95);
            if (idx > 0 && idx < senderNick.length() - 1) {
                this.senderIgn = senderNick.substring(0, idx);
                this.senderIrcName = senderNick.substring(idx + 1);
            }
            else {
                this.senderIgn = senderNick;
                this.senderIrcName = senderNick;
            }
        }
    }
}
