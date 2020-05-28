package org.promocat.promocat.data_entities.stock.city_stock;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.city.City;
import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.data_entities.stock.Stock;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 18:23 27.05.2020
 * Промежуточная сущность, которая отвечает за связь между акцией и городами.
 */
@Entity
@Table(name = "stock_city")
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StockCity extends AbstractEntity {

    private Stock stock;
    private Long numberOfPromoCodes;
    private City city;
    private Set<PromoCode> promoCodes;

    /**
     * Акция.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    public Stock getStock() {
        return stock;
    }

    /**
     * Количество промокодов.
     */
    @Column(name = "number_of_promo_codes")
    public Long getNumberOfPromoCodes() {
        return numberOfPromoCodes;
    }

    /**
     * Город.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    public City getCity() {
        return city;
    }

    /**
     * Промокоды.
     */
    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<PromoCode> getPromoCodes() {
        return promoCodes;
    }
}
