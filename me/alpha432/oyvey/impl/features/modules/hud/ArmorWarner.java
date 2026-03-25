//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import java.util.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.item.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.gui.*;

public class ArmorWarner extends Module
{
    private static ArmorWarner INSTANCE;
    private final List<String> warnings;
    int darkRed;
    public Setting<Integer> warningThreshold;
    
    public ArmorWarner() {
        super("ArmorWarner", "Warns about low armor durability", Module.Category.HUD, true, false, false);
        this.warnings = new ArrayList<String>();
        this.darkRed = -5636096;
        this.warningThreshold = (Setting<Integer>)this.register(new Setting("Armor Durablity", (Object)33, (Object)1, (Object)100, "Size of the font."));
        this.setInstance();
    }
    
    public static ArmorWarner getInstance() {
        if (ArmorWarner.INSTANCE == null) {
            ArmorWarner.INSTANCE = new ArmorWarner();
        }
        return ArmorWarner.INSTANCE;
    }
    
    private void setInstance() {
        ArmorWarner.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        if (ArmorWarner.mc.field_71439_g == null || ArmorWarner.mc.field_71441_e == null) {
            return;
        }
        this.warnings.clear();
        this.checkArmorDurability();
        if (!this.warnings.isEmpty()) {
            this.renderArmorWarnings();
        }
    }
    
    private void checkArmorDurability() {
        final EntityPlayer player = (EntityPlayer)ArmorWarner.mc.field_71439_g;
        for (int i = 0; i < 4; ++i) {
            final ItemStack armorPiece = (ItemStack)player.field_71071_by.field_70460_b.get(i);
            if (armorPiece != null && armorPiece.func_77973_b() instanceof ItemArmor) {
                final float durabilityPercent = this.getDurabilityPercent(armorPiece);
                if (durabilityPercent <= (int)this.warningThreshold.getValue()) {
                    final String pieceName = this.getArmorPieceName(i);
                    this.warnings.add(String.format("your " + pieceName + " is at " + (int)Math.floor(durabilityPercent), new Object[0]));
                }
            }
        }
    }
    
    private String getArmorPieceName(final int slot) {
        switch (slot) {
            case 3: {
                return "helmet";
            }
            case 2: {
                return "chestplate";
            }
            case 1: {
                return "leggings";
            }
            case 0: {
                return "boots";
            }
            default: {
                return "Armor";
            }
        }
    }
    
    private float getDurabilityPercent(final ItemStack stack) {
        final float maxDurability = (float)stack.func_77958_k();
        final float currentDamage = (float)stack.func_77952_i();
        return 100.0f - currentDamage / maxDurability * 100.0f;
    }
    
    private void renderArmorWarnings() {
        final ScaledResolution sr = new ScaledResolution(ArmorWarner.mc);
        final int screenWidth = sr.func_78326_a();
        final int yOffset = sr.func_78328_b() / 4;
        for (int i = 0; i < this.warnings.size(); ++i) {
            final String warning = this.warnings.get(i);
            final int textWidth = this.renderer.getStringWidth(warning);
            final int xPos = (screenWidth - textWidth) / 2;
            final int yPos = yOffset + i * 12;
            this.renderer.drawString(warning, (float)xPos, (float)yPos, this.darkRed, true);
        }
    }
    
    static {
        ArmorWarner.INSTANCE = new ArmorWarner();
    }
}
