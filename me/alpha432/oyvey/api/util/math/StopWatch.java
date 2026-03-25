//Decompiled by Procyon!

package me.alpha432.oyvey.api.util.math;

public class StopWatch implements Passable
{
    private volatile long time;
    
    public boolean passed(final double ms) {
        return System.currentTimeMillis() - this.time >= ms;
    }
    
    public boolean passed(final long ms) {
        return System.currentTimeMillis() - this.time >= ms;
    }
    
    public StopWatch reset() {
        this.time = System.currentTimeMillis();
        return this;
    }
    
    public long getTime() {
        return System.currentTimeMillis() - this.time;
    }
    
    public void setTime(final long time) {
        this.time = time;
    }
}
