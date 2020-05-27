package org.promocat.promocat.data_entities.promocode_activation;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.data_entities.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "promocode_activation")
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
