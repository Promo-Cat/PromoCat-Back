package org.promocat.promocat.data_entities.stock.stock_city;

import org.promocat.promocat.dto.StockCityDTO;
import org.promocat.promocat.mapper.StockCityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:36 27.05.2020
 */
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
}
