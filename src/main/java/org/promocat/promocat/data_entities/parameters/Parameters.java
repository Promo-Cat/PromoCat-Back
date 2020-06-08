package org.promocat.promocat.data_entities.parameters;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

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
    private Double panel;

    @Column(name = "panel")
    public Double getPanel() {
        return panel;
    }

}
