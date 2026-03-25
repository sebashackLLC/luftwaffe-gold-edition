//Decompiled by Procyon!

package org.hamcrest.internal;

import org.hamcrest.*;

public class SelfDescribingValue<T> implements SelfDescribing
{
    private T value;
    
    public SelfDescribingValue(final T value) {
        this.value = value;
    }
    
    public void describeTo(final Description description) {
        description.appendValue(this.value);
    }
}
