package org.promocat.promocat.constraints;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
@Documented
public @interface XmlField {

    String value();

}
