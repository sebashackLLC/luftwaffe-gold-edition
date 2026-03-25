//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.entity;

import me.alpha432.oyvey.api.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import me.alpha432.oyvey.api.util.math.*;

public class RotationUtil implements Util
{
    public static Vec3d getEyesPos() {
        return new Vec3d(RotationUtil.mc.field_71439_g.field_70165_t, RotationUtil.mc.field_71439_g.field_70163_u + RotationUtil.mc.field_71439_g.func_70047_e(), RotationUtil.mc.field_71439_g.field_70161_v);
    }
    
    public static double[] calculateLookAt(final double px, final double py, final double pz, final EntityPlayer me) {
        double dirx = me.field_70165_t - px;
        double diry = me.field_70163_u - py;
        double dirz = me.field_70161_v - pz;
        final double len = Math.sqrt(dirx * dirx + diry * diry + dirz * dirz);
        double pitch = Math.asin(diry /= len);
        dirz /= len;
        double yaw = Math.atan2(dirz, dirx /= len);
        pitch = pitch * 180.0 / 3.141592653589793;
        yaw = yaw * 180.0 / 3.141592653589793;
        final double[] array = new double[2];
        yaw = (array[0] = yaw + 90.0);
        array[1] = pitch;
        return array;
    }
    
    public static float[] getRotations(final BlockPos pos) {
        return getRotations(pos.func_177958_n() + 0.5, pos.func_177956_o(), pos.func_177952_p() + 0.5);
    }
    
    public static float[] getRotations(final double x, final double y, final double z) {
        final double xDiff = x - RotationUtil.mc.field_71439_g.field_70165_t;
        final double yDiff = y - EntityUtil.getEyeHeight((Entity)RotationUtil.mc.field_71439_g);
        final double zDiff = z - RotationUtil.mc.field_71439_g.field_70161_v;
        final double dist = MathHelper.func_76133_a(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        float diff = yaw - RotationUtil.mc.field_71439_g.field_70177_z;
        if (diff < -180.0f || diff > 180.0f) {
            final float round = (float)Math.round(Math.abs(diff / 360.0f));
            diff = ((diff < 0.0f) ? (diff + 360.0f * round) : (diff - 360.0f * round));
        }
        return new float[] { RotationUtil.mc.field_71439_g.field_70177_z + diff, pitch };
    }
    
    public static float[] getLegitRotations(final Vec3d vec) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.field_72450_a - eyesPos.field_72450_a;
        final double diffY = vec.field_72448_b - eyesPos.field_72448_b;
        final double diffZ = vec.field_72449_c - eyesPos.field_72449_c;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        final float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
        return new float[] { RotationUtil.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g(yaw - RotationUtil.mc.field_71439_g.field_70177_z), RotationUtil.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g(pitch - RotationUtil.mc.field_71439_g.field_70125_A) };
    }
    
    public static void faceYawAndPitch(final float yaw, final float pitch) {
        RotationUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(yaw, pitch, RotationUtil.mc.field_71439_g.field_70122_E));
    }
    
    public static void faceVector(final Vec3d vec, final boolean normalizeAngle) {
        final float[] rotations = getLegitRotations(vec);
        RotationUtil.mc.field_71439_g.field_71174_a.func_147297_a((Packet)new CPacketPlayer.Rotation(rotations[0], normalizeAngle ? ((float)MathHelper.func_180184_b((int)rotations[1], 360)) : rotations[1], RotationUtil.mc.field_71439_g.field_70122_E));
    }
    
    public static void faceEntity(final Entity entity) {
        final float[] angle = MathUtil.calcAngle(RotationUtil.mc.field_71439_g.func_174824_e(RotationUtil.mc.func_184121_ak()), entity.func_174824_e(RotationUtil.mc.func_184121_ak()));
        faceYawAndPitch(angle[0], angle[1]);
    }
    
    public static float[] getAngle(final Entity entity) {
        return MathUtil.calcAngle(RotationUtil.mc.field_71439_g.func_174824_e(RotationUtil.mc.func_184121_ak()), entity.func_174824_e(RotationUtil.mc.func_184121_ak()));
    }
    
    public static int getDirection4D() {
        return MathHelper.func_76128_c(RotationUtil.mc.field_71439_g.field_70177_z * 4.0f / 360.0f + 0.5) & 0x3;
    }
    
    public static String getDirection4D(final boolean northRed) {
        final int dirnumber = getDirection4D();
        if (dirnumber == 0) {
            return "South (+Z)";
        }
        if (dirnumber == 1) {
            return "West (-X)";
        }
        if (dirnumber == 2) {
            return (northRed ? "\u00c2§c" : "") + "North (-Z)";
        }
        if (dirnumber == 3) {
            return "East (+X)";
        }
        return "Loading...";
    }
}
