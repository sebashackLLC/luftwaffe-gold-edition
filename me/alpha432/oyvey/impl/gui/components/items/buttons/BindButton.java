//Decompiled by Procyon!

package me.alpha432.oyvey.impl.gui.components.items.buttons;

import me.alpha432.oyvey.impl.features.modules.client.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.api.util.render.*;
import com.mojang.realmsclient.gui.*;
import org.lwjgl.input.*;
import net.minecraft.init.*;
import net.minecraft.client.audio.*;
import me.alpha432.oyvey.api.features.settings.*;

public class BindButton extends Button
{
    private final Setting setting;
    public boolean isListening;
    private boolean ignoreNextClick;
    
    public BindButton(final Setting setting) {
        super(setting.getName());
        this.ignoreNextClick = false;
        this.setting = setting;
        this.width = 15;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ColorSetting mainColor = (ColorSetting)Color.getInstance().mainColor.getValue();
        final int color = ColorUtil.toARGB(mainColor.getRed(), mainColor.getGreen(), mainColor.getBlue(), 255);
        RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height - 0.5f, this.getState() ? ((long)(this.isHovering(mouseX, mouseY) ? -2007673515 : 290805077)) : ((long)(this.isHovering(mouseX, mouseY) ? OyVey.colorManager.getColorWithAlpha(255) : OyVey.colorManager.getColorWithAlpha(mainColor.getAlpha()))));
        final float textY = this.y + this.height / 2.0f - 4.5f;
        if (this.isListening) {
            OyVey.textManager.drawStringWithShadow("Press a key...", this.x + 2.3f, textY, -1);
        }
        else {
            OyVey.textManager.drawStringWithShadow(this.setting.getName() + " " + ChatFormatting.GRAY + this.setting.getValue().toString().toUpperCase(), this.x + 2.3f, textY, this.getState() ? -1 : -5592406);
        }
    }
    
    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
        if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1) && !Mouse.isButtonDown(2)) {
            this.ignoreNextClick = false;
        }
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (!this.isHovering(mouseX, mouseY)) {
            if (this.isListening) {
                this.isListening = false;
            }
            return;
        }
        BindButton.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0f));
        if (this.isListening) {
            if (!this.ignoreNextClick) {
                int bindKey;
                if ((bindKey = mouseButton) == 3) {
                    bindKey = 3;
                }
                else if (mouseButton == 4) {
                    bindKey = 4;
                }
                final Bind bind = new Bind(bindKey);
                this.setting.setValue((Object)bind);
                this.isListening = false;
            }
        }
        else {
            this.isListening = true;
            this.ignoreNextClick = true;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void onKeyTyped(final char typedChar, final int keyCode) {
        if (this.isListening) {
            if (keyCode == 1) {
                this.isListening = false;
                return;
            }
            Bind bind = new Bind(keyCode);
            if (bind.toString().equalsIgnoreCase("Delete")) {
                bind = new Bind(-1);
            }
            this.setting.setValue((Object)bind);
            this.isListening = false;
        }
    }
    
    @Override
    public int getHeight() {
        return 14;
    }
    
    @Override
    public void toggle() {
        this.isListening = !this.isListening;
        this.ignoreNextClick = true;
    }
    
    @Override
    public boolean getState() {
        return !this.isListening;
    }
}
