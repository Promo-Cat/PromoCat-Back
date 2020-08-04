package org.promocat.promocat.constraints;

import java.lang.annotation.*;

/**
 * Данная аннотация помечает поля классов-наследников от класса {@link org.promocat.promocat.utils.soap.operations.AbstractOperation}
 * Обязательным параметром аннотации {@code value} является имя соответствующего тэга в xml.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
@Documented
public @interface XmlField {

    String value();

}
