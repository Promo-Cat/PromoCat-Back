package org.promocat.promocat.data_entities.promo_code;

import lombok.*;
import org.promocat.promocat.data_entities.user.UserRecord;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

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
    @NotBlank
    @Column
    private String promo_code;

    /**
     * Пользователь, у которого активен данный промокод.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserRecord user;

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof PromoCodeRecord)) {
            return false;
        }
        PromoCodeRecord promoCodeRecord = (PromoCodeRecord) o;
        return promoCodeRecord.getId().equals(id) && Objects.equals(promoCodeRecord.getPromo_code(), promo_code)
                && Objects.equals(promoCodeRecord.getUser().getId(), user.getId()) && promoCodeRecord.getUser().getId().equals(user.getId());
    }
}
