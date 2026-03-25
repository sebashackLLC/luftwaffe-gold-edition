//Decompiled by Procyon!

package org.hamcrest;

public abstract class BaseMatcher<T> implements Matcher<T>
{
    public final void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
    }
    
    @Override
    public String toString() {
        return StringDescription.toString(this);
    }
}
