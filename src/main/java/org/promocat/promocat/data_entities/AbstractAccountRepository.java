package org.promocat.promocat.data_entities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.MappedSuperclass;
import java.util.Optional;

@NoRepositoryBean
public interface AbstractAccountRepository<T extends AbstractAccount> extends JpaRepository<T, Long> {

    Optional<T> getByTelephone(String telephone);
    Optional<T> getByToken(String token);

}
