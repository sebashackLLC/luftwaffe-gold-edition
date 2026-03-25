//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.movement;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import net.minecraft.network.*;
import net.minecraft.client.entity.*;
import net.minecraft.block.*;
import java.util.concurrent.*;
import net.minecraftforge.client.event.*;
import me.alpha432.oyvey.impl.features.modules.client.*;
import net.minecraftforge.fml.common.eventhandler.*;
import net.minecraft.world.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.impl.features.modules.combat.*;
import me.alpha432.oyvey.api.util.math.*;
import me.alpha432.oyvey.impl.events.*;
import me.alpha432.oyvey.api.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.util.math.*;
import java.util.*;

public class FakeLag extends Module
{
    private static FakeLag INSTANCE;
    private final Setting<Boolean> cPacketPlayer;
    private final Setting<Mode> autoOff;
    private final Setting<Integer> timeLimit;
    private final Setting<Integer> packetLimit;
    private final Setting<Float> distance;
    private final Setting<Boolean> render;
    private final Setting<Integer> x;
    private final Setting<Integer> y;
    public final Setting<Boolean> blockWarning;
    private final Setting<Integer> x2;
    private final Setting<Integer> y2;
    private final Timer timer;
    private final Queue<Packet<?>> packets;
    private EntityOtherPlayerMP entity;
    private int packetsCanceled;
    private BlockPos startPos;
    boolean wasenabled;
    boolean wasenabled2;
    private final List<BlockPos> playerHitboxBlocks;
    private final Map<BlockPos, Block> blockStates;
    private boolean shouldRenderWarning;
    
    public FakeLag() {
        super("FakeLag", "LC Break", Module.Category.MOVEMENT, true, false, false);
        this.cPacketPlayer = (Setting<Boolean>)this.register(new Setting("CPacketPlayer", (Object)true));
        this.autoOff = (Setting<Mode>)this.register(new Setting("AutoOff", (Object)Mode.MANUAL));
        this.timeLimit = (Setting<Integer>)this.register(new Setting("Time", (Object)20, (Object)1, (Object)500, v -> this.autoOff.getValue() == Mode.TIME));
        this.packetLimit = (Setting<Integer>)this.register(new Setting("Packets", (Object)20, (Object)1, (Object)500, v -> this.autoOff.getValue() == Mode.PACKETS));
        this.distance = (Setting<Float>)this.register(new Setting("Distance", (Object)10.0f, (Object)1.0f, (Object)100.0f, v -> this.autoOff.getValue() == Mode.DISTANCE));
        this.render = (Setting<Boolean>)this.register(new Setting("Render Text", (Object)true));
        this.x = (Setting<Integer>)this.register(new Setting("Position X", (Object)600, (Object)1, (Object)1000, v -> (boolean)this.render.getValue()));
        this.y = (Setting<Integer>)this.register(new Setting("Position Y", (Object)600, (Object)1, (Object)1000, v -> (boolean)this.render.getValue()));
        this.blockWarning = (Setting<Boolean>)this.register(new Setting("WayBlockWarn", (Object)true, v -> (boolean)this.render.getValue()));
        this.x2 = (Setting<Integer>)this.register(new Setting("Position X", (Object)625, (Object)1, (Object)1000, v -> (boolean)this.render.getValue()));
        this.y2 = (Setting<Integer>)this.register(new Setting("Position Y", (Object)610, (Object)1, (Object)1000, v -> (boolean)this.render.getValue()));
        this.timer = new Timer();
        this.packets = new ConcurrentLinkedQueue<Packet<?>>();
        this.packetsCanceled = 0;
        this.startPos = null;
        this.playerHitboxBlocks = new ArrayList<BlockPos>();
        this.blockStates = new HashMap<BlockPos, Block>();
        this.shouldRenderWarning = false;
        this.setInstance();
    }
    
    public static FakeLag getInstance() {
        if (FakeLag.INSTANCE == null) {
            FakeLag.INSTANCE = new FakeLag();
        }
        return FakeLag.INSTANCE;
    }
    
    private void setInstance() {
        FakeLag.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onRender(final RenderGameOverlayEvent.Post event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.TEXT) {
            return;
        }
        if (this.render.getValue()) {
            this.renderer.drawString("Currently Blinked", (float)(int)this.x.getValue(), (float)(int)this.y.getValue(), Color.getInstance().syncColor(), true);
        }
        if ((boolean)this.blockWarning.getValue() && (boolean)this.render.getValue() && this.shouldRenderWarning && event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            this.renderBlockChangedWarning();
        }
    }
    
    private void renderBlockChangedWarning() {
        final String warningText = "Blocked";
        this.renderer.drawStringWithShadow(warningText, (float)(int)this.x2.getValue(), (float)(int)this.y2.getValue(), -65536);
    }
    
    public void onEnable() {
        if (!fullNullCheck()) {
            (this.entity = new EntityOtherPlayerMP((World)FakeLag.mc.field_71441_e, FakeLag.mc.field_71439_g.func_146103_bH())).func_82149_j((Entity)FakeLag.mc.field_71439_g);
            this.entity.field_70177_z = FakeLag.mc.field_71439_g.field_70177_z;
            this.entity.field_70759_as = FakeLag.mc.field_71439_g.field_70759_as;
            this.entity.field_71071_by.func_70455_b(FakeLag.mc.field_71439_g.field_71071_by);
            FakeLag.mc.field_71441_e.func_73027_a(6942069, (Entity)this.entity);
            this.startPos = FakeLag.mc.field_71439_g.func_180425_c();
            this.playerHitboxBlocks.clear();
            this.blockStates.clear();
            this.shouldRenderWarning = false;
            this.saveBlockStates();
        }
        else {
            this.disable();
        }
        this.packetsCanceled = 0;
        this.timer.reset();
        if (FutureCA.getInstance().isEnabled() && !(boolean)FutureCA.getInstance().antiFuture.getValue()) {
            FutureCA.getInstance().disable();
            this.wasenabled = true;
        }
        if (PhaseWalk.getInstance().isEnabled() && PhaseWalk.getInstance().isPhasing()) {
            PhaseWalk.getInstance().disable();
            this.wasenabled = true;
            this.wasenabled2 = true;
        }
    }
    
    public void onUpdate() {
        if (!fullNullCheck()) {
            this.addPlayerHitboxBlocks();
            this.checkBlockChanges();
        }
        if (nullCheck() || (this.autoOff.getValue() == Mode.TIME && this.timer.passedS((double)(int)this.timeLimit.getValue())) || (this.autoOff.getValue() == Mode.DISTANCE && this.startPos != null && FakeLag.mc.field_71439_g.func_174818_b(this.startPos) >= MathUtil.square((double)(float)this.distance.getValue())) || (this.autoOff.getValue() == Mode.PACKETS && this.packetsCanceled >= (int)this.packetLimit.getValue())) {
            this.disable();
        }
    }
    
    public void onLogout() {
        if (this.isOn()) {
            this.disable();
        }
    }
    
    @SubscribeEvent
    public void onSendPacket(final PacketEvent.Send event) {
        if (FakeLag.mc.field_71441_e != null && !Util.mc.func_71356_B()) {
            final Object packet = event.getPacket();
            if ((boolean)this.cPacketPlayer.getValue() && packet instanceof CPacketPlayer) {
                event.setCanceled(true);
                this.packets.add((Packet<?>)packet);
                ++this.packetsCanceled;
            }
            if (!(boolean)this.cPacketPlayer.getValue()) {
                if (packet instanceof CPacketChatMessage || packet instanceof CPacketConfirmTeleport || packet instanceof CPacketKeepAlive || packet instanceof CPacketTabComplete || packet instanceof CPacketClientStatus) {
                    return;
                }
                this.packets.add((Packet<?>)packet);
                event.setCanceled(true);
                ++this.packetsCanceled;
            }
        }
    }
    
    public void onDisable() {
        if (!fullNullCheck()) {
            FakeLag.mc.field_71441_e.func_73028_b(6942069);
            while (!this.packets.isEmpty()) {
                FakeLag.mc.field_71439_g.field_71174_a.func_147297_a((Packet)this.packets.poll());
            }
        }
        this.startPos = null;
        this.playerHitboxBlocks.clear();
        this.blockStates.clear();
        this.shouldRenderWarning = false;
        if (this.wasenabled) {
            FutureCA.getInstance().enable();
            this.wasenabled = false;
        }
        if (this.wasenabled2 && PhaseWalk.getInstance().isPhasing()) {
            FutureCA.getInstance().enable();
            this.wasenabled2 = false;
        }
    }
    
    private void addPlayerHitboxBlocks() {
        if (FakeLag.mc.field_71439_g == null) {
            return;
        }
        final AxisAlignedBB bb = FakeLag.mc.field_71439_g.func_174813_aQ();
        final int minX = (int)Math.floor(bb.field_72340_a);
        final int minY = (int)Math.floor(bb.field_72338_b);
        final int minZ = (int)Math.floor(bb.field_72339_c);
        final int maxX = (int)Math.floor(bb.field_72336_d);
        final int maxY = (int)Math.floor(bb.field_72337_e);
        final int maxZ = (int)Math.floor(bb.field_72334_f);
        for (int x = minX; x <= maxX; ++x) {
            for (int y = minY; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    final BlockPos pos = new BlockPos(x, y, z);
                    if (!this.playerHitboxBlocks.contains(pos)) {
                        this.playerHitboxBlocks.add(pos);
                        this.blockStates.put(pos, FakeLag.mc.field_71441_e.func_180495_p(pos).func_177230_c());
                    }
                }
            }
        }
    }
    
    private void saveBlockStates() {
        for (final BlockPos pos : this.playerHitboxBlocks) {
            this.blockStates.put(pos, FakeLag.mc.field_71441_e.func_180495_p(pos).func_177230_c());
        }
    }
    
    private void checkBlockChanges() {
        if (FakeLag.mc.field_71441_e == null) {
            return;
        }
        boolean blockChanged = false;
        for (final BlockPos pos : this.playerHitboxBlocks) {
            final Block currentBlock = FakeLag.mc.field_71441_e.func_180495_p(pos).func_177230_c();
            final Block savedBlock = this.blockStates.get(pos);
            if (savedBlock != null && currentBlock != savedBlock) {
                blockChanged = true;
                break;
            }
        }
        this.shouldRenderWarning = blockChanged;
    }
    
    public List<BlockPos> getPlayerHitboxBlocks() {
        return new ArrayList<BlockPos>(this.playerHitboxBlocks);
    }
    
    public String getDisplayInfo() {
        if (this.packets != null) {
            return this.packets.size() + "";
        }
        return null;
    }
    
    static {
        FakeLag.INSTANCE = new FakeLag();
    }
    
    public enum Mode
    {
        MANUAL, 
        TIME, 
        DISTANCE, 
        PACKETS;
    }
}
