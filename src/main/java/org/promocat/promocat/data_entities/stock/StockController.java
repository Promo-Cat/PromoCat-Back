package org.promocat.promocat.data_entities.stock;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.StockStatus;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.company.CompanyService;
import org.promocat.promocat.data_entities.promo_code.PromoCodeService;
import org.promocat.promocat.data_entities.stock.poster.PosterService;
import org.promocat.promocat.dto.PosterDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.security.ApiForbiddenException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@RestController
@Api(tags = {SpringFoxConfig.STOCK})
public class StockController {

    private final StockService stockService;
    private final PromoCodeService promoCodeService;
    private final CompanyService companyService;
    private final PosterService posterService;

    @Autowired
    public StockController(final StockService stockService,
                           final PromoCodeService promoCodeService,
                           final CompanyService companyService, final PosterService posterService) {
        this.stockService = stockService;
        this.promoCodeService = promoCodeService;
        this.companyService = companyService;
        this.posterService = posterService;
    }

    @ApiOperation(value = "Create stock",
            notes = "Adds stock for company with id specified in request.",
            response = StockDTO.class,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 400,
                    message = "Validation error",
                    response = ApiValidationException.class),
            @ApiResponse(code = 415,
                    message = "Not acceptable media type",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/company/stock", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StockDTO> addStock(@Valid @RequestBody StockDTO stock) {
        stock.setIsAlive(StockStatus.POSTER_NOT_CONFIRMED);
        return ResponseEntity.ok(stockService.create(stock));
    }


    @RequestMapping(path = "/api/company/stock/{id}/poster", method = RequestMethod.POST)
    public ResponseEntity<String> loadPoster(@PathVariable("id") Long id,
                                           @RequestParam("poster") MultipartFile file,
                                           @RequestHeader("token") String token) {
        Long companyId = companyService.findByToken(token).getId();
        if (companyService.isOwner(companyId, id)) {
            PosterDTO poster = posterService.loadPoster(file);
            StockDTO stock = stockService.findById(id);
            stock.setPosterId(poster.getId());
            stockService.save(stock);
            return ResponseEntity.ok("{}");
         } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", id));
        }
    }

    @RequestMapping(path = "/api/company/stock/{id}/poster", method = RequestMethod.GET)
    public ResponseEntity<Resource> getPoster(@PathVariable("id") Long id,
                                              @RequestHeader("token") String token) {
        Long companyId = companyService.findByToken(token).getId();
        if (companyService.isOwner(companyId, id)) {
            StockDTO stock = stockService.findById(id);
            PosterDTO poster = posterService.findById(stock.getPosterId());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(poster.getDataType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + poster.getFileName() + "\"")
                    .body(new ByteArrayResource(poster.getPoster()));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", id));
        }
    }

    // ------ Admin methods ------

//    @ApiOperation(value = "Generate promo-codes to stock.",
//            notes = "Returning stock with id specified in request",
//            response = StockDTO.class)
//    @ApiResponses(value = {
//            @ApiResponse(code = 404,
//                    message = "Stock not found",
//                    response = ApiException.class),
//            @ApiResponse(code = 406,
//                    message = "Some DB problems",
//                    response = ApiException.class)
//    })
//    @RequestMapping(path = "/admin/company/stock/generate/{id}", method = RequestMethod.POST)
//    public ResponseEntity<StockDTO> generate(@PathVariable("id") Long id) {
//        StockDTO stock = stockService.findById(id);
//        if (Objects.isNull(stock.getIsAlive())) {
//            companyService.verify(stock.getCompanyId());
//            return ResponseEntity.ok(promoCodeService.savePromoCodes(stockService.setActive(id, true)));
//        }
//        throw new ApiStockActivationStatusException(String.format(
//                "Stock with id: %d is already %s", id, stock.getIsAlive() ? "activated" : "deactivated"));
//    }


    @ApiOperation(value = "Deactivate stock.",
            notes = "Returning stock with id specified in request",
            response = StockDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/company/stock/active/{id}", method = RequestMethod.POST)
    public ResponseEntity<StockDTO> deactivateStock(@PathVariable("id") Long id,
                                                    @RequestParam("activation_status") StockStatus activationStatus) {
        return ResponseEntity.ok(stockService.setActive(id, activationStatus));
    }


    @ApiOperation(value = "Get stock by id",
            notes = "Returning stock with id specified in request",
            response = StockDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/stock/{id}", method = RequestMethod.GET)
    public ResponseEntity<StockDTO> getStockById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(stockService.findById(id));
    }

    @ApiOperation(value = "Delete stock by id",
            notes = "Deleting stock, whose id specified in params",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/stock/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteStockById(@PathVariable("id") final Long id) {
        stockService.deleteById(id);
        return ResponseEntity.ok("{}");
    }

    @ApiOperation(value = "Set new status for stock",
            notes = "Set confirmed without prepay",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/stock/{id}/set/confirm/without/prepay", method = RequestMethod.POST)
    public ResponseEntity<String> setConfirmedWithoutPrepay(@PathVariable("id") final Long id) {
        stockService.setActive(id, StockStatus.POSTER_CONFIRMED_WITHOUT_PREPAY);
        return ResponseEntity.ok("{}");
    }

    @ApiOperation(value = "Set new status for stock",
            notes = "Set confirmed with prepay, but not active",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/stock/{id}/set/confirm/with/prepay/inactively", method = RequestMethod.POST)
    public ResponseEntity<String> setConfirmedWithPrepayNotActive(@PathVariable("id") final Long id) {
        stockService.setActive(id, StockStatus.POSTER_CONFIRMED_WITH_PREPAY_NOT_ACTIVE);
        return ResponseEntity.ok("{}");
    }

    @ApiOperation(value = "Set new status for stock",
            notes = "Set active",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/stock/{id}/set/active", method = RequestMethod.POST)
    public ResponseEntity<String> setStockActive(@PathVariable("id") final Long id) {
        stockService.setActive(id, StockStatus.ACTIVE);
        return ResponseEntity.ok("{}");
    }

    @ApiOperation(value = "Set new status for stock",
            notes = "Set stock is over, without postpay",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/stock/{id}/set/over/without/postpay", method = RequestMethod.POST)
    public ResponseEntity<String> setStockOverWithoutPostpay(@PathVariable("id") final Long id) {
        stockService.setActive(id, StockStatus.STOCK_IS_OVER_WITHOUT_POSTPAY);
        return ResponseEntity.ok("{}");
    }


    @ApiOperation(value = "Set new status for stock",
            notes = "Set stock is over, with postpay",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/stock/{id}/set/over/with/postpay", method = RequestMethod.POST)
    public ResponseEntity<String> setStockOverWithPostpay(@PathVariable("id") final Long id) {
        stockService.setActive(id, StockStatus.STOCK_IS_OVER_WITH_POSTPAY);
        return ResponseEntity.ok("{}");
    }

    @ApiOperation(value = "Set new status for stock",
            notes = "Set active",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/stock/{id}/set/ban", method = RequestMethod.POST)
    public ResponseEntity<String> setStockBan(@PathVariable("id") final Long id) {
        stockService.setActive(id, StockStatus.BAN);
        return ResponseEntity.ok("{}");
    }
}
