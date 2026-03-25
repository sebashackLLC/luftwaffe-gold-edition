//Decompiled by Procyon!

package me.alpha432.oyvey.impl.gui.components.items.buttons;

import me.alpha432.oyvey.api.features.*;
import net.minecraft.util.*;
import me.alpha432.oyvey.impl.gui.components.items.*;
import org.lwjgl.opengl.*;
import me.alpha432.oyvey.api.features.settings.*;
import java.util.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import me.alpha432.oyvey.api.util.render.*;
import me.alpha432.oyvey.*;
import net.minecraft.init.*;
import net.minecraft.client.audio.*;

public class ModuleButton extends Button
{
    private final Module module;
    private final ResourceLocation logo;
    private List<Item> items;
    private boolean subOpen;
    
    public ModuleButton(final Module module) {
        super(module.getName());
        this.logo = new ResourceLocation("textures/oyvey.png");
        this.items = new ArrayList<Item>();
        this.module = module;
        this.initSettings();
    }
    
    public static void drawCompleteImage(final float posX, final float posY, final int width, final int height) {
        GL11.glPushMatrix();
        GL11.glTranslatef(posX, posY, 0.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3f(0.0f, 0.0f, 0.0f);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3f(0.0f, (float)height, 0.0f);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3f((float)width, (float)height, 0.0f);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3f((float)width, 0.0f, 0.0f);
        GL11.glEnd();
        GL11.glPopMatrix();
    }
    
    public void initSettings() {
        final ArrayList<Item> newItems = new ArrayList<Item>();
        if (!this.module.getSettings().isEmpty()) {
            for (final Setting setting : this.module.getSettings()) {
                if (setting.getValue() instanceof Boolean && !setting.getName().equals("Enabled")) {
                    newItems.add((Item)new BooleanButton(setting));
                }
                if (setting.getValue() instanceof Bind && !setting.getName().equalsIgnoreCase("Keybind") && !this.module.getName().equalsIgnoreCase("Hud")) {
                    newItems.add((Item)new BindButton(setting));
                }
                if ((setting.getValue() instanceof String || setting.getValue() instanceof Character) && !setting.getName().equalsIgnoreCase("displayName")) {
                    newItems.add((Item)new StringButton(setting));
                }
                if (setting.getValue() instanceof ColorSetting) {
                    newItems.add((Item)new ColorButton(setting));
                }
                else if (setting.isNumberSetting() && setting.hasRestriction()) {
                    newItems.add((Item)new Slider(setting));
                }
                else {
                    if (!setting.isEnumSetting()) {
                        continue;
                    }
                    newItems.add((Item)new EnumButton(setting));
                }
            }
        }
        newItems.add((Item)new BindButton(this.module.getSettingByName("Keybind")));
        this.items = newItems;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.width = 120;
        final int bgColor = this.module.isEnabled() ? SomaGui.getInstance().syncModuleColor() : Integer.MIN_VALUE;
        RenderUtil.drawRect(this.x, this.y, this.x + this.width, this.y + 14.0f, (long)bgColor);
        RenderUtil.drawOutlineRect(this.x, this.y, this.x + this.width, this.y + 14.0f, 0.5f, -16777216);
        OyVey.textManager.drawStringWithShadow(this.module.getName(), this.x + 8.0f, this.y + 4.0f, this.module.isEnabled() ? -1 : -5592406);
        final String indicator = this.subOpen ? "-" : "+";
        OyVey.textManager.drawStringWithShadow(indicator, this.x + this.width - 12.0f, this.y + 4.0f, -1);
        if (!this.items.isEmpty() && this.subOpen) {
            final int settingsWidth = 120;
            GL11.glEnable(3089);
            RenderUtil.scissor(this.x + 1.0f, this.y + 15.0f, (float)(settingsWidth - 2), (float)(this.getHeight() - 16));
            final float dropdownY = this.y + 14.0f;
            RenderUtil.drawRect(this.x + 1.0f, dropdownY + 1.0f, this.x + settingsWidth - 1.0f, dropdownY + this.getHeight() - 15.0f, (long)SomaGui.getInstance().syncGuiColor());
            float itemY = dropdownY + 2.0f;
            for (final Item item : this.items) {
                if (!item.isHidden()) {
                    item.setLocation(this.x + 2.0f, itemY);
                    item.setWidth(settingsWidth - 4);
                    item.setHeight(13);
                    item.drawScreen(mouseX, mouseY, partialTicks);
                    itemY += item.getHeight() + 1;
                }
                item.update();
            }
            GL11.glDisable(3089);
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (!this.items.isEmpty()) {
            if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
                this.subOpen = !this.subOpen;
                ModuleButton.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0f));
            }
            if (this.subOpen) {
                for (final Item item : this.items) {
                    if (item.isHidden()) {
                        continue;
                    }
                    item.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }
    
    public void onKeyTyped(final char typedChar, final int keyCode) {
        super.onKeyTyped(typedChar, keyCode);
        if (!this.items.isEmpty() && this.subOpen) {
            for (final Item item : this.items) {
                if (item.isHidden()) {
                    continue;
                }
                item.onKeyTyped(typedChar, keyCode);
            }
        }
    }
    
    public int getHeight() {
        if (this.subOpen) {
            int height = 14;
            for (final Item item : this.items) {
                if (item.isHidden()) {
                    continue;
                }
                height += item.getHeight() + 1;
            }
            return height + 2;
        }
        return 14;
    }
    
    public Module getModule() {
        return this.module;
    }
    
    public void toggle() {
        this.module.toggle();
    }
    
    public boolean getState() {
        return this.module.isEnabled();
    }
}
