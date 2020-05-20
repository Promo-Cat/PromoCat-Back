package org.promocat.promocat.data_entities.company;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@RestController
public class CompanyController {

    private final CompanyService service;

    @Autowired
    public CompanyController(final CompanyService service) {
        this.service = service;
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
        log.info(String.format("Trying to save company: %s. Organization telephone: %s",
                company.getOrganizationName(), company.getTelephone()));
        return service.save(company);
    }

    // ------ Admin methods ------

    @RequestMapping(path = "/admin/company/id", method = RequestMethod.GET)
    public ResponseEntity<CompanyDTO> getById(@RequestParam("id") Long id) {
        log.info(String.format("Admin trying to get company with id: %d", id));
        return ResponseEntity.ok(service.findById(id));
    }

    @RequestMapping(path = "/admin/company/telephone", method = RequestMethod.GET)
    public ResponseEntity<CompanyDTO> getByTelephone(@RequestParam("telephone") String telephone) {
        log.info(String.format("Admin trying to get company with telephone: %s", telephone));
        return ResponseEntity.ok(service.findByTelephone(telephone));
    }

    @RequestMapping(path = "/admin/company/organizationName", method = RequestMethod.GET)
    public ResponseEntity<CompanyDTO> getByOrganizationName(@RequestParam("organizationName") String organizationName) {
        log.info(String.format("Admin trying to get company with organization name: %s", organizationName));
        return ResponseEntity.ok(service.findByOrganizationName(organizationName));
    }

    @RequestMapping(path = "/admin/company/mail", method = RequestMethod.GET)
    public ResponseEntity<CompanyDTO> getByMail(@RequestParam("mail") String mail) {
        log.info(String.format("Admin trying to get company with mail: %s", mail));
        return ResponseEntity.ok(service.findByMail(mail));
    }
}
