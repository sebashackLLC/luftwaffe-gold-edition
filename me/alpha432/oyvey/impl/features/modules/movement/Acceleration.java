//Decompiled by Procyon!

package me.alpha432.oyvey.impl.features.modules.movement;

import me.alpha432.oyvey.api.features.*;
import me.alpha432.oyvey.api.features.settings.*;
import me.alpha432.oyvey.impl.events.*;
import me.alpha432.oyvey.api.util.entity.*;
import me.alpha432.oyvey.api.managers.*;
import net.minecraft.util.*;
import java.lang.reflect.*;
import net.minecraftforge.fml.common.eventhandler.*;

public class Acceleration extends Module
{
    private static Acceleration INSTANCE;
    public final Setting<Boolean> noBedrock;
    public final Setting<Boolean> noSlide;
    public final Setting<Boolean> onlyGround;
    public final Setting<Boolean> boost;
    public final Setting<BoostMode> boostMode;
    public final Setting<Double> boostMultiplier;
    
    public Acceleration() {
        super("Acceleration", "Removes acceleration when you are moving", Module.Category.MOVEMENT, true, false, false);
        this.noBedrock = (Setting<Boolean>)this.register(new Setting("NoBedrock", (Object)false));
        this.noSlide = (Setting<Boolean>)this.register(new Setting("NoSlide", (Object)true));
        this.onlyGround = (Setting<Boolean>)this.register(new Setting("OnlyGround", (Object)true));
        this.boost = (Setting<Boolean>)this.register(new Setting("Boost", (Object)false));
        this.boostMode = (Setting<BoostMode>)this.register(new Setting("BoostMode", (Object)BoostMode.REPLACE));
        this.boostMultiplier = (Setting<Double>)this.register(new Setting("BoostMultiplier", (Object)1.5, (Object)0.1, (Object)5.0));
    }
    
    public static Acceleration getInstance() {
        if (Acceleration.INSTANCE == null) {
            Acceleration.INSTANCE = new Acceleration();
        }
        return Acceleration.INSTANCE;
    }
    
    private void setInstance() {
        Acceleration.INSTANCE = this;
    }
    
    @SubscribeEvent
    public void onMoveEvent(final MoveEvent event) {
        if (!HoleSnap.getInstance().isEnabled()) {
            if ((boolean)this.onlyGround.getValue() && !EntityUtil.isOnGround()) {
                return;
            }
            if ((Acceleration.mc.field_71439_g.field_70163_u >= 7.0 || !(boolean)this.noBedrock.getValue()) && event.getStage() == 0 && !Acceleration.mc.field_71439_g.func_70093_af() && !Acceleration.mc.field_71439_g.func_70090_H() && !Acceleration.mc.field_71439_g.func_180799_ab() && !Acceleration.mc.field_71439_g.func_184218_aH()) {
                final MovementInput movementInput = Acceleration.mc.field_71439_g.field_71158_b;
                float moveForward = movementInput.field_192832_b;
                float moveStrafe = movementInput.field_78902_a;
                float rotationYaw = Acceleration.mc.field_71439_g.field_70177_z;
                if (moveForward == 0.0f && moveStrafe == 0.0f) {
                    if (this.noSlide.getValue()) {
                        event.setX(0.0);
                        event.setZ(0.0);
                    }
                }
                else {
                    if (moveForward != 0.0f) {
                        if (moveStrafe > 0.0f) {
                            rotationYaw += ((moveForward > 0.0f) ? -45 : 45);
                        }
                        else if (moveStrafe < 0.0f) {
                            rotationYaw += ((moveForward > 0.0f) ? 45 : -45);
                        }
                        moveStrafe = 0.0f;
                        if (moveForward != 0.0f) {
                            moveForward = ((moveForward > 0.0f) ? 1.0f : -1.0f);
                        }
                    }
                    moveStrafe = ((moveStrafe == 0.0f) ? moveStrafe : ((moveStrafe > 0.0f) ? 1.0f : -1.0f));
                    final double radians = Math.toRadians(rotationYaw + 90.0f);
                    final double cos = Math.cos(radians);
                    final double sin = Math.sin(radians);
                    double finalSpeed;
                    final double maxSpeed = finalSpeed = EntityUtil.getMaxSpeed();
                    boolean fakeLagEnabled = false;
                    try {
                        final Class<?> fakeLagClass = Class.forName("me.alpha432.oyvey.impl.features.modules.movement.FakeLag");
                        final Method getInstanceMethod = fakeLagClass.getMethod("getInstance", (Class<?>[])new Class[0]);
                        final Method isEnabledMethod = fakeLagClass.getMethod("isEnabled", (Class<?>[])new Class[0]);
                        final Object fakeLagInstance = getInstanceMethod.invoke(null, new Object[0]);
                        fakeLagEnabled = (boolean)isEnabledMethod.invoke(fakeLagInstance, new Object[0]);
                    }
                    catch (Exception e) {
                        fakeLagEnabled = false;
                    }
                    if ((boolean)this.boost.getValue() && BoostManager.INSTANCE != null && !fakeLagEnabled) {
                        final double boostSpeed = BoostManager.INSTANCE.getBoostSpeed(true);
                        if (boostSpeed > 0.0) {
                            switch ((BoostMode)this.boostMode.getValue()) {
                                case MULTIPLY: {
                                    finalSpeed = maxSpeed * (double)this.boostMultiplier.getValue();
                                    break;
                                }
                                case ADD: {
                                    finalSpeed = maxSpeed + boostSpeed;
                                    break;
                                }
                                case REPLACE: {
                                    finalSpeed = Math.max(maxSpeed, boostSpeed);
                                    break;
                                }
                                case OVERRIDE: {
                                    finalSpeed = boostSpeed;
                                    break;
                                }
                            }
                        }
                    }
                    event.setX(moveForward * finalSpeed * cos + moveStrafe * finalSpeed * sin);
                    event.setZ(moveForward * finalSpeed * sin - moveStrafe * finalSpeed * cos);
                }
            }
        }
    }
    
    static {
        Acceleration.INSTANCE = new Acceleration();
    }
    
    public enum BoostMode
    {
        MULTIPLY, 
        ADD, 
        REPLACE, 
        OVERRIDE;
    }
}
