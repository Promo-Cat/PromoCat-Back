package org.promocat.promocat.data_entities.company;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.promocat.promocat.data_entities.company.dto.CompanyDTO;
import org.promocat.promocat.data_entities.stock.StockController;
import org.promocat.promocat.data_entities.stock.StockRecord;
import org.promocat.promocat.data_entities.stock.dto.StockDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Danil Lyskin at 20:42 12.05.2020
 */
@RestController
public class CompanyController {

    private static Logger logger = LoggerFactory.getLogger(CompanyController.class);
    private final CompanyService companyService;

    public static CompanyRecord companyDTOToRecord(CompanyDTO companyDTO) {
        Set<StockRecord> stockRecords = new HashSet<>();
        if (Objects.nonNull(companyDTO.getStocks())) {
            for (StockDTO stock : companyDTO.getStocks()) {
                stockRecords.add(StockController.stockDTOToRecord(stock));
            }
        }
        return new CompanyRecord(companyDTO.getId(), companyDTO.getOrganizationName(), companyDTO.getSupervisorFirstName(),
                companyDTO.getSupervisorSecondName(), companyDTO.getSupervisorPatronymic(), companyDTO.getOgrn(), companyDTO.getInn(),
                companyDTO.getTelephone(), companyDTO.getMail(), companyDTO.getCity(), stockRecords);
    }

    @Autowired
    public CompanyController(final CompanyService companyService) {
        this.companyService = companyService;
    }

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
    @RequestMapping(path = "/auth/company/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CompanyDTO addCompany(@Valid @RequestBody CompanyRecord company) {
        logger.info("Trying to save company with telephone: " + company.getTelephone());
        CompanyDTO res = companyService.save(company);
        System.out.println(res.getStocks());
        return res;
    }

    @RequestMapping(path = "/api/company/getById", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CompanyDTO getCompanyById(@RequestBody Long id) {
        logger.info("Trying to find company with id: " + id);
        return companyService.findById(id);
    }
}
