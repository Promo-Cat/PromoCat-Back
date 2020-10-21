package org.promocat.promocat.data_entities.stock_activation_code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StockActivationCode extends AbstractEntity {

    private StockCity stockCity;
    private User user;
    private String code;
    private LocalDateTime validUntil;
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_city_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public StockCity getStockCity() {
        return stockCity;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public User getUser() {
        return user;
    }

    @Column(name = "code", nullable = false)
    public String getCode() {
        return code;
    }

    @Column(name = "valid_until")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    @Column(name = "active")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Boolean getActive() {
        return active;
    }
}
