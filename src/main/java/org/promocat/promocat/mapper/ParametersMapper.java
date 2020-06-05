package org.promocat.promocat.mapper;

import org.promocat.promocat.data_entities.parameters.Parameters;
import org.promocat.promocat.dto.ParametersDTO;

public class ParametersMapper extends AbstractMapper<Parameters, ParametersDTO> {
    public ParametersMapper() {
        super(Parameters.class, ParametersDTO.class);
    }
}
