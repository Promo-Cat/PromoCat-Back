package org.promocat.promocat.data_entities.car;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.car_number.CarNumber;
import org.promocat.promocat.data_entities.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * @author maksimgrankin
 */
@Entity
@Table(name = "car")
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@NoArgsConstructor
public class Car extends AbstractEntity {

    private String car_make;
    private String color;
    private User user;
    private CarNumber number;

    public Car(String carmake, String color, User user, CarNumber number) {
        this.car_make = carmake;
        this.color = color;
        this.user = user;
        this.number = number;
    }

    /**
     * Марка автомобиля.
     */
    @NotBlank(message = "Марка машины не может быть пустой")
    @Column(name = "car_make")
    public String getCar_make() {
        return car_make;
    }

    /**
     * Цвет автомобиля.
     */
    @NotBlank(message = "Цвет не может быть пустым")
    @Column(name = "color")
    public String getColor() {
        return color;
    }

    /**
     * Пользователь, у которого данный автомобиль.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    /**
     * Номерной знак автомобиля.
     */
    @OneToOne(mappedBy = "car", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public CarNumber getNumber() {
        return number;
    }
}
