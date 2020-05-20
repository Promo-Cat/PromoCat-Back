package org.promocat.promocat.data_entities.stock;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Service
public class StockService {

    private final StockMapper mapper;
    private final StockRepository repository;

    @Autowired
    public StockService(final StockMapper mapper, final StockRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public StockDTO save(StockDTO dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    public StockDTO findById(Long id) {
        Optional<Stock> stock = repository.findById(id);
        if (stock.isPresent()) {
            return mapper.toDto(stock.get());
        } else {
            throw new UsernameNotFoundException(String.format("No stock with such id: %d in db.", id));
        }
    }

    public List<StockDTO> getByTime(LocalDateTime time, Long days) {
        List<Stock> stocks = repository.getByStartTimeLessThanAndDurationEquals(time, days);
        List<StockDTO> result = new ArrayList<>();
        for (Stock stock : stocks) {
            result.add(mapper.toDto(stock));
        }
        return result;
    }
}
