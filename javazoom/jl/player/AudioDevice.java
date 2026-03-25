//Decompiled by Procyon!

package javazoom.jl.player;

import javazoom.jl.decoder.*;

public interface AudioDevice
{
    void open(final Decoder p0) throws JavaLayerException;
    
    boolean isOpen();
    
    void write(final short[] p0, final int p1, final int p2) throws JavaLayerException;
    
    void close();
    
    void flush();
    
    int getPosition();
}
