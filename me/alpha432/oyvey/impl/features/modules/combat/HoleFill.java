//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.combat;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.api.util.math.*;
import net.minecraft.block.*;
import me.alpha432.oyvey.api.util.world.hole.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.util.math.*;
import me.alpha432.oyvey.api.util.world.*;
import me.alpha432.oyvey.impl.command.*;
import java.util.*;
import me.alpha432.oyvey.impl.features.modules.movement.*;
import net.minecraft.util.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import me.alpha432.oyvey.impl.events.*;
import java.awt.*;
import me.alpha432.oyvey.api.util.render.*;
import org.lwjgl.opengl.*;

public class HoleFill extends Module
{
    public final Setting<Boolean> holes;
    Setting<modes> Modes;
    public final Setting<Boolean> doubleHoles;
    public final Setting<Integer> validHoleHeight;
    public final Setting<Integer> bps;
    public final Setting<Integer> range;
    public final Setting<Float> validPlayerRange;
    public final Setting<Boolean> toggle;
    public final Setting<Boolean> packet;
    public final Setting<Boolean> rotate;
    public final Setting<Boolean> smart;
    public final Setting<Integer> distance;
    public final Setting<Boolean> predict;
    public final Setting<Integer> ticks;
    Setting<Boolean> autoBurrow;
    Setting<Integer> burrowRange;
    public final Setting<Boolean> render;
    public final Setting<Boolean> box;
    public final Setting<Boolean> outline;
    public final Setting<Boolean> fade;
    public final Setting<ColorSetting> boxColor;
    public final Setting<ColorSetting> outlineColor;
    public final Setting<Float> lineWidth;
    public final Setting<Integer> renderTime;
    EntityPlayer target;
    int obiSlot;
    public static Map<String, BlockPos> map;
    public static Entity targett;
    private final Set<Integer> processedEntities;
    private boolean shouldExecuteSelfFill;
    private int selfFillCooldown;
    private final Map<BlockPos, Long> placedBlocks;
    
    public HoleFill() {
        super("HoleFill", "Automatically files holes", Module.Category.COMBAT, true, false, false);
        this.holes = (Setting<Boolean>)this.register(new Setting("Holes", (Object)true));
        this.Modes = (Setting<modes>)this.register(new Setting("Modes", (Object)modes.Default));
        this.doubleHoles = (Setting<Boolean>)new Setting("FillDoubleHoles", (Object)true);
        this.validHoleHeight = (Setting<Integer>)new Setting("ValidHoleHeight", (Object)2, (Object)1, (Object)5);
        this.bps = (Setting<Integer>)new Setting("BPS", (Object)1, (Object)1, (Object)8);
        this.range = (Setting<Integer>)this.register(new Setting("Range", (Object)4, (Object)1, (Object)10, v -> (boolean)this.holes.getValue()));
        this.validPlayerRange = (Setting<Float>)new Setting("ValidPlayerRange", (Object)5.0f, (Object)0.1f, (Object)15.0f);
        this.toggle = (Setting<Boolean>)new Setting("Disables", (Object)false);
        this.packet = (Setting<Boolean>)new Setting("Packet", (Object)true);
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (Object)true, v -> (boolean)this.holes.getValue()));
        this.smart = (Setting<Boolean>)new Setting("Smart", (Object)true, v -> (boolean)this.holes.getValue());
        this.distance = (Setting<Integer>)this.register(new Setting("Smart Range", (Object)3, (Object)1, (Object)5, v -> (boolean)this.holes.getValue()));
        this.predict = (Setting<Boolean>)new Setting("Predict", (Object)true, v -> (boolean)this.holes.getValue());
        this.ticks = (Setting<Integer>)this.register(new Setting("Predict", (Object)3, (Object)0, (Object)10, v -> (boolean)this.holes.getValue()));
        this.autoBurrow = (Setting<Boolean>)this.register(new Setting("SelfFill", (Object)true));
        this.burrowRange = (Setting<Integer>)this.register(new Setting("Range", (Object)3, (Object)1, (Object)5, v -> (boolean)this.autoBurrow.getValue()));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", (Object)true));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", (Object)true, v -> (boolean)this.render.getValue()));
        this.outline = (Setting<Boolean>)this.register(new Setting("Outline", (Object)true, v -> (boolean)this.render.getValue()));
        this.fade = (Setting<Boolean>)this.register(new Setting("Fade", (Object)true, v -> (boolean)this.render.getValue()));
        this.boxColor = (Setting<ColorSetting>)this.register(new Setting("BoxColor", (Object)new ColorSetting(255, 0, 0, 50), v -> (boolean)this.render.getValue() && (boolean)this.box.getValue()));
        this.outlineColor = (Setting<ColorSetting>)this.register(new Setting("OutlineColor", (Object)new ColorSetting(255, 0, 0, 255), v -> (boolean)this.render.getValue() && (boolean)this.outline.getValue()));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (Object)1.0f, (Object)0.1f, (Object)5.0f, v -> (boolean)this.render.getValue() && (boolean)this.outline.getValue()));
        this.renderTime = (Setting<Integer>)this.register(new Setting("RenderTime", (Object)1000, (Object)0, (Object)5000, v -> (boolean)this.render.getValue()));
        this.target = null;
        this.processedEntities = new HashSet<Integer>();
        this.shouldExecuteSelfFill = false;
        this.selfFillCooldown = 0;
        this.placedBlocks = new HashMap<BlockPos, Long>();
    }
    
    public void onUpdate() {
        HoleFill.targett = this.getTarget();
        if (this.shouldExecuteSelfFill && this.selfFillCooldown <= 0) {
            this.executeSelfFill();
            this.shouldExecuteSelfFill = false;
            this.selfFillCooldown = 20;
        }
        if (this.selfFillCooldown > 0) {
            --this.selfFillCooldown;
        }
        if (this.render.getValue()) {
            final long currentTime = System.currentTimeMillis();
            this.placedBlocks.entrySet().removeIf(entry -> currentTime - entry.getValue() > (int)this.renderTime.getValue());
        }
    }
    
    public void onDisable() {
        this.processedEntities.clear();
        this.shouldExecuteSelfFill = false;
        this.selfFillCooldown = 0;
        this.placedBlocks.clear();
        super.onDisable();
    }
    
    private Entity getTarget() {
        Entity targett = null;
        double distance = (float)Resolver.getInstance().range.getValue();
        double maxHealth = 36.0;
        final Set<Integer> currentTickEntities = new HashSet<Integer>();
        for (final Entity entity : HoleFill.mc.field_71441_e.field_73010_i) {
            if (((boolean)Resolver.getInstance().players.getValue() && entity instanceof EntityPlayer) || ((boolean)Resolver.getInstance().animals.getValue() && EntityUtil.isPassive(entity)) || ((boolean)Resolver.getInstance().mobs.getValue() && EntityUtil.isMobAggressive(entity)) || ((boolean)Resolver.getInstance().vehicles.getValue() && EntityUtil.isVehicle(entity)) || ((boolean)Resolver.getInstance().projectiles.getValue() && EntityUtil.isProjectile(entity))) {
                if (entity instanceof EntityLivingBase && EntityUtil.isntValid(entity, distance)) {
                    continue;
                }
                if (!HoleFill.mc.field_71439_g.func_70685_l(entity) && !EntityUtil.canEntityFeetBeSeen(entity) && HoleFill.mc.field_71439_g.func_70068_e(entity) > MathUtil.square((double)(float)Resolver.getInstance().raytrace.getValue())) {
                    continue;
                }
                final Vec3d entityPos = this.predict.getValue() ? EntityUtil.getEntityPosVec(entity, ((int)this.ticks.getValue() > 0) ? ((int)this.ticks.getValue()) : 0) : entity.func_174791_d();
                if (HoleFill.mc.field_71439_g.func_70011_f(entityPos.field_72450_a, entityPos.field_72448_b, entityPos.field_72449_c) <= (int)this.burrowRange.getValue()) {
                    currentTickEntities.add(entity.func_145782_y());
                    if (!this.processedEntities.contains(entity.func_145782_y())) {
                        this.processedEntities.add(entity.func_145782_y());
                        this.shouldExecuteSelfFill = true;
                    }
                }
                if (targett == null) {
                    targett = entity;
                    distance = HoleFill.mc.field_71439_g.func_70068_e(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
                else {
                    if (entity instanceof EntityPlayer && DamageUtil.isArmorLow((EntityPlayer)entity, 18)) {
                        targett = entity;
                        break;
                    }
                    if (HoleFill.mc.field_71439_g.func_70068_e(entity) < distance) {
                        targett = entity;
                        distance = HoleFill.mc.field_71439_g.func_70068_e(entity);
                        maxHealth = EntityUtil.getHealth(entity);
                    }
                    if (EntityUtil.getHealth(entity) >= maxHealth) {
                        continue;
                    }
                    targett = entity;
                    distance = HoleFill.mc.field_71439_g.func_70068_e(entity);
                    maxHealth = EntityUtil.getHealth(entity);
                }
            }
        }
        this.processedEntities.removeIf(id -> !currentTickEntities.contains(id));
        return (Entity)this.target;
    }
    
    private void executeSelfFill() {
        if (EntityUtil.isInHole((Entity)HoleFill.mc.field_71439_g) && (boolean)this.autoBurrow.getValue()) {
            if (!SelfFill.getInstance().isEnabled()) {
                SelfFill.getInstance().enable();
            }
            if (SelfFill.getInstance().isEnabled()) {
                SelfFill.getInstance().onUpdate();
            }
        }
    }
    
    public String getDisplayInfo() {
        if (this.target != null) {
            return this.target.func_70005_c_();
        }
        return null;
    }
    
    public void onTick() {
        if (nullCheck()) {
            return;
        }
        this.obiSlot = InventoryUtil.findHotbarBlock((Class)BlockObsidian.class);
        int placed = 0;
        if (this.obiSlot == -1) {
            return;
        }
        final ArrayList<HoleUtiler.Hole> holeList = (ArrayList<HoleUtiler.Hole>)HoleUtiler.holes((float)(int)this.range.getValue(), (int)this.validHoleHeight.getValue());
        if (this.smart.getValue()) {
            this.target = CombatUtil.getTarget((double)(float)this.validPlayerRange.getValue());
            if (this.target == null) {
                return;
            }
            final Vec3d vec = this.predict.getValue() ? this.target.func_174791_d() : EntityUtil.getEntityPosVec((Entity)this.target, ((int)this.ticks.getValue() > 0) ? ((int)this.ticks.getValue()) : 0);
            final Vec3d vec3d2;
            HoleUtiler.Hole hole;
            final ArrayList list;
            Vec3d vec3d;
            holeList.removeIf(e -> {
                if (e instanceof HoleUtiler.SingleHole) {
                    return vec3d2.func_72436_e(new Vec3d((Vec3i)((HoleUtiler.SingleHole)e).pos).func_72441_c(0.5, 0.5, 0.5)) >= (int)this.distance.getValue() * (int)this.distance.getValue();
                }
                else if (e instanceof HoleUtiler.DoubleHole) {
                    hole = HoleUtiler.getHole(EntityUtil.getEntityPosFloored((Entity)this.target), 1);
                    if (hole instanceof HoleUtiler.DoubleHole && list.contains(hole)) {
                        return true;
                    }
                    else {
                        vec3d = new Vec3d((Vec3i)e.pos);
                        if (vec3d2.func_72436_e(vec3d.func_72441_c(0.5, 0.5, 0.5)) >= (int)this.distance.getValue() * (int)this.distance.getValue()) {
                            return true;
                        }
                        else {
                            return vec3d2.func_72436_e(new Vec3d((Vec3i)e.pos1).func_72441_c(0.5, 0.5, 0.5)) >= (int)this.distance.getValue() * (int)this.distance.getValue();
                        }
                    }
                }
                else {
                    return false;
                }
            });
        }
        if (holeList.isEmpty()) {
            return;
        }
        for (final HoleUtiler.Hole hole2 : holeList) {
            if (this.smart.getValue()) {
                final EntityPlayer target = CombatUtil.getTarget(10.0);
                if (placed >= (int)this.bps.getValue()) {
                    continue;
                }
                if (hole2 instanceof HoleUtiler.SingleHole && !EntityUtil.isInHole((Entity)target) && WorldUtil.empty.contains(WorldUtil.getBlock(((HoleUtiler.SingleHole)hole2).pos)) && !BlockUtil.isIntercepted(((HoleUtiler.SingleHole)hole2).pos)) {
                    this.doPlace(((HoleUtiler.SingleHole)hole2).pos);
                    ++placed;
                }
                if (placed >= (int)this.bps.getValue() || !(hole2 instanceof HoleUtiler.DoubleHole)) {
                    continue;
                }
                if (!(boolean)this.doubleHoles.getValue()) {
                    continue;
                }
                final HoleUtiler.DoubleHole doubleH = (HoleUtiler.DoubleHole)hole2;
                if (BlockUtil.hasEntity(doubleH.pos)) {
                    continue;
                }
                if (BlockUtil.hasEntity(doubleH.pos1)) {
                    continue;
                }
                if (this.getDist(doubleH.pos) && !EntityUtil.isInHole((Entity)target) && !BlockUtil.isInterceptedByOther(doubleH.pos) && WorldUtil.empty.contains(WorldUtil.getBlock(doubleH.pos)) && !BlockUtil.isIntercepted(doubleH.pos)) {
                    this.doPlace(doubleH.pos);
                    ++placed;
                }
                if (placed >= (int)this.bps.getValue() || !this.getDist(doubleH.pos1) || EntityUtil.isInHole((Entity)target) || BlockUtil.isInterceptedByOther(doubleH.pos1) || !WorldUtil.empty.contains(WorldUtil.getBlock(doubleH.pos1))) {
                    continue;
                }
                if (BlockUtil.isIntercepted(doubleH.pos1)) {
                    continue;
                }
                this.doPlace(doubleH.pos1);
                ++placed;
            }
            if (placed >= (int)this.bps.getValue()) {
                continue;
            }
            if (hole2 instanceof HoleUtiler.SingleHole && WorldUtil.empty.contains(WorldUtil.getBlock(((HoleUtiler.SingleHole)hole2).pos)) && !BlockUtil.isIntercepted(((HoleUtiler.SingleHole)hole2).pos)) {
                this.doPlace(((HoleUtiler.SingleHole)hole2).pos);
                ++placed;
            }
            if (placed >= (int)this.bps.getValue() || !(hole2 instanceof HoleUtiler.DoubleHole)) {
                continue;
            }
            if (!(boolean)this.doubleHoles.getValue()) {
                continue;
            }
            final HoleUtiler.DoubleHole doubleH2 = (HoleUtiler.DoubleHole)hole2;
            if (BlockUtil.hasEntity(doubleH2.pos)) {
                continue;
            }
            if (BlockUtil.hasEntity(doubleH2.pos1)) {
                continue;
            }
            if (this.getDist(doubleH2.pos) && !BlockUtil.isInterceptedByOther(doubleH2.pos) && WorldUtil.empty.contains(WorldUtil.getBlock(doubleH2.pos)) && !BlockUtil.isIntercepted(doubleH2.pos)) {
                this.doPlace(doubleH2.pos);
                ++placed;
            }
            if (placed >= (int)this.bps.getValue() || !this.getDist(doubleH2.pos1) || BlockUtil.isInterceptedByOther(doubleH2.pos1) || !WorldUtil.empty.contains(WorldUtil.getBlock(doubleH2.pos1))) {
                continue;
            }
            if (BlockUtil.isIntercepted(doubleH2.pos1)) {
                continue;
            }
            this.doPlace(doubleH2.pos1);
            ++placed;
        }
        if (placed != 0 || !holeList.isEmpty() || !(boolean)this.toggle.getValue()) {
            return;
        }
        Command.sendMessage("Finished Holefilling, disabling!");
        this.disable();
    }
    
    public void doPlace(final BlockPos pos) {
        if (this.holes.getValue()) {
            if (isMine(pos)) {
                return;
            }
            if (FakeLag.getInstance().isEnabled() || PhaseWalk.getInstance().isPhasing()) {
                return;
            }
            if (this.Modes.getValue() == modes.Default) {
                final int oldSlot = HoleFill.mc.field_71439_g.field_71071_by.field_70461_c;
                InventoryUtil.switchToSlot(this.obiSlot);
                BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, (boolean)this.rotate.getValue(), (boolean)this.packet.getValue(), HoleFill.mc.field_71439_g.field_70122_E);
                InventoryUtil.switchToSlot(oldSlot);
                if (this.render.getValue()) {
                    this.placedBlocks.put(pos, System.currentTimeMillis());
                }
            }
            else {
                this.futureFill();
            }
        }
    }
    
    public static boolean isMine(final BlockPos pos) {
        for (final Map.Entry<String, BlockPos> block : HoleFill.map.entrySet()) {
            if (!block.getValue().equals((Object)pos)) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    @SubscribeEvent
    public void onPacket(final PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketBlockBreakAnim) {
            final SPacketBlockBreakAnim packet = (SPacketBlockBreakAnim)event.getPacket();
            final BlockPos pos = packet.func_179821_b();
            final EntityPlayer breaker = (EntityPlayer)HoleFill.mc.field_71441_e.func_73045_a(packet.func_148845_c());
            if (breaker == null || breaker.func_70092_e(pos.func_177958_n() + 0.5, (double)pos.func_177956_o(), pos.func_177952_p() + 0.5) > 7.0) {
                return;
            }
            HoleFill.map.put(breaker.func_70005_c_(), pos);
        }
    }
    
    private boolean getDist(final BlockPos pos) {
        return !nullCheck() && pos != null && HoleFill.mc.field_71439_g.func_174791_d().func_72438_d(new Vec3d(pos.func_177958_n() + 0.5, pos.func_177956_o() + 0.5, pos.func_177952_p() + 0.5)) < (int)this.range.getValue();
    }
    
    public void futureFill() {
        HoleFill.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "t holefill true");
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (!(boolean)this.render.getValue() || this.placedBlocks.isEmpty()) {
            return;
        }
        final long currentTime = System.currentTimeMillis();
        for (final Map.Entry<BlockPos, Long> entry : this.placedBlocks.entrySet()) {
            final BlockPos pos = entry.getKey();
            final long placeTime = entry.getValue();
            final long elapsedTime = currentTime - placeTime;
            final long totalRenderTime = (int)this.renderTime.getValue();
            float fadeMultiplier = 1.0f;
            if ((boolean)this.fade.getValue() && totalRenderTime > 0L) {
                final float timeProgress = elapsedTime / (float)totalRenderTime;
                fadeMultiplier = Math.max(0.0f, 1.0f - timeProgress);
            }
            if (this.box.getValue()) {
                final ColorSetting boxC = (ColorSetting)this.boxColor.getValue();
                final int fadedBoxAlpha = (int)(boxC.getAlpha() * fadeMultiplier);
                if (fadedBoxAlpha > 0) {
                    RenderUtil.drawBox(pos, new Color(boxC.getRed(), boxC.getGreen(), boxC.getBlue(), fadedBoxAlpha));
                }
            }
            if (this.outline.getValue()) {
                final ColorSetting outlineC = (ColorSetting)this.outlineColor.getValue();
                final int fadedOutlineAlpha = (int)(outlineC.getAlpha() * fadeMultiplier);
                if (fadedOutlineAlpha <= 0) {
                    continue;
                }
                this.drawCleanOutline(pos, new Color(outlineC.getRed(), outlineC.getGreen(), outlineC.getBlue(), fadedOutlineAlpha), (float)this.lineWidth.getValue());
            }
        }
    }
    
    private void drawCleanOutline(final BlockPos pos, final Color color, final float lineWidth) {
        RenderUtil.RenderTesselator.prepareGL();
        GL11.glLineWidth(lineWidth);
        GL11.glBegin(1);
        GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        final double x = pos.func_177958_n() - HoleFill.mc.func_175598_ae().field_78730_l;
        final double y = pos.func_177956_o() - HoleFill.mc.func_175598_ae().field_78731_m;
        final double z = pos.func_177952_p() - HoleFill.mc.func_175598_ae().field_78728_n;
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x + 1.0, y, z);
        GL11.glVertex3d(x + 1.0, y, z);
        GL11.glVertex3d(x + 1.0, y, z + 1.0);
        GL11.glVertex3d(x + 1.0, y, z + 1.0);
        GL11.glVertex3d(x, y, z + 1.0);
        GL11.glVertex3d(x, y, z + 1.0);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y + 1.0, z);
        GL11.glVertex3d(x + 1.0, y + 1.0, z);
        GL11.glVertex3d(x + 1.0, y + 1.0, z);
        GL11.glVertex3d(x + 1.0, y + 1.0, z + 1.0);
        GL11.glVertex3d(x + 1.0, y + 1.0, z + 1.0);
        GL11.glVertex3d(x, y + 1.0, z + 1.0);
        GL11.glVertex3d(x, y + 1.0, z + 1.0);
        GL11.glVertex3d(x, y + 1.0, z);
        GL11.glVertex3d(x, y, z);
        GL11.glVertex3d(x, y + 1.0, z);
        GL11.glVertex3d(x + 1.0, y, z);
        GL11.glVertex3d(x + 1.0, y + 1.0, z);
        GL11.glVertex3d(x + 1.0, y, z + 1.0);
        GL11.glVertex3d(x + 1.0, y + 1.0, z + 1.0);
        GL11.glVertex3d(x, y, z + 1.0);
        GL11.glVertex3d(x, y + 1.0, z + 1.0);
        GL11.glEnd();
        RenderUtil.RenderTesselator.releaseGL();
    }
    
    static {
        HoleFill.map = new HashMap<String, BlockPos>();
    }
    
    public enum modes
    {
        Future, 
        Default;
    }
}
