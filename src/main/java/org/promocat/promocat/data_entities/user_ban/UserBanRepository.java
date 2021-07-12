package org.promocat.promocat.data_entities.user_ban;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserBanRepository extends JpaRepository<UserBan, Long> {

    List<UserBan> getByUserIdAndStockId(Long userId, Long stockId);

    List<UserBan> getAllByUserId(Long userId);

    List<UserBan> getAllByUserIdOrderByBanDateTime(Long userId);

    @Transactional
    void deleteAllByUserIdAndStockId(Long userId, Long stockId);

    Boolean existsByUserIdAndStockId(Long userId, Long stockId);
}
