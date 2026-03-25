//Decompiled by Procyon!

package org.junit.experimental.categories;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface Category {
    Class<?>[] value();
}
