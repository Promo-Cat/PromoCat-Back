package org.promocat.promocat.data_entities.promo_code;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.data_entities.car_number.CarNumberRecord;
import org.promocat.promocat.data_entities.user.UserRecord;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Table(name = "promo_code")
public class PromoCodeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column
    private String promo_code;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserRecord user;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PromoCodeRecord)) {
            return false;
        }
        PromoCodeRecord promoCodeRecord = (PromoCodeRecord) o;
        return promoCodeRecord.getId().equals(id) && promoCodeRecord.getPromo_code().equals(promo_code)
               && promoCodeRecord.getUser().getId().equals(user.getId());
    }
}
