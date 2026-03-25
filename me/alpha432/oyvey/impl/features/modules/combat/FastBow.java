//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.combat;

import me.alpha432.oyvey.api.features.*;
import net.minecraft.client.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraftforge.fml.common.gameevent.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.init.*;
import net.minecraft.item.*;

public class FastBow extends Module
{
    private static FastBow INSTANCE;
    private final Minecraft mc;
    private boolean isShooting;
    private int previousSlot;
    private int bowSlot;
    private int shootTimer;
    private boolean hasFired;
    Setting<Integer> delay;
    
    public FastBow() {
        super("FastBow", "Automatically swaps on bow shooting and swaping back", Module.Category.COMBAT, true, false, false);
        this.mc = Minecraft.func_71410_x();
        this.isShooting = false;
        this.previousSlot = -1;
        this.bowSlot = -1;
        this.shootTimer = 0;
        this.hasFired = false;
        this.delay = (Setting<Integer>)this.register(new Setting("Ticks", (Object)3, (Object)3, (Object)7));
    }
    
    public static FastBow getInstance() {
        if (FastBow.INSTANCE == null) {
            FastBow.INSTANCE = new FastBow();
        }
        return FastBow.INSTANCE;
    }
    
    public void onEnable() {
        super.onEnable();
        this.resetState();
    }
    
    private void resetState() {
        this.isShooting = false;
        this.previousSlot = -1;
        this.bowSlot = -1;
        this.shootTimer = 0;
        this.hasFired = false;
    }
    
    @SubscribeEvent
    public void onClientTick(final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || this.mc.field_71439_g == null || this.hasFired) {
            return;
        }
        if (this.bowSlot == -1) {
            this.findBowInHotbar();
            if (this.bowSlot == -1) {
                return;
            }
        }
        if (!this.isShooting) {
            this.startShooting();
        }
        else {
            this.handleShooting();
        }
    }
    
    private void findBowInHotbar() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack stack = this.mc.field_71439_g.field_71071_by.func_70301_a(i);
            if (!stack.func_190926_b() && stack.func_77973_b() == Items.field_151031_f) {
                this.bowSlot = i;
                break;
            }
        }
    }
    
    private void startShooting() {
        this.previousSlot = this.mc.field_71439_g.field_71071_by.field_70461_c;
        this.mc.field_71439_g.field_71071_by.field_70461_c = this.bowSlot;
        this.isShooting = true;
        this.mc.field_71474_y.field_74313_G.field_74513_e = true;
        this.shootTimer = 0;
    }
    
    private void handleShooting() {
        ++this.shootTimer;
        if (this.shootTimer >= (int)this.delay.getValue()) {
            this.mc.field_71474_y.field_74313_G.field_74513_e = false;
            this.isShooting = false;
            this.hasFired = true;
            if (this.previousSlot != -1 && this.previousSlot < 9) {
                this.mc.field_71439_g.field_71071_by.field_70461_c = this.previousSlot;
            }
            this.disable();
        }
    }
    
    static {
        FastBow.INSTANCE = new FastBow();
    }
}
