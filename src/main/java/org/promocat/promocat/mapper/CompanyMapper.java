package org.promocat.promocat.mapper;

import org.promocat.promocat.data_entities.company.Company;
import org.promocat.promocat.dto.CompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:14 14.05.2020
 */
@Component
public class CompanyMapper extends AbstractMapper<Company, CompanyDTO> {

    @Autowired
    public CompanyMapper() {
        super(Company.class, CompanyDTO.class);
    }
}
