package org.promocat.promocat.data_entities.user_ban;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBanRepository extends JpaRepository<UserBan, Long> {

    Optional<UserBan> getByUserIdAndStockId(Long userId, Long stockId);

    List<UserBan> getAllByUserId(Long userId);

    List<UserBan> getAllByUserIdOrderByBanDateTime(Long userId);
}
