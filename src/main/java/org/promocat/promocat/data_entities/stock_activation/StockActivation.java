package org.promocat.promocat.data_entities.stock_activation;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.stock.stock_city.StockCity;
import org.promocat.promocat.data_entities.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "promocode_activation")
public class StockActivation extends AbstractEntity {


    private User user;
    private StockCity stockCity;
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    @ManyToOne
    @JoinColumn(name = "stock_city_id")
    public StockCity getStockCity() {
        return stockCity;
    }

    @Column(name = "date")
    public LocalDateTime getDate() {
        return date;
    }
}
