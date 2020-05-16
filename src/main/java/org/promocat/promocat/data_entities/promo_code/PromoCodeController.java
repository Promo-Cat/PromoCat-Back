package org.promocat.promocat.data_entities.promo_code;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by Danil Lyskin at 22:13 05.05.2020
 */
@Slf4j
@RestController
public class PromoCodeController {


    private final PromoCodeService promoCodeService;

    //TODO endpoint for adding promo codes by company???

    @Autowired
    public PromoCodeController(final PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    @RequestMapping(path = "/auth/user/promoCode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PromoCodeDTO addPromoCode(@Valid @RequestBody PromoCodeDTO promoCodeDTO) {
        log.info(String.format("Trying to set promo code: %s from stock: %d",
                promoCodeDTO.getPromCode(), promoCodeDTO.getStockId()));
        return promoCodeService.save(promoCodeDTO);
    }

    //TODO
    @RequestMapping(path = "/auth/user/generate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public StockDTO generate(@RequestBody StockDTO stock) {
        return promoCodeService.savePromoCodes(stock);
    }
}
