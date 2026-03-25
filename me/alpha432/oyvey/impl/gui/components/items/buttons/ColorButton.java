//Decompiled by Procyon!

package me.alpha432.oyvey.impl.gui.components.items.buttons;

import me.alpha432.oyvey.api.features.settings.*;
import org.lwjgl.input.*;
import me.alpha432.oyvey.api.util.render.*;
import me.alpha432.oyvey.*;
import net.minecraft.client.renderer.*;
import java.awt.*;
import org.lwjgl.opengl.*;
import net.minecraft.init.*;
import net.minecraft.client.audio.*;

public class ColorButton extends Button
{
    private final Setting<ColorSetting> setting;
    private boolean expanded;
    private boolean draggingPicker;
    private boolean draggingHue;
    private boolean draggingAlpha;
    private static final int PICKER_SIZE = 80;
    private static final int SLIDER_HEIGHT = 10;
    private static final int PREVIEW_SIZE = 14;
    private static final int PADDING = 4;
    private static ColorSetting clipboard;
    private float lastValidHue;
    
    public ColorButton(final Setting<ColorSetting> setting) {
        super(setting.getName());
        this.expanded = false;
        this.draggingPicker = false;
        this.draggingHue = false;
        this.draggingAlpha = false;
        this.lastValidHue = 0.0f;
        this.setting = setting;
        this.width = 15;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (!Mouse.isButtonDown(0)) {
            this.draggingPicker = false;
            this.draggingHue = false;
            this.draggingAlpha = false;
        }
        final ColorSetting color = (ColorSetting)this.setting.getValue();
        final float buttonWidth = this.width + 7.4f;
        final int bgColor = this.isHovering(mouseX, mouseY) ? 1614823488 : 1345335344;
        RenderUtil.drawRect(this.x, this.y, this.x + buttonWidth, this.y + this.height - 0.5f, (long)bgColor);
        final float previewX = this.x + buttonWidth - 14.0f - 2.0f;
        final float previewY = this.y + (this.height - 14.0f) / 2.0f;
        RenderUtil.drawRect(previewX, previewY, previewX + 14.0f, previewY + 14.0f, (long)color.getARGB());
        this.drawOutline(previewX, previewY, previewX + 14.0f, previewY + 14.0f, -16777216);
        final float textY = this.y + this.height / 2.0f - 4.5f;
        OyVey.textManager.drawStringWithShadow(this.getName(), this.x + 2.3f, textY, -1);
        final String indicator = this.expanded ? "-" : "+";
        OyVey.textManager.drawStringWithShadow(indicator, previewX - 12.0f, textY, -5592406);
        if (this.expanded) {
            this.drawColorPicker(mouseX, mouseY, color);
        }
    }
    
    private void drawColorPicker(final int mouseX, final int mouseY, final ColorSetting color) {
        final float pickerX = this.x;
        final float pickerY = this.y + this.height;
        final float totalWidth = this.width + 7.4f;
        RenderUtil.drawRect(pickerX, pickerY, pickerX + totalWidth, pickerY + 80.0f + 20.0f + 16.0f + 12.0f, -534765536L);
        this.drawOutline(pickerX, pickerY, pickerX + totalWidth, pickerY + 80.0f + 20.0f + 16.0f + 12.0f, -16777216);
        final float svX = pickerX + 4.0f;
        final float svY = pickerY + 4.0f;
        final float svWidth = totalWidth - 8.0f;
        final float svHeight = 80.0f;
        if (this.draggingPicker) {
            final float[] currentHsb = color.getHSB();
            final float newSat = Math.max(0.0f, Math.min(1.0f, (mouseX - svX) / svWidth));
            final float newBri = Math.max(0.0f, Math.min(1.0f, 1.0f - (mouseY - svY) / svHeight));
            final float hueToUse = (currentHsb[1] > 0.0f && currentHsb[2] > 0.0f) ? currentHsb[0] : this.lastValidHue;
            color.setFromHSB(hueToUse, newSat, newBri);
            this.setting.setValue((Object)color);
        }
        if (this.draggingHue) {
            final float[] currentHsb = color.getHSB();
            final float newHue = Math.max(0.0f, Math.min(1.0f, (mouseX - svX) / svWidth));
            color.setFromHSB(this.lastValidHue = newHue, currentHsb[1], currentHsb[2]);
            this.setting.setValue((Object)color);
        }
        final float[] hsb = color.getHSB();
        final float hue = hsb[0];
        final float saturation = hsb[1];
        final float brightness = hsb[2];
        if (saturation > 0.0f && brightness > 0.0f) {
            this.lastValidHue = hue;
        }
        final float displayHue = (saturation > 0.0f && brightness > 0.0f) ? hue : this.lastValidHue;
        this.drawSaturationBrightnessPicker(svX, svY, svWidth, svHeight, displayHue);
        final float cursorX = svX + saturation * svWidth;
        final float cursorY = svY + (1.0f - brightness) * svHeight;
        this.drawPickerCursor(cursorX, cursorY);
        final float hueY = svY + svHeight + 4.0f;
        this.drawHueSlider(svX, hueY, svWidth, 10.0f);
        final float huePos = svX + displayHue * svWidth;
        this.drawSliderCursor(huePos, hueY, 10.0f);
        final float alphaY = hueY + 10.0f + 4.0f;
        this.drawAlphaSlider(svX, alphaY, svWidth, 10.0f, color);
        final float alphaPos = svX + color.getAlpha() / 255.0f * svWidth;
        this.drawSliderCursor(alphaPos, alphaY, 10.0f);
        if (this.draggingAlpha) {
            final int newAlpha = (int)Math.max(0.0f, Math.min(255.0f, (mouseX - svX) / svWidth * 255.0f));
            color.setAlpha(newAlpha);
            this.setting.setValue((Object)color);
        }
        final float buttonY = alphaY + 10.0f + 4.0f;
        final float buttonWidth = (svWidth - 4.0f) / 2.0f;
        final boolean hoverCopy = mouseX >= svX && mouseX <= svX + buttonWidth && mouseY >= buttonY && mouseY <= buttonY + 10.0f;
        RenderUtil.drawRect(svX, buttonY, svX + buttonWidth, buttonY + 10.0f, hoverCopy ? -11513776L : -12566464L);
        OyVey.textManager.drawStringWithShadow("Copy", svX + buttonWidth / 2.0f - 10.0f, buttonY + 1.0f, -1);
        final float pasteX = svX + buttonWidth + 4.0f;
        final boolean hoverPaste = mouseX >= pasteX && mouseX <= pasteX + buttonWidth && mouseY >= buttonY && mouseY <= buttonY + 10.0f;
        RenderUtil.drawRect(pasteX, buttonY, pasteX + buttonWidth, buttonY + 10.0f, hoverPaste ? -11513776L : -12566464L);
        OyVey.textManager.drawStringWithShadow("Paste", pasteX + buttonWidth / 2.0f - 12.0f, buttonY + 1.0f, -1);
    }
    
    private void drawSaturationBrightnessPicker(final float x, final float y, final float width, final float height, final float hue) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        GlStateManager.func_179103_j(7425);
        final int baseColor = Color.HSBtoRGB(hue, 1.0f, 1.0f);
        GL11.glBegin(7);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y + height);
        final float r = (baseColor >> 16 & 0xFF) / 255.0f;
        final float g = (baseColor >> 8 & 0xFF) / 255.0f;
        final float b = (baseColor & 0xFF) / 255.0f;
        GL11.glColor4f(r, g, b, 1.0f);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();
        GL11.glBegin(7);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glEnd();
        GlStateManager.func_179103_j(7424);
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
        this.drawOutline(x, y, x + width, y + height, -16777216);
    }
    
    private void drawHueSlider(final float x, final float y, final float width, final float height) {
        GlStateManager.func_179094_E();
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        GlStateManager.func_179103_j(7425);
        GL11.glBegin(7);
        for (int i = 0; i < 6; ++i) {
            final float startHue = i / 6.0f;
            final float endHue = (i + 1) / 6.0f;
            final int startColor = Color.HSBtoRGB(startHue, 1.0f, 1.0f);
            final int endColor = Color.HSBtoRGB(endHue, 1.0f, 1.0f);
            final float startX = x + width * i / 6.0f;
            final float endX = x + width * (i + 1) / 6.0f;
            GL11.glColor3f((startColor >> 16 & 0xFF) / 255.0f, (startColor >> 8 & 0xFF) / 255.0f, (startColor & 0xFF) / 255.0f);
            GL11.glVertex2f(startX, y);
            GL11.glVertex2f(startX, y + height);
            GL11.glColor3f((endColor >> 16 & 0xFF) / 255.0f, (endColor >> 8 & 0xFF) / 255.0f, (endColor & 0xFF) / 255.0f);
            GL11.glVertex2f(endX, y + height);
            GL11.glVertex2f(endX, y);
        }
        GL11.glEnd();
        GlStateManager.func_179103_j(7424);
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
        this.drawOutline(x, y, x + width, y + height, -16777216);
    }
    
    private void drawAlphaSlider(final float x, final float y, final float width, final float height, final ColorSetting color) {
        for (int checkSize = 4, i = 0; i < width / checkSize; ++i) {
            for (int j = 0; j < height / checkSize; ++j) {
                final int checkColor = ((i + j) % 2 == 0) ? -3355444 : -6710887;
                final float cx = x + i * checkSize;
                final float cy = y + j * checkSize;
                final float cw = Math.min((float)checkSize, x + width - cx);
                final float ch = Math.min((float)checkSize, y + height - cy);
                RenderUtil.drawRect(cx, cy, cx + cw, cy + ch, (long)checkColor);
            }
        }
        GlStateManager.func_179094_E();
        GlStateManager.func_179090_x();
        GlStateManager.func_179147_l();
        GlStateManager.func_179118_c();
        GlStateManager.func_179120_a(770, 771, 1, 0);
        GlStateManager.func_179103_j(7425);
        GL11.glBegin(7);
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.0f);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y + height);
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();
        GlStateManager.func_179103_j(7424);
        GlStateManager.func_179141_d();
        GlStateManager.func_179098_w();
        GlStateManager.func_179084_k();
        GlStateManager.func_179121_F();
        this.drawOutline(x, y, x + width, y + height, -16777216);
    }
    
    private void drawPickerCursor(final float x, final float y) {
        final float radius = 4.0f;
        RenderUtil.drawRect(x - radius - 1.0f, y - 1.0f, x + radius + 1.0f, y + 1.0f, -16777216L);
        RenderUtil.drawRect(x - 1.0f, y - radius - 1.0f, x + 1.0f, y + radius + 1.0f, -16777216L);
        RenderUtil.drawRect(x - radius, y, x + radius, y + 1.0f, -1L);
        RenderUtil.drawRect(x, y - radius, x + 1.0f, y + radius, -1L);
        RenderUtil.drawRect(x - 1.0f, y - 1.0f, x + 1.0f, y + 1.0f, -16777216L);
    }
    
    private void drawSliderCursor(final float x, final float y, final float height) {
        RenderUtil.drawRect(x - 1.0f, y - 1.0f, x + 1.0f, y + height + 1.0f, -1L);
        this.drawOutline(x - 2.0f, y - 2.0f, x + 2.0f, y + height + 2.0f, -16777216);
    }
    
    private void drawOutline(final float x1, final float y1, final float x2, final float y2, final int color) {
        RenderUtil.drawRect(x1, y1, x2, y1 + 1.0f, (long)color);
        RenderUtil.drawRect(x1, y2 - 1.0f, x2, y2, (long)color);
        RenderUtil.drawRect(x1, y1, x1 + 1.0f, y2, (long)color);
        RenderUtil.drawRect(x2 - 1.0f, y1, x2, y2, (long)color);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0) {
            if (this.isHovering(mouseX, mouseY)) {
                this.expanded = !this.expanded;
                ColorButton.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0f));
                return;
            }
            if (this.expanded) {
                final ColorSetting color = (ColorSetting)this.setting.getValue();
                final float pickerX = this.x;
                final float pickerY = this.y + this.height;
                final float totalWidth = this.width + 7.4f;
                final float svX = pickerX + 4.0f;
                final float svY = pickerY + 4.0f;
                final float svWidth = totalWidth - 8.0f;
                final float svHeight = 80.0f;
                if (mouseX >= svX && mouseX <= svX + svWidth && mouseY >= svY && mouseY <= svY + svHeight) {
                    this.draggingPicker = true;
                    return;
                }
                final float hueY = svY + svHeight + 4.0f;
                if (mouseX >= svX && mouseX <= svX + svWidth && mouseY >= hueY && mouseY <= hueY + 10.0f) {
                    this.draggingHue = true;
                    return;
                }
                final float alphaY = hueY + 10.0f + 4.0f;
                if (mouseX >= svX && mouseX <= svX + svWidth && mouseY >= alphaY && mouseY <= alphaY + 10.0f) {
                    this.draggingAlpha = true;
                    return;
                }
                final float buttonY = alphaY + 10.0f + 4.0f;
                final float buttonWidth = (svWidth - 4.0f) / 2.0f;
                if (mouseX >= svX && mouseX <= svX + buttonWidth && mouseY >= buttonY && mouseY <= buttonY + 10.0f) {
                    ColorButton.clipboard = color.copy();
                    ColorButton.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0f));
                    return;
                }
                final float pasteX = svX + buttonWidth + 4.0f;
                if (mouseX >= pasteX && mouseX <= pasteX + buttonWidth && mouseY >= buttonY && mouseY <= buttonY + 10.0f) {
                    if (ColorButton.clipboard != null) {
                        this.setting.setValue((Object)ColorButton.clipboard.copy());
                        ColorButton.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0f));
                    }
                }
            }
        }
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0) {
            this.draggingPicker = false;
            this.draggingHue = false;
            this.draggingAlpha = false;
        }
    }
    
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }
    
    public int getHeight() {
        if (this.expanded) {
            return 142;
        }
        return 14;
    }
    
    public void toggle() {
    }
    
    public boolean getState() {
        return this.expanded;
    }
    
    public boolean isExpanded() {
        return this.expanded;
    }
    
    static {
        ColorButton.clipboard = null;
    }
}
