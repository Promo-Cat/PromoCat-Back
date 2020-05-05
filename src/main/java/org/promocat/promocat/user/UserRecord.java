package org.promocat.promocat.User;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.Car.CarRecord;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * @author maksimgrankin
 */
@Data
@NoArgsConstructor(force = true)
@Entity
public class UserRecord {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @NotBlank
    @Column
    private String first_name;

    @NotBlank
    @Column
    private String last_name;

    @NotBlank
    @Pattern(regexp = "\\+7\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}")
    @Column(unique = true)
    private String telephone;

    @NotNull
    @Column
    private Long balance;

    @OneToMany(mappedBy = "user")
    private List<CarRecord> car = new ArrayList<>();
}
