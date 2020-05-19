package org.promocat.promocat.data_entities.stock;

import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.data_entities.promo_code.PromoCodeService;
import org.promocat.promocat.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Danil Lyskin at 20:30 18.05.2020
 */
@EnableScheduling
public class StockAlive {

    // TODO autowired не будут работать если StockAlive это не бин, разобраться
    @Autowired
    private StockService stockService;

    @Autowired
    private PromoCodeService promoCodeService;

    @Autowired
    private StockMapper stockMapper;

    @Scheduled(cron = "0 0 0 * * *")
    public void checkAlive() {
        for (int i = 0; i < Stock.daysLength(); i++) {
            List<Stock> stocks = stockService.getByTime(LocalDateTime.now().minusDays(Stock.getDay(i)), Stock.getDay(i));
            for (Stock stock : stocks) {
                for (PromoCode code : stock.getCodes()) {
                    promoCodeService.delById(code.getId());
                }
                // TODO сделать [] вместо null
                stock.setCodes(null);
                stock.setIsAlive(false);
                stockService.save(stockMapper.toDto(stock));
            }
        }
    }
}
