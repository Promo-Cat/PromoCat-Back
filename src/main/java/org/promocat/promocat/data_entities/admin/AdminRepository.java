package org.promocat.promocat.data_entities.admin;

import org.promocat.promocat.data_entities.AbstractAccountRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends AbstractAccountRepository<Admin> {

    boolean existsAdminByTelephone(String telephone);

}
