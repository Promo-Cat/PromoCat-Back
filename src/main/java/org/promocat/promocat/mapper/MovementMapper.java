package org.promocat.promocat.mapper;

import org.promocat.promocat.data_entities.movement.Movement;
import org.promocat.promocat.dto.MovementDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovementMapper extends AbstractMapper<Movement, MovementDTO> {
    @Autowired
    MovementMapper() {
        super(Movement.class, MovementDTO.class);
    }
}
