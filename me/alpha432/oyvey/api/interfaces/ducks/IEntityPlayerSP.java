//Decompiled by Procyon!

package me.alpha432.oyvey.api.interfaces.ducks;

public interface IEntityPlayerSP
{
    float getLastReportedYaw();
    
    float getLastReportedPitch();
    
    void setLastReportedYaw(final float p0);
    
    void setLastReportedPitch(final float p0);
    
    void getOnUpdate();
    
    void getUpdateWalkingPlayer();
    
    boolean getPrevOnGround();
}
