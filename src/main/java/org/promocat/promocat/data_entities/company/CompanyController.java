package org.promocat.promocat.data_entities.company;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.CompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@RestController
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(final CompanyService companyService) {
        this.companyService = companyService;
    }

    @RequestMapping(path = "/auth/company", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CompanyDTO addCompany(@Valid @RequestBody CompanyDTO company) {
        log.info(String.format("Trying to save company: %s. Organization telephone: %s",
                company.getOrganizationName(), company.getTelephone()));
        return companyService.save(company);
    }
}
