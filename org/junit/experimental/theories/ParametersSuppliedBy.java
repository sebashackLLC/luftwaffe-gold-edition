//Decompiled by Procyon!

package org.junit.experimental.theories;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface ParametersSuppliedBy {
    Class<? extends ParameterSupplier> value();
}
