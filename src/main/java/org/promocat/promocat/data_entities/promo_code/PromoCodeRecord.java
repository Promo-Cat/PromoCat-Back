package org.promocat.promocat.data_entities.promo_code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.user.UserRecord;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Table(name = "promo_code")
public class PromoCodeRecord {

    /**
     * id промокода.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Промокод.
     */
    @NotBlank(message = "Промокод не может быть пустым")
    @Column
    private String promo_code;

    /**
     * Пользователь, у которого активен данный промокод.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserRecord user;

    private boolean check(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        return  o instanceof PromoCodeRecord;
    }

    @Override
    public boolean equals(Object o) {
        PromoCodeRecord promoCodeRecord = (PromoCodeRecord) o;

        return check(o) && promoCodeRecord.getId().equals(id);
    }

    public boolean equalsFields(Object o) {
        PromoCodeRecord promoCodeRecord = (PromoCodeRecord) o;

        return check(o) && promoCodeRecord.getId().equals(id) && promoCodeRecord.getUser().equals(user)
                && promoCodeRecord.getPromo_code().equals(promo_code);
    }
}
