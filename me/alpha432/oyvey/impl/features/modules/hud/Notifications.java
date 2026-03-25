//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.hud;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import java.util.concurrent.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.client.gui.*;
import me.alpha432.oyvey.*;
import me.alpha432.oyvey.api.util.render.*;
import me.alpha432.oyvey.impl.features.modules.client.*;

public class Notifications extends Module
{
    private final Setting<Side> side;
    private final Setting<Integer> yOffset;
    private final Setting<Integer> timeout;
    private final Setting<Integer> fadeTime;
    private final Setting<Integer> backgroundAlpha;
    private static Notifications INSTANCE;
    private final CopyOnWriteArrayList<Notification> notifications;
    
    public Notifications() {
        super("Notifications", "Shows module toggle notifications with slide animation", Module.Category.HUD, true, false, false);
        this.side = (Setting<Side>)this.register(new Setting("Side", (Object)Side.Right));
        this.yOffset = (Setting<Integer>)this.register(new Setting("Y Offset", (Object)30, (Object)0, (Object)500));
        this.timeout = (Setting<Integer>)this.register(new Setting("Timeout", (Object)3000, (Object)1000, (Object)10000));
        this.fadeTime = (Setting<Integer>)this.register(new Setting("Fade Time", (Object)300, (Object)100, (Object)1000));
        this.backgroundAlpha = (Setting<Integer>)this.register(new Setting("Background Alpha", (Object)180, (Object)0, (Object)255));
        this.notifications = new CopyOnWriteArrayList<Notification>();
        Notifications.INSTANCE = this;
    }
    
    public static Notifications getInstance() {
        if (Notifications.INSTANCE == null) {
            Notifications.INSTANCE = new Notifications();
        }
        return Notifications.INSTANCE;
    }
    
    public void onRender2D(final Render2DEvent event) {
        if (Notifications.mc.field_71439_g == null || Notifications.mc.field_71441_e == null) {
            return;
        }
        final ScaledResolution sr = event.scaledResolution;
        final int screenWidth = sr.func_78326_a();
        final int screenHeight = sr.func_78328_b();
        float y = (float)(screenHeight - (int)this.yOffset.getValue());
        final boolean isLeft = this.side.getValue() == Side.Left;
        for (int i = this.notifications.size() - 1; i >= 0; --i) {
            final Notification notif = this.notifications.get(i);
            final float renderY = notif.render(screenWidth, y, isLeft);
            if (renderY < 0.0f) {
                this.notifications.remove(notif);
            }
            else {
                y -= renderY + 4.0f;
            }
        }
    }
    
    public void addNotification(final String moduleName, final boolean enabled) {
        if (!this.isOn()) {
            return;
        }
        this.notifications.add(new Notification(moduleName, enabled, (int)this.timeout.getValue(), (int)this.fadeTime.getValue(), (int)this.backgroundAlpha.getValue()));
    }
    
    public enum Side
    {
        Left, 
        Right;
    }
    
    private class Notification
    {
        private final String moduleName;
        private final boolean enabled;
        private final long startTime;
        private final int timeout;
        private final int fadeTime;
        private final int bgAlpha;
        private static final float MIN_WIDTH = 150.0f;
        private static final float HEIGHT = 20.0f;
        private static final float PADDING = 4.0f;
        
        public Notification(final String moduleName, final boolean enabled, final int timeout, final int fadeTime, final int bgAlpha) {
            this.moduleName = moduleName;
            this.enabled = enabled;
            this.startTime = System.currentTimeMillis();
            this.timeout = timeout;
            this.fadeTime = fadeTime;
            this.bgAlpha = bgAlpha;
        }
        
        public float render(final int screenWidth, final float y, final boolean isLeft) {
            final long elapsed = System.currentTimeMillis() - this.startTime;
            final long totalDuration = this.timeout + this.fadeTime * 2L;
            if (elapsed > totalDuration) {
                return -1.0f;
            }
            final String statusText = this.enabled ? " enabled" : " disabled";
            final String fullText = this.moduleName + statusText;
            final float textWidth = (float)OyVey.textManager.getStringWidth(fullText);
            final float width = Math.max(150.0f, textWidth + 16.0f);
            float slideProgress;
            float textAlpha;
            if (elapsed < this.fadeTime) {
                final float progress = elapsed / (float)this.fadeTime;
                slideProgress = this.easeOutCubic(progress);
                textAlpha = this.easeOutCubic(Math.min(1.0f, progress * 2.0f));
            }
            else if (elapsed < this.timeout + this.fadeTime) {
                slideProgress = 1.0f;
                textAlpha = 1.0f;
            }
            else {
                final float progress = (elapsed - this.timeout - this.fadeTime) / (float)this.fadeTime;
                slideProgress = 1.0f - this.easeOutCubic(progress);
                textAlpha = 1.0f - this.easeOutCubic(progress);
            }
            final float renderY = y - 20.0f;
            float x1;
            float x2;
            float barX1;
            float barX2;
            if (isLeft) {
                x1 = -(width * (1.0f - slideProgress));
                x2 = x1 + width;
                barX1 = x2 - 4.0f;
                barX2 = x2;
            }
            else {
                x1 = screenWidth - width * slideProgress;
                x2 = (float)screenWidth;
                barX1 = x1;
                barX2 = x1 + 4.0f;
            }
            final int bgColor = ColorUtil.toRGBA(30, 30, 30, (int)(this.bgAlpha * slideProgress));
            Render2DMethods.drawRect(x1, renderY, x2, renderY + 20.0f, bgColor);
            final int syncedColor = Color.getInstance().syncColor();
            final float[] colorParts = ColorUtil.toArray(syncedColor);
            final int accentColor = ColorUtil.toRGBA((int)(colorParts[0] * 255.0f), (int)(colorParts[1] * 255.0f), (int)(colorParts[2] * 255.0f), (int)(255.0f * slideProgress));
            Render2DMethods.drawRect(barX1, renderY, barX2, renderY + 20.0f, accentColor);
            if (textAlpha > 0.1f) {
                final float textX = isLeft ? (x1 + 8.0f) : (x1 + 8.0f);
                final float textY = renderY + (20.0f - OyVey.textManager.getFontHeight()) / 2.0f;
                final int nameColor = ColorUtil.toRGBA((int)(colorParts[0] * 255.0f), (int)(colorParts[1] * 255.0f), (int)(colorParts[2] * 255.0f), (int)(255.0f * textAlpha));
                OyVey.textManager.drawString(this.moduleName, textX, textY, nameColor, true);
                final float statusX = textX + OyVey.textManager.getStringWidth(this.moduleName);
                final int statusColor = this.enabled ? ColorUtil.toRGBA(0, 255, 0, (int)(255.0f * textAlpha)) : ColorUtil.toRGBA(255, 0, 0, (int)(255.0f * textAlpha));
                OyVey.textManager.drawString(statusText, statusX, textY, statusColor, true);
            }
            return 24.0f * slideProgress;
        }
        
        private float easeOutCubic(final float x) {
            return 1.0f - (float)Math.pow(1.0f - x, 3.0);
        }
    }
}
