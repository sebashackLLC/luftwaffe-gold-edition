//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.combat;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import org.lwjgl.input.*;
import me.alpha432.oyvey.impl.command.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.api.util.*;
import me.alpha432.oyvey.impl.features.modules.hud.*;
import net.minecraft.util.text.*;
import net.minecraftforge.common.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.api.util.entity.*;
import java.util.*;
import me.alpha432.oyvey.manager.*;

public class FutureCA extends Module
{
    private static FutureCA INSTANCE;
    public static Entity target;
    private boolean dmgKeyPressed;
    private boolean isApplied;
    public Setting<Boolean> antiFuture;
    public Setting<Bind> dmgKey;
    Setting<Double> dmg;
    Setting<Double> minDmg;
    
    public FutureCA() {
        super("AutoCrystal", "Thing for sync future autocrystal with 58h4ck", Module.Category.COMBAT, false, false, false);
        this.dmgKeyPressed = false;
        this.isApplied = false;
        this.antiFuture = (Setting<Boolean>)this.register(new Setting("AntiFuture", (Object)false));
        this.dmgKey = (Setting<Bind>)this.register(new Setting("DmgKey", (Object)new Bind(-1), "Keybind to call for apply minimum damage override"));
        this.dmg = (Setting<Double>)this.register(new Setting("Damage", (Object)4.0, (Object)0.0, (Object)20.0));
        this.minDmg = (Setting<Double>)this.register(new Setting("Min Damage", (Object)2.0, (Object)0.0, (Object)20.0));
        this.setInstance();
    }
    
    public static FutureCA getInstance() {
        if (FutureCA.INSTANCE == null) {
            FutureCA.INSTANCE = new FutureCA();
        }
        return FutureCA.INSTANCE;
    }
    
    private void setInstance() {
        FutureCA.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onUpdate() {
        FutureCA.target = this.getTarget();
        if (((Bind)this.dmgKey.getValue()).getKey() != -1 && Keyboard.isKeyDown(((Bind)this.dmgKey.getValue()).getKey())) {
            if (!this.dmgKeyPressed) {
                this.dmgKeyPressed = true;
                if (this.isEnabled()) {
                    if (!this.isApplied) {
                        FutureCA.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "autocrystal mindamage " + this.minDmg.getValue());
                        this.isApplied = true;
                    }
                    else {
                        FutureCA.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "autocrystal mindamage " + this.dmg.getValue());
                        this.isApplied = false;
                    }
                }
                else if (!this.isEnabled()) {
                    Command.sendMessage("�c[IRC] IRC module is not enabled!");
                }
            }
        }
        else {
            this.dmgKeyPressed = false;
        }
    }
    
    public void onEnable() {
        if (fullNullCheck()) {
            this.disable();
            return;
        }
        if (!(boolean)this.antiFuture.getValue()) {
            if (Sync.getInstance().Mode.getValue() == Sync.clients.Future) {
                FutureCA.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "t autocrystal true");
            }
            else {
                FutureCA.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "autocrystal enabled true");
            }
        }
    }
    
    public void onDisable() {
        if (fullNullCheck()) {
            return;
        }
        if (!(boolean)this.antiFuture.getValue()) {
            if (Sync.getInstance().Mode.getValue() == Sync.clients.Future) {
                FutureCA.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "t autocrystal false");
            }
            else {
                FutureCA.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "autocrystal enabled false");
            }
        }
    }
    
    public void enable() {
        this.enabled.setValue((Object)Boolean.TRUE);
        this.onToggle();
        this.onEnable();
        if (Management.getInstance().notifyToggles.getValue()) {
            if (Management.getInstance().mode.getValue() == Management.Modes.Luftwaffe) {
                final TextComponentString text = new TextComponentString(OyVey.commandManager.getClientMessage() + " " + ChatFormatting.BOLD + ChatFormatting.WHITE + this.getDisplayName() + TextUtil.coloredString(" toggled", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + ChatFormatting.GREEN + " on");
                Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
            }
            if (Management.getInstance().mode.getValue() == Management.Modes.DotGod) {
                final TextComponentString text = new TextComponentString(ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + (String)Management.getInstance().clientName.getValue() + ChatFormatting.DARK_PURPLE + "] " + ChatFormatting.DARK_AQUA + this.getDisplayName() + ChatFormatting.LIGHT_PURPLE + " was " + ChatFormatting.GREEN + "enabled");
                Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
            }
            if (Management.getInstance().mode.getValue() == Management.Modes.Sn0w) {
                final TextComponentString text = new TextComponentString(ChatFormatting.BLUE + "[" + ChatFormatting.AQUA + "\u2744" + ChatFormatting.BLUE + "] " + ChatFormatting.WHITE + ChatFormatting.BOLD + this.getDisplayName() + ChatFormatting.RESET + " was " + ChatFormatting.BLUE + "enabled" + ChatFormatting.RESET + ".");
                Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
            }
        }
        if (this.isOn() && this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.register((Object)this);
        }
    }
    
    public void disable() {
        if (this.hasListener && !this.alwaysListening) {
            MinecraftForge.EVENT_BUS.unregister((Object)this);
        }
        this.enabled.setValue((Object)false);
        if (Management.getInstance().notifyToggles.getValue()) {
            if (Management.getInstance().mode.getValue() == Management.Modes.Luftwaffe) {
                final TextComponentString text = new TextComponentString(OyVey.commandManager.getClientMessage() + " " + ChatFormatting.BOLD + ChatFormatting.WHITE + this.getDisplayName() + TextUtil.coloredString(" toggled", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + ChatFormatting.RED + " off");
                Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
            }
            if (Management.getInstance().mode.getValue() == Management.Modes.DotGod) {
                final TextComponentString text = new TextComponentString(ChatFormatting.DARK_PURPLE + "[" + ChatFormatting.LIGHT_PURPLE + (String)Management.getInstance().clientName.getValue() + ChatFormatting.DARK_PURPLE + "] " + ChatFormatting.DARK_AQUA + this.getDisplayName() + ChatFormatting.LIGHT_PURPLE + " was " + ChatFormatting.RED + "disabled");
                Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
            }
            if (Management.getInstance().mode.getValue() == Management.Modes.Sn0w) {
                final TextComponentString text = new TextComponentString(ChatFormatting.BLUE + "[" + ChatFormatting.AQUA + "\u2744" + ChatFormatting.BLUE + "] " + ChatFormatting.WHITE + ChatFormatting.BOLD + this.getDisplayName() + ChatFormatting.RESET + " was " + ChatFormatting.BLUE + "disabled" + ChatFormatting.RESET + ".");
                Module.mc.field_71456_v.func_146158_b().func_146234_a((ITextComponent)text, 1);
            }
        }
        this.onToggle();
        this.onDisable();
    }
    
    private Entity getTarget() {
        Entity target = null;
        double distance = (float)Resolver.getInstance().range.getValue();
        double maxHealth = 36.0;
        for (final Entity entity : FutureCA.mc.field_71441_e.field_73010_i) {
            if (((boolean)Resolver.getInstance().players.getValue() && entity instanceof EntityPlayer) || ((boolean)Resolver.getInstance().animals.getValue() && EntityUtil.isPassive(entity)) || ((boolean)Resolver.getInstance().mobs.getValue() && EntityUtil.isMobAggressive(entity)) || ((boolean)Resolver.getInstance().vehicles.getValue() && EntityUtil.isVehicle(entity)) || ((boolean)Resolver.getInstance().projectiles.getValue() && EntityUtil.isProjectile(entity))) {
                if (entity instanceof EntityLivingBase && EntityUtil.isntValid(entity, distance)) {
                    continue;
                }
                if (!FutureCA.mc.field_71439_g.func_70685_l(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && FutureCA.mc.field_71439_g.func_70068_e(entity) > MathUtil.square((double)(float)Resolver.getInstance().raytrace.getValue())) {
                    continue;
                }
                if (target == null) {
                    target = entity;
                    distance = FutureCA.mc.field_71439_g.func_70068_e(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
                else {
                    if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer)entity, 18)) {
                        target = entity;
                        break;
                    }
                    if (FutureCA.mc.field_71439_g.func_70068_e(entity) < distance) {
                        target = entity;
                        distance = FutureCA.mc.field_71439_g.func_70068_e(entity);
                        maxHealth = EntityUtil.getHealth(entity);
                    }
                    if (EntityUtil.getHealth(entity) >= maxHealth) {
                        continue;
                    }
                    target = entity;
                    distance = FutureCA.mc.field_71439_g.func_70068_e(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
            }
        }
        return target;
    }
    
    public String getDisplayInfo() {
        if (!(FutureCA.target instanceof EntityPlayer)) {
            return ChatFormatting.RED + "none" + ChatFormatting.RESET;
        }
        final String targetName = FutureCA.target.func_70005_c_();
        final String ircName = IrcNameManager.getIrcNameForPlayer(targetName);
        if (ircName != null) {
            return ircName.toLowerCase();
        }
        return targetName.toLowerCase();
    }
    
    static {
        FutureCA.INSTANCE = new FutureCA();
    }
    
    public enum SwapMode
    {
        Normal, 
        Silent, 
        Bypass;
    }
}
