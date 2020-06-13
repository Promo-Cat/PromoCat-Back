package org.promocat.promocat.data_entities.user;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.car.Car;
import org.promocat.promocat.data_entities.city.City;
import org.promocat.promocat.data_entities.movement.Movement;
import org.promocat.promocat.data_entities.promo_code.PromoCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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

    private String name;
    private City city;
    private Long balance = 0L;
    private Set<Car> cars = new HashSet<>();
    private Set<Movement> movements = new HashSet<>();
    private PromoCode promoCode;
    private Double totalDistance = 0.0;
    private Double totalEarnings = 0.0;

    public User(String name, City city, Long balance, PromoCode promoCode) {
        this.name = name;
        this.city = city;
        this.balance = balance;
        this.promoCode = promoCode;
        this.setAccountType(AccountType.USER);
    }

    /**
     * Имя пользователя.
     */
    @NotBlank(message = "Имя не может быть пустым")
    @Column(name = "name")
    public String getName() {
        return name;
    }

    /**
     * Город пользователя.
     */
    @NotNull(message = "Город не может быть пустой")
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
    @OneToOne
    @JoinColumn(name = "promo_code_id")
    public PromoCode getPromoCode() {
        return promoCode;
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
}
