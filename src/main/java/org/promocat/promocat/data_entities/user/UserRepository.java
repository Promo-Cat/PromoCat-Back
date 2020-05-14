package org.promocat.promocat.data_entities.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getByToken(String token);
    Optional<User> getByTelephone(String telephone);

}
