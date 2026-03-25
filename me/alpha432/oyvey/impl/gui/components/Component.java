//Decompiled by Procyon!

package me.alpha432.oyvey.impl.gui.components;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.impl.gui.components.items.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import me.alpha432.oyvey.api.util.render.*;
import me.alpha432.oyvey.*;
import java.util.*;
import me.alpha432.oyvey.impl.gui.*;
import net.minecraft.init.*;
import net.minecraft.client.audio.*;
import me.alpha432.oyvey.impl.gui.components.items.buttons.*;

public class Component extends Feature
{
    public static int[] counter1;
    private final ArrayList<Item> items;
    public boolean drag;
    private int x;
    private int y;
    private int x2;
    private int y2;
    private int width;
    private int height;
    private boolean open;
    private boolean hidden;
    
    public Component(final String name, final int x, final int y, final boolean open) {
        super(name);
        this.items = new ArrayList<Item>();
        this.width = 125;
        this.height = 18;
        this.hidden = false;
        this.x = x;
        this.y = y;
        this.open = open;
        this.setupItems();
    }
    
    public void setupItems() {
    }
    
    private void drag(final int mouseX, final int mouseY) {
        if (!this.drag) {
            return;
        }
        this.x = this.x2 + mouseX;
        this.y = this.y2 + mouseY;
    }
    
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drag(mouseX, mouseY);
        Component.counter1 = new int[] { 1 };
        final float totalItemHeight = this.open ? this.getTotalItemHeight() : 0.0f;
        final int headerColor1 = SomaGui.getInstance().syncGuiColor();
        final int headerColor2 = SomaGui.getInstance().syncGuiColor();
        RenderUtil.drawGradientRect(this.x, this.y, this.x + this.width, this.y + this.height, headerColor1, headerColor2);
        if (this.open) {
            final int bodyColor = SomaGui.getInstance().syncGuiColor();
            RenderUtil.drawRect((float)this.x, (float)(this.y + this.height), (float)(this.x + this.width), this.y + this.height + totalItemHeight, (long)bodyColor);
        }
        OyVey.textManager.drawStringWithShadow(this.getName(), (float)(this.x + 8), (float)(this.y + 6), -1);
        final String indicator = this.open ? "\u2212" : "+";
        OyVey.textManager.drawStringWithShadow(indicator, (float)(this.x + this.width - 15), (float)(this.y + 6), -5592406);
        if (this.open) {
            float itemY = (float)(this.y + this.height + 2);
            for (final Item item : this.getItems()) {
                final int[] counter1 = Component.counter1;
                final int n = 0;
                ++counter1[n];
                if (item.isHidden()) {
                    continue;
                }
                item.setLocation((float)(this.x + 2), itemY);
                item.setWidth(this.width - 4);
                item.drawScreen(mouseX, mouseY, partialTicks);
                itemY += item.getHeight() + 2;
            }
        }
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        if (mouseButton == 0 && this.isHovering(mouseX, mouseY)) {
            this.x2 = this.x - mouseX;
            this.y2 = this.y - mouseY;
            OyVeyGui.getClickGui().getComponents().forEach(component -> {
                if (component.drag) {
                    component.drag = false;
                }
                return;
            });
            this.drag = true;
            return;
        }
        if (mouseButton == 1 && this.isHovering(mouseX, mouseY)) {
            this.open = !this.open;
            Component.mc.func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_184371_a(SoundEvents.field_187909_gi, 1.0f));
            return;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int releaseButton) {
        if (releaseButton == 0) {
            this.drag = false;
        }
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
    }
    
    public void onKeyTyped(final char typedChar, final int keyCode) {
        if (!this.open) {
            return;
        }
        this.getItems().forEach(item -> item.onKeyTyped(typedChar, keyCode));
    }
    
    public void addButton(final Button button) {
        this.items.add(button);
    }
    
    public int getX() {
        return this.x;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public boolean isHidden() {
        return this.hidden;
    }
    
    public void setHidden(final boolean hidden) {
        this.hidden = hidden;
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public final ArrayList<Item> getItems() {
        return this.items;
    }
    
    private boolean isHovering(final int mouseX, final int mouseY) {
        return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() && mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight();
    }
    
    private float getTotalItemHeight() {
        float height = 0.0f;
        for (final Item item : this.getItems()) {
            if (!item.isHidden()) {
                height += item.getHeight() + 2;
            }
        }
        return height + 4.0f;
    }
    
    static {
        Component.counter1 = new int[] { 1 };
    }
}
