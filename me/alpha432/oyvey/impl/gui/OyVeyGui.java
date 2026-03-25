//Decompiled by Procyon!

package me.alpha432.oyvey.impl.gui;

import me.alpha432.oyvey.impl.gui.components.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.impl.gui.components.items.buttons.*;
import me.alpha432.oyvey.api.features.*;
import java.util.function.*;
import java.util.*;
import me.alpha432.oyvey.impl.gui.components.items.*;
import net.minecraft.client.gui.*;
import org.lwjgl.input.*;
import java.io.*;

public class OyVeyGui extends GuiScreen
{
    private static OyVeyGui INSTANCE;
    private final ArrayList<Component> components;
    private boolean loaded;
    
    public OyVeyGui() {
        this.components = new ArrayList<Component>();
        this.loaded = false;
        this.setInstance();
    }
    
    public static OyVeyGui getInstance() {
        if (OyVeyGui.INSTANCE == null) {
            OyVeyGui.INSTANCE = new OyVeyGui();
        }
        return OyVeyGui.INSTANCE;
    }
    
    public static OyVeyGui getClickGui() {
        return getInstance();
    }
    
    private void setInstance() {
        OyVeyGui.INSTANCE = this;
    }
    
    private void load() {
        if (this.loaded || this.field_146297_k == null) {
            return;
        }
        final int startX = 2;
        final int startY = 20;
        int currentX = startX;
        final int panelWidth = 125;
        final int panelSpacing = 5;
        for (final Module.Category category : OyVey.moduleManager.getCategories()) {
            this.components.add(new Component(category.getName(), currentX, startY, true) {
                public void setupItems() {
                    OyVeyGui$1.counter1 = new int[] { 1 };
                    OyVey.moduleManager.getModulesByCategory(category).forEach(module -> {
                        if (!module.hidden) {
                            this.addButton((Button)new ModuleButton(module));
                        }
                    });
                }
            });
            currentX += panelWidth + panelSpacing;
        }
        this.components.forEach(components -> components.getItems().sort(Comparator.comparing((Function<? super E, ? extends Comparable>)Feature::getName)));
        this.loaded = true;
    }
    
    public void updateModule(final Module module) {
        if (!this.loaded) {
            return;
        }
        for (final Component component : this.components) {
            for (final Item item : component.getItems()) {
                if (!(item instanceof ModuleButton)) {
                    continue;
                }
                final ModuleButton button = (ModuleButton)item;
                final Module mod = button.getModule();
                if (module == null) {
                    continue;
                }
                if (!module.equals(mod)) {
                    continue;
                }
                button.initSettings();
            }
        }
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        if (!this.loaded) {
            this.load();
        }
        if (!this.loaded) {
            return;
        }
        this.checkMouseWheel();
        this.func_73733_a(0, 0, this.field_146294_l, this.field_146295_m, 1342177280, 1342177280);
        this.components.forEach(components -> components.drawScreen(mouseX, mouseY, partialTicks));
        final ModuleButton hovered = this.getHoveredModuleButton(mouseX, mouseY);
        if (hovered != null) {
            final String desc = hovered.getModule().getDescription();
            if (desc != null && !desc.isEmpty()) {
                final int descWidth = OyVey.textManager.getStringWidth(desc) + 8;
                final int descHeight = 12;
                final int x = mouseX + 10;
                final int y = mouseY + 10;
                Gui.func_73734_a(x, y, x + descWidth, y + descHeight, -1442840576);
                OyVey.textManager.drawStringWithShadow(desc, (float)(x + 4), (float)(y + 2), 16777215);
            }
        }
    }
    
    public ModuleButton getHoveredModuleButton(final int mouseX, final int mouseY) {
        for (final Component component : this.components) {
            for (final Item item : component.getItems()) {
                if (item instanceof ModuleButton && item.isHovering(mouseX, mouseY)) {
                    return (ModuleButton)item;
                }
            }
        }
        return null;
    }
    
    protected void func_73864_a(final int mouseX, final int mouseY, final int clickedButton) {
        if (!this.loaded) {
            return;
        }
        this.components.forEach(components -> components.mouseClicked(mouseX, mouseY, clickedButton));
    }
    
    protected void func_146286_b(final int mouseX, final int mouseY, final int releaseButton) {
        if (!this.loaded) {
            return;
        }
        this.components.forEach(components -> components.mouseReleased(mouseX, mouseY, releaseButton));
    }
    
    public boolean func_73868_f() {
        return false;
    }
    
    public final ArrayList<Component> getComponents() {
        return this.components;
    }
    
    public void checkMouseWheel() {
        if (!this.loaded) {
            return;
        }
        final int dWheel = Mouse.getDWheel();
        if (dWheel < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 15));
        }
        else if (dWheel > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 15));
        }
    }
    
    public int getTextOffset() {
        return 0;
    }
    
    public Component getComponentByName(final String name) {
        if (!this.loaded) {
            return null;
        }
        for (final Component component : this.components) {
            if (!component.getName().equalsIgnoreCase(name)) {
                continue;
            }
            return component;
        }
        return null;
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        super.func_73869_a(typedChar, keyCode);
        if (this.loaded) {
            this.components.forEach(component -> component.onKeyTyped(typedChar, keyCode));
        }
    }
    
    static {
        OyVeyGui.INSTANCE = new OyVeyGui();
    }
}
