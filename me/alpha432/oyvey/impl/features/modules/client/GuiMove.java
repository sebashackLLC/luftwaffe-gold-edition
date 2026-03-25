//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.client;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraft.client.settings.*;
import org.lwjgl.input.*;
import me.alpha432.oyvey.impl.gui.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.gui.*;

public class GuiMove extends Module
{
    private static GuiMove INSTANCE;
    public Setting<Boolean> rotate;
    public Setting<Boolean> inventory;
    public Setting<Boolean> clickGui;
    public Setting<Boolean> anyGui;
    
    public GuiMove() {
        super("GuiMove", "Allows you to move while GUIs are open", Module.Category.CLIENT, true, false, false);
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (Object)true));
        this.inventory = (Setting<Boolean>)this.register(new Setting("Inventory", (Object)true));
        this.clickGui = (Setting<Boolean>)this.register(new Setting("ClickGui", (Object)true));
        this.anyGui = (Setting<Boolean>)this.register(new Setting("AnyGui", (Object)false));
        this.setInstance();
    }
    
    public static GuiMove getInstance() {
        if (GuiMove.INSTANCE == null) {
            GuiMove.INSTANCE = new GuiMove();
        }
        return GuiMove.INSTANCE;
    }
    
    private void setInstance() {
        GuiMove.INSTANCE = this;
    }
    
    public void onUpdate() {
        if (GuiMove.mc.field_71462_r == null) {
            return;
        }
        if (!this.shouldMove(GuiMove.mc.field_71462_r)) {
            return;
        }
        final KeyBinding[] array;
        final KeyBinding[] moveKeys = array = new KeyBinding[] { GuiMove.mc.field_71474_y.field_74351_w, GuiMove.mc.field_71474_y.field_74368_y, GuiMove.mc.field_71474_y.field_74370_x, GuiMove.mc.field_71474_y.field_74366_z, GuiMove.mc.field_71474_y.field_74314_A, GuiMove.mc.field_71474_y.field_151444_V };
        for (final KeyBinding key : array) {
            KeyBinding.func_74510_a(key.func_151463_i(), Keyboard.isKeyDown(key.func_151463_i()));
        }
        if (Keyboard.isKeyDown(GuiMove.mc.field_71474_y.field_74311_E.func_151463_i())) {
            KeyBinding.func_74510_a(GuiMove.mc.field_71474_y.field_74311_E.func_151463_i(), true);
        }
    }
    
    public boolean shouldMove(final GuiScreen screen) {
        return screen != null && !(screen instanceof GuiChat) && ((screen instanceof OyVeyGui && (boolean)this.clickGui.getValue()) || (screen instanceof GuiContainer && (boolean)this.inventory.getValue()) || screen instanceof GuiIngameMenu || screen instanceof GuiRepair || (boolean)this.anyGui.getValue());
    }
    
    public boolean shouldRotate() {
        return this.isOn() && (boolean)this.rotate.getValue() && GuiMove.mc.field_71462_r != null && this.shouldMove(GuiMove.mc.field_71462_r);
    }
    
    static {
        GuiMove.INSTANCE = new GuiMove();
    }
}
