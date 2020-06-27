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
import org.promocat.promocat.data_entities.user.UserService;
import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.dto.MovementDTO;
import org.promocat.promocat.dto.PromoCodeActivationDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.pojo.DistanceDTO;
import org.promocat.promocat.dto.pojo.DistanceWithCityDTO;
import org.promocat.promocat.dto.pojo.PromoCodeActivationStatisticDTO;
import org.promocat.promocat.dto.pojo.PromoCodesInCityDTO;
import org.promocat.promocat.dto.pojo.StockCostDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.company.ApiCompanyNotFoundException;
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
// TODO задокуметировать companyId у всех методов статистики. Сейчас не понятно кто передает companyId
public class CompanyController {

    private final CompanyService companyService;
    private final StockService stockService;
    private final PromoCodeActivationService promoCodeActivationService;
    private final MovementService movementService;
    private final UserService userService;

    @Autowired
    public CompanyController(final CompanyService companyService,
                             final StockService stockService,
                             final PromoCodeActivationService promoCodeActivationService,
                             final MovementService movementService,
                             final UserService userService) {
        this.companyService = companyService;
        this.stockService = stockService;
        this.promoCodeActivationService = promoCodeActivationService;
        this.movementService = movementService;
        this.userService = userService;
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
        if (companyService.getAllCompanyByInnAndVerified(company.getInn(), true).isEmpty()) {
            return ResponseEntity.ok(companyService.save(company));
        } else {
            // TODO: 13.06.2020 MAKS EXCEPTION (попытка добавить компанию с существующим ИНН у верифицированной компании)
            throw new ApiCompanyNotFoundException(String.format("There is another verified company with inn %s", company.getInn()));
        }
    }

    @ApiOperation(value = "Update company",
            notes = "Updates company",
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
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class)
    })
    @RequestMapping(path = {"/api/company", "/admin/company"}, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompanyDTO> updateCompany(@Valid @RequestBody CompanyDTO company) {
        // TODO: 17.06.2020 check permissions (company can update only himself)
        if (company.getId() == null) {
            // TODO: 17.06.2020 ApiFieldException
            throw new ApiCompanyNotFoundException("For update operation id must be not null");
        }
        if (companyService.findById(company.getId()) == null) {
            throw new ApiCompanyNotFoundException(String.format("User with id %d doesn`t found", company.getId()));
        }
        return ResponseEntity.ok(companyService.save(company));
    }

    @ApiOperation(value = "Get company, who authorized with token from request header",
            notes = "Registering company with telephone in format +X(XXX)XXX-XX-XX",
            response = CompanyDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Not company`s token",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/api/company", method = RequestMethod.GET)
    public ResponseEntity<CompanyDTO> getCompany(@RequestHeader("token") String token) {
        JwtReader jwtReader = new JwtReader(token);
        AccountType accountType = AccountType.of(jwtReader.getValue("account_type"));
        if (accountType != AccountType.COMPANY) {
            throw new ApiForbiddenException("Account type is not a company.");
        }
        return ResponseEntity.ok(companyService.findByToken(token));
    }

    @ApiOperation(value = "Delete company by id",
            notes = "Deletes company by id from request params",
            response = CompanyDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/company", method = RequestMethod.DELETE)
    public ResponseEntity<CompanyDTO> deleteCompany(@RequestParam("id") Long id) {
        CompanyDTO companyDTO = companyService.findById(id);
        companyDTO.getStocks().forEach(
                x -> {
                    x.getMovements().stream()
                            .peek(y -> y.setStockId(null))
                            .forEach(movementService::save);
                    x.getCities().forEach(
                            y -> y.getUsers().stream()
                                    .peek(z -> z.setStockCityId(null))
                                    .forEach(userService::save)
                    );
                }
        );
        companyService.deleteById(companyDTO.getId());
        return ResponseEntity.ok(companyDTO);
    }

    @ApiOperation(value = "Get total number of activated promo-codes.",
            notes = "Getting all activated promo-codes.",
            response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = {"/api/company/stock/{stockId}/promoCodeActivation/summary",
            "/admin/statistic/company/stock/{stockId}/promoCodeActivation/summary"}, method = RequestMethod.GET)
    public ResponseEntity<Long> getSummaryPromoCodeActivation(@PathVariable("stockId") Long stockId,
                                                              @RequestParam(value = "companyId", required = false) Long companyId,
                                                              @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.getCompanyForStatistics(token, companyId);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(promoCodeActivationService.getSummaryCountByStock(stockId));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }


    //TODO если город не активный выдает 0 промокодов(отличие с одним из следующих)
    @ApiOperation(value = "Get total number of activated promo-codes in city.",
            notes = "Getting all activated promo-codes in a given city.",
            response = Long.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = {"/api/company/stock/{stockId}/promoCodeActivation/byCity/{cityId}",
            "/admin/statistic/company/stock/{stockId}/promoCodeActivation/byCity/{cityId}"}, method = RequestMethod.GET)
    public ResponseEntity<Long> getPromoCodeActivationByCity(@PathVariable("stockId") Long stockId,
                                                             @PathVariable("cityId") Long cityId,
                                                             @RequestParam(value = "companyId", required = false) Long companyId,
                                                             @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.getCompanyForStatistics(token, companyId);
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
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = {"/api/company/stock/{stockId}/promoCodeActivation/byCity",
            "/admin/statistic/company/stock/{stockId}/promoCodeActivation/byCity"}, method = RequestMethod.GET)
    public ResponseEntity<List<PromoCodeActivationStatisticDTO>> getPromoCodeActivationByCity(
            @PathVariable("stockId") Long stockId,
            @RequestParam(value = "companyId", required = false) Long companyId,
            @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.getCompanyForStatistics(token, companyId);
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
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = {"/api/company/stock/{stockId}/statistic/byCity/{cityId}",
            "/admin/statistic/company/stock/{stockId}/byCity/{cityId}"}, method = RequestMethod.GET)
    public ResponseEntity<Long> getAmountOfPromoCodesInCity(@PathVariable("stockId") Long stockId,
                                                            @PathVariable("cityId") Long cityId,
                                                            @RequestParam(value = "companyId", required = false) Long companyId,
                                                            @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.getCompanyForStatistics(token, companyId);
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
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = {"/api/company/stock/{stockId}/statistic/total",
            "/admin/statistic/company/stock/{stockId}/total"}, method = RequestMethod.GET)
    public ResponseEntity<Long> getTotalAmountOfPromoCodes(@PathVariable("stockId") Long stockId,
                                                           @RequestParam(value = "companyId", required = false) Long companyId,
                                                           @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.getCompanyForStatistics(token, companyId);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(stockService.getTotalAmountOfPromoCodes(stockId));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }

    //TODO если город не активный, то выдает 404
    @ApiOperation(value = "Get total number of promo-codes in all cities.",
            notes = "Getting all promo-codes in all cities.",
            response = PromoCodesInCityDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = {"/api/company/stock/{stockId}/statistic/byCity",
            "/admin/statistic/company/stock/{stockId}/byCity"}, method = RequestMethod.GET)
    public ResponseEntity<List<PromoCodesInCityDTO>> getAmountOfPromoCodesForEachCity(@PathVariable("stockId") Long stockId,
                                                                                      @RequestParam(value = "companyId", required = false) Long companyId,
                                                                                      @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.getCompanyForStatistics(token, companyId);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(stockService.getAmountOfPromoCodesForEachCity(stockId));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }

    @ApiOperation(value = "Get total mileage for each day.",
            notes = "Getting total mileage.",
            response = DistanceDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = {"/api/company/stock/{stockId}/movements/forEachDay/summary",
            "/admin/statistic/company/stock/{stockId}/movements/forEachDay/summary"}, method = RequestMethod.GET)
    public ResponseEntity<List<DistanceDTO>> getMovementsByStock(@PathVariable("stockId") Long stockId,
                                                                 @RequestParam(value = "companyId", required = false) Long companyId,
                                                                 @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.getCompanyForStatistics(token, companyId);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(movementService.getSummaryMovementsByStockForEachDay(stockId));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }

    @ApiOperation(value = "Get total mileage in all cities for each day.",
            notes = "Getting total mileage in all cities.",
            response = DistanceWithCityDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = {"/api/company/stock/{stockId}/movements/forEachDay",
            "/admin/statistic/company/stock/{stockId}/movements/forEachDay"}, method = RequestMethod.GET)
    public ResponseEntity<List<DistanceWithCityDTO>> getMovementsByStockForEveryCity(@PathVariable("stockId") Long stockId,
                                                                                     @RequestParam(value = "companyId", required = false) Long companyId,
                                                                                     @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.getCompanyForStatistics(token, companyId);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(movementService.getMovementsByStockForEveryCityForEachDay(stockId));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }

    @ApiOperation(value = "Get total mileage in city for each day.",
            notes = "Getting total mileage in given city.",
            response = DistanceWithCityDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "City not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = {"/api/company/stock/{stockId}/movements/forEachDay/byCity/{cityId}",
            "/admin/statistic/company/stock/{stockId}/movements/forEachDay/byCity/{cityId}"}, method = RequestMethod.GET)
    public ResponseEntity<List<DistanceWithCityDTO>> getMovementsByStockAndCity(@PathVariable("stockId") Long stockId,
                                                                                @PathVariable("cityId") Long cityId,
                                                                                @RequestParam(value = "companyId", required = false) Long companyId,
                                                                                @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.getCompanyForStatistics(token, companyId);
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
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = {"/api/company/stock/history",
            "/admin/statistic/company/stock/history"}, method = RequestMethod.GET)
    public ResponseEntity<Set<StockDTO>> getAllStocks(
            @RequestParam(value = "companyId", required = false) Long companyId,
            @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.getCompanyForStatistics(token, companyId);
        return ResponseEntity.ok(companyService.getAllStocks(companyDTO));
    }

    @ApiOperation(value = "Get total mileage in all cities for all time.",
            notes = "Getting total mileage in all cities.",
            response = DistanceWithCityDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = {"/api/company/stock/{stockId}/movements/summary",
            "/admin/statistic/company/stock/{stockId}/movements/summary"}, method = RequestMethod.GET)
    public ResponseEntity<DistanceDTO> getMovementsSummaryByStock(@PathVariable("stockId") Long stockId,
                                                                  @RequestParam(value = "companyId", required = false) Long companyId,
                                                                  @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.getCompanyForStatistics(token, companyId);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(movementService.getSummaryMovementsByStock(stockId));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }

    @ApiOperation(value = "Get total mileage in city for all time.",
            notes = "Getting total mileage in given city.",
            response = DistanceWithCityDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "City not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = {"/api/company/stock/{stockId}/movements/summary/byCity/{cityId}",
            "/admin/statistic/company/stock/{stockId}/movements/summary/byCity/{cityId}"}, method = RequestMethod.GET)
    public ResponseEntity<DistanceWithCityDTO> getMovementsSummaryByStockAndCity(@PathVariable("stockId") Long stockId,
                                                                                 @PathVariable("cityId") Long cityId,
                                                                                 @RequestParam(value = "companyId", required = false) Long companyId,
                                                                                 @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.getCompanyForStatistics(token, companyId);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            List<DistanceWithCityDTO> res = movementService.getMovementsByStockAndCity(stockId, cityId);
            if (res.size() == 1) {
                return ResponseEntity.ok(res.get(0));
            } else {
                return ResponseEntity.ok(new DistanceWithCityDTO(0.0, cityId));
            }
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }

    @ApiOperation(value = "Get total mileage for each city for all time.",
            notes = "Getting total mileage for each city for all time.",
            response = DistanceWithCityDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Stock is not owned by this company.",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Company not found",
                    response = ApiException.class),
            @ApiResponse(code = 404,
                    message = "Stock not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = {"/api/company/stock/{stockId}/movements/summary/byCity",
            "/admin/statistic/company/stock/{stockId}/movements/summary/byCity"}, method = RequestMethod.GET)
    public ResponseEntity<List<DistanceWithCityDTO>> getMovementsSummaryByStockForEachCity(@PathVariable("stockId") Long stockId,
                                                                                           @RequestParam(value = "companyId", required = false) Long companyId,
                                                                                           @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.getCompanyForStatistics(token, companyId);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(movementService.getMovementsByStockAndCity(stockId, null));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
    }

    @RequestMapping(path = {"/api/company/stock/{stockId}/cost",
            "/admin/statistic/company/stock/{stockId}/cost"}, method = RequestMethod.GET)
    public ResponseEntity<StockCostDTO> getStockCost(@PathVariable("stockId") Long stockId,
                                                     @RequestParam(value = "companyId", required = false) Long companyId,
                                                     @RequestHeader("token") String token) {
        CompanyDTO companyDTO = companyService.getCompanyForStatistics(token, companyId);
        if (companyService.isOwner(companyDTO.getId(), stockId)) {
            return ResponseEntity.ok(movementService.getSummaryEarningsStatisticByStock(stockId));
        } else {
            throw new ApiForbiddenException(String.format("The stock: %d is not owned by this company.", stockId));
        }
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
