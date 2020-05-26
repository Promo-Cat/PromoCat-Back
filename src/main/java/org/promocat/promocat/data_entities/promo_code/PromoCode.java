package org.promocat.promocat.data_entities.promo_code;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.stock.Stock;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Entity
@Table(name = "promo_code")
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PromoCode extends AbstractEntity {

    private String promoCode;
    private Stock stock;
    private Boolean isActive;
    private LocalDateTime activeDate;

    /**
     * Промокод.
     */
    @NotBlank(message = "Промокод не может быть пустым.")
    @Column(name = "promo_code", unique = true)
    public String getPromoCode() {
        return promoCode;
    }

    /**
     * Id акции.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    public Stock getStock() {
        return stock;
    }

    /**
     * Активность промокода
     */
    @Column(columnDefinition = "boolean default false", name = "is_active")
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     * Дата активации промокода.
     */
     @NotNull(message = "Дата активации промокода не может быть пустой.")
     @Column(name = "active_date")
     public LocalDateTime getActiveDate() {
         return activeDate;
     }
}
