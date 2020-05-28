package org.promocat.promocat.data_entities.movement;

import org.promocat.promocat.data_entities.stock.Stock;
import org.promocat.promocat.data_entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByUserAndAndStock(User user, Stock stock);
}
