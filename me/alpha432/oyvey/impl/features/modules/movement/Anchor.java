//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.movement;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.events.*;
import me.alpha432.oyvey.api.util.*;
import me.alpha432.oyvey.api.util.world.*;
import net.minecraft.init.*;
import net.minecraft.world.*;
import me.alpha432.oyvey.api.util.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.block.state.*;
import me.alpha432.oyvey.api.util.world.hole.holeesp.*;
import net.minecraft.util.math.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Anchor extends Module
{
    private final Setting<Integer> pitch;
    private final Setting<Boolean> doubles;
    private final Setting<Boolean> stopMotion;
    private final Setting<Boolean> pullDown;
    private final Setting<Boolean> terrain;
    protected boolean anchoring;
    
    public Anchor() {
        super("Anchor", "Pulls you down into holes", Module.Category.MOVEMENT, true, false, false);
        this.pitch = (Setting<Integer>)new Setting("Pitch", (Object)37, (Object)0, (Object)90);
        this.doubles = (Setting<Boolean>)new Setting("Doubles", (Object)true);
        this.stopMotion = (Setting<Boolean>)new Setting("StopMotion", (Object)false);
        this.pullDown = (Setting<Boolean>)new Setting("PullDown", (Object)false);
        this.terrain = (Setting<Boolean>)new Setting("Terrain", (Object)false);
        this.register((Setting)this.pitch);
        this.register((Setting)this.doubles);
        this.register((Setting)this.stopMotion);
        this.register((Setting)this.pullDown);
        this.register((Setting)this.terrain);
        this.anchoring = false;
    }
    
    public void onEnable() {
        this.anchoring = false;
    }
    
    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (NullUtils.isPlayerNull()) {
            return;
        }
        if (Anchor.mc.field_71439_g.field_70125_A < (int)this.pitch.getValue()) {
            this.anchoring = false;
            return;
        }
        final BlockPos playerPos = PositionUtil.getPosition();
        final IBlockState headState = Anchor.mc.field_71441_e.func_180495_p(playerPos.func_177984_a());
        boolean head = false;
        if (headState.func_177230_c() != Blocks.field_150350_a) {
            head = (headState.func_185900_c((IBlockAccess)Anchor.mc.field_71441_e, playerPos).field_72337_e > Anchor.mc.field_71439_g.field_70163_u);
        }
        if (EntityUtil.isBurrow((EntityPlayer)Anchor.mc.field_71439_g) || HoleUtil.isHole(PositionUtil.getPosition()) || head) {
            this.anchoring = false;
            return;
        }
        for (int height = 5, i = 0; i <= height; ++i) {
            if (Anchor.mc.field_71441_e.func_175623_d(playerPos.func_177979_c(i + 1))) {
                this.anchoring = false;
            }
            else {
                final Hole hole = HoleUtil.getHole(playerPos.func_177979_c(i), (boolean)this.doubles.getValue(), (boolean)this.terrain.getValue());
                if (hole == null) {
                    this.anchoring = false;
                    break;
                }
                final Vec3d vec = HoleUtil.getCenter(hole);
                if (this.stopMotion.getValue()) {
                    Anchor.mc.field_71439_g.field_70159_w = 0.0;
                    Anchor.mc.field_71439_g.field_70179_y = 0.0;
                    Anchor.mc.field_71439_g.field_191988_bg = 0.0f;
                    Anchor.mc.field_71439_g.field_70702_br = 0.0f;
                }
                if (Anchor.mc.field_71439_g.field_70181_x > -0.1 && (boolean)this.pullDown.getValue()) {
                    Anchor.mc.field_71439_g.field_70181_x = -0.1;
                }
                final double xSpeed = vec.field_72450_a - Anchor.mc.field_71439_g.field_70165_t;
                final double zSpeed = vec.field_72449_c - Anchor.mc.field_71439_g.field_70161_v;
                event.setX(xSpeed / 2.0);
                event.setZ(zSpeed / 2.0);
                this.anchoring = true;
            }
        }
    }
    
    public boolean isAnchoring() {
        return this.isEnabled() && this.anchoring;
    }
}
