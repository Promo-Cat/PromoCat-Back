package org.promocat.promocat.data_entities.promocode_activation;

import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.data_entities.stock.Stock;
import org.promocat.promocat.data_entities.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "promocode_activation")
public class PromoCodeActivation extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "promo_code_id")
    private PromoCode promoCode;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;

}
