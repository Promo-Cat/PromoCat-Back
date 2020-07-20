package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.stock.StockRepository;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.data_entities.user_ban.UserBan;
import org.promocat.promocat.dto.UserBanDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class UserBanMapper extends AbstractMapper<UserBan, UserBanDTO> {

    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    @Autowired
    public UserBanMapper(final ModelMapper mapper,
                         final UserRepository userRepository,
                         final StockRepository stockRepository) {
        super(UserBan.class, UserBanDTO.class);
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.stockRepository = stockRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(UserBan.class, UserBanDTO.class)
                .addMappings(m -> {
                    m.skip(UserBanDTO::setUserId);
                    m.skip(UserBanDTO::setStockId);
                }).setPostConverter(toDtoConverter());
        mapper.createTypeMap(UserBanDTO.class, UserBan.class)
                .addMappings(m -> {
                    m.skip(UserBan::setUser);
                    m.skip(UserBan::setStock);
                }).setPostConverter(toEntityConverter());
    }

    @Override
    void mapSpecificFields(UserBan source, UserBanDTO destination) {
        destination.setUserId(getUserId(source));
        destination.setStockId(getStockId(source));
    }

    private Long getUserId(UserBan source) {
        return Objects.isNull(source) || Objects.isNull(source.getUser()) ? null : source.getUser().getId();
    }

    private Long getStockId(UserBan source) {
        return Objects.isNull(source) || Objects.isNull(source.getStock()) ? null : source.getStock().getId();
    }

    @Override
    void mapSpecificFields(UserBanDTO source, UserBan destination) {
        Long userId = source.getUserId() == null ? 0L : source.getUserId();
        destination.setUser(userRepository.findById(userId).orElse(null));
        Long stockId = source.getStockId() == null ? 0L : source.getStockId();
        destination.setStock(stockRepository.findById(stockId).orElse(null));
    }

}
