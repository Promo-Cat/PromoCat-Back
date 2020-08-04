package org.promocat.promocat.data_entities.user_ban;

import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.UserBanDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.stock_city.ApiStockCityNotFoundException;
import org.promocat.promocat.mapper.UserBanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserBanService {

    private final UserBanRepository userBanRepository;
    private final StockCityService stockCityService;
    private final StockService stockService;
    private final UserBanMapper userBanMapper;
    private final UserService userService;

    @Autowired
    public UserBanService(final UserBanRepository userBanRepository,
                          final StockCityService stockCityService,
                          final StockService stockService,
                          final UserBanMapper userBanMapper, UserService userService) {
        this.userBanRepository = userBanRepository;
        this.stockCityService = stockCityService;
        this.stockService = stockService;
        this.userBanMapper = userBanMapper;
        this.userService = userService;
    }

    public UserBanDTO save(UserBanDTO userBanDTO) {
        return userBanMapper.toDto(userBanRepository.save(userBanMapper.toEntity(userBanDTO)));
    }

    public UserBan findById(Long id) {
        return userBanRepository.findById(id).orElseThrow();
    }

    /**
     * Банит юзера в акции, в которой он принимает участие на данный момент.
     * @param userDTO Юзер, который будет забанен
     * @return Объектное представление бана в бд
     */
    public UserBanDTO ban(UserDTO userDTO) {
        StockCityDTO stockCity = stockCityService.findById(userDTO.getStockCityId());
        if (Objects.isNull(stockCity)) {
            throw new ApiStockCityNotFoundException(String.format("Stock for user with telephone: %s not found. " +
                    "Can't ban", userDTO.getTelephone()));
        }
        StockDTO stock = stockService.findById(stockCity.getId());
        UserBanDTO userBan = new UserBanDTO();
        userBan.setStockId(stock.getId());
        userBan.setUserId(userDTO.getId());
        // TODO: 20.07.2020 Получить заработок за акцию
        userBan.setBannedEarnings(111.0);
        userDTO.setStockCityId(null);
        userService.save(userDTO);
        return save(userBan);
    }

    public boolean isBanned(UserDTO userDTO) {
        return !userBanRepository.getAllByUserId(userDTO.getId()).isEmpty();
    }

    /**
     * Проверяет забанен ли юзер {@code userDTO} в акции {@code stockDTO}
     * @param userDTO Юзер, статус которого проверяется
     * @param stockDTO Проверка проходит в отношении данной акции
     * @return {@code true} - если юзер забанен в данной акции, {@code false} - иначе
     */
    public boolean isBanned(UserDTO userDTO, StockDTO stockDTO) {
        return userBanRepository.getAllByUserIdAndStockId(userDTO.getId(), stockDTO.getId()).isPresent();
    }
}
