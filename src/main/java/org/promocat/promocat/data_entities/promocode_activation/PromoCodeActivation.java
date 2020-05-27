package org.promocat.promocat.data_entities.promocode_activation;

import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.data_entities.stock.Stock;
import org.promocat.promocat.data_entities.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "promocode_activation")
@Setter
public class PromoCodeActivation extends AbstractEntity {


    private User user;
    private PromoCode promoCode;
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    @ManyToOne
    @JoinColumn(name = "promo_code_id")
    public PromoCode getPromoCode() {
        return promoCode;
    }

    @Column(name = "date")
    public LocalDateTime getDate() {
        return date;
    }
}
