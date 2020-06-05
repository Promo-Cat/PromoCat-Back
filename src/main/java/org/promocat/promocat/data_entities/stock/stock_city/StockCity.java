package org.promocat.promocat.data_entities.stock.stock_city;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.city.City;
import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.data_entities.stock.Stock;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
    @Cascade({CascadeType.SAVE_UPDATE})
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
    @Cascade({CascadeType.SAVE_UPDATE})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    public City getCity() {
        return city;
    }

    @Cascade({CascadeType.ALL})
    @OneToMany(mappedBy = "stockCity", fetch = FetchType.LAZY)
    public Set<PromoCode> getPromoCodes() {
        return promoCodes;
    }
}
