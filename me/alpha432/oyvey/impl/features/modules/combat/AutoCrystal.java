//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.combat;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraft.entity.item.*;
import com.mojang.realmsclient.gui.*;
import me.alpha432.oyvey.api.util.entity.*;
import java.util.function.*;
import net.minecraft.network.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.play.client.*;
import net.minecraft.item.*;
import net.minecraft.network.play.server.*;
import net.minecraftforge.fml.common.eventhandler.*;
import me.alpha432.oyvey.impl.events.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import me.alpha432.oyvey.api.util.render.*;
import me.alpha432.oyvey.*;
import java.util.stream.*;
import java.util.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import net.minecraft.util.*;
import net.minecraft.enchantment.*;
import net.minecraft.util.math.*;

public class AutoCrystal extends Module
{
    private final Timer placeTimer;
    private final Timer breakTimer;
    private final Timer preditTimer;
    private final Timer manualTimer;
    Setting<pages> page;
    private final Setting<Integer> attackFactor;
    private final Setting<ColorSetting> renderColor;
    private final Setting<Integer> red;
    private final Setting<Integer> green;
    private final Setting<Integer> blue;
    private final Setting<Integer> alpha;
    private final Setting<Integer> boxAlpha;
    private final Setting<Float> lineWidth;
    public Setting<Boolean> place;
    public Setting<Float> placeDelay;
    public Setting<Float> placeRange;
    public Setting<Float> placeWallRange;
    public Setting<Boolean> explode;
    public Setting<Boolean> packetBreak;
    public Setting<Boolean> predicts;
    public Setting<Boolean> rotate;
    public Setting<Boolean> smoothRotate;
    public Setting<Float> rotateSpeed;
    public Setting<SequenceMode> sequence;
    public Setting<Float> breakDelay;
    public Setting<Float> breakRange;
    public Setting<Float> breakWallRange;
    public Setting<Boolean> ecmeplace;
    public Setting<Boolean> suicide;
    public Setting<Boolean> autoswitch;
    public Setting<Boolean> ignoreUseAmount;
    public Setting<Integer> wasteAmount;
    public Setting<Boolean> facePlaceSword;
    public Setting<Boolean> removeAttack;
    public Setting<Float> targetRange;
    public Setting<Float> minDamage;
    public Setting<Float> facePlace;
    public Setting<Float> breakMaxSelfDamage;
    public Setting<Float> breakMinDmg;
    public Setting<Float> minArmor;
    public Setting<SwingMode> swingMode;
    public Setting<Boolean> pauseWhileEating;
    public Setting<Boolean> render;
    public Setting<Boolean> renderDmg;
    public Setting<Boolean> box;
    public Setting<Boolean> outline;
    EntityEnderCrystal crystal;
    private EntityLivingBase target;
    private BlockPos pos;
    private int hotBarSlot;
    private boolean armor;
    private boolean armorTarget;
    private int crystalCount;
    private int predictWait;
    private int predictPackets;
    private boolean packetCalc;
    private float yaw;
    public EntityLivingBase realTarget;
    private int predict;
    private float pitch;
    private boolean rotating;
    private Float lastYaw;
    private Float lastPitch;
    private boolean placeFlag;
    private boolean breakFlag;
    
    public AutoCrystal() {
        super("OyVeyAutoCrystal", ChatFormatting.GOLD + "Premium quality" + ChatFormatting.WHITE + " AutoCrystal for " + ChatFormatting.GREEN + "jballs", Module.Category.COMBAT, true, false, false);
        this.placeTimer = new Timer();
        this.breakTimer = new Timer();
        this.preditTimer = new Timer();
        this.manualTimer = new Timer();
        this.page = (Setting<pages>)this.register(new Setting("Page", (Object)pages.Place));
        this.attackFactor = (Setting<Integer>)this.register(new Setting("PredictDelay", (Object)0, (Object)0, (Object)200, v -> this.page.getValue() == pages.Break));
        this.renderColor = (Setting<ColorSetting>)this.register(new Setting("RenderColor", (Object)new ColorSetting(155, 175, 255, 255), v -> this.page.getValue() == pages.Render));
        this.red = (Setting<Integer>)this.register(new Setting("Red", (Object)155, (Object)0, (Object)255, v -> this.page.getValue() == pages.Render));
        this.green = (Setting<Integer>)this.register(new Setting("Green", (Object)175, (Object)0, (Object)255, v -> this.page.getValue() == pages.Render));
        this.blue = (Setting<Integer>)this.register(new Setting("Blue", (Object)255, (Object)0, (Object)255, v -> this.page.getValue() == pages.Render));
        this.alpha = (Setting<Integer>)this.register(new Setting("Alpha", (Object)255, (Object)0, (Object)255, v -> this.page.getValue() == pages.Render));
        this.boxAlpha = (Setting<Integer>)this.register(new Setting("BoxAlpha", (Object)70, (Object)0, (Object)255, v -> this.page.getValue() == pages.Render));
        this.lineWidth = (Setting<Float>)this.register(new Setting("LineWidth", (Object)1.0f, (Object)0.1f, (Object)5.0f, v -> this.page.getValue() == pages.Render));
        this.place = (Setting<Boolean>)this.register(new Setting("Place", (Object)true, v -> this.page.getValue() == pages.Place));
        this.placeDelay = (Setting<Float>)this.register(new Setting("PlaceDelay", (Object)4.0f, (Object)0.0f, (Object)300.0f, v -> this.page.getValue() == pages.Place));
        this.placeRange = (Setting<Float>)this.register(new Setting("PlaceRange", (Object)4.0f, (Object)0.1f, (Object)7.0f, v -> this.page.getValue() == pages.Place));
        this.placeWallRange = (Setting<Float>)this.register(new Setting("PlaceWallRange", (Object)3.0f, (Object)0.1f, (Object)7.0f, v -> this.page.getValue() == pages.Place));
        this.explode = (Setting<Boolean>)this.register(new Setting("Break", (Object)true, v -> this.page.getValue() == pages.Break));
        this.packetBreak = (Setting<Boolean>)this.register(new Setting("PacketBreak", (Object)true, v -> this.page.getValue() == pages.Break));
        this.predicts = (Setting<Boolean>)this.register(new Setting("Predict", (Object)true, v -> this.page.getValue() == pages.Break));
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (Object)true, v -> this.page.getValue() == pages.Calc));
        this.smoothRotate = (Setting<Boolean>)this.register(new Setting("SmoothRotate", (Object)false, v -> (boolean)this.rotate.getValue() && this.page.getValue() == pages.Calc));
        this.rotateSpeed = (Setting<Float>)this.register(new Setting("RotateSpeed", (Object)0.5f, (Object)0.1f, (Object)2.0f, v -> (boolean)this.smoothRotate.getValue() && (boolean)this.rotate.getValue()));
        this.sequence = (Setting<SequenceMode>)this.register(new Setting("Sequence", (Object)SequenceMode.None, v -> this.page.getValue() == pages.Calc));
        this.breakDelay = (Setting<Float>)this.register(new Setting("BreakDelay", (Object)4.0f, (Object)0.0f, (Object)300.0f, v -> this.page.getValue() == pages.Break));
        this.breakRange = (Setting<Float>)this.register(new Setting("BreakRange", (Object)4.0f, (Object)0.1f, (Object)7.0f, v -> this.page.getValue() == pages.Break));
        this.breakWallRange = (Setting<Float>)this.register(new Setting("BreakWallRange", (Object)4.0f, (Object)0.1f, (Object)7.0f, v -> this.page.getValue() == pages.Break));
        this.ecmeplace = (Setting<Boolean>)this.register(new Setting("1.13 Place", (Object)true, v -> this.page.getValue() == pages.Place));
        this.suicide = (Setting<Boolean>)this.register(new Setting("AntiSuicide", (Object)true, v -> this.page.getValue() == pages.Calc));
        this.autoswitch = (Setting<Boolean>)this.register(new Setting("AutoSwitch", (Object)true, v -> this.page.getValue() == pages.Calc));
        this.ignoreUseAmount = (Setting<Boolean>)this.register(new Setting("IgnoreUseAmount", (Object)true, v -> this.page.getValue() == pages.Calc));
        this.wasteAmount = (Setting<Integer>)this.register(new Setting("UseAmount", (Object)4, (Object)1, (Object)5, v -> this.page.getValue() == pages.Calc));
        this.facePlaceSword = (Setting<Boolean>)this.register(new Setting("FacePlaceSword", (Object)true, v -> this.page.getValue() == pages.Break));
        this.removeAttack = (Setting<Boolean>)this.register(new Setting("EntityRemove", (Object)false, v -> this.page.getValue() == pages.Break));
        this.targetRange = (Setting<Float>)this.register(new Setting("TargetRange", (Object)4.0f, (Object)1.0f, (Object)12.0f, v -> this.page.getValue() == pages.Calc));
        this.minDamage = (Setting<Float>)this.register(new Setting("MinDamage", (Object)4.0f, (Object)0.1f, (Object)20.0f, v -> this.page.getValue() == pages.Place));
        this.facePlace = (Setting<Float>)this.register(new Setting("FacePlaceHP", (Object)4.0f, (Object)0.0f, (Object)36.0f, v -> this.page.getValue() == pages.Calc));
        this.breakMaxSelfDamage = (Setting<Float>)this.register(new Setting("BreakMaxSelf", (Object)4.0f, (Object)0.1f, (Object)12.0f, v -> this.page.getValue() == pages.Break));
        this.breakMinDmg = (Setting<Float>)this.register(new Setting("BreakMinDmg", (Object)4.0f, (Object)0.1f, (Object)7.0f, v -> this.page.getValue() == pages.Break));
        this.minArmor = (Setting<Float>)this.register(new Setting("MinArmor", (Object)4.0f, (Object)0.1f, (Object)80.0f, v -> this.page.getValue() == pages.Calc));
        this.swingMode = (Setting<SwingMode>)this.register(new Setting("Swing", (Object)SwingMode.MainHand, v -> this.page.getValue() == pages.Calc));
        this.pauseWhileEating = (Setting<Boolean>)this.register(new Setting("PauseEating", (Object)true, v -> this.page.getValue() == pages.Place));
        this.render = (Setting<Boolean>)this.register(new Setting("Render", (Object)true, v -> this.page.getValue() == pages.Render));
        this.renderDmg = (Setting<Boolean>)this.register(new Setting("RenderDmg", (Object)true, v -> this.page.getValue() == pages.Render));
        this.box = (Setting<Boolean>)this.register(new Setting("Box", (Object)true, v -> this.page.getValue() == pages.Render));
        this.outline = (Setting<Boolean>)new Setting("Outline", (Object)true, v -> this.page.getValue() == pages.Render);
        this.yaw = 0.0f;
        this.pitch = 0.0f;
        this.rotating = false;
        this.lastYaw = null;
        this.lastPitch = null;
        this.placeFlag = false;
        this.breakFlag = false;
    }
    
    public void onUpdate() {
        final ColorSetting color = (ColorSetting)this.renderColor.getValue();
        this.red.setValueNoEvent((Object)color.getRed());
        this.green.setValueNoEvent((Object)color.getGreen());
        this.blue.setValueNoEvent((Object)color.getBlue());
        this.alpha.setValueNoEvent((Object)color.getAlpha());
    }
    
    public static List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final ArrayList<BlockPos> circleblocks = new ArrayList<BlockPos>();
        final int cx = loc.func_177958_n();
        final int cy = loc.func_177956_o();
        final int cz = loc.func_177952_p();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                int y = sphere ? (cy - (int)r) : cy;
                while (true) {
                    final float f2;
                    final float f = f2 = (sphere ? (cy + r) : ((float)(cy + h)));
                    if (y >= f) {
                        break;
                    }
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                    ++y;
                }
            }
        }
        return circleblocks;
    }
    
    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if (event.getStage() == 0 && (boolean)this.rotate.getValue() && this.rotating && event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            packet.field_149476_e = this.yaw;
            packet.field_149473_f = this.pitch;
            this.rotating = false;
        }
        if (event.getStage() == 0 && event.getPacket() instanceof CPacketUseEntity) {
            final CPacketUseEntity packet2 = (CPacketUseEntity)event.getPacket();
            if (this.removeAttack.getValue()) {
                packet2.func_149564_a((World)AutoCrystal.mc.field_71441_e).func_70106_y();
                AutoCrystal.mc.field_71441_e.func_73028_b(packet2.field_149567_a);
            }
        }
    }
    
    private void rotateTo(final Entity entity) {
        if (this.rotate.getValue()) {
            final float[] angle = this.calculateRotations(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v);
            if (this.smoothRotate.getValue()) {
                final float[] smoothed = this.smoothRotation(angle);
                this.yaw = smoothed[0];
                this.pitch = smoothed[1];
            }
            else {
                this.yaw = angle[0];
                this.pitch = angle[1];
            }
            this.rotating = true;
        }
    }
    
    private void rotateToPos(final BlockPos pos) {
        if (this.rotate.getValue()) {
            final float[] angle = this.calculateRotations(pos.func_177958_n() + 0.5, pos.func_177956_o() + 0.5, pos.func_177952_p() + 0.5);
            if (this.smoothRotate.getValue()) {
                final float[] smoothed = this.smoothRotation(angle);
                this.yaw = smoothed[0];
                this.pitch = smoothed[1];
            }
            else {
                this.yaw = angle[0];
                this.pitch = angle[1];
            }
            this.rotating = true;
        }
    }
    
    private float[] calculateRotations(final double x, final double y, final double z) {
        final Vec3d eyesPos = AutoCrystal.mc.field_71439_g.func_174824_e(AutoCrystal.mc.func_184121_ak());
        final double deltaX = x - eyesPos.field_72450_a;
        final double deltaY = y - eyesPos.field_72448_b;
        final double deltaZ = z - eyesPos.field_72449_c;
        final double dist = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        final float calcYaw = (float)(Math.atan2(deltaZ, deltaX) * 180.0 / 3.141592653589793) - 90.0f;
        final float calcPitch = (float)(-(Math.atan2(deltaY, dist) * 180.0 / 3.141592653589793));
        final float prevYaw = AutoCrystal.mc.field_71439_g.field_70177_z;
        float yawDiff = calcYaw - prevYaw;
        if (yawDiff < -180.0f || yawDiff > 180.0f) {
            final float round = (float)Math.round(Math.abs(yawDiff / 360.0f));
            yawDiff = ((yawDiff < 0.0f) ? (yawDiff + 360.0f * round) : (yawDiff - 360.0f * round));
        }
        return new float[] { prevYaw + yawDiff, calcPitch };
    }
    
    private float[] smoothRotation(final float[] targetRot) {
        if (this.lastYaw == null) {
            this.lastYaw = AutoCrystal.mc.field_71439_g.field_70177_z;
        }
        if (this.lastPitch == null) {
            this.lastPitch = AutoCrystal.mc.field_71439_g.field_70125_A;
        }
        float yawChange = this.wrapAngleTo180(targetRot[0] - this.lastYaw);
        float pitchChange = this.wrapAngleTo180(targetRot[1] - this.lastPitch);
        final float speed = (float)this.rotateSpeed.getValue();
        final float yawChangeFactor = Math.abs(yawChange) / 180.0f;
        final float pitchChangeFactor = Math.abs(pitchChange) / 180.0f;
        final float maxYawChange = 115.0f * yawChangeFactor * speed;
        final float maxPitchChange = 115.0f * pitchChangeFactor * speed;
        if (Math.abs(yawChange) > maxYawChange) {
            yawChange = ((yawChange > 0.0f) ? maxYawChange : (-maxYawChange));
        }
        if (Math.abs(pitchChange) > maxPitchChange) {
            pitchChange = ((pitchChange > 0.0f) ? maxPitchChange : (-maxPitchChange));
        }
        final float newYaw = this.lastYaw + yawChange;
        final float newPitch = this.lastPitch + pitchChange;
        this.lastYaw = newYaw;
        this.lastPitch = newPitch;
        return new float[] { newYaw, newPitch };
    }
    
    private float wrapAngleTo180(float angle) {
        angle %= 360.0f;
        if (angle >= 180.0f) {
            angle -= 360.0f;
        }
        if (angle < -180.0f) {
            angle += 360.0f;
        }
        return angle;
    }
    
    public void onEnable() {
        this.placeTimer.reset();
        this.breakTimer.reset();
        this.predictWait = 0;
        this.hotBarSlot = -1;
        this.pos = null;
        this.crystal = null;
        this.predict = 0;
        this.predictPackets = 1;
        this.target = null;
        this.packetCalc = false;
        this.realTarget = null;
        this.armor = false;
        this.armorTarget = false;
    }
    
    public void onDisable() {
        this.rotating = false;
        this.lastYaw = null;
        this.lastPitch = null;
    }
    
    public void onTick() {
        this.onCrystal();
    }
    
    public String getDisplayInfo() {
        if (this.realTarget != null) {
            return this.realTarget.func_70005_c_();
        }
        return null;
    }
    
    public void onCrystal() {
        if (AutoCrystal.mc.field_71441_e == null || AutoCrystal.mc.field_71439_g == null) {
            return;
        }
        if ((boolean)this.pauseWhileEating.getValue() && EntityUtil.isEating()) {
            final ItemStack activeStack = AutoCrystal.mc.field_71439_g.func_184607_cu();
            if (!activeStack.func_190926_b()) {
                final Item activeItem = activeStack.func_77973_b();
                if (activeItem instanceof ItemFood || activeItem instanceof ItemAppleGold || activeItem == Items.field_185161_cS || activeItem == Items.field_151068_bn) {
                    return;
                }
            }
        }
        this.realTarget = null;
        this.manualBreaker();
        this.crystalCount = 0;
        if (!(boolean)this.ignoreUseAmount.getValue()) {
            for (final Entity crystal : AutoCrystal.mc.field_71441_e.field_72996_f) {
                if (crystal instanceof EntityEnderCrystal) {
                    if (!this.IsValidCrystal(crystal)) {
                        continue;
                    }
                    boolean count = false;
                    final double damage = this.calculateDamage(this.target.func_180425_c().func_177958_n() + 0.5, this.target.func_180425_c().func_177956_o() + 1.0, this.target.func_180425_c().func_177952_p() + 0.5, (Entity)this.target);
                    if (damage >= (float)this.minDamage.getValue()) {
                        count = true;
                    }
                    if (!count) {
                        continue;
                    }
                    ++this.crystalCount;
                }
            }
        }
        this.hotBarSlot = -1;
        if (AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_185158_cP) {
            final int n;
            int crystalSlot = n = ((AutoCrystal.mc.field_71439_g.func_184614_ca().func_77973_b() == Items.field_185158_cP) ? AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c : -1);
            if (crystalSlot == -1) {
                for (int l = 0; l < 9; ++l) {
                    if (AutoCrystal.mc.field_71439_g.field_71071_by.func_70301_a(l).func_77973_b() == Items.field_185158_cP) {
                        crystalSlot = l;
                        this.hotBarSlot = l;
                        break;
                    }
                }
            }
            if (crystalSlot == -1) {
                this.pos = null;
                this.target = null;
                return;
            }
        }
        if (AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_151153_ao && AutoCrystal.mc.field_71439_g.func_184614_ca().func_77973_b() != Items.field_185158_cP) {
            this.pos = null;
            this.target = null;
            return;
        }
        if (this.target == null) {
            this.target = (EntityLivingBase)this.getTarget();
        }
        if (this.target == null) {
            this.crystal = null;
            this.realTarget = null;
            return;
        }
        this.realTarget = this.target;
        if (this.target.func_70032_d((Entity)AutoCrystal.mc.field_71439_g) > 12.0f) {
            this.crystal = null;
            this.target = null;
            this.realTarget = null;
        }
        this.crystal = (EntityEnderCrystal)AutoCrystal.mc.field_71441_e.field_72996_f.stream().filter(this::IsValidCrystal).map(p_Entity -> p_Entity).min(Comparator.comparing(p_Entity -> this.target.func_70032_d(p_Entity))).orElse(null);
        this.handleSequence();
        if (this.breakFlag || (!this.placeFlag && this.crystal != null && (boolean)this.explode.getValue() && this.breakTimer.passedMs(((Float)this.breakDelay.getValue()).longValue()))) {
            if (this.breakFlag) {
                this.breakFlag = false;
            }
            if (this.crystal != null && (boolean)this.explode.getValue() && this.breakTimer.passedMs(((Float)this.breakDelay.getValue()).longValue())) {
                this.breakTimer.reset();
                if (this.packetBreak.getValue()) {
                    this.rotateTo((Entity)this.crystal);
                    AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity((Entity)this.crystal));
                }
                else {
                    this.rotateTo((Entity)this.crystal);
                    AutoCrystal.mc.field_71442_b.func_78764_a((EntityPlayer)AutoCrystal.mc.field_71439_g, (Entity)this.crystal);
                }
                if (this.swingMode.getValue() == SwingMode.MainHand) {
                    AutoCrystal.mc.field_71439_g.func_184609_a(EnumHand.MAIN_HAND);
                }
                else if (this.swingMode.getValue() == SwingMode.OffHand) {
                    AutoCrystal.mc.field_71439_g.func_184609_a(EnumHand.OFF_HAND);
                }
            }
        }
        if (this.placeFlag || (!this.breakFlag && this.placeTimer.passedMs(((Float)this.placeDelay.getValue()).longValue()) && (boolean)this.place.getValue())) {
            if (this.placeFlag) {
                this.placeFlag = false;
            }
            if (this.placeTimer.passedMs(((Float)this.placeDelay.getValue()).longValue()) && (boolean)this.place.getValue()) {
                this.placeTimer.reset();
                double damage2 = 0.5;
                for (final BlockPos blockPos : this.placePostions((float)this.placeRange.getValue())) {
                    final double targetRange;
                    if (blockPos != null && this.target != null && AutoCrystal.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(blockPos)).isEmpty() && (targetRange = this.target.func_70011_f((double)blockPos.func_177958_n(), (double)blockPos.func_177956_o(), (double)blockPos.func_177952_p())) <= (float)this.targetRange.getValue() && !this.target.field_70128_L) {
                        if (this.target.func_110143_aJ() + this.target.func_110139_bj() <= 0.0f) {
                            continue;
                        }
                        if (!this.canSeePos(blockPos)) {
                            final double distanceToPos = AutoCrystal.mc.field_71439_g.func_70011_f((double)blockPos.func_177958_n(), (double)blockPos.func_177956_o(), (double)blockPos.func_177952_p());
                            if (distanceToPos > (float)this.placeWallRange.getValue()) {
                                continue;
                            }
                        }
                        final double targetDmg = this.calculateDamage(blockPos.func_177958_n() + 0.5, blockPos.func_177956_o() + 1.0, blockPos.func_177952_p() + 0.5, (Entity)this.target);
                        this.armor = false;
                        for (final ItemStack is : this.target.func_184193_aE()) {
                            final float green = (is.func_77958_k() - (float)is.func_77952_i()) / is.func_77958_k();
                            final float red = 1.0f - green;
                            final int dmg = 100 - (int)(red * 100.0f);
                            if (dmg > (float)this.minArmor.getValue()) {
                                continue;
                            }
                            this.armor = true;
                        }
                        Label_1504: {
                            if (targetDmg < (float)this.minDamage.getValue()) {
                                if (this.facePlaceSword.getValue()) {
                                    if (this.target.func_110139_bj() + this.target.func_110143_aJ() <= (float)this.facePlace.getValue()) {
                                        break Label_1504;
                                    }
                                }
                                else if (!(AutoCrystal.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword) && this.target.func_110139_bj() + this.target.func_110143_aJ() <= (float)this.facePlace.getValue()) {
                                    break Label_1504;
                                }
                                if (this.facePlaceSword.getValue()) {
                                    if (!this.armor) {
                                        continue;
                                    }
                                }
                                else if (AutoCrystal.mc.field_71439_g.func_184614_ca().func_77973_b() instanceof ItemSword || !this.armor) {
                                    continue;
                                }
                            }
                        }
                        final double selfDmg;
                        if ((selfDmg = this.calculateDamage(blockPos.func_177958_n() + 0.5, blockPos.func_177956_o() + 1.0, blockPos.func_177952_p() + 0.5, (Entity)AutoCrystal.mc.field_71439_g)) + (this.suicide.getValue() ? 2.0 : 0.5) >= AutoCrystal.mc.field_71439_g.func_110143_aJ() + AutoCrystal.mc.field_71439_g.func_110139_bj() && selfDmg >= targetDmg && targetDmg < this.target.func_110143_aJ() + this.target.func_110139_bj()) {
                            continue;
                        }
                        if (damage2 >= targetDmg) {
                            continue;
                        }
                        this.pos = blockPos;
                        damage2 = targetDmg;
                    }
                }
                if (damage2 == 0.5) {
                    this.pos = null;
                    this.target = null;
                    this.realTarget = null;
                    return;
                }
                this.realTarget = this.target;
                if (this.hotBarSlot != -1 && (boolean)this.autoswitch.getValue() && !AutoCrystal.mc.field_71439_g.func_70644_a(MobEffects.field_76437_t)) {
                    AutoCrystal.mc.field_71439_g.field_71071_by.field_70461_c = this.hotBarSlot;
                }
                if (!(boolean)this.ignoreUseAmount.getValue()) {
                    int crystalLimit = (int)this.wasteAmount.getValue();
                    if (this.crystalCount >= crystalLimit) {
                        return;
                    }
                    if (damage2 < (float)this.minDamage.getValue()) {
                        crystalLimit = 1;
                    }
                    if (this.crystalCount < crystalLimit && this.pos != null) {
                        this.rotateToPos(this.pos);
                        AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, EnumFacing.UP, (AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                    }
                }
                else if (this.pos != null) {
                    this.rotateToPos(this.pos);
                    AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(this.pos, EnumFacing.UP, (AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() == Items.field_185158_cP) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                }
            }
        }
    }
    
    private void handleSequence() {
        this.placeFlag = false;
        this.breakFlag = false;
        if (this.sequence.getValue() == SequenceMode.None) {
            if (this.pos != null && this.pos.equals((Object)this.pos) && this.crystal != null && AutoCrystal.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(this.pos.func_177982_a(0, 1, 0))).contains(this.crystal) && this.crystal.func_180425_c().func_177977_b().equals((Object)this.pos)) {
                this.breakFlag = true;
            }
            else if (this.crystal != null && !this.crystal.func_180425_c().func_177977_b().equals((Object)this.pos)) {
                this.placeFlag = true;
            }
            else {
                this.breakFlag = true;
            }
        }
        else if (this.sequence.getValue() == SequenceMode.Soft) {
            if (this.crystal != null) {
                boolean recentlyHit = false;
                final long currentTime = System.currentTimeMillis();
                if (!this.breakTimer.passedMs(100L)) {
                    recentlyHit = true;
                }
                if (recentlyHit) {
                    this.placeFlag = true;
                }
                else {
                    this.breakFlag = true;
                }
            }
            else {
                this.placeFlag = true;
            }
        }
        else if (this.sequence.getValue() == SequenceMode.Full) {
            if (this.crystal != null) {
                this.breakFlag = true;
                this.placeFlag = false;
            }
            else if (this.pos != null) {
                this.placeFlag = true;
                this.breakFlag = false;
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onPacketReceive(final PacketEvent.Receive event) {
        final SPacketSpawnObject packet;
        if (event.getPacket() instanceof SPacketSpawnObject && (packet = (SPacketSpawnObject)event.getPacket()).func_148993_l() == 51 && (boolean)this.predicts.getValue() && this.preditTimer.passedMs((long)this.attackFactor.getValue()) && (boolean)this.predicts.getValue() && (boolean)this.explode.getValue() && (boolean)this.packetBreak.getValue() && this.target != null) {
            if (!this.isPredicting(packet)) {
                return;
            }
            final CPacketUseEntity predict = new CPacketUseEntity();
            predict.field_149567_a = packet.func_149001_c();
            predict.field_149566_b = CPacketUseEntity.Action.ATTACK;
            AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)predict);
        }
    }
    
    public void onRender3D(final Render3DEvent event) {
        if (this.pos != null && (boolean)this.render.getValue() && this.target != null) {
            RenderUtil.drawBoxESP(this.pos, ((boolean)Color.getInstance().rainbow.getValue()) ? ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()) : new java.awt.Color((int)this.red.getValue(), (int)this.green.getValue(), (int)this.blue.getValue(), (int)this.alpha.getValue()), (boolean)this.outline.getValue(), ((boolean)Color.getInstance().rainbow.getValue()) ? ColorUtil.rainbow((int)Color.getInstance().rainbowHue.getValue()) : new java.awt.Color((int)this.red.getValue(), (int)this.green.getValue(), (int)this.blue.getValue(), (int)this.alpha.getValue()), (float)this.lineWidth.getValue(), (boolean)this.outline.getValue(), (boolean)this.box.getValue(), (int)this.boxAlpha.getValue(), true);
            if (this.renderDmg.getValue()) {
                final double renderDamage = this.calculateDamage(this.pos.func_177958_n() + 0.5, this.pos.func_177956_o() + 1.0, this.pos.func_177952_p() + 0.5, (Entity)this.target);
                RenderUtil.drawText(this.pos, ((Math.floor(renderDamage) == renderDamage) ? Integer.valueOf((int)renderDamage) : String.format(ChatFormatting.WHITE + "%.1f", renderDamage)) + "");
            }
        }
    }
    
    private boolean isPredicting(final SPacketSpawnObject packet) {
        final BlockPos packPos = new BlockPos(packet.func_186880_c(), packet.func_186882_d(), packet.func_186881_e());
        if (AutoCrystal.mc.field_71439_g.func_70011_f(packet.func_186880_c(), packet.func_186882_d(), packet.func_186881_e()) > (float)this.breakRange.getValue()) {
            return false;
        }
        if (!this.canSeePos(packPos) && AutoCrystal.mc.field_71439_g.func_70011_f(packet.func_186880_c(), packet.func_186882_d(), packet.func_186881_e()) > (float)this.breakWallRange.getValue()) {
            return false;
        }
        final double targetDmg = this.calculateDamage(packet.func_186880_c() + 0.5, packet.func_186882_d() + 1.0, packet.func_186881_e() + 0.5, (Entity)this.target);
        if (EntityUtil.isInHole((Entity)AutoCrystal.mc.field_71439_g) && targetDmg >= 1.0) {
            return true;
        }
        final double selfDmg = this.calculateDamage(packet.func_186880_c() + 0.5, packet.func_186882_d() + 1.0, packet.func_186881_e() + 0.5, (Entity)AutoCrystal.mc.field_71439_g);
        final double d = this.suicide.getValue() ? 2.0 : 0.5;
        if (selfDmg + d < AutoCrystal.mc.field_71439_g.func_110143_aJ() + AutoCrystal.mc.field_71439_g.func_110139_bj() && targetDmg >= this.target.func_110139_bj() + this.target.func_110143_aJ()) {
            return true;
        }
        this.armorTarget = false;
        for (final ItemStack is : this.target.func_184193_aE()) {
            final float green = (is.func_77958_k() - (float)is.func_77952_i()) / is.func_77958_k();
            final float red = 1.0f - green;
            final int dmg = 100 - (int)(red * 100.0f);
            if (dmg > (float)this.minArmor.getValue()) {
                continue;
            }
            this.armorTarget = true;
        }
        return (targetDmg >= (float)this.breakMinDmg.getValue() && selfDmg <= (float)this.breakMaxSelfDamage.getValue()) || (EntityUtil.isInHole((Entity)this.target) && this.target.func_110143_aJ() + this.target.func_110139_bj() <= (float)this.facePlace.getValue());
    }
    
    private boolean IsValidCrystal(final Entity p_Entity) {
        if (p_Entity == null) {
            return false;
        }
        if (!(p_Entity instanceof EntityEnderCrystal)) {
            return false;
        }
        if (this.target == null) {
            return false;
        }
        if (p_Entity.func_70032_d((Entity)AutoCrystal.mc.field_71439_g) > (float)this.breakRange.getValue()) {
            return false;
        }
        if (!AutoCrystal.mc.field_71439_g.func_70685_l(p_Entity) && p_Entity.func_70032_d((Entity)AutoCrystal.mc.field_71439_g) > (float)this.breakWallRange.getValue()) {
            return false;
        }
        final double targetDmg = this.calculateDamage(p_Entity.func_180425_c().func_177958_n() + 0.5, p_Entity.func_180425_c().func_177956_o() + 1.0, p_Entity.func_180425_c().func_177952_p() + 0.5, (Entity)this.target);
        if (EntityUtil.isInHole((Entity)AutoCrystal.mc.field_71439_g) && targetDmg >= 1.0) {
            return true;
        }
        final double selfDmg = this.calculateDamage(p_Entity.func_180425_c().func_177958_n() + 0.5, p_Entity.func_180425_c().func_177956_o() + 1.0, p_Entity.func_180425_c().func_177952_p() + 0.5, (Entity)AutoCrystal.mc.field_71439_g);
        final double d = this.suicide.getValue() ? 2.0 : 0.5;
        if (selfDmg + d < AutoCrystal.mc.field_71439_g.func_110143_aJ() + AutoCrystal.mc.field_71439_g.func_110139_bj() && targetDmg >= this.target.func_110139_bj() + this.target.func_110143_aJ()) {
            return true;
        }
        this.armorTarget = false;
        for (final ItemStack is : this.target.func_184193_aE()) {
            final float green = (is.func_77958_k() - (float)is.func_77952_i()) / is.func_77958_k();
            final float red = 1.0f - green;
            final int dmg = 100 - (int)(red * 100.0f);
            if (dmg > (float)this.minArmor.getValue()) {
                continue;
            }
            this.armorTarget = true;
        }
        return (targetDmg >= (float)this.breakMinDmg.getValue() && selfDmg <= (float)this.breakMaxSelfDamage.getValue()) || (EntityUtil.isInHole((Entity)this.target) && this.target.func_110143_aJ() + this.target.func_110139_bj() <= (float)this.facePlace.getValue());
    }
    
    EntityPlayer getTarget() {
        EntityPlayer closestPlayer = null;
        for (final EntityPlayer entity : AutoCrystal.mc.field_71441_e.field_73010_i) {
            if (AutoCrystal.mc.field_71439_g != null && !AutoCrystal.mc.field_71439_g.field_70128_L && !entity.field_70128_L && entity != AutoCrystal.mc.field_71439_g && !OyVey.friendManager.isFriend(entity.func_70005_c_())) {
                if (entity.func_70032_d((Entity)AutoCrystal.mc.field_71439_g) > 12.0f) {
                    continue;
                }
                this.armorTarget = false;
                for (final ItemStack is : entity.func_184193_aE()) {
                    final float green = (is.func_77958_k() - (float)is.func_77952_i()) / is.func_77958_k();
                    final float red = 1.0f - green;
                    final int dmg = 100 - (int)(red * 100.0f);
                    if (dmg > (float)this.minArmor.getValue()) {
                        continue;
                    }
                    this.armorTarget = true;
                }
                if (EntityUtil.isInHole((Entity)entity) && entity.func_110139_bj() + entity.func_110143_aJ() > (float)this.facePlace.getValue() && !this.armorTarget && (float)this.minDamage.getValue() > 2.2f) {
                    continue;
                }
                if (closestPlayer == null) {
                    closestPlayer = entity;
                }
                else {
                    if (closestPlayer.func_70032_d((Entity)AutoCrystal.mc.field_71439_g) <= entity.func_70032_d((Entity)AutoCrystal.mc.field_71439_g)) {
                        continue;
                    }
                    closestPlayer = entity;
                }
            }
        }
        return closestPlayer;
    }
    
    private void manualBreaker() {
        final RayTraceResult result;
        if (this.manualTimer.passedMs(200L) && AutoCrystal.mc.field_71474_y.field_74313_G.func_151470_d() && AutoCrystal.mc.field_71439_g.func_184592_cb().func_77973_b() != Items.field_151153_ao && AutoCrystal.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151153_ao && AutoCrystal.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151031_f && AutoCrystal.mc.field_71439_g.field_71071_by.func_70448_g().func_77973_b() != Items.field_151062_by && (result = AutoCrystal.mc.field_71476_x) != null) {
            if (result.field_72313_a.equals((Object)RayTraceResult.Type.ENTITY)) {
                final Entity entity = result.field_72308_g;
                if (entity instanceof EntityEnderCrystal) {
                    if (this.packetBreak.getValue()) {
                        AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(entity));
                    }
                    else {
                        AutoCrystal.mc.field_71442_b.func_78764_a((EntityPlayer)AutoCrystal.mc.field_71439_g, entity);
                    }
                    this.manualTimer.reset();
                }
            }
            else if (result.field_72313_a.equals((Object)RayTraceResult.Type.BLOCK)) {
                final BlockPos mousePos = new BlockPos((double)AutoCrystal.mc.field_71476_x.func_178782_a().func_177958_n(), AutoCrystal.mc.field_71476_x.func_178782_a().func_177956_o() + 1.0, (double)AutoCrystal.mc.field_71476_x.func_178782_a().func_177952_p());
                for (final Entity target : AutoCrystal.mc.field_71441_e.func_72839_b((Entity)null, new AxisAlignedBB(mousePos))) {
                    if (!(target instanceof EntityEnderCrystal)) {
                        continue;
                    }
                    if (this.packetBreak.getValue()) {
                        AutoCrystal.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketUseEntity(target));
                    }
                    else {
                        AutoCrystal.mc.field_71442_b.func_78764_a((EntityPlayer)AutoCrystal.mc.field_71439_g, target);
                    }
                    this.manualTimer.reset();
                }
            }
        }
    }
    
    private boolean canSeePos(final BlockPos pos) {
        return AutoCrystal.mc.field_71441_e.func_147447_a(new Vec3d(AutoCrystal.mc.field_71439_g.field_70165_t, AutoCrystal.mc.field_71439_g.field_70163_u + AutoCrystal.mc.field_71439_g.func_70047_e(), AutoCrystal.mc.field_71439_g.field_70161_v), new Vec3d((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p()), false, true, false) == null;
    }
    
    private NonNullList<BlockPos> placePostions(final float placeRange) {
        final NonNullList positions = NonNullList.func_191196_a();
        positions.addAll((Collection)getSphere(new BlockPos(Math.floor(AutoCrystal.mc.field_71439_g.field_70165_t), Math.floor(AutoCrystal.mc.field_71439_g.field_70163_u), Math.floor(AutoCrystal.mc.field_71439_g.field_70161_v)), placeRange, (int)placeRange, false, true, 0).stream().filter(pos -> this.canPlaceCrystal(pos, true)).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
        return (NonNullList<BlockPos>)positions;
    }
    
    private boolean canPlaceCrystal(final BlockPos blockPos, final boolean specialEntityCheck) {
        final BlockPos boost = blockPos.func_177982_a(0, 1, 0);
        final BlockPos boost2 = blockPos.func_177982_a(0, 2, 0);
        try {
            if (!(boolean)this.ecmeplace.getValue()) {
                if (AutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h && AutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z) {
                    return false;
                }
                if (AutoCrystal.mc.field_71441_e.func_180495_p(boost).func_177230_c() != Blocks.field_150350_a || AutoCrystal.mc.field_71441_e.func_180495_p(boost2).func_177230_c() != Blocks.field_150350_a) {
                    return false;
                }
                if (!this.canPlaceCrystalWithoutInterference(blockPos)) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return AutoCrystal.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty() && AutoCrystal.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(boost2)).isEmpty();
                }
                for (final Entity entity : AutoCrystal.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) {
                        continue;
                    }
                    return false;
                }
                for (final Entity entity : AutoCrystal.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(boost2))) {
                    if (entity instanceof EntityEnderCrystal) {
                        continue;
                    }
                    return false;
                }
            }
            else {
                if (AutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150357_h && AutoCrystal.mc.field_71441_e.func_180495_p(blockPos).func_177230_c() != Blocks.field_150343_Z) {
                    return false;
                }
                if (AutoCrystal.mc.field_71441_e.func_180495_p(boost).func_177230_c() != Blocks.field_150350_a) {
                    return false;
                }
                if (!this.canPlaceCrystalWithoutInterference(blockPos)) {
                    return false;
                }
                if (!specialEntityCheck) {
                    return AutoCrystal.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(boost)).isEmpty();
                }
                for (final Entity entity : AutoCrystal.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) {
                        continue;
                    }
                    return false;
                }
            }
        }
        catch (Exception ignored) {
            return false;
        }
        return true;
    }
    
    private boolean canPlaceCrystalWithoutInterference(final BlockPos pos) {
        final AxisAlignedBB newCrystalBox = new AxisAlignedBB(pos.func_177958_n() + 0.0, pos.func_177956_o() + 1.0, pos.func_177952_p() + 0.0, pos.func_177958_n() + 1.0, pos.func_177956_o() + 3.0, pos.func_177952_p() + 1.0);
        for (final Entity entity : AutoCrystal.mc.field_71441_e.field_72996_f) {
            if (entity instanceof EntityEnderCrystal) {
                final AxisAlignedBB existingCrystalBox = entity.func_174813_aQ();
                if (newCrystalBox.func_72326_a(existingCrystalBox)) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }
    
    private float calculateDamage(final double posX, final double posY, final double posZ, final Entity entity) {
        final float doubleExplosionSize = 12.0f;
        final double distancedsize = entity.func_70011_f(posX, posY, posZ) / 12.0;
        final Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = 0.0;
        try {
            blockDensity = entity.field_70170_p.func_72842_a(vec3d, entity.func_174813_aQ());
        }
        catch (Exception ex) {}
        final double v = (1.0 - distancedsize) * blockDensity;
        final float damage = (float)(int)((v * v + v) / 2.0 * 7.0 * 12.0 + 1.0);
        double finald = 1.0;
        if (entity instanceof EntityLivingBase) {
            finald = this.getBlastReduction((EntityLivingBase)entity, this.getDamageMultiplied(damage), new Explosion((World)AutoCrystal.mc.field_71441_e, (Entity)null, posX, posY, posZ, 6.0f, false, true));
        }
        return (float)finald;
    }
    
    private float getBlastReduction(final EntityLivingBase entity, final float damageI, final Explosion explosion) {
        float damage = damageI;
        if (entity instanceof EntityPlayer) {
            final EntityPlayer ep = (EntityPlayer)entity;
            final DamageSource ds = DamageSource.func_94539_a(explosion);
            damage = CombatRules.func_189427_a(damage, (float)ep.func_70658_aO(), (float)ep.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
            int k = 0;
            try {
                k = EnchantmentHelper.func_77508_a(ep.func_184193_aE(), ds);
            }
            catch (Exception ex) {}
            final float f = MathHelper.func_76131_a((float)k, 0.0f, 20.0f);
            damage *= 1.0f - f / 25.0f;
            if (entity.func_70644_a(MobEffects.field_76429_m)) {
                damage -= damage / 4.0f;
            }
            damage = Math.max(damage, 0.0f);
            return damage;
        }
        damage = CombatRules.func_189427_a(damage, (float)entity.func_70658_aO(), (float)entity.func_110148_a(SharedMonsterAttributes.field_189429_h).func_111126_e());
        return damage;
    }
    
    private float getDamageMultiplied(final float damage) {
        final int diff = AutoCrystal.mc.field_71441_e.func_175659_aa().func_151525_a();
        return damage * ((diff == 0) ? 0.0f : ((diff == 2) ? 1.0f : ((diff == 1) ? 0.5f : 1.5f)));
    }
    
    public enum pages
    {
        Place, 
        Break, 
        Calc, 
        Render;
    }
    
    public enum SwingMode
    {
        MainHand, 
        OffHand, 
        None;
    }
    
    public enum SequenceMode
    {
        None, 
        Soft, 
        Full;
    }
}
