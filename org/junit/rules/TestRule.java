//Decompiled by Procyon!

package org.junit.rules;

import org.junit.runners.model.*;
import org.junit.runner.*;

public interface TestRule
{
    Statement apply(final Statement p0, final Description p1);
}
