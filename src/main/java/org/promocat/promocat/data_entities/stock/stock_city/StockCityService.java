package org.promocat.promocat.data_entities.stock.stock_city;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.CityDTO;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.exception.stock_city.ApiStockCityNotFoundException;
import org.promocat.promocat.mapper.CityMapper;
import org.promocat.promocat.mapper.StockCityMapper;
import org.promocat.promocat.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:36 27.05.2020
 */
@Slf4j
@Service
public class StockCityService {

    private final StockCityRepository stockCityRepository;
    private final StockCityMapper stockCityMapper;
    private final CityMapper cityMapper;
    private final StockMapper stockMapper;

    @Autowired
    public StockCityService(final StockCityRepository stockCityRepository, final StockCityMapper stockCityMapper,
                            final CityMapper cityMapper, final StockMapper stockMapper) {
        this.stockCityRepository = stockCityRepository;
        this.stockCityMapper = stockCityMapper;
        this.cityMapper = cityMapper;
        this.stockMapper = stockMapper;
    }

    /**
     * Сохранение промежуточной сущности связывающей акцию и город.
     *
     * @param dto объектное представление промежуточноой сущности. {@link StockCityDTO}
     * @return Представление промежуточной сущности в БД. {@link StockCityDTO}
     */
    public StockCityDTO save(final StockCityDTO dto) {
        return stockCityMapper.toDto(stockCityRepository.save(stockCityMapper.toEntity(dto)));
    }

    /**
     * Поиск промежуточной сущности связывающей акцию и город.
     *
     * @param id промежуточной сущности.
     * @return Представление промежуточной сущности в БД. {@link StockCityDTO}
     * @throws ApiStockCityNotFoundException если такой сущности нет.
     */
    public StockCityDTO findById(final Long id) {
        if (id == null) {
            throw new ApiStockCityNotFoundException(String.format("No such stockCity: %d", id));
        }
        Optional<StockCity> stockCity = stockCityRepository.findById(id);
        if (stockCity.isPresent()) {
            stockCityRepository.flush();
            return stockCityMapper.toDto(stockCity.get());
        } else {
            throw new ApiStockCityNotFoundException(String.format("No such stockCity: %d", id));
        }
    }

    /**
     * Проверка на существование промежуточной сущности связывающей акцию и город.
     *
     * @param stockDTO акция.
     * @param cityDTO  город.
     * @return true если существует, false иначе
     */
    public Boolean existsByStockAndCity(final StockDTO stockDTO, final CityDTO cityDTO) {
        return stockCityRepository.existsByStockAndCity(stockMapper.toEntity(stockDTO), cityMapper.toEntity(cityDTO));
    }

    /**
     * Поиск промежуточной сущности связывающей акцию и город.
     *
     * @param stock акция.
     * @param city  город.
     * @return Представление промежуточной сущности в БД. {@link StockCityDTO}
     * @throws ApiStockCityNotFoundException если такой сущности нет.
     */
    public StockCityDTO findByStockAndCity(final StockDTO stock, final CityDTO city) {
        return stockCityMapper.toDto(stockCityRepository.findByStockAndCity(
                stockMapper.toEntity(stock), cityMapper.toEntity(city))
                .orElseThrow(() -> new ApiStockCityNotFoundException(
                        String.format("No such stockCity with stock: %d, city: %s", stock.getId(), city.getCity()))));
    }

    public Boolean existsById(final Long id) {
        return stockCityRepository.existsById(id);
    }
}
