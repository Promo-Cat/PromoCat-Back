package org.promocat.promocat.data_entities.promo_code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    @NotBlank(message = "Промокод не может быть пустым")
    @Column
    private String promo_code;

    /**
     * Id акции.
     */
    @NotBlank(message = "Id акции не может быть пустым")
    @Column
    private Long stock_id;

    /**
     * Пользователь, у которого активен данный промокод.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserRecord user;

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PromoCodeRecord)) {
            return false;
        }
        PromoCodeRecord that = (PromoCodeRecord) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, promo_code);
    }

    public boolean equalsFields(Object o) {
        PromoCodeRecord that = (PromoCodeRecord) o;

        return equals(o) && Objects.equals(id, that.id) &&
                Objects.equals(promo_code, that.promo_code) &&
                Objects.equals(user, that.user);
    }
}
