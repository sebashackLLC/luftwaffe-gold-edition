//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.misc;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import com.mojang.realmsclient.gui.*;
import java.text.*;
import java.util.*;
import net.minecraft.util.text.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.network.play.client.*;
import me.alpha432.oyvey.*;

public class BetterChat extends Module
{
    private static final String[] RAINBOW_COLORS;
    private static final String[] SN0W_RAINBOW;
    private static final String[] TRANS_COLORS;
    private static BetterChat INSTANCE;
    Setting<timemodes> timeModes;
    public Setting<SuffixMode> suffix;
    public Setting<Colors> suffixColor;
    public Setting<Boolean> suffixBold;
    public Setting<Boolean> suffixItalic;
    public Setting<Boolean> rainbowText;
    Setting<modes> Modes;
    public Setting<Colors> customColor;
    public Setting<Boolean> doubleColor;
    public Setting<Colors> customColor2;
    public Setting<Boolean> boldText;
    public Setting<Boolean> italicText;
    public Setting<Modificator> textModificator;
    public Setting<Boolean> coolText;
    public Setting<Background> background;
    public Setting<Boolean> infinite;
    public Setting<Boolean> animatedChat;
    private final Random random;
    private int lastColorIndex;
    private int sn0wIndex;
    public boolean check;
    private int transIndex;
    private int customDoubleIndex;
    private String[] filters;
    private final Map<Character, String> coolTextMap;
    
    public BetterChat() {
        super("BetterChat", "Chat tweaks", Module.Category.MISC, true, false, false);
        this.timeModes = (Setting<timemodes>)this.register(new Setting("TimeStamp", (Object)timemodes.None));
        this.suffix = (Setting<SuffixMode>)this.register(new Setting("Suffix", (Object)SuffixMode.None));
        this.suffixColor = (Setting<Colors>)this.register(new Setting("SuffixColor", (Object)Colors.Red, v -> this.suffix.getValue() != SuffixMode.None));
        this.suffixBold = (Setting<Boolean>)this.register(new Setting("SuffixBold", (Object)false, v -> this.suffix.getValue() != SuffixMode.None));
        this.suffixItalic = (Setting<Boolean>)this.register(new Setting("SuffixItalic", (Object)false, v -> this.suffix.getValue() != SuffixMode.None));
        this.rainbowText = (Setting<Boolean>)this.register(new Setting("ColoredText", (Object)false));
        this.Modes = (Setting<modes>)this.register(new Setting("ColorScheme", (Object)modes.Sn0w, v -> (boolean)this.rainbowText.getValue()));
        this.customColor = (Setting<Colors>)this.register(new Setting("Color", (Object)Colors.Green, v -> this.Modes.getValue() == modes.Custom && (boolean)this.rainbowText.getValue()));
        this.doubleColor = (Setting<Boolean>)this.register(new Setting("Double", (Object)false, v -> this.Modes.getValue() == modes.Custom && (boolean)this.rainbowText.getValue()));
        this.customColor2 = (Setting<Colors>)this.register(new Setting("Color2", (Object)Colors.Blue, v -> this.Modes.getValue() == modes.Custom && (boolean)this.doubleColor.getValue() && (boolean)this.rainbowText.getValue()));
        this.boldText = (Setting<Boolean>)this.register(new Setting("Bold", (Object)false, v -> (boolean)this.rainbowText.getValue()));
        this.italicText = (Setting<Boolean>)this.register(new Setting("Italic", (Object)false, v -> (boolean)this.rainbowText.getValue()));
        this.textModificator = (Setting<Modificator>)this.register(new Setting("Modificator", (Object)Modificator.None, v -> (boolean)this.rainbowText.getValue()));
        this.coolText = (Setting<Boolean>)this.register(new Setting("CoolText", (Object)false, v -> (boolean)this.rainbowText.getValue()));
        this.background = (Setting<Background>)this.register(new Setting("Background", (Object)Background.Default));
        this.infinite = (Setting<Boolean>)this.register(new Setting("InfiniteChat", (Object)false, "Makes your chat infinite."));
        this.animatedChat = (Setting<Boolean>)this.register(new Setting("AnimatedChat", (Object)false, "Animates new messages sliding in from the left."));
        this.random = new Random();
        this.lastColorIndex = -1;
        this.sn0wIndex = 0;
        this.transIndex = 0;
        this.customDoubleIndex = 0;
        this.filters = new String[] { ".", "/", ",", ":", "`", "=", "-" };
        this.coolTextMap = new HashMap<Character, String>() {
            {
                this.put('a', "\u1d00");
                this.put('b', "\u0299");
                this.put('c', "\u1d04");
                this.put('d', "\u1d05");
                this.put('e', "\u1d07");
                this.put('f', "\ua730");
                this.put('g', "\u0262");
                this.put('h', "\u029c");
                this.put('i', "\u026a");
                this.put('j', "\u1d0a");
                this.put('k', "\u1d0b");
                this.put('l', "\u029f");
                this.put('m', "\u1d0d");
                this.put('n', "\u0274");
                this.put('o', "\u1d0f");
                this.put('p', "\u1d18");
                this.put('q', "\u01eb");
                this.put('r', "\u0280");
                this.put('s', "\ua731");
                this.put('t', "\u1d1b");
                this.put('u', "\u1d1c");
                this.put('v', "\u1d20");
                this.put('w', "\u1d21");
                this.put('x', "x");
                this.put('y', "\u028f");
                this.put('z', "\u1d22");
                this.put('A', "\u1d00");
                this.put('B', "\u0299");
                this.put('C', "\u1d04");
                this.put('D', "\u1d05");
                this.put('E', "\u1d07");
                this.put('F', "\ua730");
                this.put('G', "\u0262");
                this.put('H', "\u029c");
                this.put('I', "\u026a");
                this.put('J', "\u1d0a");
                this.put('K', "\u1d0b");
                this.put('L', "\u029f");
                this.put('M', "\u1d0d");
                this.put('N', "\u0274");
                this.put('O', "\u1d0f");
                this.put('P', "\u1d18");
                this.put('Q', "\u01eb");
                this.put('R', "\u0280");
                this.put('S', "\ua731");
                this.put('T', "\u1d1b");
                this.put('U', "\u1d1c");
                this.put('V', "\u1d20");
                this.put('W', "\u1d21");
                this.put('X', "x");
                this.put('Y', "\u028f");
                this.put('Z', "\u1d22");
                this.put('0', "\ud835\udff6");
                this.put('1', "\ud835\udff7");
                this.put('2', "\ud835\udff8");
                this.put('3', "\ud835\udff9");
                this.put('4', "\ud835\udffa");
                this.put('5', "\ud835\udffb");
                this.put('6', "\ud835\udffc");
                this.put('7', "\ud835\udffd");
                this.put('8', "\ud835\udffe");
                this.put('9', "\ud835\udfff");
                this.put(' ', " ");
                this.put('!', "!");
                this.put('?', "?");
                this.put('.', ".");
                this.put(',', ",");
                this.put(':', ":");
                this.put(';', ";");
                this.put('-', "-");
                this.put('_', "_");
                this.put('(', "(");
                this.put(')', ")");
                this.put('[', "[");
                this.put(']', "]");
                this.put('{', "{");
                this.put('}', "}");
                this.put('|', "|");
                this.put('/', "/");
                this.put('\\', "\\");
                this.put('@', "@");
                this.put('#', "#");
                this.put('$', "$");
                this.put('%', "%");
                this.put('^', "^");
                this.put('&', "&");
                this.put('*', "*");
                this.put('+', "+");
                this.put('=', "=");
                this.put('<', "<");
                this.put('>', ">");
                this.put('~', "~");
                this.put('`', "`");
            }
        };
        this.setInstance();
    }
    
    public static BetterChat getInstance() {
        if (BetterChat.INSTANCE == null) {
            BetterChat.INSTANCE = new BetterChat();
        }
        return BetterChat.INSTANCE;
    }
    
    private void setInstance() {
        BetterChat.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onChat(final ClientChatEvent event) {
        final String originalMessage = event.getMessage();
        if (originalMessage.isEmpty() || this.isCommandPrefix(originalMessage.charAt(0))) {
            return;
        }
        String modifiedMessage = this.applyTextModificator(originalMessage);
        modifiedMessage = this.applyTextStyle(modifiedMessage);
        if (this.coolText.getValue()) {
            modifiedMessage = this.applyCoolText(modifiedMessage);
        }
        if (this.rainbowText.getValue()) {
            final StringBuilder rainbowMessage = new StringBuilder();
            if (this.Modes.getValue() == modes.Sn0w) {
                for (final char c : modifiedMessage.toCharArray()) {
                    if (c == ' ') {
                        rainbowMessage.append(c);
                    }
                    else {
                        final String color = this.getSn0wRainbow();
                        rainbowMessage.append(color).append(this.getFormatting()).append(c);
                    }
                }
            }
            else if (this.Modes.getValue() == modes.Custom) {
                if (this.doubleColor.getValue()) {
                    for (final char c : modifiedMessage.toCharArray()) {
                        if (c == ' ') {
                            rainbowMessage.append(c);
                        }
                        else {
                            final String color = this.getCustomDoubleRainbow();
                            rainbowMessage.append(color).append(this.getFormatting()).append(c);
                        }
                    }
                }
                else {
                    final String colorCode = ((Colors)this.customColor.getValue()).getCode();
                    rainbowMessage.append(colorCode).append(this.getFormatting()).append(modifiedMessage);
                }
            }
            else if (this.Modes.getValue() == modes.Trans) {
                for (final char c : modifiedMessage.toCharArray()) {
                    if (c == ' ') {
                        rainbowMessage.append(c);
                    }
                    else {
                        final String color = this.getTransRainbow();
                        rainbowMessage.append(color).append(this.getFormatting()).append(c);
                    }
                }
            }
            else {
                for (final char c : modifiedMessage.toCharArray()) {
                    if (c == ' ') {
                        rainbowMessage.append(c);
                    }
                    else {
                        int randomIndex;
                        do {
                            randomIndex = this.random.nextInt(BetterChat.RAINBOW_COLORS.length);
                        } while (randomIndex == this.lastColorIndex && BetterChat.RAINBOW_COLORS.length > 1);
                        this.lastColorIndex = randomIndex;
                        rainbowMessage.append(BetterChat.RAINBOW_COLORS[randomIndex]);
                        rainbowMessage.append(this.getFormatting());
                        rainbowMessage.append(c);
                    }
                }
            }
            String finalMessage = rainbowMessage.toString();
            if (finalMessage.endsWith("&")) {
                finalMessage = finalMessage.substring(0, finalMessage.length() - 1);
            }
            event.setMessage(finalMessage);
        }
        else {
            event.setMessage(modifiedMessage);
        }
    }
    
    @SubscribeEvent
    public void onMessageRecieved(final ClientChatReceivedEvent event) {
        if (this.timeModes.getValue() == timemodes.Sn0w && this.timeModes.getValue() != timemodes.None) {
            final TextComponentString newTextComponentString = new TextComponentString(ChatFormatting.BLUE + "<" + ChatFormatting.AQUA + new SimpleDateFormat("k:mm").format(new Date()) + ChatFormatting.BLUE + ">" + ChatFormatting.RESET + " ");
            newTextComponentString.func_150257_a(event.getMessage());
            event.setMessage((ITextComponent)newTextComponentString);
        }
        else if (this.timeModes.getValue() != timemodes.None) {
            final TextComponentString newTextComponentString = new TextComponentString(ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + new SimpleDateFormat("k:mm").format(new Date()) + ChatFormatting.DARK_PURPLE + "]" + ChatFormatting.RESET + " ");
            newTextComponentString.func_150257_a(event.getMessage());
            event.setMessage((ITextComponent)newTextComponentString);
        }
    }
    
    private String applyTextModificator(final String message) {
        switch ((Modificator)this.textModificator.getValue()) {
            case CapsLook: {
                return message.toUpperCase();
            }
            case FirstHigh: {
                if (message.length() > 0) {
                    return Character.toUpperCase(message.charAt(0)) + message.substring(1).toLowerCase();
                }
                return message;
            }
            case Crazy: {
                return this.applyCrazyCase(message);
            }
            default: {
                return message;
            }
        }
    }
    
    private String applyTextStyle(final String message) {
        return message;
    }
    
    private String applyCoolText(final String message) {
        final StringBuilder result = new StringBuilder();
        for (final char c : message.toCharArray()) {
            final String coolChar = this.coolTextMap.get(c);
            if (coolChar != null) {
                result.append(coolChar);
            }
            else {
                result.append(c);
            }
        }
        return result.toString();
    }
    
    private String applyGangStyle(final String message) {
        String result = message;
        result = result.replaceAll("(?i)\\bing\\b", "in'");
        result = result.replaceAll("(?i)\\b(\\w+)ing\\b", "$1in'");
        result = result.replaceAll("(?i)\\bthe\\b", "da");
        result = result.replaceAll("(?i)\\bur\\b", "ya");
        result = result.replaceAll("(?i)\\bu r\\b", "ya");
        result = result.replaceAll("(?i)\\byou are\\b", "ya");
        result = result.replaceAll("(?i)\\brich\\b", "rich$");
        return result;
    }
    
    private String applyCrazyCase(final String message) {
        final StringBuilder result = new StringBuilder();
        boolean upper = true;
        for (final char c : message.toCharArray()) {
            if (Character.isLetter(c)) {
                if (upper) {
                    result.append(Character.toUpperCase(c));
                }
                else {
                    result.append(Character.toLowerCase(c));
                }
                upper = !upper;
            }
            else {
                result.append(c);
                if (c == ' ' || c == '.' || c == ',' || c == '!' || c == '?') {
                    upper = true;
                }
            }
        }
        return result.toString();
    }
    
    private String getSn0wRainbow() {
        if (this.sn0wIndex > BetterChat.SN0W_RAINBOW.length - 1) {
            this.sn0wIndex = 0;
        }
        final String color = BetterChat.SN0W_RAINBOW[this.sn0wIndex];
        ++this.sn0wIndex;
        return color;
    }
    
    private String getTransRainbow() {
        if (this.transIndex > BetterChat.TRANS_COLORS.length - 1) {
            this.transIndex = 0;
        }
        final String color = BetterChat.TRANS_COLORS[this.transIndex];
        ++this.transIndex;
        return color;
    }
    
    private String getCustomDoubleRainbow() {
        String color;
        if (this.customDoubleIndex % 2 == 0) {
            color = ((Colors)this.customColor.getValue()).getCode();
        }
        else {
            color = ((Colors)this.customColor2.getValue()).getCode();
        }
        ++this.customDoubleIndex;
        return color;
    }
    
    private String getFormatting() {
        final StringBuilder formatting = new StringBuilder();
        if (this.boldText.getValue()) {
            formatting.append("&l");
        }
        if (this.italicText.getValue()) {
            formatting.append("&o");
        }
        return formatting.toString();
    }
    
    private String getSuffixFormatting() {
        final StringBuilder formatting = new StringBuilder();
        if (this.suffixBold.getValue()) {
            formatting.append("&l");
        }
        if (this.suffixItalic.getValue()) {
            formatting.append("&o");
        }
        return formatting.toString();
    }
    
    private boolean isCommandPrefix(final char firstChar) {
        return firstChar == '/' || firstChar == '+' || firstChar == '.' || firstChar == '-' || firstChar == ';' || firstChar == '`' || firstChar == ',' || firstChar == ':' || firstChar == '=';
    }
    
    private boolean allowMessage(final String message) {
        boolean allow = true;
        for (final String s : this.filters) {
            if (message.startsWith(s)) {
                allow = false;
                break;
            }
        }
        return allow;
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (this.suffix.getValue() != SuffixMode.None && event.getStage() == 0 && event.getPacket() instanceof CPacketChatMessage) {
            try {
                String message = ((CPacketChatMessage)event.getPacket()).func_149439_c();
                if (((CPacketChatMessage)event.getPacket()).func_149439_c().startsWith("/")) {
                    return;
                }
                final String suffixText = ((Colors)this.suffixColor.getValue()).getCode() + this.getSuffixFormatting() + ((SuffixMode)this.suffix.getValue()).getSuffix();
                message += suffixText;
                if (message.length() >= 256) {
                    message = message.substring(0, 256);
                }
                ((CPacketChatMessage)event.getPacket()).field_149440_a = message;
            }
            catch (Exception ex) {}
        }
        if (event.getPacket() instanceof CPacketChatMessage) {
            final String s = ((CPacketChatMessage)event.getPacket()).func_149439_c();
            this.check = !s.startsWith(OyVey.commandManager.getPrefix());
        }
    }
    
    static {
        RAINBOW_COLORS = new String[] { "&c", "&6", "&e", "&a", "&b", "&9", "&d" };
        SN0W_RAINBOW = new String[] { "&d", "&c", "&6", "&e", "&2", "&a", "&b", "&3", "&9" };
        TRANS_COLORS = new String[] { "&b", "&d", "&f", "&d", "&b" };
        BetterChat.INSTANCE = new BetterChat();
    }
    
    public enum timemodes
    {
        None, 
        DotGod, 
        Sn0w;
    }
    
    public enum Modificator
    {
        None, 
        CapsLook, 
        FirstHigh, 
        Crazy;
    }
    
    public enum modes
    {
        Luftwaffe, 
        Sn0w, 
        Custom, 
        Trans;
    }
    
    public enum Background
    {
        Default, 
        None, 
        Gradient;
    }
    
    public enum Colors
    {
        Black("&0"), 
        DarkBlue("&1"), 
        DarkGreen("&2"), 
        DarkAqua("&3"), 
        DarkRed("&4"), 
        Purple("&5"), 
        Gold("&6"), 
        Gray("&7"), 
        DarkGray("&8"), 
        Blue("&9"), 
        Green("&a"), 
        Aqua("&b"), 
        Red("&c"), 
        Pink("&d"), 
        Yellow("&e"), 
        White("&f");
        
        private final String code;
        
        private Colors(final String code) {
            this.code = code;
        }
        
        public String getCode() {
            return this.code;
        }
    }
    
    public enum SuffixMode
    {
        None(""), 
        Luftwaffe(" | \u029f\u1d1c\ua730\u1d1b\u1d21\u1d00\ua730\ua730\u1d07"), 
        Sn0w(" \u2744");
        
        private final String suffix;
        
        private SuffixMode(final String suffix) {
            this.suffix = suffix;
        }
        
        public String getSuffix() {
            return this.suffix;
        }
    }
}
