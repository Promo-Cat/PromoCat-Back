package org.promocat.promocat.data_entities.notification_npd;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Danil Lyskin at 10:59 02.02.2021
 */
public interface NotifNPDRepository extends JpaRepository<NotifNPD, Long> {
    Boolean existsByNotifId(String id);
}
