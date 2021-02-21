package org.promocat.promocat.data_entities.receipt;

import org.promocat.promocat.attributes.ReceiptCancelReason;
import org.promocat.promocat.data_entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    List<Receipt> getAllByUser(User user);
    Optional<Receipt> getByReceiptId(String receiptId);

}
