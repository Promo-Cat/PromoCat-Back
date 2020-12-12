package org.promocat.promocat.data_entities.receipt;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.promocat.promocat.data_entities.abstract_account.AbstractEntity;
import org.promocat.promocat.data_entities.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "receipt")
@Entity
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Receipt extends AbstractEntity {

    private String receiptId;
    private String receiptLink;
    private LocalDateTime dateTime;
    private User user;

    @Column(name = "receipt_id")
    public String getReceiptId() {
        return receiptId;
    }

    @Column(name = "receipt_link")
    public String getReceiptLink() {
        return receiptLink;
    }

    @Column(name = "time")
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }
}
