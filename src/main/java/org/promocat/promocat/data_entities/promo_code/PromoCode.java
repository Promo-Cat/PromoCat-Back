package org.promocat.promocat.data_entities.promo_code;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.stock.stock_city.StockCity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
    private Boolean isActive;
    private LocalDateTime activeDate;
    private LocalDateTime deactivateDate;
    private StockCity stockCity;

    /**
     * Промокод.
     */
    @NotBlank(message = "Промокод не может быть пустым.")
    @Column(name = "promo_code", unique = true)
    public String getPromoCode() {
        return promoCode;
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

    /**
     * Дата деактивации промокода.
     */
    @Column(name = "deactivate_date")
    public LocalDateTime getDeactivateDate() {
        return deactivateDate;
    }

    /**
     * Город где активен промокод.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_city_id")
    public StockCity getStockCity() {
        return stockCity;
    }
}
