//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import net.minecraft.entity.player.*;
import me.alpha432.oyvey.api.util.math.*;
import java.util.concurrent.*;
import net.minecraft.tileentity.*;
import net.minecraft.world.*;
import net.minecraft.util.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import java.lang.reflect.*;
import net.minecraft.inventory.*;
import java.util.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import me.alpha432.oyvey.api.util.render.*;
import net.minecraft.client.renderer.*;

public class ToolTips extends Module
{
    private static final ResourceLocation SHULKER_GUI_TEXTURE;
    private static ToolTips INSTANCE;
    public Map<EntityPlayer, ItemStack> spiedPlayers;
    public Map<EntityPlayer, Timer> playerTimers;
    private int textRadarY;
    
    public ToolTips() {
        super("ShulkerHud", "Draws items in shulker when some1 opening it", Module.Category.HUD, true, false, false);
        this.spiedPlayers = new ConcurrentHashMap<EntityPlayer, ItemStack>();
        this.playerTimers = new ConcurrentHashMap<EntityPlayer, Timer>();
        this.textRadarY = 0;
        this.setInstance();
    }
    
    public static ToolTips getInstance() {
        if (ToolTips.INSTANCE == null) {
            ToolTips.INSTANCE = new ToolTips();
        }
        return ToolTips.INSTANCE;
    }
    
    public static void displayInv(final ItemStack stack, final String name) {
        try {
            final Item item = stack.func_77973_b();
            final TileEntityShulkerBox entityBox = new TileEntityShulkerBox();
            final ItemShulkerBox shulker = (ItemShulkerBox)item;
            entityBox.func_145834_a((World)ToolTips.mc.field_71441_e);
            final NBTTagCompound blockEntityTag = stack.func_77978_p().func_74775_l("BlockEntityTag");
            if (blockEntityTag.func_150297_b("Items", 9)) {
                final NonNullList<ItemStack> items = (NonNullList<ItemStack>)NonNullList.func_191197_a(27, (Object)ItemStack.field_190927_a);
                ItemStackHelper.func_191283_b(blockEntityTag, (NonNullList)items);
                try {
                    final Field itemsField = TileEntityShulkerBox.class.getDeclaredField("items");
                    itemsField.setAccessible(true);
                    itemsField.set(entityBox, items);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            entityBox.func_145839_a(stack.func_77978_p().func_74775_l("BlockEntityTag"));
            entityBox.func_190575_a((name == null) ? stack.func_82833_r() : name);
            final IInventory inventory;
            new Thread(() -> {
                try {
                    Thread.sleep(200L);
                }
                catch (InterruptedException ex) {}
                ToolTips.mc.field_71439_g.func_71007_a(inventory);
            }).start();
        }
        catch (Exception ex2) {}
    }
    
    private void setInstance() {
        ToolTips.INSTANCE = this;
    }
    
    public void onUpdate() {
        if (!fullNullCheck()) {
            for (final EntityPlayer player : ToolTips.mc.field_71441_e.field_73010_i) {
                if (player != null && player.func_184614_ca().func_77973_b() instanceof ItemShulkerBox && ToolTips.mc.field_71439_g != player) {
                    final ItemStack stack = player.func_184614_ca();
                    this.spiedPlayers.put(player, stack);
                }
            }
        }
    }
    
    public void onRender2D(final Render2DEvent event) {
        if (!fullNullCheck()) {
            final int x = -3;
            int y = 124;
            this.textRadarY = 0;
            for (final EntityPlayer player : ToolTips.mc.field_71441_e.field_73010_i) {
                if (this.spiedPlayers.get(player) != null) {
                    if (player.func_184614_ca() != null && player.func_184614_ca().func_77973_b() instanceof ItemShulkerBox) {
                        final Timer playerTimer;
                        if (player.func_184614_ca().func_77973_b() instanceof ItemShulkerBox && (playerTimer = this.playerTimers.get(player)) != null) {
                            playerTimer.reset();
                            this.playerTimers.put(player, playerTimer);
                        }
                    }
                    else {
                        final Timer playerTimer = this.playerTimers.get(player);
                        if (playerTimer == null) {
                            final Timer timer = new Timer();
                            timer.reset();
                            this.playerTimers.put(player, timer);
                        }
                        else if (playerTimer.passedS(3.0)) {
                            continue;
                        }
                    }
                    final ItemStack stack = this.spiedPlayers.get(player);
                    this.renderShulkerToolTip(stack, x, y, player.func_70005_c_());
                    y += 78;
                    this.textRadarY = y - 10 - 114 + 2;
                }
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void makeTooltip(final ItemTooltipEvent event) {
    }
    
    public void renderShulkerToolTip(final ItemStack stack, final int x, final int y, final String name) {
        final NBTTagCompound tagCompound = stack.func_77978_p();
        final NBTTagCompound blockEntityTag;
        if (tagCompound != null && tagCompound.func_150297_b("BlockEntityTag", 10) && (blockEntityTag = tagCompound.func_74775_l("BlockEntityTag")).func_150297_b("Items", 9)) {
            GlStateManager.func_179126_j();
            GlStateManager.func_179132_a(true);
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.func_179140_f();
            GlStateManager.func_187401_a(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            ToolTips.mc.func_110434_K().func_110577_a(ToolTips.SHULKER_GUI_TEXTURE);
            RenderUtil.drawTexturedRect(x, y, 0, 0, 176, 16, 500);
            RenderUtil.drawTexturedRect(x, y + 16, 0, 16, 176, 57, 500);
            RenderUtil.drawTexturedRect(x, y + 16 + 54, 0, 160, 176, 8, 500);
            GlStateManager.func_179084_k();
            final ColorSetting mainColor = (ColorSetting)Color.getInstance().mainColor.getValue();
            final java.awt.Color color = new java.awt.Color(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), 200);
            this.renderer.drawStringWithShadow((name == null) ? stack.func_82833_r() : name, (float)(x + 8), (float)(y + 6), ColorUtil.toRGBA(color));
            GlStateManager.func_179145_e();
            RenderHelper.func_74520_c();
            GlStateManager.func_179094_E();
            GlStateManager.func_179126_j();
            GlStateManager.func_179147_l();
            final NonNullList<ItemStack> nonnulllist = (NonNullList<ItemStack>)NonNullList.func_191197_a(27, (Object)ItemStack.field_190927_a);
            ItemStackHelper.func_191283_b(blockEntityTag, (NonNullList)nonnulllist);
            for (int i = 0; i < nonnulllist.size(); ++i) {
                final int iX = x + i % 9 * 18 + 8;
                final int iY = y + i / 9 * 18 + 18;
                final ItemStack itemStack = (ItemStack)nonnulllist.get(i);
                ToolTips.mc.func_175599_af().field_77023_b = 501.0f;
                RenderUtil.itemRender.func_180450_b(itemStack, iX, iY);
                RenderUtil.itemRender.func_180453_a(ToolTips.mc.field_71466_p, itemStack, iX, iY, (String)null);
                ToolTips.mc.func_175599_af().field_77023_b = 0.0f;
            }
            GlStateManager.func_179132_a(true);
            GlStateManager.func_179121_F();
            GlStateManager.func_179131_c(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
    
    static {
        SHULKER_GUI_TEXTURE = new ResourceLocation("textures/gui/container/shulker_box.png");
        ToolTips.INSTANCE = new ToolTips();
    }
}
