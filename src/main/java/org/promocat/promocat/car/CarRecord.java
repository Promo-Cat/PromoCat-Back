package org.promocat.promocat.car;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.car_number.CarNumberRecord;
import org.promocat.promocat.user.UserRecord;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * @author maksimgrankin
 */
@Data
@Entity
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Table(name = "car")
public class CarRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column
    private String car_make;

    @NotBlank
    @Column
    private String color;

    @ManyToOne
    // TODO JsonManager/JsonBack
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "user_id")
    private UserRecord user;

    @OneToOne(mappedBy = "car")
    private CarNumberRecord number;
}
