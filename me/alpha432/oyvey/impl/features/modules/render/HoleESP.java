//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.init.*;
import me.alpha432.oyvey.api.util.world.*;
import net.minecraft.util.math.*;
import net.minecraft.client.renderer.vertex.*;
import net.minecraft.client.renderer.*;
import org.lwjgl.opengl.*;

public class HoleESP extends Module
{
    private final Setting<Integer> range;
    private final Setting<Float> height;
    private final Setting<Integer> alpha;
    private final Setting<Integer> lineAlpha;
    private final Setting<Float> lineWidth;
    public final Setting<Boolean> crossed;
    private final Setting<Boolean> fadeBox;
    private final Setting<Boolean> invertFadeBox;
    private final Setting<Boolean> fadeLine;
    private final Setting<Boolean> invertFadeLine;
    private final Setting<ColorSetting> bedrockColor;
    private final Setting<ColorSetting> obsidianColor;
    private int count;
    
    public HoleESP() {
        super("HoleESP", "Just ESP for holes", Module.Category.RENDER, false, false, false);
        this.range = (Setting<Integer>)this.register(new Setting("Range", (Object)10, (Object)0, (Object)25));
        this.height = (Setting<Float>)this.register(new Setting("Height", (Object)0.0f, (Object)(-1.0f), (Object)1.0f));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (Object)128, (Object)0, (Object)255));
        this.lineAlpha = (Setting<Integer>)this.register(new Setting("LineAlpha", (Object)255, (Object)0, (Object)255));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (Object)1.2f, (Object)0.1f, (Object)5.0f));
        this.crossed = (Setting<Boolean>)this.register(new Setting("Crossed", (Object)true));
        this.fadeBox = (Setting<Boolean>)this.register(new Setting("FadeBox", (Object)false));
        this.invertFadeBox = (Setting<Boolean>)this.register(new Setting("InvertFadeBox", (Object)false));
        this.fadeLine = (Setting<Boolean>)this.register(new Setting("FadeLine", (Object)false));
        this.invertFadeLine = (Setting<Boolean>)this.register(new Setting("InvertFadeLine", (Object)false));
        this.bedrockColor = (Setting<ColorSetting>)this.register(new Setting("BedrockColor", (Object)new ColorSetting(0, 255, 0, 255)));
        this.obsidianColor = (Setting<ColorSetting>)this.register(new Setting("ObsidianColor", (Object)new ColorSetting(255, 0, 0, 255)));
        this.count = 0;
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (HoleESP.mc.field_175622_Z == null) {
            return;
        }
        this.count = 0;
        final Vec3i playerPos = new Vec3i(HoleESP.mc.field_175622_Z.field_70165_t, HoleESP.mc.field_175622_Z.field_70163_u, HoleESP.mc.field_175622_Z.field_70161_v);
        for (int rangeValue = (int)this.range.getValue(), x = playerPos.func_177958_n() - rangeValue; x <= playerPos.func_177958_n() + rangeValue; ++x) {
            for (int z = playerPos.func_177952_p() - rangeValue; z <= playerPos.func_177952_p() + rangeValue; ++z) {
                for (int y = playerPos.func_177956_o() + rangeValue; y >= playerPos.func_177956_o() - rangeValue; --y) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    if (this.isValidHole(pos)) {
                        if (this.isBedrockHole(pos)) {
                            this.drawHole(pos, true);
                        }
                        else if (this.isSafeHole(pos)) {
                            this.drawHole(pos, false);
                        }
                    }
                }
            }
        }
    }
    
    private boolean isValidHole(final BlockPos pos) {
        return HoleESP.mc.field_71441_e.func_180495_p(pos).func_177230_c() == Blocks.field_150350_a && HoleESP.mc.field_71441_e.func_180495_p(pos.func_177984_a()).func_177230_c() == Blocks.field_150350_a && HoleESP.mc.field_71441_e.func_180495_p(pos.func_177981_b(2)).func_177230_c() == Blocks.field_150350_a;
    }
    
    private boolean isBedrockHole(final BlockPos pos) {
        return HoleESP.mc.field_71441_e.func_180495_p(pos.func_177978_c()).func_177230_c() == Blocks.field_150357_h && HoleESP.mc.field_71441_e.func_180495_p(pos.func_177974_f()).func_177230_c() == Blocks.field_150357_h && HoleESP.mc.field_71441_e.func_180495_p(pos.func_177976_e()).func_177230_c() == Blocks.field_150357_h && HoleESP.mc.field_71441_e.func_180495_p(pos.func_177968_d()).func_177230_c() == Blocks.field_150357_h && HoleESP.mc.field_71441_e.func_180495_p(pos.func_177977_b()).func_177230_c() == Blocks.field_150357_h;
    }
    
    private boolean isSafeHole(final BlockPos pos) {
        return BlockUtil.isBlockUnSafe(HoleESP.mc.field_71441_e.func_180495_p(pos.func_177977_b()).func_177230_c()) && BlockUtil.isBlockUnSafe(HoleESP.mc.field_71441_e.func_180495_p(pos.func_177974_f()).func_177230_c()) && BlockUtil.isBlockUnSafe(HoleESP.mc.field_71441_e.func_180495_p(pos.func_177976_e()).func_177230_c()) && BlockUtil.isBlockUnSafe(HoleESP.mc.field_71441_e.func_180495_p(pos.func_177968_d()).func_177230_c()) && BlockUtil.isBlockUnSafe(HoleESP.mc.field_71441_e.func_180495_p(pos.func_177978_c()).func_177230_c());
    }
    
    public void drawHole(final BlockPos pos, final boolean isBedrock) {
        ++this.count;
        final double x1 = pos.func_177958_n();
        final double y1 = pos.func_177956_o();
        final double z1 = pos.func_177952_p();
        final double x2 = x1 + 1.0;
        final double y2 = y1 + (float)this.height.getValue();
        final double z2 = z1 + 1.0;
        float red;
        float green;
        float blue;
        if (isBedrock) {
            final ColorSetting c = (ColorSetting)this.bedrockColor.getValue();
            red = c.getRed() / 255.0f;
            green = c.getGreen() / 255.0f;
            blue = c.getBlue() / 255.0f;
        }
        else {
            final ColorSetting c = (ColorSetting)this.obsidianColor.getValue();
            red = c.getRed() / 255.0f;
            green = c.getGreen() / 255.0f;
            blue = c.getBlue() / 255.0f;
        }
        final float alphaValue = (int)this.alpha.getValue() / 255.0f;
        final float lineAlphaValue = (int)this.lineAlpha.getValue() / 255.0f;
        final AxisAlignedBB bb = new AxisAlignedBB(x1 - HoleESP.mc.func_175598_ae().field_78730_l, y1 - HoleESP.mc.func_175598_ae().field_78731_m, z1 - HoleESP.mc.func_175598_ae().field_78728_n, x2 - HoleESP.mc.func_175598_ae().field_78730_l, y2 - HoleESP.mc.func_175598_ae().field_78731_m, z2 - HoleESP.mc.func_175598_ae().field_78728_n);
        this.setupRender();
        this.renderFilledBoxWithFade(bb, red, green, blue, alphaValue, (boolean)this.fadeBox.getValue(), (boolean)this.invertFadeBox.getValue());
        this.renderBoxWithFade(bb, red, green, blue, lineAlphaValue, (boolean)this.fadeLine.getValue(), (boolean)this.invertFadeLine.getValue());
        this.cleanupRender();
    }
    
    private void renderFilledBoxWithFade(final AxisAlignedBB bb, final float red, final float green, final float blue, final float alpha, final boolean fade, final boolean invert) {
        if (!fade) {
            RenderGlobal.func_189696_b(bb, red, green, blue, alpha);
            return;
        }
        final Tessellator tessellator = Tessellator.func_178181_a();
        final BufferBuilder buffer = tessellator.func_178180_c();
        final float topAlpha = invert ? 0.0f : alpha;
        final float bottomAlpha = invert ? alpha : 0.0f;
        buffer.func_181668_a(7, DefaultVertexFormats.field_181706_f);
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        tessellator.func_78381_a();
    }
    
    private void setupRender() {
        GlStateManager.func_179094_E();
        GlStateManager.func_179147_l();
        GlStateManager.func_179097_i();
        GlStateManager.func_179120_a(770, 771, 0, 1);
        GlStateManager.func_179090_x();
        GlStateManager.func_179132_a(false);
        GlStateManager.func_179129_p();
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth((float)this.lineWidth.getValue());
    }
    
    private void cleanupRender() {
        GL11.glDisable(2848);
        GlStateManager.func_179089_o();
        GlStateManager.func_179132_a(true);
        GlStateManager.func_179126_j();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
    }
    
    private void renderBoxWithFade(final AxisAlignedBB bb, final float red, final float green, final float blue, final float alpha, final boolean fade, final boolean invert) {
        final Tessellator tessellator = Tessellator.func_178181_a();
        final BufferBuilder buffer = tessellator.func_178180_c();
        final float topAlpha = fade ? (invert ? 0.0f : alpha) : alpha;
        final float bottomAlpha = fade ? (invert ? alpha : 0.0f) : alpha;
        buffer.func_181668_a(1, DefaultVertexFormats.field_181706_f);
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        tessellator.func_78381_a();
        if (this.crossed.getValue()) {
            this.renderCrossesWithFade(bb, red, green, blue, topAlpha, bottomAlpha);
        }
    }
    
    private void renderCrossesWithFade(final AxisAlignedBB bb, final float red, final float green, final float blue, final float topAlpha, final float bottomAlpha) {
        final Tessellator tessellator = Tessellator.func_178181_a();
        final BufferBuilder buffer = tessellator.func_178180_c();
        buffer.func_181668_a(1, DefaultVertexFormats.field_181706_f);
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, bottomAlpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, topAlpha).func_181675_d();
        tessellator.func_78381_a();
    }
    
    private void renderBox(final AxisAlignedBB bb, final float red, final float green, final float blue, final float alpha) {
        final Tessellator tessellator = Tessellator.func_178181_a();
        final BufferBuilder buffer = tessellator.func_178180_c();
        buffer.func_181668_a(1, DefaultVertexFormats.field_181706_f);
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        tessellator.func_78381_a();
        if (this.crossed.getValue()) {
            this.renderCrosses(bb, red, green, blue, alpha);
        }
    }
    
    private void renderCrosses(final AxisAlignedBB bb, final float red, final float green, final float blue, final float alpha) {
        final Tessellator tessellator = Tessellator.func_178181_a();
        final BufferBuilder buffer = tessellator.func_178180_c();
        buffer.func_181668_a(1, DefaultVertexFormats.field_181706_f);
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72336_d, bb.field_72338_b, bb.field_72334_f).func_181666_a(red, green, blue, alpha).func_181675_d();
        buffer.func_181662_b(bb.field_72340_a, bb.field_72337_e, bb.field_72339_c).func_181666_a(red, green, blue, alpha).func_181675_d();
        tessellator.func_78381_a();
    }
    
    public String getDisplayInfo() {
        return String.valueOf(this.count);
    }
}
