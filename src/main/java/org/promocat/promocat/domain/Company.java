package org.promocat.promocat.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor(force = true)
@Entity
public class Company {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long company_id;

    @NotBlank
    @Column
    private String company_name;

}
