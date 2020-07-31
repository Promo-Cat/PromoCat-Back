package org.promocat.promocat.data_entities.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.attributes.UserStatus;
import org.promocat.promocat.constraints.RequiredForFull;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.car.Car;
import org.promocat.promocat.data_entities.city.City;
import org.promocat.promocat.data_entities.movement.Movement;
import org.promocat.promocat.data_entities.stock.stock_city.StockCity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

/**
 * @author maksimgrankin
 */
@Entity
@Table(name = "users")
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractAccount {

    private City city;
    private Double balance = 0.0;
    private Set<Car> cars = new HashSet<>();
    private Set<Movement> movements = new HashSet<>();
    private StockCity stockCity;
    private Double totalDistance = 0.0;
    private Double totalEarnings = 0.0;
    private UserStatus status;
    private String account;
    private String inn;
    private String taxConnectionId;

    public User(City city, Double balance, StockCity stockCity) {
        this.city = city;
        this.balance = balance;
        this.stockCity = stockCity;
        this.setAccountType(AccountType.USER);
    }

    /**
     * Город пользователя.
     */
    @ManyToOne
    @JoinColumn(name = "city")
    public City getCity() {
        return city;
    }

    /**
     * Баланс пользователя.
     */
    @Min(0)
    @Column(name = "balance")
    public Double getBalance() {
        return balance;
    }

    /**
     * Автомобили пользователя.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    public Set<Car> getCars() {
        return cars;
    }

    /**
     * Передвижения пользователя участвующего в акции.
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
    public Set<Movement> getMovements() {
        return movements;
    }

    /**
     * Действующий промокод.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_city_id")
    public StockCity getStockCity() {
        return stockCity;
    }

    /**
     * Общий заработок пользователя за все время.
     */
    @Column(name = "total_earnings")
    public Double getTotalEarnings() {
        return totalEarnings;
    }

    /**
     * Общее расстояние, которое проехал пользователь за все время.
     */
    @Column(name = "total_distance")
    public Double getTotalDistance() {
        return totalDistance;
    }

    /**
     * Статус пользователя.
     * {@code FULl} если пользователь указал все обязательные поля.
     * {@code JUST_REGISTERED} если пользователь указал не все обязательные поля.
     * {@code BANNED} если пользователь забанен
     */
    @Enumerated
    @Column(name = "status")
    public UserStatus getStatus() {
        return status;
    }

    /**
     * Расчетный счет.
     */
    @Pattern(regexp = "\\d{5}.\\d{3}.\\d{1}.\\d{11}",
            message = "Расчетный счет должен соответствовать шаблону: XXXXX.XXX.X.XXXXXXXXXXX")
    @Column(name = "account", unique = true)
    public String getAccount() {
        return account;
    }

    /**
     * ИНН
     */
    @Pattern(regexp = "\\d{12}",
            message = "ИНН должен соответствовать шаблону: XXXXXXXXXXXX")
    @Column(name = "inn", unique = true)
    public String getInn() {
        return inn;
    }

    /**
     * ID подключения к Мой налог.
     */
    @Column(name = "tax_connection_id", unique = true)
    public String getTaxConnectionId() {
        return taxConnectionId;
    }
}
