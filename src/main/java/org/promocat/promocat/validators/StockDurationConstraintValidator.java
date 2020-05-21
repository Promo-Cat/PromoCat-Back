package org.promocat.promocat.validators;

import org.promocat.promocat.constraints.StockDurationConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 15:24 21.05.2020
 */
public class StockDurationConstraintValidator implements ConstraintValidator<StockDurationConstraint, Long> {

    // TODO перенести куда-то
    private static final List<Long> allowedDuration = List.of(
            1L, // TODO удалить возможность делать акцию длительностью в 1 день.
            7L,
            14L,
            21L,
            28L
    );

    public static List<Long> getAllowedDuration() {
        return allowedDuration;
    }

    @Override
    public boolean isValid(Long duration, ConstraintValidatorContext constraintValidatorContext) {
        return allowedDuration.contains(duration);
    }
}
