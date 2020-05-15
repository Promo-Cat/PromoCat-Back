package org.promocat.promocat.data_entities.car;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * @author maksimgrankin
 */
@Entity
@Table(name = "car", indexes = { @Index (columnList = "number,region", unique = true)})
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@NoArgsConstructor
public class Car extends AbstractEntity {

    private String car_make;
    private String color;
    private User user;
    private String number;
    private String region;

    public Car(String carMake, String color, User user, String number, String region) {
        this.car_make = carMake;
        this.color = color;
        this.user = user;
        this.number = number;
        this.region = region;
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
    @NotBlank(message = "Номер автомобиля не может быть пустым")
    @Column(name = "number")
    public String getNumber() {
        return number;
    }

    /**
     * Регион автомобиля.
     */
    @NotBlank(message = "Регион автомобиля не может быть пустым")
    @Column(name = "region")
    public String getRegion() {
        return region;
    }
}
