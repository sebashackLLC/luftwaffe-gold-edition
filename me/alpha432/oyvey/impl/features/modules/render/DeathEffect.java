//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import java.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.effect.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.util.*;

public class DeathEffect extends Module
{
    public Setting<Integer> numbersThunder;
    ArrayList<EntityPlayer> playersDead;
    
    public DeathEffect() {
        super("DeathEffect", "Render thunders when some1 is dying", Module.Category.RENDER, true, false, false);
        this.numbersThunder = (Setting<Integer>)this.register(new Setting("Thunder value", (Object)1, (Object)1, (Object)10));
        this.playersDead = new ArrayList<EntityPlayer>();
    }
    
    public void onEnable() {
        this.playersDead.clear();
    }
    
    public void onUpdate() {
        if (DeathEffect.mc.field_71441_e == null) {
            this.playersDead.clear();
            return;
        }
        int i;
        DeathEffect.mc.field_71441_e.field_73010_i.forEach(entity -> {
            if (this.playersDead.contains(entity)) {
                if (entity.func_110143_aJ() > 0.0f) {
                    this.playersDead.remove(entity);
                }
            }
            else if (entity.func_110143_aJ() == 0.0f) {
                if (this.isOn()) {
                    for (i = 0; i < (int)this.numbersThunder.getValue(); ++i) {
                        DeathEffect.mc.field_71441_e.func_72838_d((Entity)new EntityLightningBolt((World)DeathEffect.mc.field_71441_e, entity.field_70165_t, entity.field_70163_u, entity.field_70161_v, true));
                    }
                }
                this.playersDead.add(entity);
            }
        });
    }
    
    @SubscribeEvent
    public void onDeath(final LivingDeathEvent event) {
        if (event.getEntity() == DeathEffect.mc.field_71439_g) {
            return;
        }
        if (this.shouldRenderParticle(event.getEntity())) {}
    }
    
    public boolean shouldRenderParticle(final Entity entity) {
        return entity != DeathEffect.mc.field_71439_g || (entity instanceof EntityPlayer && this.isOn()) || entity instanceof EntityMob || entity instanceof EntitySlime || entity instanceof EntityAnimal;
    }
    
    public void totemPop(final Entity entity) {
        DeathEffect.mc.field_71452_i.func_191271_a(entity, EnumParticleTypes.TOTEM, 1);
    }
}
