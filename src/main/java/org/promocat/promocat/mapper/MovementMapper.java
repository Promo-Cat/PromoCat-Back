package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.movement.Movement;
import org.promocat.promocat.data_entities.stock.StockRepository;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.MovementDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:39 26.05.2020
 */
@Component
public class MovementMapper extends AbstractMapper<Movement, MovementDTO> {

    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    @Autowired
    public MovementMapper(final ModelMapper mapper, final UserRepository userRepository, final StockRepository stockRepository) {
        super(Movement.class, MovementDTO.class);
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Movement.class, MovementDTO.class)
                .addMappings(m -> m.skip(MovementDTO::setUserId))
                .addMappings(m -> m.skip(MovementDTO::setStockId)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(MovementDTO.class, Movement.class)
                .addMappings(m -> m.skip(Movement::setUser))
                .addMappings(m -> m.skip(Movement::setStock)).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Movement source, MovementDTO destination) {
        destination.setId(getId(source));
    }

    private Long getId(Movement source) {
        return Objects.isNull(source) || Objects.isNull(source.getUser()) ? null : source.getId();
    }

    @Override
    void mapSpecificFields(MovementDTO source, Movement destination) {
        destination.setUser(userRepository.findById(source.getUserId()).orElse(null));
        destination.setStock(stockRepository.findById(source.getStockId()).orElse(null));
    }
}
