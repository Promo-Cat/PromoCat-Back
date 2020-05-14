package org.promocat.promocat.data_entities.stock;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import org.promocat.promocat.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class StockService {

//    private final StockRepository stockRepository;
//
//    @Transactional
//    public StockDTO save(Stock stock) {
//        Stock res = stockRepository.save(stock);
//        return new StockDTO(res);
//    }
//
//    @Transactional
//    public StockDTO findById(Long id) {
//        Optional<Stock> stockRecord = stockRepository.findById(id);
//        if (stockRecord.isPresent()) {
//            return new StockDTO(stockRecord.get());
//        } else {
//            throw new UsernameNotFoundException("No stock with such id in db.");
//        }
//    }
//
//    @Autowired
//    public StockService(final StockRepository stockRepository) {
//        this.stockRepository = stockRepository;
//    }
}
