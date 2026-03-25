//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.combat;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.impl.features.modules.movement.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import net.minecraft.network.*;
import net.minecraft.client.network.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.network.play.server.*;
import net.minecraft.client.entity.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.block.*;
import me.alpha432.oyvey.impl.command.*;
import me.alpha432.oyvey.api.util.world.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.state.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraft.network.play.client.*;
import net.minecraft.init.*;
import net.minecraft.potion.*;
import net.minecraft.item.*;
import java.util.*;
import com.google.common.collect.*;

public class SelfFill extends Module
{
    private static SelfFill INSTANCE;
    private volatile double last_x;
    private volatile double last_y;
    private volatile double last_z;
    private Setting<OffsetMode> offsetMode;
    public static final Set<Block> BAD_BLOCKS;
    public static final Set<Block> SHULKERS;
    public Setting<Float> vClip;
    public Setting<Float> minDown;
    public Setting<Float> maxDown;
    public Setting<Float> minUp;
    public Setting<Float> maxUp;
    public Setting<Float> scaleFactor;
    public Setting<Integer> scaleDelay;
    public Setting<Integer> cooldown;
    public Setting<Integer> delay;
    public Setting<Boolean> scaleDown;
    public Setting<Boolean> scaleVelocity;
    public Setting<Boolean> scaleExplosion;
    public Setting<Boolean> attackBefore;
    public Setting<Boolean> antiWeakness;
    public Setting<Boolean> attack;
    public Setting<Boolean> surroundDisable;
    public Setting<Boolean> deltaY;
    public Setting<Boolean> placeDisable;
    public Setting<Boolean> wait;
    public Setting<Boolean> highBlock;
    public Setting<Boolean> evade;
    public Setting<Boolean> noVoid;
    public Setting<Boolean> conflict;
    public Setting<Boolean> onGround;
    public Setting<Boolean> allowUp;
    public Setting<Boolean> beacon;
    public Setting<Boolean> echest;
    public Setting<Boolean> anvil;
    public Setting<Boolean> rotate;
    public Setting<Boolean> discrete;
    public Setting<Boolean> air;
    public Setting<Boolean> fallback;
    public Setting<Boolean> skipZero;
    protected final Timer scaleTimer;
    protected final Timer timer;
    protected double motionY;
    protected BlockPos startPos;
    
    public SelfFill() {
        super("SelfFill", "Placing block in your legs", Module.Category.COMBAT, true, false, false);
        this.offsetMode = (Setting<OffsetMode>)this.register(new Setting("Mode", (Object)OffsetMode.Smart));
        this.vClip = (Setting<Float>)new Setting("V-Clip", (Object)(-9.0f), (Object)(-256.0f), (Object)256.0f);
        this.minDown = (Setting<Float>)new Setting("Min-Down", (Object)3.0f, (Object)0.0f, (Object)1337.0f);
        this.maxDown = (Setting<Float>)new Setting("Max-Down", (Object)10.0f, (Object)0.0f, (Object)1337.0f);
        this.minUp = (Setting<Float>)new Setting("Min-Up", (Object)3.0f, (Object)0.0f, (Object)1337.0f);
        this.maxUp = (Setting<Float>)new Setting("Max-Up", (Object)10.0f, (Object)0.0f, (Object)1337.0f);
        this.scaleFactor = (Setting<Float>)new Setting("Scale-Factor", (Object)1.0f, (Object)0.1f, (Object)10.0f);
        this.scaleDelay = (Setting<Integer>)new Setting("Scale-Delay", (Object)250, (Object)0, (Object)1000);
        this.cooldown = (Setting<Integer>)new Setting("Cooldown", (Object)500, (Object)0, (Object)500);
        this.delay = (Setting<Integer>)new Setting("Delay", (Object)100, (Object)0, (Object)1000);
        this.scaleDown = (Setting<Boolean>)new Setting("Scale-Down", (Object)false);
        this.scaleVelocity = (Setting<Boolean>)new Setting("Scale-Velocity", (Object)false);
        this.scaleExplosion = (Setting<Boolean>)new Setting("Scale-Explosion", (Object)false);
        this.attackBefore = (Setting<Boolean>)new Setting("Attack-Before", (Object)true);
        this.antiWeakness = (Setting<Boolean>)this.register(new Setting("AntiWeakness", (Object)false));
        this.attack = (Setting<Boolean>)this.register(new Setting("Attack", (Object)false));
        this.deltaY = (Setting<Boolean>)new Setting("Delta-Y", (Object)true);
        this.placeDisable = (Setting<Boolean>)new Setting("AutoDisable", (Object)false);
        this.wait = (Setting<Boolean>)new Setting("Wait", (Object)true);
        this.highBlock = (Setting<Boolean>)new Setting("HighBlock", (Object)false);
        this.evade = (Setting<Boolean>)new Setting("Evade", (Object)false);
        this.noVoid = (Setting<Boolean>)new Setting("NoVoid", (Object)false);
        this.conflict = (Setting<Boolean>)new Setting("Conflict", (Object)true);
        this.onGround = (Setting<Boolean>)new Setting("OnGround", (Object)true);
        this.allowUp = (Setting<Boolean>)new Setting("Allow-Up", (Object)false);
        this.beacon = (Setting<Boolean>)new Setting("Beacon", (Object)false);
        this.echest = (Setting<Boolean>)this.register(new Setting("EChest", (Object)false));
        this.anvil = (Setting<Boolean>)new Setting("Anvil", (Object)false);
        this.rotate = (Setting<Boolean>)this.register(new Setting("Rotate", (Object)true));
        this.discrete = (Setting<Boolean>)new Setting("Discrete", (Object)true);
        this.air = (Setting<Boolean>)new Setting("Air", (Object)false);
        this.fallback = (Setting<Boolean>)new Setting("Fallback", (Object)true);
        this.skipZero = (Setting<Boolean>)new Setting("SkipZero", (Object)true);
        this.surroundDisable = (Setting<Boolean>)this.register(new Setting("FutureSurroundDisable", (Object)false));
        this.scaleTimer = new Timer();
        this.timer = new Timer();
    }
    
    public static SelfFill getInstance() {
        if (SelfFill.INSTANCE == null) {
            SelfFill.INSTANCE = new SelfFill();
        }
        return SelfFill.INSTANCE;
    }
    
    private void setInstance() {
        SelfFill.INSTANCE = this;
    }
    
    public void onEnable() {
        this.timer.reset();
        if (SelfFill.mc.field_71441_e != null && SelfFill.mc.field_71439_g != null) {
            this.startPos = this.getPlayerPos();
        }
        if (FakeLag.getInstance().isEnabled()) {
            FakeLag.getInstance().disable();
        }
        if (this.surroundDisable.getValue()) {
            SelfFill.mc.field_71439_g.func_71165_d((String)Sync.getInstance().futurePrefix.getValue() + "toggle feettrap false");
        }
    }
    
    protected void attack(final Packet<?> attacking, final int slot) {
        if (slot != -1) {
            switchToHotbarSlot(slot, true);
        }
        send(attacking);
        this.swing(EnumHand.MAIN_HAND);
    }
    
    public static void send(final Packet<?> packet) {
        final NetHandlerPlayClient connection = SelfFill.mc.func_147114_u();
        if (connection != null) {
            connection.func_147297_a((Packet)packet);
        }
    }
    
    public void swing(final EnumHand hand) {
        swingPacket(hand);
    }
    
    public static void swingPacket(final EnumHand hand) {
        Objects.requireNonNull(SelfFill.mc.func_147114_u()).func_147297_a((Packet)new CPacketAnimation(hand));
    }
    
    public static void switchToHotbarSlot(final int slot, final boolean silent) {
        if (SelfFill.mc.field_71439_g.field_71071_by.field_70461_c != slot && slot >= 0) {
            if (silent) {
                SelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
                SelfFill.mc.field_71442_b.func_78765_e();
            }
            else {
                SelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketHeldItemChange(slot));
                SelfFill.mc.field_71439_g.field_71071_by.field_70461_c = slot;
                SelfFill.mc.field_71442_b.func_78765_e();
            }
        }
    }
    
    @SubscribeEvent
    public void onPacketReceive(final PacketEvent.Receive event) {
        if (event.getStage() == 0) {
            if (event.getPacket() instanceof SPacketExplosion && (boolean)this.scaleExplosion.getValue()) {
                this.motionY = ((SPacketExplosion)event.getPacket()).func_149144_d();
                this.scaleTimer.reset();
            }
            if (event.getPacket() instanceof SPacketExplosion) {
                if (this.scaleVelocity.getValue()) {
                    return;
                }
                final EntityPlayerSP playerSP = SelfFill.mc.field_71439_g;
                if (playerSP != null) {
                    this.motionY = ((SPacketExplosion)event.getPacket()).func_149144_d() / 8000.0;
                    this.scaleTimer.reset();
                }
            }
            if (event.getPacket() instanceof SPacketPlayerPosLook) {
                final SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
                double x = packet.func_148932_c();
                double y = packet.func_148928_d();
                double z = packet.func_148933_e();
                if (packet.func_179834_f().contains(SPacketPlayerPosLook.EnumFlags.X)) {
                    x += SelfFill.mc.field_71439_g.field_70165_t;
                }
                if (packet.func_179834_f().contains(SPacketPlayerPosLook.EnumFlags.Y)) {
                    y += SelfFill.mc.field_71439_g.field_70163_u;
                }
                if (packet.func_179834_f().contains(SPacketPlayerPosLook.EnumFlags.Z)) {
                    z += SelfFill.mc.field_71439_g.field_70161_v;
                }
                this.last_x = MathHelper.func_151237_a(x, -3.0E7, 3.0E7);
                this.last_y = y;
                this.last_z = MathHelper.func_151237_a(z, -3.0E7, 3.0E7);
            }
        }
    }
    
    public void onUpdate() {
        if (this.wait.getValue()) {
            final BlockPos currentPos = this.getPlayerPos();
            if (!currentPos.equals((Object)this.startPos)) {
                this.disable();
                return;
            }
        }
        if (!this.isInsideBlock()) {
            final EntityPlayer rEntity = (EntityPlayer)SelfFill.mc.field_71439_g;
            final BlockPos pos = getPosition((Entity)rEntity);
            if (!SelfFill.mc.field_71441_e.func_180495_p(pos).func_185904_a().func_76222_j()) {
                if (!(boolean)this.wait.getValue()) {
                    this.disable();
                }
            }
            else {
                final BlockPos posHead = getPosition((Entity)rEntity).func_177984_a().func_177984_a();
                if (SelfFill.mc.field_71441_e.func_180495_p(posHead).func_185904_a().func_76222_j() || !(boolean)this.wait.getValue()) {
                    final CPacketUseEntity attacking = null;
                    boolean crystals = false;
                    final float currentDmg = Float.MAX_VALUE;
                    for (final Entity entity : SelfFill.mc.field_71441_e.func_72872_a((Class)Entity.class, new AxisAlignedBB(pos))) {
                        if (entity != null && !SelfFill.mc.field_71439_g.equals((Object)entity) && entity.func_70089_S()) {
                            if (!(entity instanceof EntityEnderCrystal) || !(boolean)this.attack.getValue()) {
                                if (!(boolean)this.wait.getValue()) {
                                    this.disable();
                                }
                                return;
                            }
                            EntityUtil.attackEntity(entity, true, true);
                            crystals = true;
                        }
                    }
                    int weaknessSlot = -1;
                    if (crystals) {
                        if (attacking == null) {
                            if (!(boolean)this.wait.getValue()) {
                                this.disable();
                            }
                            return;
                        }
                        if (!canBreakWeakness(true) && (!(boolean)this.antiWeakness.getValue() || (int)this.cooldown.getValue() != 0 || (weaknessSlot = findAntiWeakness()) == -1)) {
                            if (!(boolean)this.wait.getValue()) {
                                this.disable();
                            }
                            return;
                        }
                    }
                    if (!(boolean)this.allowUp.getValue()) {
                        final BlockPos upUp = pos.func_177981_b(2);
                        final IBlockState upState = SelfFill.mc.field_71441_e.func_180495_p(upUp);
                        if (upState.func_185904_a().func_76230_c()) {
                            if (!(boolean)this.wait.getValue()) {
                                this.disable();
                            }
                            return;
                        }
                    }
                    boolean useEchest = false;
                    int slot;
                    if (this.anvil.getValue()) {
                        slot = InventoryUtil.findHotbarBlock(Blocks.field_150467_bQ);
                    }
                    else if (this.beacon.getValue()) {
                        slot = InventoryUtil.findHotbarBlock((Block)Blocks.field_150461_bJ);
                    }
                    else if ((boolean)this.echest.getValue() || SelfFill.mc.field_71441_e.func_180495_p(pos.func_177977_b()).func_177230_c() == Blocks.field_150477_bB) {
                        slot = InventoryUtil.findHotbarBlock(Blocks.field_150477_bB);
                        useEchest = true;
                    }
                    else {
                        slot = InventoryUtil.findHotbarBlock((Class)BlockObsidian.class);
                        if (slot == -1) {
                            slot = InventoryUtil.findHotbarBlock(Blocks.field_150477_bB);
                            useEchest = (slot != -1);
                        }
                    }
                    if (slot == -1) {
                        Command.sendMessage("No Block found!");
                        return;
                    }
                    final EnumFacing f = BlockUtil.getFacing(pos);
                    if (f == null) {
                        if (!(boolean)this.wait.getValue()) {
                            this.disable();
                        }
                        return;
                    }
                    final double y = this.applyScale(this.getY((Entity)rEntity, (OffsetMode)this.offsetMode.getValue()));
                    if (Double.isNaN(y)) {
                        return;
                    }
                    final BlockPos on = pos.func_177972_a(f);
                    final float[] r = getRotations(on, f.func_176734_d(), (Entity)rEntity);
                    final RayTraceResult result = getRayTraceResultWithEntity(r[0], r[1], (Entity)rEntity);
                    final float[] vec = hitVecToPlaceVec(on, result.field_72307_f);
                    final boolean sneaking = !shouldSneak(on, true);
                    if (this.singlePlayerCheck(pos, useEchest)) {
                        if (!(boolean)this.wait.getValue() || (boolean)this.placeDisable.getValue()) {
                            this.disable();
                        }
                        return;
                    }
                    final int lastSlot = SelfFill.mc.field_71439_g.field_71071_by.field_70461_c;
                    if ((boolean)this.attackBefore.getValue() && attacking != null) {
                        this.attack((Packet<?>)attacking, weaknessSlot);
                    }
                    if ((boolean)this.conflict.getValue() || (boolean)this.rotate.getValue()) {
                        if (this.rotate.getValue()) {
                            if (rEntity.func_174791_d().equals((Object)this.getVec())) {
                                doRotation(r[0], r[1], true);
                            }
                            else {
                                doPosRot(rEntity.field_70165_t, rEntity.field_70163_u, rEntity.field_70161_v, r[0], r[1], true);
                            }
                        }
                        else {
                            doPosition(rEntity.field_70165_t, rEntity.field_70163_u, rEntity.field_70161_v, true);
                        }
                    }
                    doY((Entity)rEntity, rEntity.field_70163_u + 0.42, (boolean)this.onGround.getValue());
                    doY((Entity)rEntity, rEntity.field_70163_u + 0.75, (boolean)this.onGround.getValue());
                    doY((Entity)rEntity, rEntity.field_70163_u + 1.01, (boolean)this.onGround.getValue());
                    doY((Entity)rEntity, rEntity.field_70163_u + 1.16, (boolean)this.onGround.getValue());
                    InventoryUtil.switchToHotbarSlot(slot, false);
                    if (!sneaking) {
                        SelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)SelfFill.mc.field_71439_g, CPacketEntityAction.Action.START_SNEAKING));
                    }
                    place(on, f.func_176734_d(), slot, vec[0], vec[1], vec[2]);
                    if (this.highBlock.getValue()) {
                        doY((Entity)rEntity, rEntity.field_70163_u + 1.67, (boolean)this.onGround.getValue());
                        doY((Entity)rEntity, rEntity.field_70163_u + 2.01, (boolean)this.onGround.getValue());
                        doY((Entity)rEntity, rEntity.field_70163_u + 2.42, (boolean)this.onGround.getValue());
                        final BlockPos highPos = pos.func_177984_a();
                        final EnumFacing face = EnumFacing.DOWN;
                        place(highPos.func_177972_a(face), face.func_176734_d(), slot, vec[0], vec[1], vec[2]);
                    }
                    swing(slot);
                    InventoryUtil.switchToHotbarSlot(lastSlot, false);
                    if (!sneaking) {
                        SelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketEntityAction((Entity)SelfFill.mc.field_71439_g, CPacketEntityAction.Action.STOP_SNEAKING));
                    }
                    doY((Entity)rEntity, y, false);
                    this.timer.reset();
                    if (!(boolean)this.wait.getValue() || (boolean)this.placeDisable.getValue()) {
                        this.disable();
                    }
                }
            }
        }
    }
    
    public static void swing(final int slot) {
        SelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketAnimation(getHand(slot)));
    }
    
    public Vec3d getVec() {
        return new Vec3d(this.last_x, this.last_y, this.last_z);
    }
    
    public static float[] getRotations(final BlockPos pos, final EnumFacing facing, final Entity from) {
        return getRotations(pos, facing, from, (IBlockAccess)SelfFill.mc.field_71441_e, SelfFill.mc.field_71441_e.func_180495_p(pos));
    }
    
    public static float[] getRotations(final BlockPos pos, final EnumFacing facing, final Entity from, final IBlockAccess world, final IBlockState state) {
        final AxisAlignedBB bb = state.func_185900_c(world, pos);
        double x = pos.func_177958_n() + (bb.field_72340_a + bb.field_72336_d) / 2.0;
        double y = pos.func_177956_o() + (bb.field_72338_b + bb.field_72337_e) / 2.0;
        double z = pos.func_177952_p() + (bb.field_72339_c + bb.field_72334_f) / 2.0;
        if (facing != null) {
            x += facing.func_176730_m().func_177958_n() * ((bb.field_72340_a + bb.field_72336_d) / 2.0);
            y += facing.func_176730_m().func_177956_o() * ((bb.field_72338_b + bb.field_72337_e) / 2.0);
            z += facing.func_176730_m().func_177952_p() * ((bb.field_72339_c + bb.field_72334_f) / 2.0);
        }
        return getRotations(x, y, z, from);
    }
    
    public static float[] getRotations(final double x, final double y, final double z, final Entity f) {
        return getRotations(x, y, z, f.field_70165_t, f.field_70163_u, f.field_70161_v, f.func_70047_e());
    }
    
    public static float[] getRotations(final double x, final double y, final double z, final double fromX, final double fromY, final double fromZ, final float fromHeight) {
        final double xDiff = x - fromX;
        final double yDiff = y - (fromY + fromHeight);
        final double zDiff = z - fromZ;
        final double dist = MathHelper.func_76133_a(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        final float prevYaw = SelfFill.mc.field_71439_g.field_70177_z;
        float diff = yaw - prevYaw;
        if (diff < -180.0f || diff > 180.0f) {
            final float round = (float)Math.round(Math.abs(diff / 360.0f));
            diff = ((diff < 0.0f) ? (diff + 360.0f * round) : (diff - 360.0f * round));
        }
        return new float[] { prevYaw + diff, pitch };
    }
    
    public static void doRotation(final float yaw, final float pitch, final boolean onGround) {
        SelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)rotation(yaw, pitch, onGround));
    }
    
    public static CPacketPlayer rotation(final float yaw, final float pitch, final boolean onGround) {
        return (CPacketPlayer)new CPacketPlayer.Rotation(yaw, pitch, onGround);
    }
    
    protected double getY(final Entity entity, final OffsetMode mode) {
        if (mode == OffsetMode.Constant) {
            double d = entity.field_70163_u + (float)this.vClip.getValue();
            if ((boolean)this.evade.getValue() && Math.abs(d) < 1.0) {
                d = -1.0;
            }
            return d;
        }
        double d = this.getY(entity, (float)this.minDown.getValue(), (float)this.maxDown.getValue(), true);
        if (Double.isNaN(d)) {
            d = this.getY(entity, -(float)this.minUp.getValue(), -(float)this.maxUp.getValue(), false);
            if (Double.isNaN(d) && (boolean)this.fallback.getValue()) {
                return this.getY(entity, OffsetMode.Constant);
            }
        }
        return d;
    }
    
    public static void doY(final Entity entity, final double y, final boolean onGround) {
        doPosition(entity.field_70165_t, y, entity.field_70161_v, onGround);
    }
    
    public static void doPosition(final double x, final double y, final double z, final boolean onGround) {
        SelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)position(x, y, z, onGround));
    }
    
    public static CPacketPlayer position(final double x, final double y, final double z) {
        return position(x, y, z, SelfFill.mc.field_71439_g.field_70122_E);
    }
    
    public static CPacketPlayer position(final double x, final double y, final double z, final boolean onGround) {
        return (CPacketPlayer)new CPacketPlayer.Position(x, y, z, onGround);
    }
    
    public static void doPosRot(final double x, final double y, final double z, final float yaw, final float pitch, final boolean onGround) {
        SelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)positionRotation(x, y, z, yaw, pitch, onGround));
    }
    
    public static CPacketPlayer positionRotation(final double x, final double y, final double z, final float yaw, final float pitch, final boolean onGround) {
        return (CPacketPlayer)new CPacketPlayer.PositionRotation(x, y, z, yaw, pitch, onGround);
    }
    
    public static void place(final BlockPos on, final EnumFacing facing, final int slot, final float x, final float y, final float z) {
        try {
            place(on, facing, getHand(slot), x, y, z);
        }
        catch (Exception var7) {
            Command.sendMessage("Failed to place the block");
        }
    }
    
    public static EnumHand getHand(final int slot) {
        return (slot == -2) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }
    
    public static void place(final BlockPos on, final EnumFacing facing, final EnumHand hand, final float x, final float y, final float z) {
        try {
            SelfFill.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayerTryUseItemOnBlock(on, facing, hand, x, y, z));
        }
        catch (Exception var7) {
            Command.sendMessage("Failed to place the block");
        }
    }
    
    protected double getY(final Entity entity, final double min, final double max, final boolean add) {
        if ((min <= max || !add) && (max <= min || add)) {
            final double x = entity.field_70165_t;
            final double y = entity.field_70163_u;
            final double z = entity.field_70161_v;
            boolean air = false;
            double lastOff = 0.0;
            BlockPos last = null;
            double off = min;
            while (true) {
                if (add) {
                    if (off >= max) {
                        break;
                    }
                }
                else if (off <= max) {
                    break;
                }
                final BlockPos pos = new BlockPos(x, y - off, z);
                if (!(boolean)this.noVoid.getValue() || pos.func_177956_o() >= 0) {
                    if ((boolean)this.skipZero.getValue() && Math.abs(y) < 1.0) {
                        air = false;
                        last = pos;
                        lastOff = y - off;
                    }
                    else {
                        final IBlockState state = SelfFill.mc.field_71441_e.func_180495_p(pos);
                        if (((boolean)this.air.getValue() || state.func_185904_a().func_76230_c()) && state.func_177230_c() != Blocks.field_150350_a) {
                            air = false;
                        }
                        else if (air) {
                            if (add) {
                                return this.discrete.getValue() ? pos.func_177956_o() : (y - off);
                            }
                            return this.discrete.getValue() ? last.func_177956_o() : lastOff;
                        }
                        else {
                            air = true;
                        }
                        last = pos;
                        lastOff = y - off;
                    }
                }
                off = (add ? (++off) : (--off));
            }
            return Double.NaN;
        }
        return Double.NaN;
    }
    
    protected double applyScale(double value) {
        if ((value >= SelfFill.mc.field_71439_g.field_70163_u || (boolean)this.scaleDown.getValue()) && ((boolean)this.scaleExplosion.getValue() || (boolean)this.scaleVelocity.getValue()) && !this.scaleTimer.passedMs((long)(int)this.scaleDelay.getValue()) && this.motionY != 0.0) {
            if (value < SelfFill.mc.field_71439_g.field_70163_u) {
                value -= this.motionY * (float)this.scaleFactor.getValue();
            }
            else {
                value += this.motionY * (float)this.scaleFactor.getValue();
            }
            return this.discrete.getValue() ? Math.floor(value) : value;
        }
        return value;
    }
    
    protected BlockPos getPlayerPos() {
        return ((boolean)this.deltaY.getValue() && Math.abs(SelfFill.mc.field_71439_g.field_70181_x) > 0.1) ? new BlockPos((Entity)SelfFill.mc.field_71439_g) : getPosition((Entity)SelfFill.mc.field_71439_g);
    }
    
    public static BlockPos getPosition(final Entity entity) {
        return getPosition(entity, 0.0);
    }
    
    public static BlockPos getPosition(final Entity entity, final double yOffset) {
        double y = entity.field_70163_u + yOffset;
        if (entity.field_70163_u - Math.floor(entity.field_70163_u) > 0.5) {
            y = Math.ceil(entity.field_70163_u);
        }
        return new BlockPos(entity.field_70165_t, y, entity.field_70161_v);
    }
    
    protected boolean isInsideBlock() {
        final double x = SelfFill.mc.field_71439_g.field_70165_t;
        final double y = SelfFill.mc.field_71439_g.field_70163_u + 0.2;
        final double z = SelfFill.mc.field_71439_g.field_70161_v;
        return SelfFill.mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_185904_a().func_76230_c() || !SelfFill.mc.field_71439_g.field_70122_E;
    }
    
    protected boolean singlePlayerCheck(final BlockPos pos, final boolean useEchest) {
        if (!SelfFill.mc.func_71356_B()) {
            return false;
        }
        final EntityPlayer player = (EntityPlayer)SelfFill.mc.func_71401_C().func_184103_al().func_177451_a(SelfFill.mc.field_71439_g.func_110124_au());
        if (player == null) {
            this.disable();
            return true;
        }
        final IBlockState state = useEchest ? Blocks.field_150477_bB.func_176223_P() : Blocks.field_150343_Z.func_176223_P();
        player.func_130014_f_().func_175656_a(pos, state);
        SelfFill.mc.field_71441_e.func_175656_a(pos, state);
        return true;
    }
    
    public static boolean canBreakWeakness(final boolean checkStack) {
        if (!SelfFill.mc.field_71439_g.func_70644_a(MobEffects.field_76437_t)) {
            return true;
        }
        int strengthAmp = 0;
        final PotionEffect effect = SelfFill.mc.field_71439_g.func_70660_b(MobEffects.field_76420_g);
        if (effect != null) {
            strengthAmp = effect.func_76458_c();
        }
        return strengthAmp >= 1 || (checkStack && canBreakWeakness(SelfFill.mc.field_71439_g.func_184614_ca()));
    }
    
    public static boolean canBreakWeakness(final ItemStack stack) {
        return stack.func_77973_b() instanceof ItemSword;
    }
    
    public static boolean shouldSneak(final BlockPos pos, final boolean manager) {
        return shouldSneak(pos, (IBlockAccess)SelfFill.mc.field_71441_e, manager);
    }
    
    public static boolean shouldSneak(final BlockPos pos, final IBlockAccess provider, final boolean manager) {
        return shouldSneak(provider.func_180495_p(pos).func_177230_c(), manager);
    }
    
    public static boolean shouldSneak(final Block block, final boolean manager) {
        return (!manager || !SelfFill.mc.field_71439_g.func_70093_af()) && (SelfFill.BAD_BLOCKS.contains(block) || SelfFill.SHULKERS.contains(block));
    }
    
    public static float[] hitVecToPlaceVec(final BlockPos pos, final Vec3d hitVec) {
        final float x = (float)(hitVec.field_72450_a - pos.func_177958_n());
        final float y = (float)(hitVec.field_72448_b - pos.func_177956_o());
        final float z = (float)(hitVec.field_72449_c - pos.func_177952_p());
        return new float[] { x, y, z };
    }
    
    public static RayTraceResult getRayTraceResultWithEntity(final float yaw, final float pitch, final Entity from) {
        return getRayTraceResult(yaw, pitch, SelfFill.mc.field_71442_b.func_78757_d(), from);
    }
    
    public static RayTraceResult getRayTraceResult(final float yaw, final float pitch, final float d, final Entity from) {
        final Vec3d vec3d = getEyePos(from);
        final Vec3d lookVec = getVec3d(yaw, pitch);
        final Vec3d rotations = vec3d.func_72441_c(lookVec.field_72450_a * d, lookVec.field_72448_b * d, lookVec.field_72449_c * d);
        final RayTraceResult rayTraceResult;
        return Optional.ofNullable(SelfFill.mc.field_71441_e.func_147447_a(vec3d, rotations, false, false, false)).orElseGet(() -> {
            new RayTraceResult(RayTraceResult.Type.MISS, new Vec3d(0.5, 1.0, 0.5), EnumFacing.UP, BlockPos.field_177992_a);
            return rayTraceResult;
        });
    }
    
    public static Vec3d getVec3d(final float yaw, final float pitch) {
        final float vx = -MathHelper.func_76126_a(rad(yaw)) * MathHelper.func_76134_b(rad(pitch));
        final float vz = MathHelper.func_76134_b(rad(yaw)) * MathHelper.func_76134_b(rad(pitch));
        final float vy = -MathHelper.func_76126_a(rad(pitch));
        return new Vec3d((double)vx, (double)vy, (double)vz);
    }
    
    public static float rad(final float angle) {
        return (float)(angle * 3.141592653589793 / 180.0);
    }
    
    public static Vec3d getEyePos(final Entity entity) {
        return new Vec3d(entity.field_70165_t, getEyeHeight(entity), entity.field_70161_v);
    }
    
    public static double getEyeHeight() {
        return getEyeHeight((Entity)SelfFill.mc.field_71439_g);
    }
    
    public static double getEyeHeight(final Entity entity) {
        return entity.field_70163_u + entity.func_70047_e();
    }
    
    public static int findAntiWeakness() {
        int slot = -1;
        for (int i = 8; i > -1 && (!canBreakWeakness(SelfFill.mc.field_71439_g.field_71071_by.func_70301_a(i)) || SelfFill.mc.field_71439_g.field_71071_by.field_70461_c != (slot = i)); --i) {}
        return slot;
    }
    
    List<BlockPos> getOverlapPos() {
        final ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        final double decimalX = SelfFill.mc.field_71439_g.field_70165_t - Math.floor(SelfFill.mc.field_71439_g.field_70165_t);
        final double decimalZ = SelfFill.mc.field_71439_g.field_70161_v - Math.floor(SelfFill.mc.field_71439_g.field_70161_v);
        final int offX = this.calcOffset(decimalX);
        final int offZ = this.calcOffset(decimalZ);
        positions.add(this.getPlayerPos());
        for (int x = 0; x <= Math.abs(offX); ++x) {
            for (int z = 0; z <= Math.abs(offZ); ++z) {
                final int properX = x * offX;
                final int properZ = z * offZ;
                positions.add(this.getPlayerPos().func_177982_a(properX, 0, properZ));
            }
        }
        return positions;
    }
    
    int calcOffset(final double dec) {
        return (dec >= 0.7) ? 1 : ((dec <= 0.3) ? -1 : 0);
    }
    
    static {
        SelfFill.INSTANCE = new SelfFill();
        BAD_BLOCKS = Sets.newHashSet((Object[])new Block[] { Blocks.field_150477_bB, Blocks.field_150381_bn, (Block)Blocks.field_150486_ae, Blocks.field_150447_bR, Blocks.field_150467_bQ, Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA });
        SHULKERS = Sets.newHashSet((Object[])new Block[] { Blocks.field_190977_dl, Blocks.field_190978_dm, Blocks.field_190979_dn, Blocks.field_190980_do, Blocks.field_190981_dp, Blocks.field_190982_dq, Blocks.field_190983_dr, Blocks.field_190984_ds, Blocks.field_190985_dt, Blocks.field_190986_du, Blocks.field_190987_dv, Blocks.field_190988_dw, Blocks.field_190989_dx, Blocks.field_190990_dy, Blocks.field_190991_dz, Blocks.field_190975_dA });
    }
    
    public enum OffsetMode
    {
        Constant, 
        Smart;
    }
    
    private static class PopCounter
    {
        private final Timer timer;
        private int pops;
        
        private PopCounter() {
            this.timer = new Timer();
        }
        
        public int getPops() {
            return this.pops;
        }
        
        public void pop() {
            this.timer.reset();
            ++this.pops;
        }
        
        public void reset() {
            this.pops = 0;
        }
        
        public long lastPop() {
            return this.timer.getPassedTimeMs();
        }
    }
}
