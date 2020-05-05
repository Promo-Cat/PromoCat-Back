package org.promocat.promocat.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

/**
 * @author maksimgrankin
 */
@Data
@Entity
@NoArgsConstructor(force = true)
public class Number {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long number_id;

    @NotBlank
    @Column
    private String number;

    @NotBlank
    @Column
    private String region;
}
