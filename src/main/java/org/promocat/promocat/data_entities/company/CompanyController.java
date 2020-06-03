package org.promocat.promocat.data_entities.company;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.promocode_activation.PromoCodeActivationService;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.dto.PromoCodeActivationStatisticDTO;
import org.promocat.promocat.dto.PromoCodesInCityDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.promocat.promocat.utils.JwtReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@RestController
@Api(tags = {SpringFoxConfig.COMPANY})
public class CompanyController {

    private final CompanyService service;
    private final StockService stockService;
    private final PromoCodeActivationService promoCodeActivationService;

    @Autowired
    public CompanyController(final CompanyService service,
                             final StockService stockService,
                             final PromoCodeActivationService promoCodeActivationService) {
        this.service = service;
        this.stockService = stockService;
        this.promoCodeActivationService = promoCodeActivationService;
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
    public CompanyDTO addCompany(@Valid @RequestBody CompanyDTO company) {
        return service.save(company);
    }

    @ApiOperation(value = "Get company, who authorized with token from request header",
            notes = "Registering company with telephone in format +X(XXX)XXX-XX-XX",
            response = CompanyDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 403,
                    message = "Not company`s token"),
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
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.ok(service.findByTelephone(telephone));
    }

    // TODO docs
    @RequestMapping(path = "/company/stock/{stockId}/promoCodeActivation/summary", method = RequestMethod.GET)
    public ResponseEntity<Long> getSummaryPromoCodeActivation(@PathVariable("stockId") Long stockId,
                                                              @RequestHeader("token") String token) {
        CompanyDTO companyDTO = service.findByToken(token).orElseThrow();
        StockDTO stockDTO = stockService.findById(stockId);
        if (Objects.isNull(stockDTO)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!service.isOwner(companyDTO.getId(), stockId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return ResponseEntity.ok(promoCodeActivationService.getSummaryCountByStock(stockId));
        }
    }

    // TODO docs
    // TODO check stock (exists and company is owner) in other method
    @RequestMapping(path = "/company/stock/{stockId}/promoCodeActivation/byCity/{cityId}", method = RequestMethod.GET)
    public ResponseEntity<Long> getPromoCodeActivationByCity(@PathVariable("stockId") Long stockId,
                                                             @PathVariable("cityId") Long cityId,
                                                             @RequestHeader("token") String token) {
        CompanyDTO companyDTO = service.findByToken(token).orElseThrow();
        StockDTO stockDTO = stockService.findById(stockId);
        if (Objects.isNull(stockDTO)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!service.isOwner(companyDTO.getId(), stockId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return ResponseEntity.ok(promoCodeActivationService.getCountByCityAndStock(cityId, stockId));
        }
    }

    // TODO docs
    // TODO check stock (exists and company is owner) in other method
    @RequestMapping(path = "/company/stock/{stockId}/promoCodeActivation/byCity", method = RequestMethod.GET)
    public ResponseEntity<List<PromoCodeActivationStatisticDTO>> getPromoCodeActivationByCity(@PathVariable("stockId") Long stockId,
                                                                                              @RequestHeader("token") String token) {
        CompanyDTO companyDTO = service.findByToken(token).orElseThrow();
        StockDTO stockDTO = stockService.findById(stockId);
        if (Objects.isNull(stockDTO)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!service.isOwner(companyDTO.getId(), stockId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return ResponseEntity.ok(promoCodeActivationService.getCountForEveryCityByStock(stockId));
        }
    }

    // TODO docs
    @RequestMapping(path = "/company/stock/{stockId}/statistic/byCity/{cityId}", method = RequestMethod.GET)
    public ResponseEntity<Long> getAmountOfPromoCodesInCity(@PathVariable("stockId") Long stockId,
                                                            @PathVariable("cityId") Long cityId,
                                                            @RequestHeader("token") String token) {
        CompanyDTO companyDTO = service.findByToken(token).orElseThrow();
        if (!service.isOwner(companyDTO.getId(), stockId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return ResponseEntity.ok(stockService.getAmountOfPromoCodesInCity(stockId, cityId));
        }
    }

    // TODO docs
    @RequestMapping(path = "/company/stock/{stockId}/statistic/total", method = RequestMethod.GET)
    public ResponseEntity<Long> getTotalAmountOfPromoCodes(@PathVariable("stockId") Long stockId,
                                                            @RequestHeader("token") String token) {
        CompanyDTO companyDTO = service.findByToken(token).orElseThrow();
        if (!service.isOwner(companyDTO.getId(), stockId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return ResponseEntity.ok(stockService.getTotalAmountOfPromoCodes(stockId));
        }
    }

    // TODO docs
    @RequestMapping(path = "/company/stock/{stockId}/statistic/forEachCity", method = RequestMethod.GET)
    public ResponseEntity<List<PromoCodesInCityDTO>> getAmountOfPromoCodesForEachCity(@PathVariable("stockId") Long stockId,
                                                                                      @RequestHeader("token") String token) {
        CompanyDTO companyDTO = service.findByToken(token).orElseThrow();
        if (!service.isOwner(companyDTO.getId(), stockId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } else {
            return ResponseEntity.ok(stockService.getAmountOfPromoCodesForEachCity(stockId));
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
    @RequestMapping(path = "/admin/company/id", method = RequestMethod.GET)
    public ResponseEntity<CompanyDTO> getById(@RequestParam("id") Long id) {
        return ResponseEntity.ok(service.findById(id));
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
    public ResponseEntity<CompanyDTO> getByTelephone(@RequestParam("telephone") String telephone) {
        return ResponseEntity.ok(service.findByTelephone(telephone));
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
    public ResponseEntity<CompanyDTO> getByOrganizationName(@RequestParam("organizationName") String organizationName) {
        return ResponseEntity.ok(service.findByOrganizationName(organizationName));
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
    public ResponseEntity<CompanyDTO> getByMail(@RequestParam("mail") String mail) {
        return ResponseEntity.ok(service.findByMail(mail));
    }

}
