//Decompiled by Procyon!

package me.alpha432.oyvey.impl.gui.components.items.buttons;

import me.alpha432.oyvey.api.features.settings.*;
import org.lwjgl.input.*;
import me.alpha432.oyvey.api.util.render.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import net.minecraft.init.*;
import net.minecraft.client.audio.*;
import net.minecraft.util.math.*;

public class Slider extends Button
{
    private final Setting setting;
    private boolean isDragging;
    private static Slider activeSlider;
    
    public Slider(final Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        if (this.isDragging && Slider.activeSlider == this && !Mouse.isButtonDown(0)) {
            this.isDragging = false;
            Slider.activeSlider = null;
        }
        if (this.isDragging && Slider.activeSlider == this) {
            this.updateSliderValue(mouseX);
        }
        final float sliderWidth = this.width + 7.4f;
        final float percentage = this.getPercentage();
        final float sliderPos = this.x + sliderWidth * percentage;
        RenderUtil.drawRect(this.x, this.y, this.x + sliderWidth, this.y + this.height - 0.5f, this.isHovering(mouseX, mouseY) ? 1614823488L : 1345335344L);
        RenderUtil.drawRect(this.x, this.y, sliderPos, this.y + this.height - 0.5f, this.isDragging ? -12285697L : -13408564L);
        final String displayText = this.setting.getName() + " " + ChatFormatting.GRAY + this.setting.getValue();
        final float textY = this.y + this.height / 2.0f - 4.5f;
        OyVey.textManager.drawStringWithShadow(displayText, this.x + 2.3f, textY, SomaGui.getInstance().syncSliderColor());
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY) && mouseButton == 0) {
            Slider.activeSlider = this;
            this.isDragging = true;
            this.updateSliderValue(mouseX);
            Slider.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0f));
        }
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (mouseButton == 0 && this.isDragging && Slider.activeSlider == this) {
            this.isDragging = false;
            Slider.activeSlider = null;
        }
    }
    
    private void updateSliderValue(final int mouseX) {
        final float sliderWidth = this.width + 7.4f;
        final float percentage = MathHelper.func_76131_a((mouseX - this.x) / sliderWidth, 0.0f, 1.0f);
        if (this.setting.getValue() instanceof Double) {
            final double min = (double)this.setting.getMin();
            final double max = (double)this.setting.getMax();
            double value = min + (max - min) * percentage;
            value = Math.round(value * 100.0) / 100.0;
            this.setting.setValue((Object)value);
        }
        else if (this.setting.getValue() instanceof Float) {
            final float min2 = (float)this.setting.getMin();
            final float max2 = (float)this.setting.getMax();
            float value2 = min2 + (max2 - min2) * percentage;
            value2 = Math.round(value2 * 100.0f) / 100.0f;
            this.setting.setValue((Object)value2);
        }
        else if (this.setting.getValue() instanceof Integer) {
            final int min3 = (int)this.setting.getMin();
            final int max3 = (int)this.setting.getMax();
            final int value3 = Math.round(min3 + (max3 - min3) * percentage);
            this.setting.setValue((Object)value3);
        }
    }
    
    private float getPercentage() {
        if (this.setting.getValue() instanceof Double) {
            final double min = (double)this.setting.getMin();
            final double max = (double)this.setting.getMax();
            final double value = (double)this.setting.getValue();
            return (float)((value - min) / (max - min));
        }
        if (this.setting.getValue() instanceof Float) {
            final float min2 = (float)this.setting.getMin();
            final float max2 = (float)this.setting.getMax();
            final float value2 = (float)this.setting.getValue();
            return (value2 - min2) / (max2 - min2);
        }
        if (this.setting.getValue() instanceof Integer) {
            final int min3 = (int)this.setting.getMin();
            final int max3 = (int)this.setting.getMax();
            final int value3 = (int)this.setting.getValue();
            return (value3 - min3) / (float)(max3 - min3);
        }
        return 0.0f;
    }
    
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }
    
    public int getHeight() {
        return 14;
    }
    
    public void toggle() {
    }
    
    public boolean getState() {
        return true;
    }
    
    static {
        Slider.activeSlider = null;
    }
}
