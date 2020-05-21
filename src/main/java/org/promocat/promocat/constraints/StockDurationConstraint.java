package org.promocat.promocat.constraints;

import org.promocat.promocat.validators.StockDurationConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 15:24 21.05.2020
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StockDurationConstraintValidator.class)
public @interface StockDurationConstraint {
    String message() default "Not appropriate duration";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
