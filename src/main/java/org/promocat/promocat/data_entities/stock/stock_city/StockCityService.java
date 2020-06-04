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
// TODO javadocs
public class StockCityService {

    private final StockCityRepository stockCityRepository;
    private final StockCityMapper stockCityMapper;
    private final CityMapper cityMapper;
    private final StockMapper stockMapper;

    @Autowired
    public StockCityService(final StockCityRepository stockCityRepository, final StockCityMapper stockCityMapper, final CityMapper cityMapper, final StockMapper stockMapper) {
        this.stockCityRepository = stockCityRepository;
        this.stockCityMapper = stockCityMapper;
        this.cityMapper = cityMapper;
        this.stockMapper = stockMapper;
    }

    // FIXME
    public StockCityDTO save(final StockCityDTO dto) {
        StockCity stockCity = stockCityMapper.toEntity(dto);
        StockCity stockCity1 = stockCityRepository.save(stockCity);
        StockCityDTO stockCityDTO = stockCityMapper.toDto(stockCity1);
        return stockCityDTO;
    }

    public StockCityDTO findById(final Long id) {
        Optional<StockCity> stockCity = stockCityRepository.findById(id);
        if (stockCity.isPresent()) {
            stockCityRepository.flush();
            return stockCityMapper.toDto(stockCity.get());
        } else {
            // TODO exception
            throw new ApiStockCityNotFoundException("TODO");
        }
    }

    public StockCityDTO findByStockAndCity(final StockDTO stock, final CityDTO city) {
        // TODO exception
        return stockCityMapper.toDto(stockCityRepository.findByStockAndCity(
                        stockMapper.toEntity(stock), cityMapper.toEntity(city)).orElseThrow());
    }
}
