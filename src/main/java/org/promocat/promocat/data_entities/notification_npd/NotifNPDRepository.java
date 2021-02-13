package org.promocat.promocat.data_entities.notification_npd;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Danil Lyskin at 10:59 02.02.2021
 */
public interface NotifNPDRepository extends JpaRepository<NotifNPD, Long> {
    Boolean existsByNotifId(String id);
    List<NotifNPD> findAllByUserId(Long id);
    Optional<NotifNPD> findByNotifId(Long id);
}
