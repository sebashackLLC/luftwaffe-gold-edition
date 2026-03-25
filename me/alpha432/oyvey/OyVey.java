//Decompiled by Procyon!

package me.alpha432.oyvey;

import net.minecraftforge.fml.common.*;
import me.alpha432.oyvey.manager.Capes.*;
import me.alpha432.oyvey.api.util.discord.*;
import me.alpha432.oyvey.api.managers.*;
import me.alpha432.oyvey.impl.gui.background.*;
import me.alpha432.oyvey.manager.*;
import net.minecraft.util.*;
import net.minecraft.client.*;
import java.nio.*;
import me.alpha432.oyvey.api.util.render.*;
import org.lwjgl.opengl.*;
import java.io.*;
import net.minecraftforge.fml.common.event.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import org.apache.logging.log4j.*;

@Mod(modid = "luftwaffe", name = "SomaGod", version = "2.0.5", acceptedMinecraftVersions = "[1.12.2]")
public class OyVey
{
    public static final String MODSUFFIX = ".xyz";
    public static final String MODNAME = "luftwaffe";
    public static final String GITHASH = "58cf156";
    public static final String GITREVISION = "65";
    public static final String GITDATE = "07/01/2026 15:54";
    public static final String MODVER = "2.0.5";
    public static final String BUILDNAME = "luftwaffe 2.0.5-58cf156+65";
    public static final Logger LOGGER;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static CapesManager capesManager;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static HoleManager holeManager;
    public static InventoryManager inventoryManager;
    public static PotionManager potionManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static SpeedManager speedManager;
    public static ReloadManager reloadManager;
    public static FileManager fileManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static EventManager eventManager;
    public static TextManager textManager;
    public static ChatManager chatManager;
    public static DiscordManager discordManager;
    public static TimerManager timerManager;
    public static BoostManager boostManager;
    public static CustomGuiScreen customGuiScreen;
    @Mod.Instance
    public static OyVey INSTANCE;
    private static boolean unloaded;
    
    public static void load() {
        OyVey.LOGGER.info("\n\nLoading luftwaffe by doick the king");
        OyVey.unloaded = false;
        if (OyVey.reloadManager != null) {
            OyVey.reloadManager.unload();
            OyVey.reloadManager = null;
        }
        OyVey.chatManager = new ChatManager();
        OyVey.textManager = new TextManager();
        OyVey.commandManager = new CommandManager();
        OyVey.friendManager = new FriendManager();
        OyVey.moduleManager = new ModuleManager();
        OyVey.rotationManager = new RotationManager();
        OyVey.packetManager = new PacketManager();
        OyVey.eventManager = new EventManager();
        OyVey.speedManager = new SpeedManager();
        OyVey.potionManager = new PotionManager();
        OyVey.inventoryManager = new InventoryManager();
        OyVey.serverManager = new ServerManager();
        OyVey.fileManager = new FileManager();
        OyVey.colorManager = new ColorManager();
        OyVey.positionManager = new PositionManager();
        OyVey.configManager = new ConfigManager();
        OyVey.holeManager = new HoleManager();
        OyVey.timerManager = new TimerManager();
        OyVey.boostManager = new BoostManager();
        OyVey.LOGGER.info("Managers loaded.");
        OyVey.moduleManager.init();
        OyVey.LOGGER.info("Modules loaded.");
        OyVey.configManager.init();
        OyVey.eventManager.init();
        OyVey.LOGGER.info("EventManager loaded.");
        OyVey.textManager.init(true);
        OyVey.moduleManager.onLoad();
        setWindowIcon();
        IrcNameManager.init();
        OyVey.LOGGER.info("IrcNameManager loaded.");
        OyVey.LOGGER.info("successfully loaded!\n");
    }
    
    public static void unload(final boolean unload) {
        OyVey.LOGGER.info("\n\nunloading this shit rn");
        if (unload) {
            (OyVey.reloadManager = new ReloadManager()).init((OyVey.commandManager != null) ? OyVey.commandManager.getPrefix() : ".");
        }
        onUnload();
        OyVey.eventManager = null;
        OyVey.friendManager = null;
        OyVey.speedManager = null;
        OyVey.holeManager = null;
        OyVey.positionManager = null;
        OyVey.rotationManager = null;
        OyVey.configManager = null;
        OyVey.commandManager = null;
        OyVey.colorManager = null;
        OyVey.serverManager = null;
        OyVey.fileManager = null;
        OyVey.potionManager = null;
        OyVey.inventoryManager = null;
        OyVey.moduleManager = null;
        OyVey.chatManager = null;
        OyVey.textManager = null;
        OyVey.timerManager = null;
        OyVey.boostManager = null;
        OyVey.LOGGER.info("OyVey unloaded!\n");
    }
    
    public static void reload() {
        unload(false);
        load();
    }
    
    public static void onUnload() {
        if (!OyVey.unloaded) {
            IrcNameManager.sendCloseNotification();
            if (OyVey.eventManager != null) {
                OyVey.eventManager.onUnload();
            }
            if (OyVey.moduleManager != null) {
                OyVey.moduleManager.onUnload();
            }
            if (OyVey.configManager != null) {
                OyVey.configManager.saveConfig(OyVey.configManager.config.replaceFirst("oyvey/", "").replace("/", ""));
            }
            if (OyVey.moduleManager != null) {
                OyVey.moduleManager.onUnloadPost();
            }
            OyVey.unloaded = true;
        }
    }
    
    public static void setWindowIcon() {
        if (Util.func_110647_a() != Util.EnumOS.OSX) {
            try (final InputStream inputStream16x = Minecraft.class.getResourceAsStream("/luftwaffe/icon2/16x16.png");
                 final InputStream inputStream32x = Minecraft.class.getResourceAsStream("/luftwaffe/icon2/32x32.png")) {
                final ByteBuffer[] icons = { IconUtil.INSTANCE.readImageToBuffer(inputStream16x), IconUtil.INSTANCE.readImageToBuffer(inputStream32x) };
                Display.setIcon(icons);
            }
            catch (Exception e) {
                OyVey.LOGGER.error("Couldn't set Windows Icon", (Throwable)e);
            }
        }
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        OyVey.LOGGER.info("lel");
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Display.setTitle("luftwaffe.gold");
        OyVey.customGuiScreen = new CustomGuiScreen();
        load();
        DiscordRPC.getInstance();
        initDRPC();
    }
    
    private static void initDRPC() {
        (OyVey.discordManager = DiscordManager.getInstance()).setDetails((String)DiscordRPC.getInstance().rpcText.getValue() + (DiscordRPC.getInstance().rpcIgn.getValue() ? (" | Playing as " + Minecraft.func_71410_x().func_110432_I().func_111285_a()) : ""));
        OyVey.discordManager.setState("2.0.5-58cf15665");
        OyVey.discordManager.setLargeImage("luftwaffe", "nazi ahhh client");
        OyVey.discordManager.setSmallImage("luftwaffe", "luftwaffe");
        OyVey.discordManager.setStartTimestampToNow();
    }
    
    static {
        LOGGER = LogManager.getLogger("Luftwaffe");
        OyVey.unloaded = false;
    }
}
