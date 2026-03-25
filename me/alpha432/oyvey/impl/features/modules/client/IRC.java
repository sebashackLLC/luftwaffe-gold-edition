//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.manager.*;
import org.lwjgl.input.*;
import me.alpha432.oyvey.impl.command.*;
import net.minecraft.init.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import java.util.regex.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.network.play.server.*;
import java.util.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.*;
import me.alpha432.oyvey.*;
import java.awt.*;
import me.alpha432.oyvey.api.util.render.*;
import net.minecraft.util.text.*;

public class IRC extends Module
{
    public Setting<Boolean> enableChat;
    public Setting<Boolean> chatSound;
    public Setting<Boolean> ircNames;
    public Setting<Bind> pingKey;
    public Setting<Integer> pingDuration;
    public Setting<Boolean> showNametag;
    public Setting<Boolean> pingSound;
    public Setting<Double> nametagScale;
    public Setting<Double> beamWidth;
    public Setting<ColorSetting> beaconColor;
    private BackupMarker currentMarker;
    private boolean pingKeyPressed;
    private long lastToggleTime;
    private static final Pattern BACKUP_PATTERN;
    private boolean listenerRegistered;
    
    public IRC() {
        super("IRC", "IRC system with chat and backup calling via Rizon", Module.Category.CLIENT, true, false, false);
        this.enableChat = (Setting<Boolean>)this.register(new Setting("EnableChat", (Object)true, "Enable IRC chat functionality"));
        this.chatSound = (Setting<Boolean>)this.register(new Setting("ChatSound", (Object)true, "Play sound for new chat messages"));
        this.ircNames = (Setting<Boolean>)this.register(new Setting("IRCNames", (Object)true, "Replace IGNs with IRC names in chat"));
        this.pingKey = (Setting<Bind>)this.register(new Setting("PingKey", (Object)new Bind(-1), "Keybind to call for backup"));
        this.pingDuration = (Setting<Integer>)this.register(new Setting("PingDuration", (Object)10, (Object)1, (Object)60, "Seconds to show ping beacon"));
        this.showNametag = (Setting<Boolean>)this.register(new Setting("ShowNametag", (Object)true, "Show nametag above ping location"));
        this.pingSound = (Setting<Boolean>)this.register(new Setting("PingSound", (Object)true, "Play sound for ping notifications"));
        this.nametagScale = (Setting<Double>)this.register(new Setting("NametagScale", (Object)2.0, (Object)0.5, (Object)5.0, "Nametag size multiplier"));
        this.beamWidth = (Setting<Double>)this.register(new Setting("BeamWidth", (Object)0.5, (Object)0.5, (Object)5.0, "Width of ping beacon beam"));
        this.beaconColor = (Setting<ColorSetting>)this.register(new Setting("BeaconColor", (Object)new ColorSetting(255, 0, 0, 150), "Color of the backup beacon"));
        this.currentMarker = null;
        this.pingKeyPressed = false;
        this.lastToggleTime = 0L;
        this.listenerRegistered = false;
    }
    
    public void onLoad() {
        if (!this.isEnabled()) {
            this.enabled.setValue((Object)true);
        }
        this.registerListener();
    }
    
    private void registerListener() {
        if (!this.listenerRegistered) {
            IrcNameManager.addMessageListener(this::handleIrcMessage);
            this.listenerRegistered = true;
        }
    }
    
    public void toggle() {
        this.lastToggleTime = System.currentTimeMillis();
        super.toggle();
    }
    
    public void onUpdate() {
        if (((Bind)this.pingKey.getValue()).getKey() != -1 && Keyboard.isKeyDown(((Bind)this.pingKey.getValue()).getKey())) {
            if (!this.pingKeyPressed) {
                this.pingKeyPressed = true;
                if (this.isEnabled() && System.currentTimeMillis() - this.lastToggleTime > 500L) {
                    this.callBackup();
                }
                else if (!this.isEnabled()) {
                    Command.sendMessage("§c[IRC] IRC module is not enabled!");
                }
            }
        }
        else {
            this.pingKeyPressed = false;
        }
    }
    
    public void onEnable() {
        this.currentMarker = null;
        this.lastToggleTime = System.currentTimeMillis();
        this.registerListener();
    }
    
    public void onDisable() {
        this.currentMarker = null;
    }
    
    private void handleIrcMessage(final IrcNameManager.IrcMessage msg) {
        if (!this.isEnabled()) {
            return;
        }
        final Matcher backupMatcher = IRC.BACKUP_PATTERN.matcher(msg.message);
        if (backupMatcher.matches()) {
            this.handleBackupMessage(backupMatcher);
            return;
        }
        if ((boolean)this.enableChat.getValue() && IRC.mc.field_71439_g != null) {
            Command.sendMessage("§b[IRC] §f" + msg.senderIrcName + "§7: §f" + msg.message);
            if (this.chatSound.getValue()) {
                IRC.mc.field_71441_e.func_184133_a((EntityPlayer)IRC.mc.field_71439_g, IRC.mc.field_71439_g.func_180425_c(), SoundEvents.field_193808_ex, SoundCategory.MASTER, 0.5f, 1.0f);
            }
        }
    }
    
    private void handleBackupMessage(final Matcher matcher) {
        final String username = matcher.group(1);
        final long x = Long.parseLong(matcher.group(2));
        final long z = Long.parseLong(matcher.group(3));
        final String dimension = matcher.group(4);
        final String server = matcher.group(5);
        String currentServer = "";
        if (IRC.mc.func_147104_D() != null) {
            currentServer = IRC.mc.func_147104_D().field_78845_b;
        }
        if (!server.isEmpty() && !currentServer.isEmpty() && !server.equalsIgnoreCase(currentServer) && !server.equalsIgnoreCase("Unknown")) {
            return;
        }
        this.currentMarker = new BackupMarker(username, x, z, dimension, System.currentTimeMillis());
        Command.sendMessage("§c[IRC] " + username + " needs backup at " + x + ", " + z + " (" + dimension + ")!");
        if ((boolean)this.pingSound.getValue() && IRC.mc.field_71439_g != null) {
            IRC.mc.field_71441_e.func_184133_a((EntityPlayer)IRC.mc.field_71439_g, IRC.mc.field_71439_g.func_180425_c(), SoundEvents.field_189107_dL, SoundCategory.MASTER, 1.0f, 1.0f);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(final ClientChatEvent event) {
        if (!(boolean)this.enableChat.getValue()) {
            return;
        }
        if (!IrcNameManager.isConnected()) {
            return;
        }
        final String msg = event.getMessage();
        if (!msg.startsWith("@")) {
            return;
        }
        event.setCanceled(true);
        final String ircMessage = msg.substring(1).trim();
        if (ircMessage.isEmpty()) {
            return;
        }
        String myName = IrcNameManager.getIrcName();
        if (myName == null) {
            myName = IRC.mc.field_71439_g.func_146103_bH().getName();
        }
        Command.sendMessage("§b[IRC] §a" + myName + "§7: §f" + ircMessage);
        IrcNameManager.sendMessage(ircMessage);
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (!(boolean)this.ircNames.getValue()) {
            return;
        }
        if (IRC.mc.field_71439_g == null) {
            return;
        }
        if (event.getPacket() instanceof SPacketChat) {
            final SPacketChat packet = (SPacketChat)event.getPacket();
            final String originalFormatted = packet.func_148915_c().func_150254_d();
            final String newFormatted = this.replaceIrcNamesInText(originalFormatted);
            if (!newFormatted.equals(originalFormatted)) {
                packet.field_148919_a = (ITextComponent)new TextComponentString(newFormatted);
            }
        }
    }
    
    private String replaceIrcNamesInText(String text) {
        final Map<String, String> onlineUsers = IrcNameManager.getOnlineIrcUsers();
        for (final Map.Entry<String, String> entry : onlineUsers.entrySet()) {
            final String ign = entry.getKey();
            final String ircName = entry.getValue();
            if (text.contains(ign)) {
                text = text.replace(ign, ircName);
            }
        }
        return text;
    }
    
    public void callBackup() {
        if (IRC.mc.field_71439_g == null || IRC.mc.field_71441_e == null) {
            Command.sendMessage("§c[IRC] Player or world not available");
            return;
        }
        if (!IrcNameManager.isConnected()) {
            Command.sendMessage("§c[IRC] Not connected to IRC");
            return;
        }
        String username = IrcNameManager.getIrcName();
        if (username == null) {
            username = IRC.mc.field_71439_g.func_146103_bH().getName();
        }
        final long posX = Math.round(IRC.mc.field_71439_g.field_70165_t / 10.0) * 10L;
        final long posZ = Math.round(IRC.mc.field_71439_g.field_70161_v / 10.0) * 10L;
        String server = "Unknown";
        if (IRC.mc.func_147104_D() != null) {
            server = IRC.mc.func_147104_D().field_78845_b;
        }
        final String dimension = this.getDimensionName();
        final String backupMsg = "BACKUP|" + username + "|" + posX + "|" + posZ + "|" + dimension + "|" + server;
        IrcNameManager.sendMessage(backupMsg);
        Command.sendMessage("§a[IRC] Backup call sent!");
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (IRC.mc.field_71439_g == null || IRC.mc.field_71441_e == null || this.currentMarker == null) {
            return;
        }
        final long age = System.currentTimeMillis() - this.currentMarker.timestamp;
        if (age > (int)this.pingDuration.getValue() * 1000L) {
            this.currentMarker = null;
            return;
        }
        this.renderBeacon(this.currentMarker, event.getPartialTicks());
        if (this.showNametag.getValue()) {
            this.renderNametag(this.currentMarker, event.getPartialTicks());
        }
    }
    
    private void renderBeacon(final BackupMarker marker, final float partialTicks) {
        final double x = marker.x + 0.5 - IRC.mc.func_175598_ae().field_78730_l;
        final double z = marker.z + 0.5 - IRC.mc.func_175598_ae().field_78728_n;
        final double minY = 0.0 - IRC.mc.func_175598_ae().field_78731_m;
        final double maxY = 256.0 - IRC.mc.func_175598_ae().field_78731_m;
        final float r = ((ColorSetting)this.beaconColor.getValue()).getRed() / 255.0f;
        final float g = ((ColorSetting)this.beaconColor.getValue()).getGreen() / 255.0f;
        final float b = ((ColorSetting)this.beaconColor.getValue()).getBlue() / 255.0f;
        final float a = ((ColorSetting)this.beaconColor.getValue()).getAlpha() / 255.0f;
        final double width = (double)this.beamWidth.getValue() * 0.5;
        GlStateManager.func_179094_E();
        GlStateManager.func_179123_a();
        GlStateManager.func_179090_x();
        GlStateManager.func_179097_i();
        GlStateManager.func_179140_f();
        GlStateManager.func_179129_p();
        GlStateManager.func_179147_l();
        GlStateManager.func_187401_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GL11.glMatrixMode(5889);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        final float fov = IRC.mc.field_71474_y.field_74334_X;
        final float aspect = IRC.mc.field_71443_c / (float)IRC.mc.field_71440_d;
        GLU.gluPerspective(fov, aspect, 0.01f, 1000.0f);
        GL11.glMatrixMode(5888);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(1);
        GL11.glColor4f(r, g, b, a);
        GL11.glVertex3d(x - width, minY, z - width);
        GL11.glVertex3d(x - width, maxY, z - width);
        GL11.glVertex3d(x + width, minY, z - width);
        GL11.glVertex3d(x + width, maxY, z - width);
        GL11.glVertex3d(x + width, minY, z + width);
        GL11.glVertex3d(x + width, maxY, z + width);
        GL11.glVertex3d(x - width, minY, z + width);
        GL11.glVertex3d(x - width, maxY, z + width);
        GL11.glVertex3d(x, minY, z);
        GL11.glVertex3d(x, maxY, z);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glColor4f(r, g, b, a * 0.3f);
        GL11.glVertex3d(x - width, minY, z - width);
        GL11.glVertex3d(x - width, maxY, z - width);
        GL11.glVertex3d(x + width, maxY, z - width);
        GL11.glVertex3d(x + width, minY, z - width);
        GL11.glVertex3d(x + width, minY, z + width);
        GL11.glVertex3d(x + width, maxY, z + width);
        GL11.glVertex3d(x - width, maxY, z + width);
        GL11.glVertex3d(x - width, minY, z + width);
        GL11.glVertex3d(x + width, minY, z - width);
        GL11.glVertex3d(x + width, maxY, z - width);
        GL11.glVertex3d(x + width, maxY, z + width);
        GL11.glVertex3d(x + width, minY, z + width);
        GL11.glVertex3d(x - width, minY, z + width);
        GL11.glVertex3d(x - width, maxY, z + width);
        GL11.glVertex3d(x - width, maxY, z - width);
        GL11.glVertex3d(x - width, minY, z - width);
        GL11.glEnd();
        GL11.glMatrixMode(5889);
        GL11.glPopMatrix();
        GL11.glMatrixMode(5888);
        GL11.glLineWidth(1.0f);
        GlStateManager.func_179089_o();
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179099_b();
        GlStateManager.func_179121_F();
    }
    
    private void renderNametag(final BackupMarker marker, final float partialTicks) {
        double x = marker.x - IRC.mc.func_175598_ae().field_78730_l;
        double z = marker.z - IRC.mc.func_175598_ae().field_78728_n;
        double y = IRC.mc.field_71439_g.field_70163_u + 2.5 - IRC.mc.func_175598_ae().field_78731_m;
        final String line1 = marker.username + " needs backup";
        final String line2 = "(" + marker.x + ", " + marker.z + ")";
        final int width1 = OyVey.textManager.getStringWidth(line1);
        final int width2 = OyVey.textManager.getStringWidth(line2);
        final int maxWidth = Math.max(width1, width2);
        double distance = Math.sqrt(x * x + y * y + z * z);
        final double minDistance = 0.5;
        if (distance < minDistance && distance > 0.01) {
            final double factor = minDistance / distance;
            x *= factor;
            y *= factor;
            z *= factor;
            distance = minDistance;
        }
        double scale;
        final double baseScale = scale = 0.015 * (double)this.nametagScale.getValue();
        if (distance > 5.0) {
            scale = baseScale * (1.0 + (distance - 5.0) * 0.12);
        }
        if (scale < 0.008) {
            scale = 0.008;
        }
        if (scale > 2.0) {
            scale = 2.0;
        }
        GlStateManager.func_179094_E();
        GlStateManager.func_179088_q();
        GlStateManager.func_179136_a(1.0f, -1500000.0f);
        GlStateManager.func_179140_f();
        GlStateManager.func_179129_p();
        GlStateManager.func_179097_i();
        GlStateManager.func_179118_c();
        GlStateManager.func_179137_b(x, y, z);
        GlStateManager.func_179114_b(-IRC.mc.func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
        final float xRot = (IRC.mc.field_71474_y.field_74320_O == 2) ? -1.0f : 1.0f;
        GlStateManager.func_179114_b(IRC.mc.func_175598_ae().field_78732_j, xRot, 0.0f, 0.0f);
        GlStateManager.func_179139_a(-scale, -scale, scale);
        GlStateManager.func_179147_l();
        final int fontHeight = IRC.mc.field_71466_p.field_78288_b;
        final int padding = 4;
        final int totalHeight = fontHeight * 2 + padding * 3;
        final int totalWidth = maxWidth + padding * 2;
        final int backgroundColor = new Color(0, 0, 0, 180).getRGB();
        final int borderColor = new Color(255, 255, 255, 255).getRGB();
        Render2DMethods.drawNameTagRect(-totalWidth / 2.0f, (float)(-totalHeight), totalWidth / 2.0f, 0.0f, backgroundColor, borderColor, 1.0f);
        final int line1Y = -totalHeight + padding + 2;
        final int line2Y = line1Y + fontHeight + 2;
        OyVey.textManager.drawString(line1, (float)(-width1 / 2), (float)line1Y, Color.WHITE.getRGB(), true);
        OyVey.textManager.drawString(line2, (float)(-width2 / 2), (float)line2Y, Color.WHITE.getRGB(), true);
        GlStateManager.func_179141_d();
        GlStateManager.func_179126_j();
        GlStateManager.func_179089_o();
        GlStateManager.func_179113_r();
        GlStateManager.func_179136_a(1.0f, 1500000.0f);
        GlStateManager.func_179121_F();
    }
    
    private String getDimensionName() {
        if (IRC.mc.field_71441_e == null) {
            return "Unknown";
        }
        final int dimensionId = IRC.mc.field_71441_e.field_73011_w.getDimension();
        switch (dimensionId) {
            case 0: {
                return "Overworld";
            }
            case -1: {
                return "Nether";
            }
            case 1: {
                return "End";
            }
            default: {
                return "Dimension " + dimensionId;
            }
        }
    }
    
    static {
        BACKUP_PATTERN = Pattern.compile("BACKUP\\|([^|]+)\\|(-?\\d+)\\|(-?\\d+)\\|([^|]+)\\|(.+)");
    }
    
    private static class BackupMarker
    {
        public final String username;
        public final long x;
        public final long z;
        public final String dimension;
        public final long timestamp;
        
        public BackupMarker(final String username, final long x, final long z, final String dimension, final long timestamp) {
            this.username = username;
            this.x = x;
            this.z = z;
            this.dimension = dimension;
            this.timestamp = timestamp;
        }
    }
}
