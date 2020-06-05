package org.promocat.promocat.data_entities.parameters;

import lombok.*;
import org.promocat.promocat.data_entities.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "parameters")
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Parameters extends AbstractEntity {

    /**
     * Комиссия
     */
    @Positive
    private Double panel;

    @Column(name = "panel")
    public Double getPanel() {
        return panel;
    }

}
