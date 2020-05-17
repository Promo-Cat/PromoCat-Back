package org.promocat.promocat.data_entities.promo_code;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.stock.Stock;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

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
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
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
}
