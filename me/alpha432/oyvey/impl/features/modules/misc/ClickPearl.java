//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.misc;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import org.lwjgl.input.*;
import me.alpha432.oyvey.impl.events.*;
import java.awt.*;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.item.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.init.*;
import me.alpha432.oyvey.impl.command.*;
import me.alpha432.oyvey.impl.features.modules.player.*;
import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.entity.player.*;
import net.minecraft.world.*;

public class ClickPearl extends Module
{
    private boolean clicked;
    private boolean startedTimer;
    private long startTime;
    public Setting<Boolean> charge;
    public Setting<Integer> chargeTime;
    public Setting<Boolean> offhandSwap;
    
    public ClickPearl() {
        super("ClickPearl", "Allows you to pearling by pressing only one button", Module.Category.MISC, true, false, false);
        this.clicked = false;
        this.startedTimer = false;
        this.startTime = 0L;
        this.charge = (Setting<Boolean>)this.register(new Setting("Charge", (Object)true));
        this.chargeTime = (Setting<Integer>)this.register(new Setting("ChargeTime", (Object)500, (Object)100, (Object)2000));
        this.offhandSwap = (Setting<Boolean>)this.register(new Setting("OffhandSwap", (Object)false));
    }
    
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
        }
        this.startedTimer = false;
        this.startTime = 0L;
    }
    
    public void onTick() {
        if (Mouse.isButtonDown(2)) {
            if (!this.clicked) {
                if ((boolean)this.charge.getValue() && !this.startedTimer) {
                    this.startTime = System.currentTimeMillis();
                    this.startedTimer = true;
                }
                else if (!(boolean)this.charge.getValue()) {
                    this.throwPearl();
                    this.clicked = true;
                }
            }
        }
        else {
            if (this.startedTimer && (boolean)this.charge.getValue()) {
                this.throwPearl();
            }
            this.startedTimer = false;
            this.clicked = false;
        }
    }
    
    @SubscribeEvent
    public void onRender2D(final Render2DEvent event) {
        if (this.startedTimer && (boolean)this.charge.getValue()) {
            final ScaledResolution resolution = new ScaledResolution(ClickPearl.mc);
            final int x = resolution.func_78326_a() / 2;
            final int y = resolution.func_78328_b() / 2 + 13;
            final float percentage = Math.min(1.0f, (System.currentTimeMillis() - this.startTime) / (float)(int)this.chargeTime.getValue());
            final int width = 80;
            final int height = 2;
            Gui.func_73734_a(x - 40 - 1, y - 1, x + 40 + 1, y + height + 1, new Color(0, 0, 0, 128).getRGB());
            final Color barColor = (Color.RED != null) ? Color.RED : Color.WHITE;
            final int progressWidth = (int)(width * percentage);
            Gui.func_73734_a(x - 40, y, x - 40 + progressWidth, y + height, barColor.getRGB());
        }
    }
    
    private void throwPearl() {
        final int pearlSlot = InventoryUtil.findHotbarBlock((Class)ItemEnderPearl.class);
        final boolean bl;
        boolean offhand = bl = (ClickPearl.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151079_bi);
        if (pearlSlot != -1 || offhand) {
            final int oldslot = ClickPearl.mc.field_71439_g.field_71071_by.field_70461_c;
            if ((boolean)this.offhandSwap.getValue() && pearlSlot != -1) {
                Command.sendMessage("no");
                offhand = true;
            }
            else if (!offhand) {
                InventoryUtil.switchToHotbarSlot(pearlSlot, false);
            }
            if ((boolean)this.charge.getValue() && this.startedTimer && System.currentTimeMillis() - this.startTime >= (int)this.chargeTime.getValue() && FastProjectile.getInstance() != null) {
                FastProjectile.getInstance().doTheWork();
            }
            if ((boolean)this.charge.getValue() && this.startedTimer) {
                ClickPearl.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItem(offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND));
            }
            else {
                ClickPearl.mc.field_71442_b.func_187101_a((EntityPlayer)ClickPearl.mc.field_71439_g, (World)ClickPearl.mc.field_71441_e, offhand ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
            }
            if (!offhand && !(boolean)this.offhandSwap.getValue()) {
                InventoryUtil.switchToHotbarSlot(oldslot, false);
            }
        }
    }
}
