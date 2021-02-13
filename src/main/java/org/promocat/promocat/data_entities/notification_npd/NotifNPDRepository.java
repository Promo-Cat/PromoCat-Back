package org.promocat.promocat.data_entities.notification_npd;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Danil Lyskin at 10:59 02.02.2021
 */
public interface NotifNPDRepository extends JpaRepository<NotifNPD, Long> {
    Boolean existsByNotifIdAndIsOpen(String id, Boolean flag);
    List<NotifNPD> findAllByUserId(Long id);
}
