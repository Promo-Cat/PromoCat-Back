package org.promocat.promocat.data_entities.promo_code;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Danil Lyskin at 22:13 05.05.2020
 */
@Slf4j
@RestController
@Api(tags = {SpringFoxConfig.PROMO_CODE})
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    @Autowired
    public PromoCodeController(final PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    // ------ Admin methods ------

    @RequestMapping(path = "/admin/promoCode/id", method = RequestMethod.GET)
    public ResponseEntity<PromoCodeDTO> getPromoCodeById(@RequestParam("id") Long id) {
        return ResponseEntity.ok(promoCodeService.findById(id));
    }

    @RequestMapping(path = "/admin/promoCode/promoCode", method = RequestMethod.GET)
    public ResponseEntity<PromoCodeDTO> getPromoCodeByPromoCode(@RequestParam("promoCode") String promoCode) {
        return ResponseEntity.ok(promoCodeService.findByPromoCode(promoCode));
    }
}
