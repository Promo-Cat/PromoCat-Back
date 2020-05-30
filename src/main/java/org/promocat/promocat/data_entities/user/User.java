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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private Set<Car> cars;
    private Set<Movement> movements;
    private Long promoCodeId;
    private Long totalDistance;
    private Long totalEarnings;

    public User(String name, City city, Long balance, Long promoCodeId) {
        this.name = name;
        this.city = city;
        this.balance = balance;
        this.promoCodeId = promoCodeId;
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
    @Cascade({CascadeType.ALL})
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    public Set<Car> getCars() {
        return cars;
    }

    /**
     * Передвижения пользователя участвующего в акции.
     */
    @Cascade({CascadeType.ALL})
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    public Set<Movement> getMovements() {
        return movements;
    }

    /**
     * Действующий промокод.
     */
    @Column(name = "promo_code")
    public Long getPromoCodeId() {
        return promoCodeId;
    }

    /**
     * Общее расстояние, которое проехал пользователь за все время.
     */
    @Column(name = "total_distance")
    public Long getTotalDistance() {
        return totalDistance;
    }

    /**
     * Общий заработок пользователя за все время.
     */
    @Column(name = "total_earnings")
    public Long getTotalEarnings() {
        return totalEarnings;
    }
}
