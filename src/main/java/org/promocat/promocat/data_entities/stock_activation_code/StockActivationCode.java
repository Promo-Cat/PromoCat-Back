package org.promocat.promocat.data_entities.stock_activation_code;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.stock.stock_city.StockCity;
import org.promocat.promocat.data_entities.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "stock_activation_code")
public class StockActivationCode extends AbstractEntity {

    private StockCity stockCity;
    private User user;
    private String code;
    private LocalDateTime validUntil;
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_city_id")
    public StockCity getStockCity() {
        return stockCity;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    @Column(name = "code", nullable = false)
    public String getCode() {
        return code;
    }

    @Column(name = "valid_until")
    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    @Column(name = "active")
    public Boolean getActive() {
        return active;
    }
}
