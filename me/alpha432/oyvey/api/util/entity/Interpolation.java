//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.entity;

import me.alpha432.oyvey.api.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;
import net.minecraft.util.math.*;
import net.minecraft.client.renderer.culling.*;

public class Interpolation implements Util
{
    public static Vec3d interpolatedEyePos() {
        return Interpolation.mc.field_71439_g.func_174824_e(Interpolation.mc.func_184121_ak());
    }
    
    public static Vec3d interpolatedEyeVec() {
        return Interpolation.mc.field_71439_g.func_70676_i(Interpolation.mc.func_184121_ak());
    }
    
    public static Vec3d interpolatedEyeVec(final EntityPlayer player) {
        return player.func_70676_i(Interpolation.mc.func_184121_ak());
    }
    
    public static Vec3d interpolateEntity(final Entity entity) {
        final double x = interpolateLastTickPos(entity.field_70165_t, entity.field_70142_S) - getRenderPosX();
        final double y = interpolateLastTickPos(entity.field_70163_u, entity.field_70137_T) - getRenderPosY();
        final double z = interpolateLastTickPos(entity.field_70161_v, entity.field_70136_U) - getRenderPosZ();
        return new Vec3d(x, y, z);
    }
    
    public static Vec3d interpolateEntity(final BlockPos start, final BlockPos end, final float progress) {
        final double x = start.func_177958_n() + (end.func_177958_n() - start.func_177958_n()) * progress;
        final double y = start.func_177956_o() + (end.func_177956_o() - start.func_177956_o()) * progress;
        final double z = start.func_177952_p() + (end.func_177952_p() - start.func_177952_p()) * progress;
        return new Vec3d(x, y, z);
    }
    
    private static double interpolateLastTickPos(final double pos, final double lastTickPos) {
        return lastTickPos + (pos - lastTickPos) * Interpolation.mc.func_184121_ak();
    }
    
    public static AxisAlignedBB interpolatePos(final BlockPos pos) {
        return interpolatePos(pos, 1.0f);
    }
    
    public static AxisAlignedBB interpolatePos(final BlockPos pos, final float height) {
        return new AxisAlignedBB(pos.func_177958_n() - Interpolation.mc.func_175598_ae().field_78730_l, pos.func_177956_o() - Interpolation.mc.func_175598_ae().field_78731_m, pos.func_177952_p() - Interpolation.mc.func_175598_ae().field_78728_n, pos.func_177958_n() - Interpolation.mc.func_175598_ae().field_78730_l + 1.0, pos.func_177956_o() - Interpolation.mc.func_175598_ae().field_78731_m + height, pos.func_177952_p() - Interpolation.mc.func_175598_ae().field_78728_n + 1.0);
    }
    
    public static AxisAlignedBB interpolateAxis(final AxisAlignedBB bb) {
        return new AxisAlignedBB(bb.field_72340_a - Interpolation.mc.func_175598_ae().field_78730_l, bb.field_72338_b - Interpolation.mc.func_175598_ae().field_78731_m, bb.field_72339_c - Interpolation.mc.func_175598_ae().field_78728_n, bb.field_72336_d - Interpolation.mc.func_175598_ae().field_78730_l, bb.field_72337_e - Interpolation.mc.func_175598_ae().field_78731_m, bb.field_72334_f - Interpolation.mc.func_175598_ae().field_78728_n);
    }
    
    public static AxisAlignedBB offsetRenderPos(final AxisAlignedBB bb) {
        return bb.func_72317_d(-getRenderPosX(), -getRenderPosY(), -getRenderPosZ());
    }
    
    public static double getRenderPosX() {
        return Interpolation.mc.func_175598_ae().field_78730_l;
    }
    
    public static double getRenderPosY() {
        return Interpolation.mc.func_175598_ae().field_78731_m;
    }
    
    public static double getRenderPosZ() {
        return Interpolation.mc.func_175598_ae().field_78728_n;
    }
    
    public static Frustum createFrustum(final Entity entity) {
        final Frustum frustum = new Frustum();
        setFrustum(frustum, entity);
        return frustum;
    }
    
    public static void setFrustum(final Frustum frustum, final Entity entity) {
        final double x = interpolateLastTickPos(entity.field_70165_t, entity.field_70142_S);
        final double y = interpolateLastTickPos(entity.field_70163_u, entity.field_70137_T);
        final double z = interpolateLastTickPos(entity.field_70161_v, entity.field_70136_U);
        frustum.func_78547_a(x, y, z);
    }
}
