package org.promocat.promocat.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor(force = true)
@Entity
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.TABLE)
    private long user_id;

    @NotBlank
    @Column
    private String first_name;

    @NotBlank
    @Column
    private String last_name;

    // Id???
    @NotBlank
    @Column
    private String telephone;

    @NotNull
    @Column
    private long balance;
}
