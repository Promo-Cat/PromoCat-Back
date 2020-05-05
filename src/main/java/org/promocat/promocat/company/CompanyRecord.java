package org.promocat.promocat.company;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor(force = true)
@Entity
@Table(name = "company")
public class CompanyRecord {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long company_id;

    @NotBlank
    @Column
    private String company_name;

}
