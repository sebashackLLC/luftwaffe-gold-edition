//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import java.util.concurrent.*;
import me.alpha432.oyvey.api.util.render.*;
import java.awt.*;
import net.minecraft.util.math.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.impl.events.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.impl.features.modules.hud.*;
import me.alpha432.oyvey.impl.command.*;
import java.util.*;
import net.minecraft.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.api.util.*;
import net.minecraft.client.renderer.*;

public class LogESP extends Module
{
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final Setting<Boolean> rainbow;
    private final Setting<Integer> rainbowhue;
    private final Setting<Integer> textRed;
    private final Setting<Integer> textGreen;
    private final Setting<Integer> textBlue;
    private final Setting<Integer> textAlpha;
    private final Setting<Boolean> textRainbow;
    private final Setting<Integer> textRainbowHue;
    private final Setting<Boolean> autoRemove;
    private final Setting<Integer> removeTime;
    private final Setting<Boolean> scaleing;
    private final Setting<Float> scaling;
    private final Setting<Float> factor;
    private final Setting<Boolean> smartScale;
    private final Setting<Boolean> rect;
    private final Setting<Boolean> coords;
    private final List<LogoutPos> spots;
    public Setting<Float> range;
    public Setting<Boolean> message;
    
    public LogESP() {
        super("LogoutSpots", "Renders waypoint when some1 is logging and some shit", Module.Category.RENDER, true, false, false);
        this.red = (Setting<Integer>)this.register(new Setting("Red", (Object)255, (Object)0, (Object)255));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (Object)0, (Object)0, (Object)255));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (Object)0, (Object)0, (Object)255));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (Object)255, (Object)0, (Object)255));
        this.rainbow = (Setting<Boolean>)this.register(new Setting("Rainbow", (Object)false));
        this.rainbowhue = (Setting<Integer>)this.register(new Setting("Brightness", (Object)255, (Object)0, (Object)255, v -> (boolean)this.rainbow.getValue()));
        this.textRed = (Setting<Integer>)this.register(new Setting("Text Red", (Object)255, (Object)0, (Object)255));
        this.textGreen = (Setting<Integer>)this.register(new Setting("Text Green", (Object)255, (Object)0, (Object)255));
        this.textBlue = (Setting<Integer>)this.register(new Setting("Text Blue", (Object)255, (Object)0, (Object)255));
        this.textAlpha = (Setting<Integer>)this.register(new Setting("Text Alpha", (Object)255, (Object)0, (Object)255));
        this.textRainbow = (Setting<Boolean>)this.register(new Setting("Text Rainbow", (Object)false));
        this.textRainbowHue = (Setting<Integer>)this.register(new Setting("Text Brightness", (Object)255, (Object)0, (Object)255, v -> (boolean)this.textRainbow.getValue()));
        this.autoRemove = (Setting<Boolean>)new Setting("Auto Remove", (Object)true);
        this.removeTime = (Setting<Integer>)this.register(new Setting("Remove Time", (Object)60, (Object)5, (Object)300, v -> (boolean)this.autoRemove.getValue(), "Seconds until ESP disappears"));
        this.scaleing = (Setting<Boolean>)this.register(new Setting("Scale", (Object)false));
        this.scaling = (Setting<Float>)this.register(new Setting("Size", (Object)4.0f, (Object)0.1f, (Object)20.0f));
        this.factor = (Setting<Float>)this.register(new Setting("Factor", (Object)0.3f, (Object)0.1f, (Object)1.0f, v -> (boolean)this.scaleing.getValue()));
        this.smartScale = (Setting<Boolean>)this.register(new Setting("SmartScale", (Object)Boolean.FALSE, v -> (boolean)this.scaleing.getValue()));
        this.rect = (Setting<Boolean>)new Setting("Rectangle", (Object)true);
        this.coords = (Setting<Boolean>)new Setting("Coords", (Object)true);
        this.spots = new CopyOnWriteArrayList<LogoutPos>();
        this.range = (Setting<Float>)this.register(new Setting("Range", (Object)300.0f, (Object)50.0f, (Object)500.0f));
        this.message = (Setting<Boolean>)this.register(new Setting("Message", (Object)true));
    }
    
    public void onLogout() {
        this.spots.clear();
    }
    
    public void onDisable() {
        this.spots.clear();
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (!this.spots.isEmpty()) {
            final List<LogoutPos> list2;
            final List<LogoutPos> list = list2 = this.spots;
            synchronized (list2) {
                AxisAlignedBB interpolateAxis;
                AxisAlignedBB bb;
                Color rainbow = null;
                double x;
                double y;
                double z;
                this.spots.forEach(spot -> {
                    if (spot.getEntity() != null) {
                        if (!(boolean)this.autoRemove.getValue() || System.currentTimeMillis() - spot.getCreationTime() <= (int)this.removeTime.getValue() * 1000L) {
                            bb = (interpolateAxis = RenderUtil.interpolateAxis(spot.getEntity().func_174813_aQ()));
                            if (this.rainbow.getValue()) {
                                rainbow = ColorUtil.rainbow((int)this.rainbowhue.getValue());
                            }
                            else {
                                // new(java.awt.Color.class)
                                new Color((int)this.red.getValue(), (int)this.green.getValue(), (int)this.blue.getValue(), (int)this.alpha.getValue());
                            }
                            RenderUtil.drawBlockOutline(interpolateAxis, rainbow, 1.0f);
                            x = this.interpolate(spot.getEntity().field_70142_S, spot.getEntity().field_70165_t, event.getPartialTicks()) - LogESP.mc.func_175598_ae().field_78730_l;
                            y = this.interpolate(spot.getEntity().field_70137_T, spot.getEntity().field_70163_u, event.getPartialTicks()) - LogESP.mc.func_175598_ae().field_78731_m;
                            z = this.interpolate(spot.getEntity().field_70136_U, spot.getEntity().field_70161_v, event.getPartialTicks()) - LogESP.mc.func_175598_ae().field_78728_n;
                            this.renderNameTag(spot.getName(), x, y, z, event.getPartialTicks(), spot.getX(), spot.getY(), spot.getZ(), spot.getCreationTime());
                        }
                    }
                });
            }
        }
    }
    
    public void onUpdate() {
        if (!fullNullCheck()) {
            this.spots.removeIf(spot -> LogESP.mc.field_71439_g.func_70032_d((Entity)spot.getEntity()) >= MathUtil.square((double)(float)this.range.getValue()));
            if (this.autoRemove.getValue()) {
                final long currentTime = System.currentTimeMillis();
                this.spots.removeIf(spot -> currentTime - spot.getCreationTime() > (int)this.removeTime.getValue() * 1000L);
            }
        }
    }
    
    @SubscribeEvent
    public void onConnection(final ConnectionEvent event) {
        if (event.getStage() == 0) {
            final UUID uuid = event.getUuid();
            final EntityPlayer entity = LogESP.mc.field_71441_e.func_152378_a(uuid);
            if (entity != null && (boolean)this.message.getValue()) {
                Command.sendMessage("" + ChatFormatting.WHITE + ChatFormatting.BOLD + ChatFormatting.WHITE + event.getName() + ChatFormatting.RESET + TextUtil.coloredString(" just logged in at ", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + ChatFormatting.RED + (int)entity.field_70165_t + " " + (int)entity.field_70163_u + " " + (int)entity.field_70161_v);
            }
            this.spots.removeIf(pos -> pos.getName().equalsIgnoreCase(event.getName()));
        }
        else if (event.getStage() == 1) {
            final EntityPlayer entity2 = event.getEntity();
            final UUID uuid2 = event.getUuid();
            final String name = event.getName();
            if (this.message.getValue()) {
                Command.sendMessage("" + ChatFormatting.WHITE + ChatFormatting.BOLD + ChatFormatting.WHITE + event.getName() + ChatFormatting.RESET + TextUtil.coloredString(" just logged out at ", (TextUtil.Color)Metrics.getInstance().commandColor.getPlannedValue()) + ChatFormatting.RED + (int)entity2.field_70165_t + " " + (int)entity2.field_70163_u + " " + (int)entity2.field_70161_v);
            }
            if (name != null && entity2 != null && uuid2 != null) {
                this.spots.add(new LogoutPos(name, entity2));
            }
        }
    }
    
    private void renderNameTag(final String name, final double x, final double yi, final double z, final float delta, final double xPos, final double yPos, final double zPos, final long creationTime) {
        final double y = yi + 0.7;
        final Entity camera = Util.mc.func_175606_aa();
        assert camera != null;
        final double originalPositionX = camera.field_70165_t;
        final double originalPositionY = camera.field_70163_u;
        final double originalPositionZ = camera.field_70161_v;
        camera.field_70165_t = this.interpolate(camera.field_70169_q, camera.field_70165_t, delta);
        camera.field_70163_u = this.interpolate(camera.field_70167_r, camera.field_70163_u, delta);
        camera.field_70161_v = this.interpolate(camera.field_70166_s, camera.field_70161_v, delta);
        String displayTag = name + " XYZ: " + (int)xPos + ", " + (int)yPos + ", " + (int)zPos;
        if (this.autoRemove.getValue()) {
            final long timeLeft = (creationTime + (int)this.removeTime.getValue() * 1000L - System.currentTimeMillis()) / 1000L;
            if (timeLeft >= 0L) {
                displayTag = displayTag + " (" + timeLeft + "s)";
            }
        }
        final double distance = camera.func_70011_f(x + LogESP.mc.func_175598_ae().field_78725_b, y + LogESP.mc.func_175598_ae().field_78726_c, z + LogESP.mc.func_175598_ae().field_78723_d);
        final int width = this.renderer.getStringWidth(displayTag) / 2;
        double scale = (0.0018 + (float)this.scaling.getValue() * (distance * (float)this.factor.getValue())) / 1000.0;
        if (distance <= 8.0 && (boolean)this.smartScale.getValue()) {
            scale = 0.0245;
        }
        if (!(boolean)this.scaleing.getValue()) {
            scale = (float)this.scaling.getValue() / 100.0;
        }
        GlStateManager.func_179094_E();
        RenderHelper.func_74519_b();
        GlStateManager.func_179140_f();
        GlStateManager.func_179088_q();
        GlStateManager.func_179136_a(1.0f, -1500000.0f);
        GlStateManager.func_179097_i();
        GlStateManager.func_179147_l();
        GlStateManager.func_179098_w();
        GlStateManager.func_179109_b((float)x, (float)(y + 1.399999976158142), (float)z);
        GlStateManager.func_179114_b(-LogESP.mc.func_175598_ae().field_78735_i, 0.0f, 1.0f, 0.0f);
        GlStateManager.func_179114_b(LogESP.mc.func_175598_ae().field_78732_j, (LogESP.mc.field_71474_y.field_74320_O == 2) ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.func_179139_a(-scale, -scale, scale);
        GlStateManager.func_179140_f();
        GlStateManager.func_179147_l();
        GlStateManager.func_179098_w();
        if (this.rect.getValue()) {
            RenderUtil.drawRect((float)(-width - 2), (float)(-(this.renderer.getFontHeight() + 1)), width + 2.0f, 1.5f, 1426063360L);
        }
        GlStateManager.func_179097_i();
        final Color textColor = this.textRainbow.getValue() ? ColorUtil.rainbow((int)this.textRainbowHue.getValue()) : new Color((int)this.textRed.getValue(), (int)this.textGreen.getValue(), (int)this.textBlue.getValue(), (int)this.textAlpha.getValue());
        this.renderer.drawStringWithShadow(displayTag, (float)(-width), (float)(-(this.renderer.getFontHeight() - 1)), ColorUtil.toRGBA(textColor));
        camera.field_70165_t = originalPositionX;
        camera.field_70163_u = originalPositionY;
        camera.field_70161_v = originalPositionZ;
        GlStateManager.func_179126_j();
        GlStateManager.func_179084_k();
        GlStateManager.func_179113_r();
        GlStateManager.func_179136_a(1.0f, 1500000.0f);
        GlStateManager.func_179121_F();
    }
    
    private double interpolate(final double previous, final double current, final float delta) {
        return previous + (current - previous) * delta;
    }
    
    private static class LogoutPos
    {
        private final String name;
        private final EntityPlayer entity;
        private final double x;
        private final double y;
        private final double z;
        private final long creationTime;
        
        public LogoutPos(final String name, final EntityPlayer entity) {
            this.name = name;
            this.entity = entity;
            this.x = entity.field_70165_t;
            this.y = entity.field_70163_u;
            this.z = entity.field_70161_v;
            this.creationTime = System.currentTimeMillis();
        }
        
        public String getName() {
            return this.name;
        }
        
        public EntityPlayer getEntity() {
            return this.entity;
        }
        
        public double getX() {
            return this.x;
        }
        
        public double getY() {
            return this.y;
        }
        
        public double getZ() {
            return this.z;
        }
        
        public long getCreationTime() {
            return this.creationTime;
        }
    }
}
