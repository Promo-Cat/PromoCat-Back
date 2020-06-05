package org.promocat.promocat.data_entities.user;

import org.promocat.promocat.data_entities.AbstractAccountRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Repository
public interface UserRepository extends AbstractAccountRepository<User> {
}
