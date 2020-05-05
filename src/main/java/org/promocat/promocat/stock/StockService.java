package org.promocat.promocat.stock;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockService {

    final StockRepository stockRepository;

    @Autowired
    public StockService(final StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }
}
