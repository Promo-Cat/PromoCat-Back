package org.promocat.promocat.data_entities.news_feed;

import org.promocat.promocat.attributes.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Danil Lyskin at 21:14 31.10.2020
 */

@Repository
public interface NewsFeedRepository extends JpaRepository<NewsFeed, Long> {

    Optional<NewsFeed> findById(Long id);

    Optional<List<NewsFeed>> findAllByType(AccountType type);
}
