package org.promocat.promocat.data_entities;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbstractAccountRepository<T extends AbstractAccount> extends JpaRepository<T, Long> {

    Optional<T> getByTelephone(String telephone);
    Optional<T> getByToken(String token);

}
