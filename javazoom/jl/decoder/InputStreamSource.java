//Decompiled by Procyon!

package javazoom.jl.decoder;

import java.io.*;

public class InputStreamSource implements Source
{
    private final InputStream in;
    
    public InputStreamSource(final InputStream in) {
        if (in == null) {
            throw new NullPointerException("in");
        }
        this.in = in;
    }
    
    public int read(final byte[] b, final int offs, final int len) throws IOException {
        final int read = this.in.read(b, offs, len);
        return read;
    }
    
    public boolean willReadBlock() {
        return true;
    }
    
    public boolean isSeekable() {
        return false;
    }
    
    public long tell() {
        return -1L;
    }
    
    public long seek(final long to) {
        return -1L;
    }
    
    public long length() {
        return -1L;
    }
}
