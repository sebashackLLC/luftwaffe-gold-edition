//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.util.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import com.mojang.realmsclient.gui.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.*;
import net.minecraft.potion.*;
import me.alpha432.oyvey.*;
import java.text.*;
import java.util.*;
import me.alpha432.oyvey.api.util.entity.*;
import me.alpha432.oyvey.api.util.math.*;
import net.minecraft.init.*;
import java.util.function.*;
import net.minecraft.client.renderer.*;
import me.alpha432.oyvey.api.util.render.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.impl.events.*;

public class Metrics extends Module
{
    private static final ResourceLocation box;
    private static final ItemStack totem;
    private static Metrics INSTANCE;
    private final Setting<Boolean> grayNess;
    private final Setting<Boolean> renderingUp;
    private final Setting<Boolean> waterMark;
    private final Setting<Boolean> coords;
    private final Setting<Boolean> direction;
    private final Setting<Boolean> armor;
    private final Setting<Boolean> totems;
    private final Setting<Boolean> greeter;
    private final Setting<Boolean> speed;
    private final Setting<Boolean> potions;
    private final Setting<Boolean> ping;
    private final Setting<Boolean> tps;
    private final Setting<Boolean> fps;
    private final Setting<Boolean> lag;
    private final Map<String, Integer> players;
    public Setting<TextUtil.Color> bracketColor;
    public Setting<TextUtil.Color> commandColor;
    public String commandBracket;
    public String commandBracket2;
    public String command;
    public Setting<Integer> waterMarkY;
    public Setting<Boolean> time;
    public Setting<Integer> lagTime;
    private int color;
    private boolean shouldIncrement;
    private int hitMarkerTimer;
    
    public Metrics() {
        super("Metrics", "Renders some information on your screen(old HUD, will delete useless stuff later)", Module.Category.HUD, true, false, false);
        this.grayNess = (Setting<Boolean>)this.register(new Setting("Gray", (Object)true));
        this.renderingUp = (Setting<Boolean>)new Setting("RenderingUp", (Object)true, "Orientation of the HUD-Elements.");
        this.waterMark = (Setting<Boolean>)new Setting("Watermark", (Object)false, "displays watermark");
        this.coords = (Setting<Boolean>)this.register(new Setting("Coords", (Object)false, "Your current coordinates"));
        this.direction = (Setting<Boolean>)this.register(new Setting("Direction", (Object)false, "The Direction you are facing."));
        this.armor = (Setting<Boolean>)new Setting("Armor", (Object)false, "ArmorHUD");
        this.totems = (Setting<Boolean>)new Setting("Totems", (Object)false, "TotemHUD");
        this.greeter = (Setting<Boolean>)new Setting("Welcomer", (Object)false, "The time");
        this.speed = (Setting<Boolean>)this.register(new Setting("Speed", (Object)false, "Your Speed"));
        this.potions = (Setting<Boolean>)this.register(new Setting("Potions", (Object)false, "Your Speed"));
        this.ping = (Setting<Boolean>)this.register(new Setting("Ping", (Object)false, "Your response time to the server."));
        this.tps = (Setting<Boolean>)this.register(new Setting("TPS", (Object)false, "Ticks per second of the server."));
        this.fps = (Setting<Boolean>)this.register(new Setting("FPS", (Object)false, "Your frames per second."));
        this.lag = (Setting<Boolean>)this.register(new Setting("LagNotifier", (Object)false, "The time"));
        this.players = new HashMap<String, Integer>();
        this.bracketColor = (Setting<TextUtil.Color>)this.register(new Setting("BracketColor", (Object)TextUtil.Color.DARK_AQUA));
        this.commandColor = (Setting<TextUtil.Color>)this.register(new Setting("NameColor", (Object)TextUtil.Color.DARK_AQUA));
        this.commandBracket = "[";
        this.commandBracket2 = "]";
        this.command = "luftwaffe";
        this.waterMarkY = (Setting<Integer>)this.register(new Setting("WatermarkPosY", (Object)2, (Object)0, (Object)20, v -> (boolean)this.waterMark.getValue()));
        this.time = (Setting<Boolean>)this.register(new Setting("Time", (Object)false, "The time"));
        this.lagTime = (Setting<Integer>)this.register(new Setting("LagTime", (Object)1000, (Object)0, (Object)2000));
        this.setInstance();
    }
    
    public static Metrics getInstance() {
        if (Metrics.INSTANCE == null) {
            Metrics.INSTANCE = new Metrics();
        }
        return Metrics.INSTANCE;
    }
    
    private void setInstance() {
        Metrics.INSTANCE = this;
    }
    
    public void onUpdate() {
        if (this.shouldIncrement) {
            ++this.hitMarkerTimer;
        }
        if (this.hitMarkerTimer == 10) {
            this.hitMarkerTimer = 0;
            this.shouldIncrement = false;
        }
    }
    
    public void onRender2D(final Render2DEvent event) {
        if (fullNullCheck()) {
            return;
        }
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        this.color = Color.getInstance().syncColor();
        if (this.waterMark.getValue()) {
            final String string = this.command + " v0.0.3";
            if (Color.getInstance().rainbow.getValue()) {
                if (Color.getInstance().rainbowModeHud.getValue() == Color.rainbowMode.Static) {
                    this.renderer.drawString(string, 2.0f, (float)(int)this.waterMarkY.getValue(), ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB(), true);
                }
                else {
                    final int[] arrayOfInt = { 1 };
                    final char[] stringToCharArray = string.toCharArray();
                    float f = 0.0f;
                    for (final char c : stringToCharArray) {
                        this.renderer.drawString(String.valueOf(c), 2.0f + f, (float)(int)this.waterMarkY.getValue(), ColorUtil.rainbow(arrayOfInt[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB(), true);
                        f += this.renderer.getStringWidth(String.valueOf(c));
                        ++arrayOfInt[0];
                    }
                }
            }
            else {
                this.renderer.drawStringWithGradient(string, 2.0f, (float)(int)this.waterMarkY.getValue(), true);
            }
        }
        final int[] counter1 = { 1 };
        final String grayString = this.grayNess.getValue() ? String.valueOf(ChatFormatting.GRAY) : "";
        int i = (Metrics.mc.field_71462_r instanceof GuiChat && (boolean)this.renderingUp.getValue()) ? 13 : (this.renderingUp.getValue() ? -2 : 0);
        if (this.renderingUp.getValue()) {
            if (this.potions.getValue()) {
                final List<PotionEffect> effects = new ArrayList<PotionEffect>(Minecraft.func_71410_x().field_71439_g.func_70651_bq());
                for (final PotionEffect potionEffect : effects) {
                    final String str = OyVey.potionManager.getColoredPotionString(potionEffect);
                    i += 10;
                    this.renderer.drawString(str, (float)(width - this.renderer.getStringWidth(str) - 2), (float)(height - 2 - i), potionEffect.func_188419_a().func_76401_j(), true);
                }
            }
            if (this.speed.getValue()) {
                final String str2 = grayString + "Speed " + ChatFormatting.WHITE + OyVey.speedManager.getSpeedKpH() + " km/h";
                i += 10;
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(height - 2 - i), ((boolean)Color.getInstance().rainbow.getValue()) ? ((Color.getInstance().rainbowModeA.getValue() == Color.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.time.getValue()) {
                final String str2 = grayString + "Time " + ChatFormatting.WHITE + new SimpleDateFormat("h:mm a").format(new Date());
                i += 10;
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(height - 2 - i), ((boolean)Color.getInstance().rainbow.getValue()) ? ((Color.getInstance().rainbowModeA.getValue() == Color.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.tps.getValue()) {
                final String str2 = grayString + "TPS " + ChatFormatting.WHITE + OyVey.serverManager.getTPS();
                i += 10;
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(height - 2 - i), ((boolean)Color.getInstance().rainbow.getValue()) ? ((Color.getInstance().rainbowModeA.getValue() == Color.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            final String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.field_71470_ab;
            final String str3 = grayString + "Ping " + ChatFormatting.WHITE + OyVey.serverManager.getPing();
            if (this.renderer.getStringWidth(str3) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    i += 10;
                    this.renderer.drawString(str3, (float)(width - this.renderer.getStringWidth(str3) - 2), (float)(height - 2 - i), ((boolean)Color.getInstance().rainbow.getValue()) ? ((Color.getInstance().rainbowModeA.getValue() == Color.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.fps.getValue()) {
                    i += 10;
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(height - 2 - i), ((boolean)Color.getInstance().rainbow.getValue()) ? ((Color.getInstance().rainbowModeA.getValue() == Color.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
            else {
                if (this.fps.getValue()) {
                    i += 10;
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(height - 2 - i), ((boolean)Color.getInstance().rainbow.getValue()) ? ((Color.getInstance().rainbowModeA.getValue() == Color.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.ping.getValue()) {
                    i += 10;
                    this.renderer.drawString(str3, (float)(width - this.renderer.getStringWidth(str3) - 2), (float)(height - 2 - i), ((boolean)Color.getInstance().rainbow.getValue()) ? ((Color.getInstance().rainbowModeA.getValue() == Color.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
        }
        else {
            if (this.potions.getValue()) {
                final List<PotionEffect> effects = new ArrayList<PotionEffect>(Minecraft.func_71410_x().field_71439_g.func_70651_bq());
                for (final PotionEffect potionEffect : effects) {
                    final String str = OyVey.potionManager.getColoredPotionString(potionEffect);
                    this.renderer.drawString(str, (float)(width - this.renderer.getStringWidth(str) - 2), (float)(2 + i++ * 10), potionEffect.func_188419_a().func_76401_j(), true);
                }
            }
            if (this.speed.getValue()) {
                final String str2 = grayString + "Speed " + ChatFormatting.WHITE + OyVey.speedManager.getSpeedKpH() + " km/h";
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(2 + i++ * 10), ((boolean)Color.getInstance().rainbow.getValue()) ? ((Color.getInstance().rainbowModeA.getValue() == Color.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.time.getValue()) {
                final String str2 = grayString + "Time " + ChatFormatting.WHITE + new SimpleDateFormat("h:mm a").format(new Date());
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(2 + i++ * 10), ((boolean)Color.getInstance().rainbow.getValue()) ? ((Color.getInstance().rainbowModeA.getValue() == Color.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            if (this.tps.getValue()) {
                final String str2 = grayString + "TPS " + ChatFormatting.WHITE + OyVey.serverManager.getTPS();
                this.renderer.drawString(str2, (float)(width - this.renderer.getStringWidth(str2) - 2), (float)(2 + i++ * 10), ((boolean)Color.getInstance().rainbow.getValue()) ? ((Color.getInstance().rainbowModeA.getValue() == Color.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                ++counter1[0];
            }
            final String fpsText = grayString + "FPS " + ChatFormatting.WHITE + Minecraft.field_71470_ab;
            final String str3 = grayString + "Ping " + ChatFormatting.WHITE + OyVey.serverManager.getPing();
            if (this.renderer.getStringWidth(str3) > this.renderer.getStringWidth(fpsText)) {
                if (this.ping.getValue()) {
                    this.renderer.drawString(str3, (float)(width - this.renderer.getStringWidth(str3) - 2), (float)(2 + i++ * 10), ((boolean)Color.getInstance().rainbow.getValue()) ? ((Color.getInstance().rainbowModeA.getValue() == Color.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.fps.getValue()) {
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(2 + i++ * 10), ((boolean)Color.getInstance().rainbow.getValue()) ? ((Color.getInstance().rainbowModeA.getValue() == Color.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
            else {
                if (this.fps.getValue()) {
                    this.renderer.drawString(fpsText, (float)(width - this.renderer.getStringWidth(fpsText) - 2), (float)(2 + i++ * 10), ((boolean)Color.getInstance().rainbow.getValue()) ? ((Color.getInstance().rainbowModeA.getValue() == Color.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
                if (this.ping.getValue()) {
                    this.renderer.drawString(str3, (float)(width - this.renderer.getStringWidth(str3) - 2), (float)(2 + i++ * 10), ((boolean)Color.getInstance().rainbow.getValue()) ? ((Color.getInstance().rainbowModeA.getValue() == Color.rainbowModeArray.Up) ? ColorUtil.rainbow(counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB() : ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB()) : this.color, true);
                    ++counter1[0];
                }
            }
        }
        final boolean inHell = Metrics.mc.field_71441_e.func_180494_b(Metrics.mc.field_71439_g.func_180425_c()).func_185359_l().equals("Hell");
        final int posX = (int)Metrics.mc.field_71439_g.field_70165_t;
        final int posY = (int)Metrics.mc.field_71439_g.field_70163_u;
        final int posZ = (int)Metrics.mc.field_71439_g.field_70161_v;
        final float nether = inHell ? 8.0f : 0.125f;
        final int hposX = (int)(Metrics.mc.field_71439_g.field_70165_t * nether);
        final int hposZ = (int)(Metrics.mc.field_71439_g.field_70161_v * nether);
        i = ((Metrics.mc.field_71462_r instanceof GuiChat) ? 14 : 0);
        final String coordinates = ChatFormatting.WHITE + "XYZ " + ChatFormatting.RESET + (inHell ? (posX + ", " + posY + ", " + posZ + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + ChatFormatting.WHITE + "]" + ChatFormatting.RESET) : (posX + ", " + posY + ", " + posZ + ChatFormatting.WHITE + " [" + ChatFormatting.RESET + hposX + ", " + hposZ + ChatFormatting.WHITE + "]"));
        final String direction = this.direction.getValue() ? OyVey.rotationManager.getDirection4D(false) : "";
        final String coords = this.coords.getValue() ? coordinates : "";
        i += 10;
        if (Color.getInstance().rainbow.getValue()) {
            final String rainbowCoords = this.coords.getValue() ? ("XYZ " + (inHell ? (posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]") : (posX + ", " + posY + ", " + posZ + " [" + hposX + ", " + hposZ + "]"))) : "";
            if (Color.getInstance().rainbowModeHud.getValue() == Color.rainbowMode.Static) {
                this.renderer.drawString(direction, 2.0f, (float)(height - i - 11), ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB(), true);
                this.renderer.drawString(rainbowCoords, 2.0f, (float)(height - i), ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB(), true);
            }
            else {
                final int[] counter2 = { 1 };
                final char[] stringToCharArray2 = direction.toCharArray();
                float s = 0.0f;
                for (final char c2 : stringToCharArray2) {
                    this.renderer.drawString(String.valueOf(c2), 2.0f + s, (float)(height - i - 11), ColorUtil.rainbow(counter2[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB(), true);
                    s += this.renderer.getStringWidth(String.valueOf(c2));
                    ++counter2[0];
                }
                final int[] counter3 = { 1 };
                final char[] stringToCharArray3 = rainbowCoords.toCharArray();
                float u = 0.0f;
                for (final char c3 : stringToCharArray3) {
                    this.renderer.drawString(String.valueOf(c3), 2.0f + u, (float)(height - i), ColorUtil.rainbow(counter3[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB(), true);
                    u += this.renderer.getStringWidth(String.valueOf(c3));
                    ++counter3[0];
                }
            }
        }
        else {
            this.renderer.drawStringWithGradient(direction, 2.0f, (float)(height - i - 11), true);
            this.renderer.drawStringWithGradient(coords, 2.0f, (float)(height - i), true);
        }
        if (this.armor.getValue()) {
            this.renderArmorHUD(true);
        }
        if (this.totems.getValue()) {
            this.renderTotemHUD();
        }
        if (this.greeter.getValue()) {
            this.renderGreeter();
        }
        if (this.lag.getValue()) {
            this.renderLag();
        }
    }
    
    public Map<String, Integer> getTextRadarPlayers() {
        return (Map<String, Integer>)EntityUtil.getTextRadarPlayers();
    }
    
    public void renderGreeter() {
        final int width = this.renderer.scaledWidth;
        String text = "";
        if (this.greeter.getValue()) {
            text = text + MathUtil.getTimeOfDay() + Metrics.mc.field_71439_g.getDisplayNameString();
        }
        if (Color.getInstance().rainbow.getValue()) {
            if (Color.getInstance().rainbowModeHud.getValue() == Color.rainbowMode.Static) {
                this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 2.0f, ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()).getRGB(), true);
            }
            else {
                final int[] counter1 = { 1 };
                final char[] stringToCharArray = text.toCharArray();
                float i = 0.0f;
                for (final char c : stringToCharArray) {
                    this.renderer.drawString(String.valueOf(c), width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f + i, 2.0f, ColorUtil.rainbow(counter1[0] * (int)Color.getInstance().rainbowHue.getValue()).getRGB(), true);
                    i += this.renderer.getStringWidth(String.valueOf(c));
                    ++counter1[0];
                }
            }
        }
        else {
            this.renderer.drawStringWithGradient(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 2.0f, true);
        }
    }
    
    public void renderLag() {
        final int width = this.renderer.scaledWidth;
        if (OyVey.serverManager.isServerNotResponding()) {
            final String text = ChatFormatting.RED + "Server not responding " + MathUtil.round(OyVey.serverManager.serverRespondingTime() / 1000.0f, 1) + "s.";
            this.renderer.drawString(text, width / 2.0f - this.renderer.getStringWidth(text) / 2.0f + 2.0f, 20.0f, this.color, true);
        }
    }
    
    public void renderTotemHUD() {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        int totems = Metrics.mc.field_71439_g.field_71071_by.field_70462_a.stream().filter(itemStack -> itemStack.func_77973_b() == Items.field_190929_cY).mapToInt(ItemStack::func_190916_E).sum();
        if (Metrics.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_190929_cY) {
            totems += Metrics.mc.field_71439_g.func_184592_cb().func_190916_E();
        }
        if (totems > 0) {
            GlStateManager.func_179098_w();
            final int i = width / 2;
            final int iteration = 0;
            final int y = height - 55 - ((Metrics.mc.field_71439_g.func_70090_H() && Metrics.mc.field_71442_b.func_78763_f()) ? 10 : 0);
            final int x = i - 189 + 180 + 2;
            GlStateManager.func_179126_j();
            RenderUtil.itemRender.field_77023_b = 200.0f;
            RenderUtil.itemRender.func_180450_b(Metrics.totem, x, y);
            RenderUtil.itemRender.func_180453_a(Metrics.mc.field_71466_p, Metrics.totem, x, y, "");
            RenderUtil.itemRender.field_77023_b = 0.0f;
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179097_i();
            this.renderer.drawStringWithShadow(totems + "", (float)(x + 19 - 2 - this.renderer.getStringWidth(totems + "")), (float)(y + 9), 16777215);
            GlStateManager.func_179126_j();
            GlStateManager.func_179140_f();
        }
    }
    
    public void renderArmorHUD(final boolean percent) {
        final int width = this.renderer.scaledWidth;
        final int height = this.renderer.scaledHeight;
        GlStateManager.func_179098_w();
        final int i = width / 2;
        int iteration = 0;
        final int y = height - 55 - ((Metrics.mc.field_71439_g.func_70090_H() && Metrics.mc.field_71442_b.func_78763_f()) ? 10 : 0);
        for (final ItemStack is : Metrics.mc.field_71439_g.field_71071_by.field_70460_b) {
            ++iteration;
            if (is.func_190926_b()) {
                continue;
            }
            final int x = i - 90 + (9 - iteration) * 20 + 2;
            GlStateManager.func_179126_j();
            RenderUtil.itemRender.field_77023_b = 200.0f;
            RenderUtil.itemRender.func_180450_b(is, x, y);
            RenderUtil.itemRender.func_180453_a(Metrics.mc.field_71466_p, is, x, y, "");
            RenderUtil.itemRender.field_77023_b = 0.0f;
            GlStateManager.func_179098_w();
            GlStateManager.func_179140_f();
            GlStateManager.func_179097_i();
            final String s = (is.func_190916_E() > 1) ? (is.func_190916_E() + "") : "";
            this.renderer.drawStringWithShadow(s, (float)(x + 19 - 2 - this.renderer.getStringWidth(s)), (float)(y + 9), 16777215);
            if (!percent) {
                continue;
            }
            final float green = (float)((is.func_77958_k() - is.func_77952_i()) / is.func_77958_k());
            final float red = 1.0f - green;
            final int dmg = 100 - (int)(red * 100.0f);
            this.renderer.drawStringWithShadow(dmg + "", (float)(x + 8 - this.renderer.getStringWidth(dmg + "") / 2), (float)(y - 11), ColorUtil.toRGBA((int)(red * 255.0f), (int)(green * 255.0f), 0));
        }
        GlStateManager.func_179126_j();
        GlStateManager.func_179140_f();
    }
    
    @SubscribeEvent
    public void onUpdateWalkingPlayer(final AttackEntityEvent event) {
        this.shouldIncrement = true;
    }
    
    public void onLoad() {
        OyVey.commandManager.setClientMessage(this.getCommandMessage());
    }
    
    @SubscribeEvent
    public void onSettingChange(final ClientEvent event) {
        if (event.getStage() == 2 && this.equals(event.getSetting().getFeature())) {
            OyVey.commandManager.setClientMessage(this.getCommandMessage());
        }
    }
    
    public String getCommandMessage() {
        return TextUtil.coloredString(this.commandBracket, (TextUtil.Color)this.bracketColor.getPlannedValue()) + TextUtil.coloredString(this.command, (TextUtil.Color)this.commandColor.getPlannedValue()) + TextUtil.coloredString(this.commandBracket2, (TextUtil.Color)this.bracketColor.getPlannedValue());
    }
    
    public String sendSyncCommand() {
        return TextUtil.coloredString("", (TextUtil.Color)this.commandColor.getPlannedValue());
    }
    
    public void drawTextRadar(final int yOffset) {
        if (!this.players.isEmpty()) {
            int y = this.renderer.getFontHeight() + 7 + yOffset;
            for (final Map.Entry<String, Integer> player : this.players.entrySet()) {
                final String text = player.getKey() + " ";
                final int textheight = this.renderer.getFontHeight() + 1;
                this.renderer.drawString(text, 2.0f, (float)y, this.color, true);
                y += textheight;
            }
        }
    }
    
    static {
        box = new ResourceLocation("textures/gui/container/shulker_box.png");
        totem = new ItemStack(Items.field_190929_cY);
        Metrics.INSTANCE = new Metrics();
    }
}
