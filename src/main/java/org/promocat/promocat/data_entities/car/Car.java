package org.promocat.promocat.data_entities.car;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.promocat.promocat.attributes.CarVerifyingStatus;
import org.promocat.promocat.data_entities.abstract_account.AbstractEntity;
import org.promocat.promocat.data_entities.car.car_photo.CarPhoto;
import org.promocat.promocat.data_entities.car.sts.Sts;
import org.promocat.promocat.data_entities.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author maksimgrankin
 */
@Entity
@Table(name = "car")
@EqualsAndHashCode(of = {}, callSuper = true)
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class Car extends AbstractEntity {

    private User user;
    private String number;
    private String region;
    private CarVerifyingStatus verifyingStatus = CarVerifyingStatus.PROCESSING;
    private CarPhoto carPhoto;
    private Sts sts;

    /**
     * Пользователь, у которого данный автомобиль.
     */
    @Cascade({CascadeType.SAVE_UPDATE})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    /**
     * Номерной знак автомобиля.
     */
    @NotBlank(message = "Номер не может быть пустым.")
    @Column(name = "number")
    @Pattern(regexp = "[АВЕКМНОРСТУХ]\\d{3}[АВЕКМНОРСТУХ]{2}", message = "Номер автомобиля задан некорректно.")
    public String getNumber() {
        return number;
    }

    /**
     * Регион автомобиля.
     */
    @NotBlank(message = "Регион не может быть пустым.")
    @Column(name = "region")
    @Pattern(regexp = "\\d{2,3}", message = "Регион автомобиля задан некорректно.")
    public String getRegion() {
        return region;
    }

    /**
     * Статус проверки подленности авто.
     * @see CarVerifyingStatus
     */
    @Enumerated
    @Column(name = "verifying_status")
    public CarVerifyingStatus getVerifyingStatus() {
        return verifyingStatus;
    }

    @Cascade({ CascadeType.ALL })
    @OneToOne
    @JoinColumn(name = "car_photo_id")
    public CarPhoto getCarPhoto() {
        return carPhoto;
    }

    @Cascade({ CascadeType.ALL })
    @OneToOne
    @JoinColumn(name = "sts_id")
    public Sts getSts() {
        return sts;
    }
}
