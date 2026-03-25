//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.render;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import java.util.concurrent.*;
import net.minecraft.entity.projectile.*;
import java.util.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.api.util.render.Trails.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.impl.events.*;
import me.alpha432.oyvey.api.util.render.*;
import org.lwjgl.opengl.*;
import net.minecraft.util.math.*;
import java.util.function.*;

public class Trails extends Module
{
    public static final Vec3d ORIGIN;
    private final Setting<Boolean> arrows;
    private final Setting<Boolean> pearls;
    private final Setting<Boolean> snowballs;
    private final Setting<Integer> time;
    private final Setting<ColorSetting> color;
    private final Setting<Float> width;
    protected Map<Integer, TimeAnimation> ids;
    protected Map<Integer, List<Trace>> traceLists;
    protected Map<Integer, Trace> traces;
    
    public Trails() {
        super("PearlTrail", "Too useful module", Module.Category.RENDER, true, false, false);
        this.arrows = (Setting<Boolean>)new Setting("Arrows", (Object)false);
        this.pearls = (Setting<Boolean>)new Setting("Pearls", (Object)true);
        this.snowballs = (Setting<Boolean>)new Setting("Snowballs", (Object)false);
        this.time = (Setting<Integer>)this.register(new Setting("Time", (Object)1, (Object)1, (Object)10));
        this.color = (Setting<ColorSetting>)this.register(new Setting("Color", (Object)new ColorSetting(100, 24, 250, 230)));
        this.width = (Setting<Float>)this.register(new Setting("Width", (Object)1.6f, (Object)0.1f, (Object)5.0f));
        this.ids = new ConcurrentHashMap<Integer, TimeAnimation>();
        this.traceLists = new ConcurrentHashMap<Integer, List<Trace>>();
        this.traces = new ConcurrentHashMap<Integer, Trace>();
    }
    
    public void onUpdate() {
        if (!fullNullCheck() && !this.ids.keySet().isEmpty()) {
            for (final Integer id : this.ids.keySet()) {
                if (id != null) {
                    if (Trails.mc.field_71441_e == null) {
                        return;
                    }
                    if (Trails.mc.field_71441_e.field_72996_f.isEmpty()) {
                        return;
                    }
                    Trace idTrace = this.traces.get(id);
                    final Entity entity = Trails.mc.field_71441_e.func_73045_a((int)id);
                    if (entity != null) {
                        final Vec3d vec = entity.func_174791_d();
                        if (vec.equals((Object)Trails.ORIGIN)) {
                            continue;
                        }
                        if (!this.traces.containsKey(id) || idTrace == null) {
                            this.traces.put(id, new Trace(0, (String)null, Trails.mc.field_71441_e.field_73011_w.func_186058_p(), vec, (List)new ArrayList()));
                            idTrace = this.traces.get(id);
                        }
                        List<Trace.TracePos> trace = (List<Trace.TracePos>)idTrace.getTrace();
                        final Vec3d vec3d = trace.isEmpty() ? vec : trace.get(trace.size() - 1).getPos();
                        if (!trace.isEmpty() && (vec.func_72438_d(vec3d) > 100.0 || idTrace.getType() != Trails.mc.field_71441_e.field_73011_w.func_186058_p())) {
                            this.traceLists.get(id).add(idTrace);
                            trace = new ArrayList<Trace.TracePos>();
                            this.traces.put(id, new Trace(this.traceLists.get(id).size() + 1, (String)null, Trails.mc.field_71441_e.field_73011_w.func_186058_p(), vec, (List)new ArrayList()));
                        }
                        if (trace.isEmpty() || !vec.equals((Object)vec3d)) {
                            trace.add(new Trace.TracePos(vec));
                        }
                    }
                    final TimeAnimation animation = this.ids.get(id);
                    if (entity instanceof EntityArrow && (entity.field_70122_E || entity.field_70132_H || !entity.isAddedToWorld())) {
                        animation.play();
                    }
                    if (animation == null || ((ColorSetting)this.color.getValue()).getAlpha() - animation.getCurrent() > 0.0) {
                        continue;
                    }
                    animation.stop();
                    this.ids.remove(id);
                    this.traceLists.remove(id);
                    this.traces.remove(id);
                }
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSpawnObject) {
            final SPacketSpawnObject packet = (SPacketSpawnObject)event.getPacket();
            if (((boolean)this.pearls.getValue() && packet.func_148993_l() == 65) || ((boolean)this.arrows.getValue() && packet.func_148993_l() == 60) || ((boolean)this.snowballs.getValue() && packet.func_148993_l() == 61)) {
                final ColorSetting c = (ColorSetting)this.color.getValue();
                final TimeAnimation animation = new TimeAnimation((long)((int)this.time.getValue() * 1000), 0.0, (double)c.getAlpha(), false, AnimationMode.LINEAR);
                animation.stop();
                this.ids.put(packet.func_149001_c(), animation);
                this.traceLists.put(packet.func_149001_c(), new ArrayList<Trace>());
                this.traces.put(packet.func_149001_c(), new Trace(0, (String)null, Trails.mc.field_71441_e.field_73011_w.func_186058_p(), new Vec3d(packet.func_186880_c(), packet.func_186882_d(), packet.func_186881_e()), (List)new ArrayList()));
            }
        }
        if (event.getPacket() instanceof SPacketDestroyEntities) {
            for (final int id : ((SPacketDestroyEntities)event.getPacket()).func_149098_c()) {
                if (this.ids.containsKey(id)) {
                    this.ids.get(id).play();
                }
            }
        }
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (!fullNullCheck()) {
            RenderUtil.prepareGL3D();
            final ColorSetting c = (ColorSetting)this.color.getValue();
            for (final Map.Entry<Integer, List<Trace>> entry : this.traceLists.entrySet()) {
                GL11.glLineWidth((float)this.width.getValue());
                final TimeAnimation animation = this.ids.get(entry.getKey());
                animation.add(event.getPartialTicks());
                GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, MathHelper.func_76131_a((float)(c.getAlpha() / 255.0f - animation.getCurrent() / 255.0), 0.0f, 255.0f));
                entry.getValue().forEach(trace -> {
                    GL11.glBegin(3);
                    trace.getTrace().forEach(this::renderVec);
                    GL11.glEnd();
                    return;
                });
                GL11.glColor4f(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, MathHelper.func_76131_a((float)(c.getAlpha() / 255.0f - animation.getCurrent() / 255.0), 0.0f, 255.0f));
                GL11.glBegin(3);
                final Trace trace2 = this.traces.get(entry.getKey());
                if (trace2 != null) {
                    trace2.getTrace().forEach(this::renderVec);
                }
                GL11.glEnd();
            }
            RenderUtil.releaseGL3D();
        }
    }
    
    private void renderVec(final Trace.TracePos tracePos) {
        final double x = tracePos.getPos().field_72450_a - Trails.mc.func_175598_ae().field_78730_l;
        final double y = tracePos.getPos().field_72448_b - Trails.mc.func_175598_ae().field_78731_m;
        final double z = tracePos.getPos().field_72449_c - Trails.mc.func_175598_ae().field_78728_n;
        GL11.glVertex3d(x, y, z);
    }
    
    static {
        ORIGIN = new Vec3d(8.0, 64.0, 8.0);
    }
}
