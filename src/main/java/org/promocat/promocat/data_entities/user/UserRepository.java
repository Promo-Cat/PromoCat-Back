package org.promocat.promocat.data_entities.user;

import org.promocat.promocat.data_entities.AbstractAccountRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
public interface UserRepository extends AbstractAccountRepository<User> {
}
