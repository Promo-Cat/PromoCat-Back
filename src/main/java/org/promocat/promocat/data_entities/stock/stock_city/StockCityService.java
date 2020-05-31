package org.promocat.promocat.data_entities.stock.stock_city;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.exception.stock_city.ApiStockCityNotFoundException;
import org.promocat.promocat.mapper.StockCityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @Autowired
    public StockCityService(final StockCityRepository stockCityRepository, final StockCityMapper stockCityMapper) {
        this.stockCityRepository = stockCityRepository;
        this.stockCityMapper = stockCityMapper;
    }

    public StockCityDTO save(StockCityDTO dto) {
        return stockCityMapper.toDto(stockCityRepository.save(stockCityMapper.toEntity(dto)));
    }

    public StockCityDTO findById(Long id) {
        Optional<StockCity> stockCity = stockCityRepository.findById(id);
        if (stockCity.isPresent()) {
            return stockCityMapper.toDto(stockCity.get());
        } else {
            throw new ApiStockCityNotFoundException("TODO");
        }
    }
}
