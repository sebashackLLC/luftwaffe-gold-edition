//Decompiled by Procyon!

package me.alpha432.oyvey.impl.gui.components.items.buttons;

import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.util.render.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.*;
import net.minecraft.init.*;
import net.minecraft.client.audio.*;
import net.minecraft.util.*;

public class StringButton extends Button
{
    private final Setting setting;
    public boolean isListening;
    public String currentString;
    
    public StringButton(final Setting setting) {
        super(setting.getName());
        this.currentString = "";
        this.setting = setting;
        this.width = 15;
        this.currentString = (String)this.setting.getValue();
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final int bgColor = this.isListening ? (this.isHovering(mouseX, mouseY) ? 1614823488 : 1346388032) : (this.isHovering(mouseX, mouseY) ? 1075847200 : 807411744);
        RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height - 0.5f, (long)bgColor);
        final int outlineColor = this.isListening ? (this.isHovering(mouseX, mouseY) ? -10461088 : -11513776) : (this.isHovering(mouseX, mouseY) ? -12566464 : -13619152);
        RenderUtil.drawOutlineRect(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height - 0.5f, 1.0f, outlineColor);
        if (this.isListening) {
            final float indicatorX = this.x + this.width + 2.4f;
            final float indicatorY = this.y + this.height / 2.0f - 1.5f;
            RenderUtil.drawRect(indicatorX, indicatorY, indicatorX + 3.0f, indicatorY + 3.0f, -256L);
        }
        String displayText;
        if (this.isListening) {
            displayText = this.setting.getName() + " " + ChatFormatting.GRAY + this.currentString + "_";
        }
        else {
            displayText = this.setting.getName() + " " + ChatFormatting.GRAY + this.setting.getValue();
        }
        final float textY = this.y + this.height / 2.0f - 4.5f;
        final int textColor = this.isListening ? -1 : -5592406;
        OyVey.textManager.drawStringWithShadow(displayText, this.x + 2.3f, textY, textColor);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            StringButton.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0f));
        }
    }
    
    public void onKeyTyped(final char typedChar, final int keyCode) {
        if (this.isListening) {
            switch (keyCode) {
                case 1: {}
                case 28: {
                    this.enterString();
                    break;
                }
                case 14: {
                    this.removeChar();
                    break;
                }
                default: {
                    if (ChatAllowedCharacters.func_71566_a(typedChar)) {
                        this.addChar(typedChar);
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }
    
    private void enterString() {
        if (this.currentString.isEmpty()) {
            this.setting.setValue(this.setting.getDefaultValue());
        }
        else {
            this.setting.setValue((Object)this.currentString);
        }
        this.setListening(false);
    }
    
    private void removeChar() {
        if (!this.currentString.isEmpty()) {
            this.currentString = this.currentString.substring(0, this.currentString.length() - 1);
        }
    }
    
    private void addChar(final char character) {
        this.currentString += character;
    }
    
    public int getHeight() {
        return 14;
    }
    
    public void toggle() {
        this.isListening = !this.isListening;
    }
    
    public boolean getState() {
        return !this.isListening;
    }
    
    public boolean isListening() {
        return this.isListening;
    }
    
    public void setListening(final boolean listening) {
        this.isListening = listening;
        if (listening) {
            this.currentString = (String)this.setting.getValue();
        }
    }
}
