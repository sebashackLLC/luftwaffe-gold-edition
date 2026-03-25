//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.entity;

import me.alpha432.oyvey.api.interfaces.*;
import me.alpha432.oyvey.api.interfaces.ducks.*;
import net.minecraft.entity.*;
import me.alpha432.oyvey.impl.events.*;
import net.minecraft.util.*;
import net.minecraft.init.*;
import net.minecraft.potion.*;
import java.util.*;

public final class MovementUtil implements Minecraftable
{
    public static double[] directionSpeed(final double speed) {
        float forward = MovementUtil.mc.field_71439_g.field_71158_b.field_192832_b;
        float side = MovementUtil.mc.field_71439_g.field_71158_b.field_78902_a;
        float yaw = MovementUtil.mc.field_71439_g.field_70126_B + (MovementUtil.mc.field_71439_g.field_70177_z - MovementUtil.mc.field_71439_g.field_70126_B) * MovementUtil.mc.func_184121_ak();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[] { posX, posZ };
    }
    
    public static boolean anyMovementKeys() {
        return MovementUtil.mc.field_71439_g.field_71158_b.field_78901_c || MovementUtil.mc.field_71439_g.field_71158_b.field_78899_d || MovementUtil.mc.field_71439_g.field_71158_b.field_187256_d || MovementUtil.mc.field_71439_g.field_71158_b.field_187255_c || MovementUtil.mc.field_71439_g.field_71158_b.field_187257_e || MovementUtil.mc.field_71439_g.field_71158_b.field_187258_f;
    }
    
    public static boolean anyMovementKeysNoSneak() {
        return MovementUtil.mc.field_71439_g.field_71158_b.field_78901_c || MovementUtil.mc.field_71439_g.field_71158_b.field_187256_d || MovementUtil.mc.field_71439_g.field_71158_b.field_187255_c || MovementUtil.mc.field_71439_g.field_71158_b.field_187257_e || MovementUtil.mc.field_71439_g.field_71158_b.field_187258_f;
    }
    
    public static boolean anyMovementKeysNoSneakNoJump() {
        return MovementUtil.mc.field_71439_g.field_71158_b.field_187256_d || MovementUtil.mc.field_71439_g.field_71158_b.field_187255_c || MovementUtil.mc.field_71439_g.field_71158_b.field_187257_e || MovementUtil.mc.field_71439_g.field_71158_b.field_187258_f;
    }
    
    public static boolean hasVelocity() {
        return hasVelocity((Entity)MovementUtil.mc.field_71439_g);
    }
    
    public static boolean hasVelocity(final Entity entity) {
        return entity.field_70159_w != 0.0 || entity.field_70181_x != 0.0 || entity.field_70179_y != 0.0;
    }
    
    public static boolean isRotating() {
        final double yaw = MovementUtil.mc.field_71439_g.field_70177_z - ((IEntityPlayerSP)MovementUtil.mc.field_71439_g).getLastReportedYaw();
        final double pitch = MovementUtil.mc.field_71439_g.field_70125_A - ((IEntityPlayerSP)MovementUtil.mc.field_71439_g).getLastReportedPitch();
        return yaw != 0.0 || pitch != 0.0;
    }
    
    public static boolean isFullMoving() {
        return isMoving() || isMovingVertically();
    }
    
    public static boolean isMovingVertically(final EntityLivingBase entity) {
        final double yDist = entity.field_70163_u - entity.field_70167_r;
        return yDist != 0.0;
    }
    
    public static boolean isMovingVertically() {
        return isMovingVertically((EntityLivingBase)MovementUtil.mc.field_71439_g);
    }
    
    public static boolean isMoving() {
        return isMoving((EntityLivingBase)MovementUtil.mc.field_71439_g);
    }
    
    public static boolean isMoving(final EntityLivingBase entity) {
        return entity.field_191988_bg != 0.0f || entity.field_70702_br != 0.0f;
    }
    
    public static double getDistance2D() {
        final double xDist = MovementUtil.mc.field_71439_g.field_70165_t - MovementUtil.mc.field_71439_g.field_70169_q;
        final double zDist = MovementUtil.mc.field_71439_g.field_70161_v - MovementUtil.mc.field_71439_g.field_70166_s;
        return Math.sqrt(xDist * xDist + zDist * zDist);
    }
    
    public static void strafe(final MoveEvent event, final double speed) {
        if (isMoving()) {
            final double[] strafe = strafe(speed);
            event.setX(strafe[0]);
            event.setZ(strafe[1]);
        }
        else {
            event.setX(0.0);
            event.setZ(0.0);
        }
    }
    
    public static double[] strafe(final double speed) {
        return strafe((Entity)MovementUtil.mc.field_71439_g, speed);
    }
    
    public static double[] strafe(final Entity entity, final double speed) {
        return strafe(entity, MovementUtil.mc.field_71439_g.field_71158_b, speed);
    }
    
    public static double[] strafe(final Entity entity, final MovementInput movementInput, final double speed) {
        float moveForward = movementInput.field_192832_b;
        float moveStrafe = movementInput.field_78902_a;
        float rotationYaw = entity.field_70126_B + (entity.field_70177_z - entity.field_70126_B) * MovementUtil.mc.func_184121_ak();
        if (moveForward != 0.0f) {
            if (moveStrafe > 0.0f) {
                rotationYaw += ((moveForward > 0.0f) ? -45 : 45);
            }
            else if (moveStrafe < 0.0f) {
                rotationYaw += ((moveForward > 0.0f) ? 45 : -45);
            }
            moveStrafe = 0.0f;
            if (moveForward > 0.0f) {
                moveForward = 1.0f;
            }
            else if (moveForward < 0.0f) {
                moveForward = -1.0f;
            }
        }
        final double posX = moveForward * speed * -Math.sin(Math.toRadians(rotationYaw)) + moveStrafe * speed * Math.cos(Math.toRadians(rotationYaw));
        final double posZ = moveForward * speed * Math.cos(Math.toRadians(rotationYaw)) - moveStrafe * speed * -Math.sin(Math.toRadians(rotationYaw));
        return new double[] { posX, posZ };
    }
    
    public static double getSpeed() {
        return getSpeed(false);
    }
    
    public static double getSpeed(final boolean slowness) {
        double defaultSpeed = 0.2873;
        if (MovementUtil.mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
            final int amplifier = Objects.requireNonNull(MovementUtil.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c)).func_76458_c();
            defaultSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        if (slowness && MovementUtil.mc.field_71439_g.func_70644_a(MobEffects.field_76421_d)) {
            final int amplifier = Objects.requireNonNull(MovementUtil.mc.field_71439_g.func_70660_b(MobEffects.field_76421_d)).func_76458_c();
            defaultSpeed /= 1.0 + 0.2 * (amplifier + 1);
        }
        return defaultSpeed;
    }
    
    public static double getJumpSpeed() {
        double defaultSpeed = 0.0;
        if (MovementUtil.mc.field_71439_g.func_70644_a(MobEffects.field_76430_j)) {
            final int amplifier = MovementUtil.mc.field_71439_g.func_70660_b(MobEffects.field_76430_j).func_76458_c();
            defaultSpeed += (amplifier + 1) * 0.1;
        }
        return defaultSpeed;
    }
    
    public static void setMotion(final double x, final double y, final double z) {
        if (MovementUtil.mc.field_71439_g != null && MovementUtil.mc.field_71439_g.func_184187_bx() != null) {
            if (MovementUtil.mc.field_71439_g.func_184218_aH()) {
                MovementUtil.mc.field_71439_g.func_184187_bx().field_70159_w = x;
                MovementUtil.mc.field_71439_g.func_184187_bx().field_70181_x = y;
                MovementUtil.mc.field_71439_g.func_184187_bx().field_70179_y = z;
            }
            else {
                MovementUtil.mc.field_71439_g.field_70159_w = x;
                MovementUtil.mc.field_71439_g.field_70181_x = y;
                MovementUtil.mc.field_71439_g.field_70179_y = z;
            }
        }
    }
    
    public static double calcEffects(double speed) {
        if (MovementUtil.mc.field_71439_g.func_70644_a(MobEffects.field_76424_c)) {
            final int amplifier = MovementUtil.mc.field_71439_g.func_70660_b(MobEffects.field_76424_c).func_76458_c();
            speed *= 1.0 + 0.2 * (amplifier + 1);
        }
        if (MovementUtil.mc.field_71439_g.func_70644_a(MobEffects.field_76421_d)) {
            final int amplifier = MovementUtil.mc.field_71439_g.func_70660_b(MobEffects.field_76421_d).func_76458_c();
            speed /= 1.0 + 0.2 * (amplifier + 1);
        }
        return speed;
    }
    
    public static double direction(float rotationYaw, final double moveForward, final double moveStrafing) {
        if (moveForward < 0.0) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (moveForward < 0.0) {
            forward = -0.5f;
        }
        else if (moveForward > 0.0) {
            forward = 0.5f;
        }
        if (moveStrafing > 0.0) {
            rotationYaw -= 90.0f * forward;
        }
        if (moveStrafing < 0.0) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }
    
    private MovementUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
