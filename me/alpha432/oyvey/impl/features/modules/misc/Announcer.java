//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.misc;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.util.math.*;
import com.mojang.realmsclient.gui.*;
import java.util.*;
import me.alpha432.oyvey.api.util.*;
import net.minecraft.util.*;
import java.util.concurrent.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.common.gameevent.*;
import java.text.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraft.item.*;
import net.minecraft.client.*;
import me.alpha432.oyvey.impl.command.*;

public class Announcer extends Module
{
    public static Announcer INSTANCE;
    Setting<Boolean> breakBlock;
    Setting<Boolean> place;
    Setting<Boolean> eat;
    Setting<Boolean> walk;
    Setting<Boolean> joins;
    Setting<Boolean> timeMessages;
    Setting<Boolean> clientSide;
    public static String[] breakMessages;
    public static String[] placeMessages;
    public static String[] eatMessages;
    public static String[] walkMessages;
    public static String[] joinMessages;
    public static String[] leaveMessages;
    public static String[] morningMessages;
    public static String[] noonMessages;
    public static String[] afterNoonMessages;
    public static String[] sunsetMessages;
    public static String[] midNightMessages;
    public static String[] nightMessages;
    public static String[] bedtimeMessages;
    public static String[] dayLightMessages;
    Setting<Integer> delay;
    public Queue<String> messages;
    public static int blockBrokeDelay;
    static int blockPlacedDelay;
    static int jumpDelay;
    static int attackDelay;
    static int eattingDelay;
    static long lastPositionUpdate;
    static double lastPositionX;
    static double lastPositionY;
    static double lastPositionZ;
    private static double speed;
    Timer timer;
    Timer timeCooldown;
    String heldItem;
    int blocksPlaced;
    int blocksBroken;
    int eaten;
    Random random;
    
    public Announcer() {
        super("Announcer", ChatFormatting.GOLD + "Rich module", Module.Category.MISC, true, false, false);
        this.breakBlock = (Setting<Boolean>)this.register(new Setting("Break", (Object)true));
        this.place = (Setting<Boolean>)this.register(new Setting("Place", (Object)true));
        this.eat = (Setting<Boolean>)this.register(new Setting("Eat", (Object)true));
        this.walk = (Setting<Boolean>)this.register(new Setting("Walk", (Object)true));
        this.joins = (Setting<Boolean>)this.register(new Setting("Joins", (Object)true));
        this.timeMessages = (Setting<Boolean>)this.register(new Setting("World Time", (Object)true));
        this.clientSide = (Setting<Boolean>)this.register(new Setting("Client Side", (Object)true));
        this.delay = (Setting<Integer>)this.register(new Setting("Delay", (Object)5, (Object)1, (Object)15));
        this.messages = new LinkedList<String>();
        this.timer = new Timer();
        this.timeCooldown = new Timer();
        this.heldItem = "";
        this.blocksPlaced = 0;
        this.blocksBroken = 0;
        this.eaten = 0;
        this.random = new Random();
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock && Announcer.mc.field_71439_g.func_184586_b(EnumHand.MAIN_HAND).func_77973_b() instanceof ItemBlock) {
            if (!(boolean)this.place.getValue()) {
                return;
            }
            ++this.blocksPlaced;
            final int randomNum = ThreadLocalRandom.current().nextInt(1, 11);
            if (Announcer.blockPlacedDelay >= 150 * (int)this.delay.getValue() && this.blocksPlaced > randomNum) {
                final String msg = Announcer.placeMessages[this.random.nextInt(Announcer.placeMessages.length)].replace("{amount}", "" + this.blocksPlaced).replace("{name}", "" + Announcer.mc.field_71439_g.func_184614_ca().func_82833_r());
                this.sendMessage(msg);
                this.blocksPlaced = 0;
                Announcer.blockPlacedDelay = 0;
            }
        }
    }
    
    @SubscribeEvent
    public void onConnection(final ConnectionEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        if (Announcer.mc.field_71439_g.field_70173_aa <= 20) {
            return;
        }
        if ((boolean)this.joins.getValue() && !event.getName().equalsIgnoreCase(Announcer.mc.field_71449_j.func_111285_a()) && event.getName() != null && !event.getName().equalsIgnoreCase("")) {
            this.sendMessage(Announcer.leaveMessages[this.random.nextInt(Announcer.leaveMessages.length)].replace("%name%", event.getName()));
        }
    }
    
    public void onBreakBlock(final BlockPos pos) {
        if (!(boolean)this.breakBlock.getValue()) {
            return;
        }
        ++this.blocksBroken;
        final int randomNum = ThreadLocalRandom.current().nextInt(1, 11);
        if (Announcer.blockBrokeDelay >= 300 * (int)this.delay.getValue() && this.blocksBroken > randomNum) {
            final Random random = new Random();
            final String msg = Announcer.breakMessages[random.nextInt(Announcer.breakMessages.length)].replace("{amount}", "" + this.blocksBroken).replace("{name}", "" + Announcer.mc.field_71441_e.func_180495_p(pos).func_177230_c().func_149732_F());
            this.sendMessage(msg);
            this.blocksBroken = 0;
            Announcer.blockBrokeDelay = 0;
        }
    }
    
    public void doTime() {
        if (!this.timeCooldown.passedMs(100L)) {
            return;
        }
        if (Announcer.mc.field_71439_g.field_70173_aa < 200) {
            return;
        }
        if (Announcer.mc.field_71441_e.field_73011_w.getWorldTime() == 0L) {
            this.sendMessage(Announcer.morningMessages[this.random.nextInt(Announcer.morningMessages.length)]);
            this.timeCooldown.reset();
        }
        if (Announcer.mc.field_71441_e.field_73011_w.getWorldTime() == 4000L) {
            this.sendMessage(Announcer.noonMessages[this.random.nextInt(Announcer.noonMessages.length)]);
            this.timeCooldown.reset();
        }
        if (Announcer.mc.field_71441_e.field_73011_w.getWorldTime() == 6000L) {
            this.sendMessage(Announcer.afterNoonMessages[this.random.nextInt(Announcer.afterNoonMessages.length)]);
            this.timeCooldown.reset();
        }
        if (Announcer.mc.field_71441_e.field_73011_w.getWorldTime() == 13000L) {
            this.sendMessage(Announcer.bedtimeMessages[this.random.nextInt(Announcer.bedtimeMessages.length)]);
            this.timeCooldown.reset();
        }
        if (Announcer.mc.field_71441_e.field_73011_w.getWorldTime() == 14000L) {
            this.sendMessage(Announcer.nightMessages[this.random.nextInt(Announcer.nightMessages.length)]);
            this.timeCooldown.reset();
        }
        if (Announcer.mc.field_71441_e.field_73011_w.getWorldTime() == 16000L) {
            this.sendMessage(Announcer.sunsetMessages[this.random.nextInt(Announcer.sunsetMessages.length)]);
            this.timeCooldown.reset();
        }
        if (Announcer.mc.field_71441_e.field_73011_w.getWorldTime() == 18000L) {
            this.sendMessage(Announcer.midNightMessages[this.random.nextInt(Announcer.midNightMessages.length)]);
            this.timeCooldown.reset();
        }
        if (Announcer.mc.field_71441_e.field_73011_w.getWorldTime() == 23000L) {
            this.sendMessage(Announcer.dayLightMessages[this.random.nextInt(Announcer.dayLightMessages.length)]);
            this.timeCooldown.reset();
        }
    }
    
    @SubscribeEvent
    public void onUpdate(final TickEvent.ClientTickEvent event) {
        if (NullUtils.nullCheck()) {
            return;
        }
        ++Announcer.blockBrokeDelay;
        ++Announcer.blockPlacedDelay;
        ++Announcer.jumpDelay;
        ++Announcer.attackDelay;
        ++Announcer.eattingDelay;
        this.heldItem = Announcer.mc.field_71439_g.func_184614_ca().func_82833_r();
        ++Announcer.blockBrokeDelay;
        ++Announcer.blockPlacedDelay;
        ++Announcer.jumpDelay;
        ++Announcer.attackDelay;
        ++Announcer.eattingDelay;
        this.heldItem = Announcer.mc.field_71439_g.func_184614_ca().func_82833_r();
        final double d0;
        final double d2;
        final double d3;
        if ((boolean)this.walk.getValue() && Announcer.lastPositionUpdate + 5000L * (int)this.delay.getValue() < System.currentTimeMillis() && (Announcer.speed = Math.sqrt((d0 = Announcer.lastPositionX - Announcer.mc.field_71439_g.field_70142_S) * d0 + (d2 = Announcer.lastPositionY - Announcer.mc.field_71439_g.field_70137_T) * d2 + (d3 = Announcer.lastPositionZ - Announcer.mc.field_71439_g.field_70136_U) * d3)) > 1.0 && Announcer.speed <= 5000.0) {
            final String walkAmount = new DecimalFormat("0.00").format(Announcer.speed);
            final Random random = new Random();
            this.sendMessage(Announcer.walkMessages[random.nextInt(Announcer.walkMessages.length)].replace("{blocks}", "" + walkAmount));
            Announcer.lastPositionUpdate = System.currentTimeMillis();
            Announcer.lastPositionX = Announcer.mc.field_71439_g.field_70142_S;
            Announcer.lastPositionY = Announcer.mc.field_71439_g.field_70137_T;
            Announcer.lastPositionZ = Announcer.mc.field_71439_g.field_70136_U;
        }
        if (!(boolean)this.clientSide.getValue() && this.timer.passedMs((int)this.delay.getValue() * 1000L) && !this.messages.isEmpty()) {
            Announcer.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketChatMessage((String)this.messages.poll()));
            this.timer.reset();
        }
        if (this.timeMessages.getValue()) {
            this.doTime();
        }
    }
    
    @SubscribeEvent
    public void onEntityEat(final LivingEntityUseItemEvent.Finish event) {
        if (!(boolean)this.eat.getValue()) {
            return;
        }
        final int randomNum = ThreadLocalRandom.current().nextInt(1, 11);
        if (event.getEntity() == Announcer.mc.field_71439_g && (event.getItem().func_77973_b() instanceof ItemFood || event.getItem().func_77973_b() instanceof ItemAppleGold)) {
            ++this.eaten;
            if (Announcer.eattingDelay >= 300 * (int)this.delay.getValue() && this.eaten > randomNum) {
                final Random random = new Random();
                this.sendMessage(Announcer.eatMessages[random.nextInt(Announcer.eatMessages.length)].replace("{amount}", "" + this.eaten).replace("{name}", "" + event.getItem().func_82833_r()));
                this.eaten = 0;
                Announcer.eattingDelay = 0;
            }
        }
    }
    
    public void onDisable() {
        super.onDisable();
        if (NullUtils.nullCheck()) {
            return;
        }
    }
    
    public void onEnable() {
        super.onEnable();
        if (NullUtils.nullCheck()) {
            return;
        }
    }
    
    public void sendMessage(String message) {
        message = message.replace("Sn0w", "SomaGod.CC");
        final String ip = (Minecraft.func_71410_x().func_71356_B() || Announcer.mc.func_147104_D() == null) ? "Singleplayer" : Announcer.mc.func_147104_D().field_78845_b.toLowerCase();
        message = message.replace("%serverip%", ip);
        if (this.clientSide.getValue()) {
            Command.sendMessage(ChatFormatting.WHITE + message);
        }
        else {
            this.messages.add(message);
        }
    }
    
    public String getDescription() {
        return "The " + ChatFormatting.GOLD + "rich " + ChatFormatting.WHITE + "module";
    }
    
    static {
        Announcer.breakMessages = new String[] { "I just mined {amount} {name} thanks to Sn0w!", "\u042f \u0442\u043e\u043b\u044c\u043a\u043e \u0447\u0442\u043e \u0434\u043e\u0431\u044b\u043b {amount} {name} \u0431\u043b\u043e\u043a\u0430 \u0431\u043b\u0430\u0433\u043e\u0434\u0430\u0440\u044f Sn0w!" };
        Announcer.placeMessages = new String[] { "I just built a castle made out of {amount} {name} thanks to Sn0w!", "\u042f \u0442\u043e\u043b\u044c\u043a\u043e \u0447\u0442\u043e \u043f\u043e\u0441\u0442\u0440\u043e\u0438\u043b \u0437\u0430\u043c\u043e\u043a \u0438\u0437 {amount} {name} \u0431\u043b\u0430\u0433\u043e\u0434\u0430\u0440\u044f Sn0w!" };
        Announcer.eatMessages = new String[] { "I just ate {amount} {name} thanks to Sn0w!", "\u042f \u0442\u043e\u043b\u044c\u043a\u043e \u0447\u0442\u043e \u0441\u044a\u0435\u043b {amount} {name} \u0431\u043b\u0430\u0433\u043e\u0434\u0430\u0440\u044f Sn0w!" };
        Announcer.walkMessages = new String[] { "I just magically teleported {blocks} blocks thanks to Sn0w!", "\u042f \u043f\u0440\u043e\u0441\u0442\u043e \u0432\u043e\u043b\u0448\u0435\u0431\u043d\u044b\u043c \u043e\u0431\u0440\u0430\u0437\u043e\u043c \u0442\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u043e\u0432\u0430\u043b {blocks} \u0431\u043b\u043e\u043a\u043e\u0432 \u0431\u043b\u0430\u0433\u043e\u0434\u0430\u0440\u044f Sn0w!" };
        Announcer.joinMessages = new String[] { "Welcome to %serverip% %name%!", "Welcome to hell, %name%.", "Welcome %name% everyone!", "Hey, %name%", "AbdulaSalaha blesses you, %name%", "Greetings, %name%" };
        Announcer.leaveMessages = new String[] { "%name% DoickSwag2b2t likes you" };
        Announcer.morningMessages = new String[] { "I survived another night!", "Good Morning!", "You survived another night!", "The sun is rising in the east, hurrah, hurrah!" };
        Announcer.noonMessages = new String[] { "Let's go tanning!", "Let's go to the beach!", "Enjoy the sun outside! It is currently very bright!", "It's the brightest time of the day!" };
        Announcer.afterNoonMessages = new String[] { "IT'S HIGH NOON! is what ive would of said if i used future client", "Good afternoon!" };
        Announcer.sunsetMessages = new String[] { "You can say \"Sunset has now ended! You may eat your lunch now if you are a muslim.\" if you use futureshit" };
        Announcer.midNightMessages = new String[] { "It's so dark outside...", "Its scary out there!" };
        Announcer.nightMessages = new String[] { "Let's get comfy!", "Netflix and chill!", "You survived another day!" };
        Announcer.bedtimeMessages = new String[] { "BEDTIME NOW!!!", "if ur name is mcswag bedtime is IMMEDIATE.", "You may now sleep!" };
        Announcer.dayLightMessages = new String[] { "Good bye, zombies!", "All monsters will be smited down thanks to my cheat." };
        Announcer.blockBrokeDelay = 0;
        Announcer.blockPlacedDelay = 0;
        Announcer.jumpDelay = 0;
        Announcer.attackDelay = 0;
        Announcer.eattingDelay = 0;
    }
}
