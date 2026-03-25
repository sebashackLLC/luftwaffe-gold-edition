//Decompiled by Procyon!

package me.alpha432.oyvey.api.util;

import me.alpha432.oyvey.api.interfaces.wrapper.*;

public class NullUtils implements IMinecraft
{
    public static boolean nullCheck() {
        return NullUtils.mc.field_71439_g == null || NullUtils.mc.field_71441_e == null || NullUtils.mc.field_71442_b == null;
    }
    
    public static boolean isPlayerNull() {
        return NullUtils.mc.field_71439_g == null || NullUtils.mc.field_71441_e == null;
    }
}
