//Decompiled by Procyon!

package me.alpha432.oyvey.impl.gui.components.items.buttons;

import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.util.render.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.*;
import net.minecraft.init.*;
import net.minecraft.client.audio.*;

public class EnumButton extends Button
{
    public Setting setting;
    
    public EnumButton(final Setting setting) {
        super(setting.getName());
        this.setting = setting;
        this.width = 15;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final int bgColor = this.isHovering(mouseX, mouseY) ? 1614823488 : 1345335344;
        RenderUtil.drawRect(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height - 0.5f, (long)bgColor);
        final int outlineColor = this.isHovering(mouseX, mouseY) ? -10461088 : -12566464;
        RenderUtil.drawOutlineRect(this.x, this.y, this.x + this.width + 7.4f, this.y + this.height - 0.5f, 1.0f, outlineColor);
        final float arrowX = this.x + this.width + 2.4f;
        final float arrowY = this.y + this.height / 2.0f - 1.0f;
        RenderUtil.drawRect(arrowX - 6.0f, arrowY - 1.0f, arrowX - 4.0f, arrowY + 1.0f, -7829368L);
        RenderUtil.drawRect(arrowX + 2.0f, arrowY - 1.0f, arrowX + 4.0f, arrowY + 1.0f, -7829368L);
        final String enumValue = this.setting.currentEnumName().equalsIgnoreCase("ABC") ? "ABC" : this.setting.currentEnumName();
        final String displayText = this.setting.getName() + " " + ChatFormatting.GRAY + enumValue;
        final float textY = this.y + this.height / 2.0f - 4.5f;
        OyVey.textManager.drawStringWithShadow(displayText, this.x + 2.3f, textY, -1);
    }
    
    public void update() {
        this.setHidden(!this.setting.isVisible());
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.isHovering(mouseX, mouseY)) {
            EnumButton.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0f));
        }
    }
    
    public int getHeight() {
        return 14;
    }
    
    public void toggle() {
        this.setting.increaseEnum();
    }
    
    public boolean getState() {
        return true;
    }
}
