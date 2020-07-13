package org.promocat.promocat.data_entities.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.car.Car;
import org.promocat.promocat.data_entities.city.City;
import org.promocat.promocat.data_entities.movement.Movement;
import org.promocat.promocat.data_entities.stock.stock_city.StockCity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
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

    private String mail;
    private City city;
    private Long balance = 0L;
    private Set<Car> cars = new HashSet<>();
    private Set<Movement> movements = new HashSet<>();
    private StockCity stockCity;
    private Double totalDistance = 0.0;
    private Double totalEarnings = 0.0;
    private UserStatus status;
    private Boolean termsOfUseStatus;

    public User(String mail, City city, Long balance, StockCity stockCity) {
        this.mail = mail;
        this.city = city;
        this.balance = balance;
        this.stockCity = stockCity;
        this.setAccountType(AccountType.USER);
    }

    /**
     * Имя пользователя.
     */
    @Email
//    @NotBlank(message = "Почта не может быть пустой")
    @Column(name = "mail", unique = true)
    public String getMail() {
        return mail;
    }

    // TODO: 12.07.2020 NotBlank NotNull убрал, что с ними делаем?
    /**
     * Город пользователя.
     */
//    @NotNull(message = "Город не может быть пустой")
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
    public Long getBalance() {
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

    @Enumerated
    @Column(name = "status")
    public UserStatus getStatus() {
        return status;
    }

    /**
     * Статус соглашение с пользователським соглашением.
     */
    @Column(name = "terms_of_use")
    public Boolean getTermsOfUseStatus() {
        return termsOfUseStatus;
    }
}
