package org.promocat.promocat.data_entities.company;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.movement.MovementService;
import org.promocat.promocat.data_entities.promocode_activation.PromoCodeActivationService;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.dto.DistanceDTO;
import org.promocat.promocat.dto.DistanceWithCityDTO;
import org.promocat.promocat.dto.PromoCodeActivationDTO;
import org.promocat.promocat.dto.PromoCodeActivationStatisticDTO;
import org.promocat.promocat.dto.PromoCodesInCityDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.security.ApiForbiddenException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.promocat.promocat.utils.JwtReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@RestController
@Api(tags = {SpringFoxConfig.COMPANY})
//TODO return 404 if not found, docs
public class CompanyController {

    private final CompanyService companyService;
    private final StockService stockService;
    private final PromoCodeActivationService promoCodeActivationService;
    private final MovementService movementService;

    @Autowired
    public CompanyController(final CompanyService companyService,
                             final StockService stockService,
                             final PromoCodeActivationService promoCodeActivationService,
                             final MovementService movementService) {
        this.companyService = companyService;
        this.stockService = stockService;
        this.promoCodeActivationService = promoCodeActivationService;
        this.movementService = movementService;
    }

    @ApiOperation(value = "Register company",
            notes = "Registering company with telephone in format +X(XXX)XXX-XX-XX",
            response = CompanyDTO.class,
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
    @RequestMapping(path = "/auth/register/company", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompanyDTO> addCompany(@Valid @RequestBody CompanyDTO company) {
        return ResponseEntity.ok(companyService.save(company));
    }

    @ApiOperation(value = "Get company, who authorized with token from request header",
            notes = "Registering company with telephone in format +X(XXX)XXX-XX-XX",
            response = CompanyDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Not company`s token",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/company", method = RequestMethod.GET)
    public ResponseEntity<CompanyDTO> getCompany(@RequestHeader("token") String token) {
        JwtReader jwtReader = new JwtReader(token);
        String telephone = jwtReader.getValue("telephone");
        AccountType accountType = AccountType.of(jwtReader.getValue("account_type"));
        if (accountType != AccountType.COMPANY) {
            throw new ApiForbiddenException("Account type is not a company.");
        }
        return ResponseEntity.ok(companyService.findByTelephone(telephone));
    }

    @ApiOperation(value = "Get total number of activated promo-codes.",
            notes = "Getting all activated promo-codes.",
            response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/company/stock/{stockId}/promoCodeActivation/summary", method = RequestMethod.GET)
    public ResponseEntity<Long> getSummaryPromoCodeActivation(@PathVariable("stockId") Long stockId,
                                                              @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.findByToken(token);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(promoCodeActivationService.getSummaryCountByStock(stockId));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }

    @ApiOperation(value = "Get total number of activated promo-codes in city.",
            notes = "Getting all activated promo-codes in a given city.",
            response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/company/stock/{stockId}/promoCodeActivation/byCity/{cityId}", method = RequestMethod.GET)
    public ResponseEntity<Long> getPromoCodeActivationByCity(@PathVariable("stockId") Long stockId,
                                                             @PathVariable("cityId") Long cityId,
                                                             @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.findByToken(token);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(promoCodeActivationService.getCountByCityAndStock(cityId, stockId));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }

    @ApiOperation(value = "Get total number of activated promo-codes in all cities.",
            notes = "Getting all activated promo-codes for each city individually.",
            response = PromoCodeActivationDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/company/stock/{stockId}/promoCodeActivation/byCity", method = RequestMethod.GET)
    public ResponseEntity<List<PromoCodeActivationStatisticDTO>> getPromoCodeActivationByCity(
            @PathVariable("stockId") Long stockId,
            @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.findByToken(token);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(promoCodeActivationService.getCountForEveryCityByStock(stockId));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }

    @ApiOperation(value = "Get total number of promo-codes in city.",
            notes = "Getting all promo-codes in a given city.",
            response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/company/stock/{stockId}/statistic/byCity/{cityId}", method = RequestMethod.GET)
    public ResponseEntity<Long> getAmountOfPromoCodesInCity(@PathVariable("stockId") Long stockId,
                                                            @PathVariable("cityId") Long cityId,
                                                            @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.findByToken(token);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(stockService.getAmountOfPromoCodesInCity(stockId, cityId));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }

    @ApiOperation(value = "Get total number of promo-codes.",
            notes = "Getting all promo-codes.",
            response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/company/stock/{stockId}/statistic/total", method = RequestMethod.GET)
    public ResponseEntity<Long> getTotalAmountOfPromoCodes(@PathVariable("stockId") Long stockId,
                                                           @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.findByToken(token);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(stockService.getTotalAmountOfPromoCodes(stockId));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }

    @ApiOperation(value = "Get total number of promo-codes in all cities.",
            notes = "Getting all promo-codes in all cities.",
            response = PromoCodesInCityDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/company/stock/{stockId}/statistic/byCity", method = RequestMethod.GET)
    public ResponseEntity<List<PromoCodesInCityDTO>> getAmountOfPromoCodesForEachCity(@PathVariable("stockId") Long stockId,
                                                                                      @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.findByToken(token);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(stockService.getAmountOfPromoCodesForEachCity(stockId));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }

    @ApiOperation(value = "Get total mileage.",
            notes = "Getting total mileage.",
            response = DistanceDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/company/stock/{stockId}/movements/summary", method = RequestMethod.GET)
    public ResponseEntity<List<DistanceDTO>> getMovementsByStock(@PathVariable("stockId") Long stockId,
                                                                 @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.findByToken(token);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(movementService.getSummaryMovementsByStockForEachDay(stockId));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }

    @ApiOperation(value = "Get total mileage in all cities.",
            notes = "Getting total mileage in all cities.",
            response = DistanceWithCityDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/company/stock/{stockId}/movements", method = RequestMethod.GET)
    public ResponseEntity<List<DistanceWithCityDTO>> getMovementsByStockForEveryCity(@PathVariable("stockId") Long stockId,
                                                                                     @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.findByToken(token);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(movementService.getMovementsByStockForEveryCityForEachDay(stockId));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }

    @ApiOperation(value = "Get total mileage in city.",
            notes = "Getting total mileage in given city.",
            response = DistanceWithCityDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/company/stock/{stockId}/movements/byCity/{cityId}", method = RequestMethod.GET)
    public ResponseEntity<List<DistanceWithCityDTO>> getMovementsByStockAndCity(@PathVariable("stockId") Long stockId,
                                                                                @PathVariable("cityId") Long cityId,
                                                                                @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.findByToken(token);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(movementService.getMovementsByStockAndCityForEachDay(stockId, cityId));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }

    @ApiOperation(value = "Get the history of stocks.",
            notes = "Getting the history of all stocks owned by the company.",
            response = StockDTO.class,
            responseContainer = "Set")
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/company/stock/history", method = RequestMethod.GET)
    public ResponseEntity<Set<StockDTO>> getAllStocks(@RequestHeader("token") String token) {
        return ResponseEntity.ok(companyService.getAllStocks(companyService.findByToken(token)));
    }

    // ------ Admin methods ------

    @ApiOperation(value = "Get company by id",
            notes = "Returning company, which id specified in params",
            response = CompanyDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/company/{id}", method = RequestMethod.GET)
    public ResponseEntity<CompanyDTO> getById(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(companyService.findById(id));
    }

    @ApiOperation(value = "Get company by telephone",
            notes = "Returning company, which telephone specified in params",
            response = CompanyDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/company/telephone", method = RequestMethod.GET)
    public ResponseEntity<CompanyDTO> getByTelephone(@RequestParam("telephone") final String telephone) {
        return ResponseEntity.ok(companyService.findByTelephone(telephone));
    }

    @ApiOperation(value = "Get company by organization name",
            notes = "Returning company, which organization name specified in params",
            response = CompanyDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/company/organizationName", method = RequestMethod.GET)
    public ResponseEntity<CompanyDTO> getByOrganizationName(
            @RequestParam("organizationName") final String organizationName) {
        return ResponseEntity.ok(companyService.findByOrganizationName(organizationName));
    }

    @ApiOperation(value = "Get company by mail",
            notes = "Returning company, which mail specified in params",
            response = CompanyDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/company/mail", method = RequestMethod.GET)
    public ResponseEntity<CompanyDTO> getByMail(@RequestParam("mail") final String mail) {
        return ResponseEntity.ok(companyService.findByMail(mail));
    }

}
