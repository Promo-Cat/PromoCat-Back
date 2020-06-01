package org.promocat.promocat.utils;

import org.springframework.stereotype.Component;

@Component
public class DistanceToMoneyConverterImpl implements DistanceToMoneyConverter {

    private static final double MULTIPLIER = 0.1;

    @Override
    public Double convert(Double distance) {
        return distance * MULTIPLIER;
    }
}
