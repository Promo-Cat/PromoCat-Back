package org.promocat.promocat.data_entities.promo_code;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.exception.ApiException;
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

    @ApiOperation(value = "Get promo-code by id.",
            notes = "Returning promo-code with id specified in request",
            response = StockDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Promo-code not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/promoCode/id", method = RequestMethod.GET)
    public ResponseEntity<PromoCodeDTO> getPromoCodeById(@RequestParam("id") Long id) {
        log.info("Admin trying to get promo-code by id: {}", id);
        return ResponseEntity.ok(promoCodeService.findById(id));
    }

    @ApiOperation(value = "Get promo-code",
            notes = "Returning promo-code with \"promo-code\" specified in request",
            response = StockDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Promo-code not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/promoCode/promoCode", method = RequestMethod.GET)
    public ResponseEntity<PromoCodeDTO> getPromoCodeByPromoCode(@RequestParam("promoCode") String promoCode) {
        log.info("Admin trying to get promo-code: {}", promoCode);
        return ResponseEntity.ok(promoCodeService.findByPromoCode(promoCode));
    }
}
