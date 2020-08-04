package org.promocat.promocat.data_entities.car;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
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
@Table(name = "car", indexes = {@Index(columnList = "number,region", unique = true)})
@EqualsAndHashCode(of = {}, callSuper = true)
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class Car extends AbstractEntity {

    private User user;
    private String number;
    private String region;

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
    public String getNumber() {
        return number;
    }

    /**
     * Регион автомобиля.
     */
    @NotBlank(message = "Регион не может быть пустым.")
    @Column(name = "region")
    public String getRegion() {
        return region;
    }
}
