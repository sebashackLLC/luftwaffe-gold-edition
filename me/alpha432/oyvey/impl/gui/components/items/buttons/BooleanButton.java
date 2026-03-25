//Decompiled by Procyon!

package me.alpha432.oyvey.impl.gui.components.items.buttons;

import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.util.render.*;
import me.alpha432.oyvey.*;
import net.minecraft.init.*;
import net.minecraft.client.audio.*;

public class BooleanButton extends Button
{
    private final Setting setting;
    
    public BooleanButton(final Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final int bgColor = this.getState() ? (this.isHovering(mouseX, mouseY) ? 1614823488 : 1346388032) : (this.isHovering(mouseX, mouseY) ? 1075847200 : 807411744);
        RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height - 0.5f, (long)bgColor);
        final int outlineColor = this.getState() ? (this.isHovering(mouseX, mouseY) ? -10461088 : -11513776) : (this.isHovering(mouseX, mouseY) ? -12566464 : -13619152);
        RenderUtil.drawOutlineRect(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height - 0.5f, 1.0f, outlineColor);
        final float toggleX = this.x + this.width + 4.4f;
        final float toggleY = this.y + this.height / 2.0f - 2.5f;
        final int toggleColor = this.getState() ? -10027162 : -10066330;
        RenderUtil.drawCircle(toggleX, toggleY, 2.5f, 12, toggleColor);
        final float textY = this.y + this.height / 2.0f - 4.5f;
        final int textColor = this.getState() ? -1 : -5592406;
        OyVey.textManager.drawStringWithShadow(this.getName(), this.x + 2.3f, textY, textColor);
    }
    
    @Override
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }
    
    @Override
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            BooleanButton.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0f));
        }
    }
    
    @Override
    public int getHeight() {
        return 14;
    }
    
    @Override
    public void toggle() {
        this.setting.setValue((Object)!(boolean)this.setting.getValue());
    }
    
    @Override
    public boolean getState() {
        return (boolean)this.setting.getValue();
    }
}
