//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import net.minecraft.client.*;
import net.minecraft.potion.*;
import java.util.concurrent.*;
import net.minecraftforge.fml.common.gameevent.*;
import java.util.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraftforge.client.event.*;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.relauncher.*;

public class PotionAlerts extends Module
{
    private static PotionAlerts INSTANCE;
    private final Minecraft mc;
    private final List<DisplayMessage> activeMessages;
    private final Map<Potion, PotionEffect> previousEffects;
    private static final Map<String, EffectInfo> EFFECT_INFO;
    
    public PotionAlerts() {
        super("PotionAlert", "Shows ending potion effects", Module.Category.HUD, true, false, false);
        this.mc = Minecraft.func_71410_x();
        this.activeMessages = new CopyOnWriteArrayList<DisplayMessage>();
        this.previousEffects = new HashMap<Potion, PotionEffect>();
    }
    
    @SubscribeEvent
    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player != this.mc.field_71439_g) {
            return;
        }
        final Map<Potion, PotionEffect> currentEffects = new HashMap<Potion, PotionEffect>();
        for (final PotionEffect effect : event.player.func_70651_bq()) {
            currentEffects.put(effect.func_188419_a(), effect);
        }
        for (final Map.Entry<Potion, PotionEffect> entry : this.previousEffects.entrySet()) {
            final String effectKey = entry.getKey().func_76393_a();
            if (PotionAlerts.EFFECT_INFO.containsKey(effectKey)) {
                final PotionEffect currentEffect = currentEffects.get(entry.getKey());
                if (currentEffect != null && currentEffect.func_76459_b() > 1) {
                    continue;
                }
                final int prevDuration = (entry.getValue() != null) ? entry.getValue().func_76459_b() : 0;
                if (prevDuration <= 0) {
                    continue;
                }
                this.addNewMessage(entry.getKey(), prevDuration);
            }
        }
        this.previousEffects.clear();
        this.previousEffects.putAll(currentEffects);
        final long currentTime = System.currentTimeMillis();
        final long displayTime;
        this.activeMessages.removeIf(message -> {
            displayTime = currentTime - message.createTime;
            return displayTime > 3600L;
        });
    }
    
    private void addNewMessage(final Potion potion, final int previousDuration) {
        final boolean exists = this.activeMessages.stream().anyMatch(m -> m.potion == potion && !m.isFading);
        if (!exists) {
            this.activeMessages.add(new DisplayMessage(potion, System.currentTimeMillis(), previousDuration));
        }
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderGameOverlay(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        final ScaledResolution res = new ScaledResolution(this.mc);
        final FontRenderer fr = this.mc.field_71466_p;
        final long currentTime = System.currentTimeMillis();
        final int baseY = res.func_78328_b() / 2 + 20;
        final int centerX = res.func_78326_a() / 2;
        for (int i = 0; i < this.activeMessages.size(); ++i) {
            final DisplayMessage message = this.activeMessages.get(i);
            final long displayTime = currentTime - message.createTime;
            float alpha;
            if (displayTime > 2000L) {
                message.isFading = true;
                alpha = 1.0f - (displayTime - 2000L) / 2000.0f;
                if (alpha <= 0.01f) {
                    continue;
                }
            }
            else {
                alpha = 1.0f;
            }
            final EffectInfo info = PotionAlerts.EFFECT_INFO.get(message.potion.func_76393_a());
            if (info != null) {
                final String part1 = "Potion ";
                final String part2 = info.displayName;
                final String part3 = " ended";
                final int whiteColor = (int)(alpha * 255.0f) << 24 | 0xFFFFFF;
                final int effectColor = (int)(alpha * 255.0f) << 24 | info.color;
                final int totalWidth = fr.func_78256_a(part1) + fr.func_78256_a(part2) + fr.func_78256_a(part3);
                int startX = centerX - totalWidth / 2;
                fr.func_175063_a(part1, (float)startX, (float)(baseY + i * 12), whiteColor);
                startX += fr.func_78256_a(part1);
                fr.func_175063_a(part2, (float)startX, (float)(baseY + i * 12), effectColor);
                startX += fr.func_78256_a(part2);
                fr.func_175063_a(part3, (float)startX, (float)(baseY + i * 12), whiteColor);
            }
        }
    }
    
    static {
        PotionAlerts.INSTANCE = new PotionAlerts();
        EFFECT_INFO = new HashMap<String, EffectInfo>() {
            {
                this.put("effect.moveSpeed", new EffectInfo("Speed", 8171462));
                this.put("effect.moveSlowdown", new EffectInfo("Slowness", 5926017));
                this.put("effect.damageBoost", new EffectInfo("Strength", 9643043));
                this.put("effect.weakness", new EffectInfo("Weakness", 4738376));
            }
        };
    }
    
    private static class DisplayMessage
    {
        final Potion potion;
        final long createTime;
        boolean isFading;
        final int previousDuration;
        
        DisplayMessage(final Potion potion, final long createTime, final int previousDuration) {
            this.potion = potion;
            this.createTime = createTime;
            this.isFading = false;
            this.previousDuration = previousDuration;
        }
    }
    
    private static class EffectInfo
    {
        final String displayName;
        final int color;
        
        EffectInfo(final String displayName, final int color) {
            this.displayName = displayName;
            this.color = color;
        }
    }
}
