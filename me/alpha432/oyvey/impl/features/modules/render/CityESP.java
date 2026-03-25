//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import java.util.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.init.*;
import net.minecraft.util.math.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;

public class CityESP extends Module
{
    private final Setting<Integer> playerRange;
    private final Setting<Integer> blockRange;
    private final Setting<Integer> alpha;
    private final Setting<Integer> lineAlpha;
    private final Setting<Float> lineWidth;
    private final Setting<ColorSetting> otherColor;
    private final Setting<ColorSetting> selfColor;
    
    public CityESP() {
        super("CityESP", "Highlights city-able blocks around players in holes", Module.Category.RENDER, true, false, false);
        this.playerRange = (Setting<Integer>)this.register(new Setting("PlayerRange", (Object)20, (Object)5, (Object)50));
        this.blockRange = (Setting<Integer>)this.register(new Setting("BlockRange", (Object)10, (Object)1, (Object)30));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (Object)120, (Object)0, (Object)255));
        this.lineAlpha = (Setting<Integer>)this.register(new Setting("LineAlpha", (Object)255, (Object)0, (Object)255));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (Object)1.5f, (Object)0.1f, (Object)5.0f));
        this.otherColor = (Setting<ColorSetting>)this.register(new Setting("OtherColor", (Object)new ColorSetting(120, 120, 120, 255)));
        this.selfColor = (Setting<ColorSetting>)this.register(new Setting("SelfColor", (Object)new ColorSetting(120, 50, 0, 255)));
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (CityESP.mc.field_71441_e == null || CityESP.mc.field_71439_g == null) {
            return;
        }
        for (final EntityPlayer player : CityESP.mc.field_71441_e.field_73010_i) {
            if (player.func_70032_d((Entity)CityESP.mc.field_71439_g) > (int)this.playerRange.getValue()) {
                continue;
            }
            this.checkCityBlocks(player);
        }
    }
    
    private void checkCityBlocks(final EntityPlayer player) {
        final BlockPos pos = EntityUtil.getPlayerPos(player);
        if (!EntityUtil.isSafe((Entity)player)) {
            return;
        }
        final boolean isSelf = player.func_70005_c_().equals(CityESP.mc.field_71449_j.func_111285_a());
        final ColorSetting color = (ColorSetting)(isSelf ? this.selfColor.getValue() : ((ColorSetting)this.otherColor.getValue()));
        for (int scanRange = (int)this.blockRange.getValue(), x = -scanRange; x <= scanRange; ++x) {
            for (int z = -scanRange; z <= scanRange; ++z) {
                for (int y = -1; y <= 1; ++y) {
                    final BlockPos checkPos = pos.func_177982_a(x, y, z);
                    if (this.isCityable(checkPos, pos)) {
                        this.drawCityBlock(checkPos, color);
                    }
                }
            }
        }
    }
    
    private boolean isCityable(final BlockPos obsidianPos, final BlockPos playerPos) {
        if (CityESP.mc.field_71441_e.func_180495_p(obsidianPos).func_177230_c() != Blocks.field_150343_Z) {
            return false;
        }
        if (!obsidianPos.equals((Object)playerPos.func_177978_c()) && !obsidianPos.equals((Object)playerPos.func_177974_f()) && !obsidianPos.equals((Object)playerPos.func_177968_d()) && !obsidianPos.equals((Object)playerPos.func_177976_e())) {
            return false;
        }
        final BlockPos crystalPos = this.getCrystalPos(obsidianPos, playerPos);
        return crystalPos != null && CityESP.mc.field_71441_e.func_180495_p(crystalPos).func_177230_c() == Blocks.field_150350_a && CityESP.mc.field_71441_e.func_180495_p(crystalPos.func_177984_a()).func_177230_c() == Blocks.field_150350_a && (CityESP.mc.field_71441_e.func_180495_p(crystalPos.func_177977_b()).func_177230_c() == Blocks.field_150343_Z || CityESP.mc.field_71441_e.func_180495_p(crystalPos.func_177977_b()).func_177230_c() == Blocks.field_150357_h);
    }
    
    private BlockPos getCrystalPos(final BlockPos obsidianPos, final BlockPos playerPos) {
        if (obsidianPos.equals((Object)playerPos.func_177978_c())) {
            return obsidianPos.func_177978_c();
        }
        if (obsidianPos.equals((Object)playerPos.func_177974_f())) {
            return obsidianPos.func_177974_f();
        }
        if (obsidianPos.equals((Object)playerPos.func_177968_d())) {
            return obsidianPos.func_177968_d();
        }
        if (obsidianPos.equals((Object)playerPos.func_177976_e())) {
            return obsidianPos.func_177976_e();
        }
        return null;
    }
    
    private void drawCityBlock(final BlockPos pos, final ColorSetting color) {
        final float red = color.getRed() / 255.0f;
        final float green = color.getGreen() / 255.0f;
        final float blue = color.getBlue() / 255.0f;
        final float alphaValue = (int)this.alpha.getValue() / 255.0f;
        final float lineAlphaValue = (int)this.lineAlpha.getValue() / 255.0f;
        final AxisAlignedBB bb = new AxisAlignedBB(pos.func_177958_n() - CityESP.mc.func_175598_ae().field_78730_l, pos.func_177956_o() - CityESP.mc.func_175598_ae().field_78731_m, pos.func_177952_p() - CityESP.mc.func_175598_ae().field_78728_n, pos.func_177958_n() + 1 - CityESP.mc.func_175598_ae().field_78730_l, pos.func_177956_o() + 1 - CityESP.mc.func_175598_ae().field_78731_m, pos.func_177952_p() + 1 - CityESP.mc.func_175598_ae().field_78728_n);
        this.setupRender();
        RenderGlobal.func_189696_b(bb, red, green, blue, alphaValue);
        RenderGlobal.func_189697_a(bb, red, green, blue, lineAlphaValue);
        this.cleanupRender();
    }
    
    private void setupRender() {
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179120_a(770, 771, 0, 1);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth((float)this.lineWidth.getValue());
    }
    
    private void cleanupRender() {
        GL11.glDisable(2848);
        GlStateManager.func_179132_a(true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }
}
