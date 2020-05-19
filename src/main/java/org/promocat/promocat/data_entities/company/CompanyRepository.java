package org.promocat.promocat.data_entities.company;

import org.promocat.promocat.data_entities.AbstractAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Repository
public interface CompanyRepository extends AbstractAccountRepository<Company> {

    Optional<Company> findByToken(String token);
    Optional<Company> findByTelephone(String telephone);
    Optional<Company> findByOrganizationName(String organizationName);
    Optional<Company> findByMail(String mail);
}


