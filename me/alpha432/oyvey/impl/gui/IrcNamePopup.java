//Decompiled by Procyon!

package me.alpha432.oyvey.impl.gui;

import net.minecraft.client.gui.*;
import org.lwjgl.input.*;
import java.awt.*;
import me.alpha432.oyvey.manager.*;
import java.io.*;

public class IrcNamePopup extends GuiScreen
{
    private GuiTextField ircNameField;
    private GuiButton confirmButton;
    private String errorMessage;
    
    public IrcNamePopup() {
        this.errorMessage = "";
    }
    
    public void func_73866_w_() {
        Keyboard.enableRepeatEvents(true);
        final int centerX = this.field_146294_l / 2;
        final int centerY = this.field_146295_m / 2;
        (this.ircNameField = new GuiTextField(0, this.field_146289_q, centerX - 100, centerY - 10, 200, 20)).func_146203_f(32);
        this.ircNameField.func_146195_b(true);
        this.confirmButton = new GuiButton(1, centerX - 50, centerY + 20, 100, 20, "Confirm");
        this.field_146292_n.add(this.confirmButton);
    }
    
    public void func_73863_a(final int mouseX, final int mouseY, final float partialTicks) {
        this.func_146276_q_();
        final int centerX = this.field_146294_l / 2;
        final int centerY = this.field_146295_m / 2;
        func_73734_a(centerX - 120, centerY - 60, centerX + 120, centerY + 50, new Color(30, 30, 30, 230).getRGB());
        func_73734_a(centerX - 119, centerY - 59, centerX + 119, centerY + 49, new Color(50, 50, 50, 230).getRGB());
        final String title = "Enter your IRC Name";
        this.func_73732_a(this.field_146289_q, title, centerX, centerY - 45, 16777215);
        final String subtitle = "This will be used to identify you in IRC";
        this.func_73732_a(this.field_146289_q, subtitle, centerX, centerY - 30, 11184810);
        this.ircNameField.func_146194_f();
        if (!this.errorMessage.isEmpty()) {
            this.func_73732_a(this.field_146289_q, this.errorMessage, centerX, centerY + 45, 16733525);
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
    
    protected void func_146284_a(final GuiButton button) throws IOException {
        if (button.field_146127_k == 1) {
            final String ircName = this.ircNameField.func_146179_b().trim();
            if (ircName.isEmpty()) {
                this.errorMessage = "IRC name cannot be empty!";
                return;
            }
            if (ircName.length() < 2) {
                this.errorMessage = "IRC name must be at least 2 characters!";
                return;
            }
            IrcNameManager.saveIrcName(ircName);
            IrcNameManager.sendLaunchNotification();
            this.field_146297_k.func_147108_a((GuiScreen)null);
        }
    }
    
    protected void func_73869_a(final char typedChar, final int keyCode) throws IOException {
        this.ircNameField.func_146201_a(typedChar, keyCode);
        if (keyCode == 28) {
            this.func_146284_a(this.confirmButton);
        }
    }
    
    protected void func_73864_a(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.func_73864_a(mouseX, mouseY, mouseButton);
        this.ircNameField.func_146192_a(mouseX, mouseY, mouseButton);
    }
    
    public void func_73876_c() {
        this.ircNameField.func_146178_a();
    }
    
    public void func_146281_b() {
        Keyboard.enableRepeatEvents(false);
    }
    
    public boolean func_73868_f() {
        return true;
    }
}
