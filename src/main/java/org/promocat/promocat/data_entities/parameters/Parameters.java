package org.promocat.promocat.data_entities.parameters;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.abstract_account.AbstractEntity;

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
    private Double fare;

    /**
     * Предоплата
     */
    private Double prepayment;

    /**
     * Постоплата
     */
    private Double postpayment;

    @Column(name = "fare")
    public Double getFare() {
        return fare;
    }

    @Column(name = "prepayment")
    public Double getPrepayment() {
        return prepayment;
    }

    @Column(name = "postpayment")
    public Double getPostpayment() {
        return postpayment;
    }

}
