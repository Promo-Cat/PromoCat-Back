package org.promocat.promocat.data_entities.user;

import org.promocat.promocat.attributes.UserStatus;
import org.promocat.promocat.data_entities.abstract_account.AbstractAccountRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Repository
public interface UserRepository extends AbstractAccountRepository<User> {
    Long countByStatusEqualsAndStockCityNull(final UserStatus status);
    Long countByStockCityNotNull();

    Optional<User> findByGoogleToken(String googleToken);
    Optional<User> findByInn(String inn);

    List<User> getAllByInnNotNullAndTaxConnectionIdNotNull();

    @Query(value="SELECT nextval('giveaway_number_seq')", nativeQuery = true)
    Long getGiveawayNumber();

    List<User> getAllByAccountNotNull();

    List<User> getAllByStatusAndStockCityNull(final UserStatus status);
}
