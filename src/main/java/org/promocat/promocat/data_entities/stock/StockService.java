package org.promocat.promocat.data_entities.stock;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import org.promocat.promocat.data_entities.car.CarRecord;
import org.promocat.promocat.data_entities.stock.dto.StockDTO;
import org.promocat.promocat.data_entities.user.UserRecord;
import org.promocat.promocat.data_entities.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
public class StockService {

    final StockRepository stockRepository;

    @Transactional
    public StockDTO save(StockRecord stock) {
        StockRecord res = stockRepository.save(stock);
        return new StockDTO(res);
    }

    @Autowired
    public StockService(final StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
}
