package org.promocat.promocat.data_entities.user_ban;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserBanRepository extends JpaRepository<UserBan, Long> {

    List<UserBan> getByUserIdAndStockId(Long userId, Long stockId);

    List<UserBan> getAllByUserId(Long userId);

    List<UserBan> getAllByUserIdOrderByBanDateTime(Long userId);

    void deleteAllByUserIdAndStockId(Long userId, Long stockId);
}
