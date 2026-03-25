//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.player.*;
import net.minecraft.init.*;
import java.awt.*;
import java.util.*;
import org.lwjgl.opengl.*;
import net.minecraft.client.renderer.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.api.util.render.*;

public class BurrowESP extends Module
{
    private final Setting<Boolean> textInsteadOfBox;
    private final Setting<Boolean> self;
    private final Setting<Boolean> box;
    private final Setting<Boolean> outline;
    private final Setting<ColorSetting> color;
    private final Setting<ColorSetting> textColor;
    private final Setting<Integer> textSize;
    private final Setting<Integer> range;
    private final Setting<Integer> outlineAlpha;
    private final Setting<Float> outlineWidth;
    
    public BurrowESP() {
        super("BurrowESP", "Renders a player's burrow", Module.Category.RENDER, false, false, false);
        this.textInsteadOfBox = (Setting<Boolean>)this.register(new Setting("TextInsteadOfBox", (Object)false));
        this.self = (Setting<Boolean>)this.register(new Setting("Self", (Object)false));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", (Object)true, v -> !(boolean)this.textInsteadOfBox.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (Object)true, v -> !(boolean)this.textInsteadOfBox.getValue()));
        this.color = (Setting<ColorSetting>)this.register(new Setting("Color", (Object)new ColorSetting(255, 128, 128, 128)));
        this.textColor = (Setting<ColorSetting>)this.register(new Setting("TextColor", (Object)new ColorSetting(255, 255, 255, 255), v -> (boolean)this.textInsteadOfBox.getValue()));
        this.textSize = (Setting<Integer>)this.register(new Setting("TextSize", (Object)10, (Object)1, (Object)45, v -> (boolean)this.textInsteadOfBox.getValue()));
        this.range = (Setting<Integer>)this.register(new Setting("Range", (Object)10, (Object)5, (Object)30, v -> (boolean)this.textInsteadOfBox.getValue()));
        this.outlineAlpha = (Setting<Integer>)this.register(new Setting("OutlineAlpha", (Object)255, (Object)0, (Object)255, v -> !(boolean)this.textInsteadOfBox.getValue() && (boolean)this.outline.getValue()));
        this.outlineWidth = (Setting<Float>)this.register(new Setting("OutlineWidth", (Object)1.0f, (Object)0.1f, (Object)5.0f, v -> !(boolean)this.textInsteadOfBox.getValue() && (boolean)this.outline.getValue()));
    }
    
    public void onRender3D(final Render3DEvent event) {
        final Map<BlockPos, List<EntityPlayer>> burrowedPlayers = new HashMap<BlockPos, List<EntityPlayer>>();
        for (final EntityPlayer player : BurrowESP.mc.field_71441_e.field_73010_i) {
            if (player == BurrowESP.mc.field_71439_g && !(boolean)this.self.getValue()) {
                continue;
            }
            final BlockPos blockPos = new BlockPos(Math.floor(player.field_70165_t), Math.floor(player.field_70163_u + 0.20000000298023224), Math.floor(player.field_70161_v));
            if (!BurrowESP.mc.field_71441_e.func_180495_p(blockPos).func_177230_c().equals(Blocks.field_150357_h) && !BurrowESP.mc.field_71441_e.func_180495_p(blockPos).func_177230_c().equals(Blocks.field_150343_Z) && !BurrowESP.mc.field_71441_e.func_180495_p(blockPos).func_177230_c().equals(Blocks.field_150477_bB)) {
                continue;
            }
            burrowedPlayers.computeIfAbsent(blockPos, k -> new ArrayList()).add(player);
        }
        for (final Map.Entry<BlockPos, List<EntityPlayer>> entry : burrowedPlayers.entrySet()) {
            final BlockPos blockPos = entry.getKey();
            final List<EntityPlayer> players = entry.getValue();
            final double distance = BurrowESP.mc.field_71439_g.func_70011_f(blockPos.func_177958_n() + 0.5, blockPos.func_177956_o() + 0.5, blockPos.func_177952_p() + 0.5);
            if (distance > (int)this.range.getValue()) {
                continue;
            }
            final ColorSetting c = (ColorSetting)this.color.getValue();
            if (this.textInsteadOfBox.getValue()) {
                final ColorSetting textC = (ColorSetting)this.textColor.getValue();
                this.drawColoredText(blockPos, "Burrowed", new Color(textC.getRed(), textC.getGreen(), textC.getBlue(), textC.getAlpha()));
            }
            else {
                if (this.box.getValue()) {
                    RenderUtil.drawBox(blockPos, new Color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()));
                }
                if (!(boolean)this.outline.getValue()) {
                    continue;
                }
                this.drawCleanOutline(blockPos, new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)this.outlineAlpha.getValue()), (float)this.outlineWidth.getValue());
            }
        }
    }
    
    private void drawCleanOutline(final BlockPos pos, final Color color, final float lineWidth) {
        RenderUtil.RenderTesselator.prepareGL();
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(1);
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        final double x = pos.func_177958_n() - BurrowESP.mc.func_175598_ae().field_78730_l;
        final double y = pos.func_177956_o() - BurrowESP.mc.func_175598_ae().field_78731_m;
        final double z = pos.func_177952_p() - BurrowESP.mc.func_175598_ae().field_78728_n;
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x + 1.0, y, z);
        GL11.glVertex3d(x + 1.0, y, z);
        GL11.glVertex3d(x + 1.0, y, z + 1.0);
        GL11.glVertex3d(x + 1.0, y, z + 1.0);
        GL11.glVertex3d(x, y, z + 1.0);
        GL11.glVertex3d(x, y, z + 1.0);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y + 1.0, z);
        GL11.glVertex3d(x + 1.0, y + 1.0, z);
        GL11.glVertex3d(x + 1.0, y + 1.0, z);
        GL11.glVertex3d(x + 1.0, y + 1.0, z + 1.0);
        GL11.glVertex3d(x + 1.0, y + 1.0, z + 1.0);
        GL11.glVertex3d(x, y + 1.0, z + 1.0);
        GL11.glVertex3d(x, y + 1.0, z + 1.0);
        GL11.glVertex3d(x, y + 1.0, z);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y + 1.0, z);
        GL11.glVertex3d(x + 1.0, y, z);
        GL11.glVertex3d(x + 1.0, y + 1.0, z);
        GL11.glVertex3d(x + 1.0, y, z + 1.0);
        GL11.glVertex3d(x + 1.0, y + 1.0, z + 1.0);
        GL11.glVertex3d(x, y, z + 1.0);
        GL11.glVertex3d(x, y + 1.0, z + 1.0);
        GL11.glEnd();
        RenderUtil.RenderTesselator.releaseGL();
    }
    
    private void drawColoredText(final BlockPos pos, final String text, final Color color) {
        GlStateManager.func_179094_E();
        RenderUtil.glBillboardDistanceScaled(pos.func_177958_n() + 0.5f, pos.func_177956_o() + 0.5f, pos.func_177952_p() + 0.5f, (EntityPlayer)BurrowESP.mc.field_71439_g, 0.0f);
        GlStateManager.func_179097_i();
        GlStateManager.func_179147_l();
        final float scale = 0.02f * (float)this.textSize.getValue();
        GlStateManager.func_179152_a(scale, scale, scale);
        GlStateManager.func_179137_b(-(OyVey.textManager.getStringWidth(text) / 2.0), 0.0, 0.0);
        final int width = OyVey.textManager.getStringWidth(text) / 2;
        final int bgColor = 201326592;
        Render2DMethods.drawNameTagRect((float)(-width - 2), (float)(-BurrowESP.mc.field_71466_p.field_78288_b), width + 2.0f, 1.5f, bgColor, bgColor, 1.4f);
        final int colorInt = color.getAlpha() << 24 | color.getRed() << 16 | color.getGreen() << 8 | color.getBlue();
        OyVey.textManager.drawStringWithShadow(text, 0.0f, 0.0f, colorInt);
        GlStateManager.func_179126_j();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }
}
