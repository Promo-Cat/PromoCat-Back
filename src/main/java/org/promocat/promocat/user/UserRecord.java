package org.promocat.promocat.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.car.CarRecord;

import org.promocat.promocat.promo_code.PromoCodeRecord;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CarRecord> cars = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private PromoCodeRecord promo_code;
}
