package org.promocat.promocat.data_entities.stock;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
