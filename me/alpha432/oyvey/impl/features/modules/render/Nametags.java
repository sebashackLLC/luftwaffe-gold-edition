//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import net.minecraft.util.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraft.block.*;
import java.awt.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.item.*;
import net.minecraft.enchantment.*;
import me.alpha432.oyvey.api.util.*;
import me.alpha432.oyvey.*;
import net.minecraft.init.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.manager.*;
import net.minecraft.entity.*;
import net.minecraft.util.text.*;
import net.minecraft.client.network.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import me.alpha432.oyvey.impl.events.*;
import me.alpha432.oyvey.api.util.entity.*;
import java.util.*;
import net.minecraft.client.renderer.culling.*;
import net.minecraft.entity.item.*;
import net.minecraft.util.math.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.api.util.render.*;
import net.minecraft.client.gui.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;

public class Nametags extends Module
{
    private static final ResourceLocation LUFTWAFFE_ICON;
    private static final int ICON_SIZE = 8;
    public Setting<Boolean> armor;
    public Setting<Boolean> enchants;
    public Setting<Boolean> simple;
    public Setting<Boolean> simpleSwords;
    public Setting<Boolean> superSimple;
    public Setting<Boolean> lowercase;
    public Setting<Boolean> durability;
    public Setting<Boolean> itemStack;
    public Setting<Boolean> invisibles;
    public Setting<Boolean> entityId;
    public Setting<Boolean> gameMode;
    public Setting<Boolean> totemPops;
    public Setting<Boolean> syncBorder;
    public Setting<Boolean> holeColor;
    public Setting<Boolean> userAlias;
    public Setting<Boolean> itemTags;
    public Setting<Integer> itemRange;
    public Setting<Float> scaling;
    public Setting<Float> opacity;
    public Setting<Float> outlineOpacity;
    public Setting<Boolean> noBackground;
    private final Setting<Integer> yOffset;
    protected final Setting<PingEnum> ping;
    protected final Setting<SneakEnum> sneak;
    private final Setting<ColorSetting> nametagColor;
    private final Setting<ColorSetting> friendColor;
    private final Setting<ColorSetting> borderColor;
    protected final List<Block> burrowList;
    
    public Nametags() {
        super("Nametags", "", Module.Category.RENDER, true, false, false);
        this.armor = (Setting<Boolean>)this.register(new Setting("Armor", (Object)true));
        this.enchants = (Setting<Boolean>)this.register(new Setting("Enchants", (Object)true));
        this.simple = (Setting<Boolean>)new Setting("Simple", (Object)true);
        this.simpleSwords = (Setting<Boolean>)new Setting("SimpleSwords", (Object)true);
        this.superSimple = (Setting<Boolean>)new Setting("SuperSimple", (Object)false);
        this.lowercase = (Setting<Boolean>)this.register(new Setting("LowerCase", (Object)false));
        this.durability = (Setting<Boolean>)this.register(new Setting("Durability", (Object)true));
        this.itemStack = (Setting<Boolean>)this.register(new Setting("ItemStack", (Object)true));
        this.invisibles = (Setting<Boolean>)new Setting("Invisibles", (Object)true);
        this.entityId = (Setting<Boolean>)new Setting("Entity-ID", (Object)false);
        this.gameMode = (Setting<Boolean>)new Setting("Gamemode", (Object)false);
        this.totemPops = (Setting<Boolean>)this.register(new Setting("Pops", (Object)true));
        this.syncBorder = (Setting<Boolean>)new Setting("SyncBorder", (Object)true);
        this.holeColor = (Setting<Boolean>)new Setting("HoleColor", (Object)false);
        this.userAlias = (Setting<Boolean>)new Setting("UserAlias", (Object)false);
        this.itemTags = (Setting<Boolean>)this.register(new Setting("ItemTags", (Object)false));
        this.itemRange = (Setting<Integer>)this.register(new Setting("ItemRange", (Object)20, (Object)5, (Object)50));
        this.scaling = (Setting<Float>)this.register(new Setting("Scaling", (Object)0.3f, (Object)0.1f, (Object)1.0f));
        this.opacity = (Setting<Float>)this.register(new Setting("Opacity", (Object)0.5f, (Object)0.0f, (Object)1.0f));
        this.outlineOpacity = (Setting<Float>)this.register(new Setting("OutlineOpacity", (Object)0.75f, (Object)0.0f, (Object)1.0f));
        this.noBackground = (Setting<Boolean>)this.register(new Setting("NoBackground", (Object)false));
        this.yOffset = (Setting<Integer>)new Setting("Y-Offset", (Object)0, (Object)0, (Object)255);
        this.nametagColor = (Setting<ColorSetting>)this.register(new Setting("NametagColor", (Object)new ColorSetting(255, 255, 255, 255)));
        this.friendColor = (Setting<ColorSetting>)this.register(new Setting("FriendColor", (Object)new ColorSetting(0, 255, 255, 255)));
        this.borderColor = (Setting<ColorSetting>)this.register(new Setting("BorderColor", (Object)new ColorSetting(255, 255, 255, 255), v -> (boolean)this.syncBorder.getValue()));
        this.ping = (Setting<PingEnum>)this.register(new Setting("Ping", (Object)PingEnum.NORMAL));
        this.sneak = (Setting<SneakEnum>)this.register(new Setting("Sneak", (Object)SneakEnum.NONE));
        this.burrowList = Arrays.asList(Blocks.field_150343_Z, Blocks.field_150477_bB, Blocks.field_150381_bn, Blocks.field_150467_bQ, Blocks.field_150321_G, (Block)Blocks.field_150461_bJ, Blocks.field_150347_e, Blocks.field_150460_al, (Block)Blocks.field_150486_ae, Blocks.field_150447_bR);
    }
    
    protected Color getFriendColour() {
        return ((ColorSetting)this.friendColor.getValue()).getColor();
    }
    
    protected Color getNametagColor() {
        return ((ColorSetting)this.nametagColor.getValue()).getColor();
    }
    
    protected Color getInvisColor() {
        return ((ColorSetting)this.nametagColor.getValue()).getColor();
    }
    
    protected Color getBurrowColor() {
        return ((ColorSetting)this.nametagColor.getValue()).getColor();
    }
    
    protected Color getBorderColor() {
        return ((ColorSetting)this.borderColor.getValue()).getColor();
    }
    
    @SubscribeEvent
    public void onRenderNametag(final RenderNametagEvent event) {
        if (this.isEnabled()) {
            event.setCanceled(true);
        }
    }
    
    protected void renderStack(final ItemStack stack, final int x, final int y, final int enchHeight) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179132_a(true);
        GlStateManager.func_179086_m(256);
        RenderHelper.func_74520_c();
        Nametags.mc.func_175599_af().field_77023_b = -150.0f;
        GlStateManager.func_179140_f();
        GlStateManager.func_179091_B();
        GlStateManager.func_179142_g();
        GlStateManager.func_179145_e();
        final int height = (enchHeight > 4) ? ((enchHeight - 4) * 8 / 2) : 0;
        Nametags.mc.func_175599_af().func_180450_b(stack, x, y + height);
        Nametags.mc.func_175599_af().func_175030_a(Nametags.mc.field_71466_p, stack, x, y + height);
        Nametags.mc.func_175599_af().field_77023_b = 0.0f;
        RenderHelper.func_74518_a();
        GlStateManager.func_179129_p();
        GlStateManager.func_179141_d();
        GlStateManager.func_179084_k();
        GlStateManager.func_179140_f();
        GlStateManager.func_179152_a(0.5f, 0.5f, 0.5f);
        GlStateManager.func_179097_i();
        if (this.enchants.getValue()) {
            this.renderEnchants(stack, x, y - 24);
        }
        GlStateManager.func_179091_B();
        GlStateManager.func_179152_a(2.0f, 2.0f, 2.0f);
        GlStateManager.func_179121_F();
    }
    
    private void renderEnchants(final ItemStack stack, final int xOffset, int yOffset) {
        final Set<Enchantment> e = EnchantmentHelper.func_82781_a(stack).keySet();
        final List<String> enchantTexts = new ArrayList<String>(e.size());
        for (final Enchantment enchantment : e) {
            if ((boolean)this.simple.getValue() && !enchantment.func_77320_a().contains("all") && (!enchantment.func_77320_a().contains("knockback") || (boolean)this.simpleSwords.getValue()) && (!enchantment.func_77320_a().contains("fire") || (boolean)this.simpleSwords.getValue()) && !enchantment.func_77320_a().contains("arrowDamage") && !enchantment.func_77320_a().contains("explosion") && (!enchantment.func_77320_a().contains("fall") || (boolean)this.superSimple.getValue()) && (!enchantment.func_77320_a().contains("durability") || (boolean)this.superSimple.getValue())) {
                if (!enchantment.func_77320_a().contains("mending")) {
                    continue;
                }
                if (this.superSimple.getValue()) {
                    continue;
                }
            }
            enchantTexts.add(this.getEnchantText(enchantment, EnchantmentHelper.func_77506_a(enchantment, stack)));
        }
        for (final String enchantment2 : enchantTexts) {
            if (enchantment2 != null) {
                String textToDisplay = enchantment2;
                if (this.lowercase.getValue()) {
                    textToDisplay = textToDisplay.toLowerCase();
                }
                else {
                    textToDisplay = TextUtil.capitalize(enchantment2);
                }
                OyVey.textManager.drawString(textToDisplay, xOffset * 2.0f, (float)yOffset, -1, true);
                yOffset += 8;
            }
        }
        if (stack.func_77973_b().equals(Items.field_190929_cY) && stack.func_77962_s()) {
            OyVey.textManager.drawString("God", xOffset * 2.0f, (float)yOffset, -3977919, true);
        }
    }
    
    private String getEnchantText(final Enchantment ench, final int lvl) {
        final ResourceLocation resource = (ResourceLocation)Enchantment.field_185264_b.func_177774_c((Object)ench);
        String name = (resource == null) ? ench.func_77320_a() : resource.toString();
        final int lvlOffset = (lvl > 1) ? 12 : 13;
        if (name.length() > lvlOffset) {
            name = name.substring(10, lvlOffset);
        }
        if (lvl > 1) {
            name += lvl;
        }
        return (name.length() < 2) ? name : TextUtil.getFixedName(name);
    }
    
    protected void renderText(final ItemStack stack, final float y) {
        GlStateManager.func_179152_a(0.5f, 0.5f, 0.5f);
        GlStateManager.func_179097_i();
        final String name = stack.func_82833_r();
        OyVey.textManager.drawString(name, (float)(-OyVey.textManager.getStringWidth(name) >> 1), y, -1, true);
        GlStateManager.func_179091_B();
        GlStateManager.func_179152_a(2.0f, 2.0f, 2.0f);
    }
    
    protected void renderDurability(final ItemStack stack, final float x, final float y) {
        final int percent = (int)DamageUtil.getDamageInPercent(stack);
        GlStateManager.func_179152_a(0.5f, 0.5f, 0.5f);
        GlStateManager.func_179097_i();
        OyVey.textManager.drawString(percent + "%", x * 2.0f, y, stack.func_77973_b().getRGBDurabilityForDisplay(stack), true);
        GlStateManager.func_179091_B();
        GlStateManager.func_179152_a(2.0f, 2.0f, 2.0f);
    }
    
    protected String getDisplayTag(final EntityPlayer player) {
        String displayName = player.func_145748_c_().func_150254_d();
        final String playerName = player.func_70005_c_();
        if (player.func_70005_c_().equalsIgnoreCase("goth11")) {
            displayName = displayName.replace(player.func_70005_c_(), "admin");
        }
        final String ircName = IrcNameManager.getIrcNameForPlayer(playerName);
        if (ircName != null) {
            displayName = displayName.replace(playerName, ircName);
        }
        if (this.lowercase.getValue()) {
            displayName = displayName.toLowerCase();
        }
        if (this.userAlias.getValue()) {
            if (OyVey.friendManager.isFriend(playerName) && !OyVey.friendManager.isAliasSameAsLabel(playerName)) {
                displayName = OyVey.friendManager.getFriend(playerName).getUsername();
            }
            if (displayName.contains(Nametags.mc.field_71439_g.func_70005_c_())) {
                displayName = "You";
            }
        }
        final double health = Math.ceil(EntityUtil.getHealth((Entity)player));
        String color;
        if (health > 18.0) {
            color = TextFormatting.GREEN.toString();
        }
        else if (health > 16.0) {
            color = TextFormatting.DARK_GREEN.toString();
        }
        else if (health > 12.0) {
            color = TextFormatting.YELLOW.toString();
        }
        else if (health > 8.0) {
            color = TextFormatting.GOLD.toString();
        }
        else if (health > 5.0) {
            color = TextFormatting.RED.toString();
        }
        else {
            color = TextFormatting.DARK_RED.toString();
        }
        String idString = "";
        if (this.entityId.getValue()) {
            idString = idString + " ID: " + player.func_145782_y();
        }
        String gameModeStr = "";
        if (this.gameMode.getValue()) {
            gameModeStr = (player.func_184812_l_() ? (gameModeStr + " [C]") : (player.func_175149_v() ? (gameModeStr + " [I]") : (gameModeStr + " [S]")));
        }
        String pingStr = "";
        if (this.ping.getValue() != PingEnum.NONE) {
            try {
                final int responseTime = Objects.requireNonNull(Nametags.mc.func_147114_u()).func_175102_a(player.func_110124_au()).func_178853_c();
                switch ((PingEnum)this.ping.getValue()) {
                    case COLORED: {
                        pingStr = pingStr + " " + this.getPingColor(responseTime) + responseTime + "ms";
                        break;
                    }
                    case NORMAL: {
                        pingStr = pingStr + " " + responseTime + "ms";
                        break;
                    }
                }
            }
            catch (Exception ex) {}
        }
        String popStr = "";
        if (this.totemPops.getValue()) {
            final Map<String, Integer> registry = (Map<String, Integer>)Management.TotemPopContainer;
            popStr += (registry.containsKey(player.func_70005_c_()) ? (this.getPopColor(registry.get(player.func_70005_c_())) + " -" + registry.get(player.func_70005_c_())) : "");
        }
        displayName = displayName + idString + gameModeStr + pingStr + color + " " + (int)health + popStr;
        return displayName;
    }
    
    protected int getNameColor(final EntityPlayer player) {
        if (OyVey.friendManager.isFriend(player)) {
            return this.getFriendColour().getRGB();
        }
        if (player.func_82150_aj()) {
            return this.getInvisColor().getRGB();
        }
        if (Nametags.mc.func_147114_u() != null && Nametags.mc.func_147114_u().func_175102_a(player.func_110124_au()) == null) {
            return this.getInvisColor().getRGB();
        }
        if (player.func_70093_af() && this.sneak.getValue() != SneakEnum.NONE) {
            return (this.sneak.getValue() == SneakEnum.LIGHT) ? 16750848 : -6676491;
        }
        return this.getNametagColor().getRGB();
    }
    
    protected int getBorderColor(final EntityPlayer player) {
        if (OyVey.friendManager.isFriend(player) && (boolean)this.syncBorder.getValue()) {
            return this.getFriendColour().getRGB();
        }
        return this.syncBorder.getValue() ? this.getBorderColor().getRGB() : ((int)(127.0f * (float)this.outlineOpacity.getValue()) << 24);
    }
    
    public void onRender3D(final Render3DEvent event) {
        final Entity renderEntity = RenderMethods.getEntity();
        final Frustum frustum = Interpolation.createFrustum(renderEntity);
        final Vec3d interp = Interpolation.interpolateEntity(renderEntity);
        final List<EntityPlayer> playerList = (List<EntityPlayer>)Nametags.mc.field_71441_e.field_73010_i;
        playerList.sort(Comparator.comparing(player -> Nametags.mc.field_71439_g.func_70032_d(player)));
        for (final EntityPlayer player2 : playerList) {
            final AxisAlignedBB bb = player2.func_174813_aQ();
            final Vec3d vec = Interpolation.interpolateEntity((Entity)player2);
            final AxisAlignedBB expandedBB = bb.func_72321_a(5.0, 10.0, 5.0);
            if (frustum.func_78546_a(expandedBB) && player2 != renderEntity && !EntityUtil.isDead((Entity)player2) && (!player2.func_82150_aj() || (boolean)this.invisibles.getValue())) {
                this.renderNameTag(player2, vec.field_72450_a, vec.field_72448_b, vec.field_72449_c, interp);
            }
        }
    }
    
    private void renderItemTags(final Vec3d mcPlayerInterpolation) {
        for (final Entity entity : Nametags.mc.field_71441_e.field_72996_f) {
            if (!(entity instanceof EntityItem)) {
                continue;
            }
            final EntityItem itemEntity = (EntityItem)entity;
            if (itemEntity.field_70128_L) {
                continue;
            }
            if (!itemEntity.func_70089_S()) {
                continue;
            }
            if (itemEntity.func_70032_d((Entity)Nametags.mc.field_71439_g) > (int)this.itemRange.getValue()) {
                continue;
            }
            final String itemName = itemEntity.func_92059_d().func_82833_r();
            final int count = itemEntity.func_92059_d().func_190916_E();
            final String displayText = itemName + ((count > 1) ? (" x" + count) : "");
            final Vec3d vec = Interpolation.interpolateEntity(entity);
            this.renderItemNameTag(itemEntity, displayText, vec.field_72450_a, vec.field_72448_b, vec.field_72449_c, mcPlayerInterpolation);
        }
    }
    
    private void renderNameTag(final EntityPlayer player, final double x, final double y, final double z, final Vec3d mcPlayerInterpolation) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179088_q();
        GlStateManager.func_179136_a(1.0f, -1500000.0f);
        GlStateManager.func_179140_f();
        GlStateManager.func_179129_p();
        GlStateManager.func_179097_i();
        GlStateManager.func_179118_c();
        GlStateManager.func_179147_l();
        GlStateManager.func_179098_w();
        GlStateManager.func_179121_F();
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.func_179098_w();
        GlStateManager.func_179141_d();
        GlStateManager.func_179126_j();
        GlStateManager.func_179089_o();
        GlStateManager.func_179084_k();
        final double tempY = y + (player.func_70093_af() ? 0.5 : 0.7);
        final double xDist = mcPlayerInterpolation.field_72450_a - x;
        final double yDist = mcPlayerInterpolation.field_72448_b - y;
        final double zDist = mcPlayerInterpolation.field_72449_c - z;
        final double dist = MathHelper.func_76133_a(xDist * xDist + yDist * yDist + zDist * zDist);
        double renderX = x;
        double renderY = tempY + 1.4;
        double renderZ = z;
        final double minDist = 0.3;
        if (dist < minDist && dist > 0.001) {
            final double pushFactor = minDist / dist;
            renderX = mcPlayerInterpolation.field_72450_a - xDist * pushFactor;
            renderZ = mcPlayerInterpolation.field_72449_c - zDist * pushFactor;
            renderY = tempY + 1.4;
        }
        final String displayTag = this.getDisplayTag(player);
        final boolean isIrcUser = IrcNameManager.hasIrcNameForPlayer(player.func_70005_c_());
        final int iconOffset = isIrcUser ? 10 : 0;
        final int width = (OyVey.textManager.getStringWidth(displayTag) + iconOffset) / 2;
        double scale = 0.0018 + MathUtil.fixedNametagScaling((float)this.scaling.getValue()) * dist;
        if (dist <= 8.0) {
            scale = 0.0245;
        }
        GlStateManager.func_179094_E();
        RenderHelper.func_74519_b();
        GlStateManager.func_179088_q();
        GlStateManager.func_179136_a(1.0f, -1500000.0f);
        GlStateManager.func_179140_f();
        GlStateManager.func_179109_b((float)renderX, (float)renderY, (float)renderZ);
        GlStateManager.func_179114_b(-Nametags.mc.func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
        final float xRot = (Nametags.mc.field_71474_y.field_74320_O == 2) ? -1.0f : 1.0f;
        GlStateManager.func_179114_b(Nametags.mc.func_175598_ae().field_78732_j, xRot, 0.0f, 0.0f);
        GlStateManager.func_179139_a(-scale, -scale, scale);
        GlStateManager.func_179097_i();
        GlStateManager.func_179147_l();
        if (!(boolean)this.noBackground.getValue() && (float)this.opacity.getValue() > 0.0f) {
            this.drawCustomNametagBackground((float)(-width - 2), (float)(-(Nametags.mc.field_71466_p.field_78288_b + (int)this.yOffset.getValue())), width + 2.0f, 1.5f, player);
        }
        int textStartX = -width;
        if (isIrcUser) {
            this.renderLuftwaffeIcon((float)textStartX, -8.0f);
            textStartX += 10;
        }
        OyVey.textManager.drawString(displayTag, (float)textStartX, -8.0f, this.getNameColor(player), true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179094_E();
        final ItemStack heldItemMainhand = player.func_184614_ca();
        final ItemStack heldItemOffhand = player.func_184592_cb();
        int xOffset = 0;
        int enchantOffset = 0;
        for (int i = 0, armorSize = 3; i >= 0; i = --armorSize) {
            final ItemStack itemStack;
            if (!(itemStack = (ItemStack)player.field_71071_by.field_70460_b.get(armorSize)).func_190926_b()) {
                xOffset -= 8;
                final int size;
                if ((boolean)this.enchants.getValue() && !(boolean)this.simple.getValue() && (size = EnchantmentHelper.func_82781_a(itemStack).size()) > enchantOffset) {
                    enchantOffset = size;
                }
            }
        }
        if ((!heldItemOffhand.func_190926_b() && (boolean)this.armor.getValue()) || ((boolean)this.durability.getValue() && heldItemOffhand.func_77984_f())) {
            xOffset -= 8;
            final int size2;
            if ((boolean)this.enchants.getValue() && !(boolean)this.simple.getValue() && (size2 = EnchantmentHelper.func_82781_a(heldItemOffhand).size()) > enchantOffset) {
                enchantOffset = size2;
            }
        }
        if (!heldItemMainhand.func_190926_b()) {
            final int size3;
            if ((boolean)this.enchants.getValue() && !(boolean)this.simple.getValue() && (size3 = EnchantmentHelper.func_82781_a(heldItemMainhand).size()) > enchantOffset) {
                enchantOffset = size3;
            }
            int armorOffset = this.getOffset(enchantOffset);
            if ((boolean)this.armor.getValue() || ((boolean)this.durability.getValue() && heldItemMainhand.func_77984_f())) {
                xOffset -= 8;
            }
            if (this.armor.getValue()) {
                final int oldOffset = armorOffset;
                armorOffset -= 32;
                this.renderStack(heldItemMainhand, xOffset, oldOffset, enchantOffset);
            }
            if ((boolean)this.durability.getValue() && heldItemMainhand.func_77984_f()) {
                this.renderDurability(heldItemMainhand, (float)xOffset, (float)armorOffset);
            }
            if (this.itemStack.getValue()) {
                this.renderText(heldItemMainhand, (float)(armorOffset - (this.durability.getValue() ? 10 : 2)));
            }
            if ((boolean)this.armor.getValue() || ((boolean)this.durability.getValue() && heldItemMainhand.func_77984_f())) {
                xOffset += 16;
            }
        }
        for (int i2 = 0, armorSizeI = 3; i2 >= 0; i2 = --armorSizeI) {
            final ItemStack itemStack2;
            if (!(itemStack2 = (ItemStack)player.field_71071_by.field_70460_b.get(armorSizeI)).func_190926_b()) {
                int fixedEnchantOffset = this.getOffset(enchantOffset);
                if (this.armor.getValue()) {
                    final int oldEnchantOffset = fixedEnchantOffset;
                    fixedEnchantOffset -= 32;
                    this.renderStack(itemStack2, xOffset, oldEnchantOffset, enchantOffset);
                }
                if ((boolean)this.durability.getValue() && itemStack2.func_77984_f()) {
                    this.renderDurability(itemStack2, (float)xOffset, (float)fixedEnchantOffset);
                }
                xOffset += 16;
            }
        }
        if (!heldItemOffhand.func_190926_b()) {
            int fixedEnchantOffsetI = this.getOffset(enchantOffset);
            if (this.armor.getValue()) {
                final int oldEnchantOffsetI = fixedEnchantOffsetI;
                fixedEnchantOffsetI -= 32;
                this.renderStack(heldItemOffhand, xOffset, oldEnchantOffsetI, enchantOffset);
            }
            if ((boolean)this.durability.getValue() && heldItemOffhand.func_77984_f()) {
                this.renderDurability(heldItemOffhand, (float)xOffset, (float)fixedEnchantOffsetI);
            }
        }
        GlStateManager.func_179121_F();
        GlStateManager.func_179091_B();
        GlStateManager.func_179126_j();
        GlStateManager.func_179113_r();
        GlStateManager.func_179136_a(1.0f, 1500000.0f);
        GlStateManager.func_179121_F();
    }
    
    private void renderItemNameTag(final EntityItem itemEntity, final String displayText, final double x, final double y, final double z, final Vec3d mcPlayerInterpolation) {
        final double tempY = y + 0.3;
        final double xDist = mcPlayerInterpolation.field_72450_a - x;
        final double yDist = mcPlayerInterpolation.field_72448_b - y;
        final double zDist = mcPlayerInterpolation.field_72449_c - z;
        final double dist = MathHelper.func_76133_a(xDist * xDist + yDist * yDist + zDist * zDist);
        double renderX = x;
        double renderY = tempY + 1.4;
        double renderZ = z;
        final double minDist = 0.5;
        if (dist < minDist && dist > 0.001) {
            final double pushFactor = minDist / dist;
            renderX = mcPlayerInterpolation.field_72450_a - xDist * pushFactor;
            renderY = mcPlayerInterpolation.field_72448_b - (mcPlayerInterpolation.field_72448_b - (tempY + 1.4)) * pushFactor;
            renderZ = mcPlayerInterpolation.field_72449_c - zDist * pushFactor;
        }
        final int width = OyVey.textManager.getStringWidth(displayText) / 2;
        double scale = (0.0018 + MathUtil.fixedNametagScaling((float)this.scaling.getValue()) * dist) * 0.25;
        if (dist <= 8.0) {
            scale = 0.006125;
        }
        GlStateManager.func_179094_E();
        RenderHelper.func_74519_b();
        GlStateManager.func_179088_q();
        GlStateManager.func_179136_a(1.0f, -1500000.0f);
        GlStateManager.func_179140_f();
        GlStateManager.func_179109_b((float)renderX, (float)renderY, (float)renderZ);
        GlStateManager.func_179114_b(-Nametags.mc.func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
        final float xRot = (Nametags.mc.field_71474_y.field_74320_O == 2) ? -1.0f : 1.0f;
        GlStateManager.func_179114_b(Nametags.mc.func_175598_ae().field_78732_j, xRot, 0.0f, 0.0f);
        GlStateManager.func_179139_a(-scale, -scale, scale);
        GlStateManager.func_179097_i();
        GlStateManager.func_179147_l();
        if (!(boolean)this.noBackground.getValue() && (float)this.opacity.getValue() >= 0.1f) {
            final int alpha = Math.max(25, (int)((float)this.outlineOpacity.getValue() * 255.0f));
            final int bgColor = (int)(127.0f * Math.max(0.1f, (float)this.opacity.getValue())) << 24;
            Render2DMethods.quickDrawRect((float)(-width - 2), (float)(-(Nametags.mc.field_71466_p.field_78288_b + (int)this.yOffset.getValue())), width + 2.0f, 1.5f, bgColor, false);
        }
        OyVey.textManager.drawString(displayText, (float)(-width), -8.0f, this.getNametagColor().getRGB(), true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179113_r();
        GlStateManager.func_179136_a(1.0f, 1500000.0f);
        GlStateManager.func_179121_F();
    }
    
    private int getOffset(final int offset) {
        int fixedOffset = this.armor.getValue() ? -26 : -27;
        if (offset > 4) {
            fixedOffset -= (offset - 4) * 8;
        }
        return fixedOffset;
    }
    
    private String getPopColor(final int pops) {
        if (pops == 1) {
            return TextFormatting.GREEN.toString();
        }
        if (pops == 2) {
            return TextFormatting.DARK_GREEN.toString();
        }
        if (pops == 3) {
            return TextFormatting.YELLOW.toString();
        }
        if (pops == 4) {
            return TextFormatting.GOLD.toString();
        }
        if (pops == 5) {
            return TextFormatting.RED.toString();
        }
        return TextFormatting.DARK_RED.toString();
    }
    
    private String getPingColor(final int ping) {
        if (ping > 200) {
            return TextFormatting.RED.toString();
        }
        if (ping > 100) {
            return TextFormatting.YELLOW.toString();
        }
        return TextFormatting.GREEN.toString();
    }
    
    private void renderLuftwaffeIcon(final float x, final float y) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179098_w();
        GlStateManager.func_179147_l();
        GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        Nametags.mc.func_110434_K().func_110577_a(Nametags.LUFTWAFFE_ICON);
        Gui.func_146110_a((int)x, (int)y, 0.0f, 0.0f, 8, 8, 8.0f, 8.0f);
        GlStateManager.func_179121_F();
    }
    
    private void drawCustomNametagBackground(final float left, final float top, final float right, final float bottom, final EntityPlayer player) {
        final int alpha = (int)((float)this.opacity.getValue() * 127.0f);
        final int bgColor = alpha << 24;
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179090_x();
        GlStateManager.func_187428_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        Render2DMethods.drawRect(left, top, right, bottom, bgColor);
        if (this.syncBorder.getValue()) {
            final int borderColor = this.getBorderColor(player);
            final int borderAlpha = (int)((float)this.outlineOpacity.getValue() * 255.0f);
            final int finalBorderColor = borderAlpha << 24 | (borderColor & 0xFFFFFF);
            GL11.glLineWidth(1.4f);
            GlStateManager.func_179090_x();
            final Tessellator tessellator = Tessellator.func_178181_a();
            final BufferBuilder buffer = tessellator.func_178180_c();
            buffer.func_181668_a(2, DefaultVertexFormats.field_181706_f);
            final float r = (finalBorderColor >> 16 & 0xFF) / 255.0f;
            final float g = (finalBorderColor >> 8 & 0xFF) / 255.0f;
            final float b = (finalBorderColor & 0xFF) / 255.0f;
            final float a = (finalBorderColor >> 24 & 0xFF) / 255.0f;
            buffer.func_181662_b((double)left, (double)bottom, 0.0).func_181666_a(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)right, (double)bottom, 0.0).func_181666_a(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)right, (double)top, 0.0).func_181666_a(r, g, b, a).func_181675_d();
            buffer.func_181662_b((double)left, (double)top, 0.0).func_181666_a(r, g, b, a).func_181675_d();
            tessellator.func_78381_a();
            GL11.glLineWidth(1.0f);
        }
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }
    
    static {
        LUFTWAFFE_ICON = new ResourceLocation("textures/luftwaffe/irc_icon.png");
    }
    
    public enum PingEnum
    {
        NORMAL, 
        COLORED, 
        NONE;
    }
    
    public enum SneakEnum
    {
        NONE, 
        DARK, 
        LIGHT;
    }
}
