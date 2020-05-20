package org.promocat.promocat.data_entities.stock;

import org.promocat.promocat.data_entities.promo_code.PromoCodeService;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.dto.StockDTO;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Danil Lyskin at 20:30 18.05.2020
 */
@Configuration
@EnableScheduling
public class StockAlive {
    private final StockService stockService;

    private final PromoCodeService promoCodeService;

    public StockAlive(StockService stockService, PromoCodeService promoCodeService) {
        this.stockService = stockService;
        this.promoCodeService = promoCodeService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void checkAlive() {
        for (int i = 0; i < StockDTO.daysLength(); i++) {
            List<StockDTO> stocks = stockService.getByTime(LocalDateTime.now().minusDays(StockDTO.getDay(i)), StockDTO.getDay(i));
            for (StockDTO stock : stocks) {
                for (PromoCodeDTO code : stock.getCodes()) {
                    promoCodeService.delById(code.getId());
                }
                stock.setCodes(new ArrayList<>());
                stock.setIsAlive(false);
                stockService.save(stock);
            }
        }
    }
}
