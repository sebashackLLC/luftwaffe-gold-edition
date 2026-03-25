//Decompiled by Procyon!

package org.junit.experimental.theories.suppliers;

import org.junit.experimental.theories.*;
import java.lang.annotation.*;

@ParametersSuppliedBy(TestedOnSupplier.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestedOn {
    int[] ints();
}
