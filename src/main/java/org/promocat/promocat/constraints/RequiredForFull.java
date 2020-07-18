package org.promocat.promocat.constraints;

import java.lang.annotation.*;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
@Documented
public @interface RequiredForFull {
}
