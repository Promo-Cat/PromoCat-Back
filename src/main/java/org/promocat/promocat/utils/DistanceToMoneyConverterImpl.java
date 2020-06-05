package org.promocat.promocat.utils;

import org.promocat.promocat.data_entities.parameters.ParametersService;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DistanceToMoneyConverterImpl implements DistanceToMoneyConverter {

    private static final double MULTIPLIER = 0.1;

    @Override
    public Double convert(Double distance) {
        return distance * MULTIPLIER;
    }
}
