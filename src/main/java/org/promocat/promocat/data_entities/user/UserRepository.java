package org.promocat.promocat.data_entities.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> getByToken(String token);
    Optional<User> getByTelephone(String telephone);

}
