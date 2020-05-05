package org.promocat.promocat.car_number;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.car.CarRecord;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * @author maksimgrankin
 */
@Data
@Entity
@NoArgsConstructor(force = true)
public class NumberRecord {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number_id;

    @NotBlank
    @Column
    private String number;

    @NotBlank
    @Column
    private String region;

    @OneToOne(cascade = CascadeType.ALL)
    // TODO JsonManager/JsonBack
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JoinColumn(name = "car_id")
    private CarRecord car;
}
