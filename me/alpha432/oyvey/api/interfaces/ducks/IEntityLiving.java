//Decompiled by Procyon!

package me.alpha432.oyvey.api.interfaces.ducks;

public interface IEntityLiving
{
    int getTicksSinceLastSwing();
    
    int getActiveItemStackUseCount();
    
    int getArmSwingAnim();
    
    void setTicksSinceLastSwing(final int p0);
    
    void setActiveItemStackUseCount(final int p0);
}
