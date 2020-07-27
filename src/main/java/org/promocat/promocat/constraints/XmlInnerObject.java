package org.promocat.promocat.constraints;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface XmlInnerObject {

    Class<?> value();

}
