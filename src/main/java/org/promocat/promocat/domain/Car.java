package org.promocat.promocat.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@NoArgsConstructor(force = true)
public class Car {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long car_id;

    @NotBlank
    @Column
    private String car_make;

    @NotBlank
    @Column
    private String color;

}
