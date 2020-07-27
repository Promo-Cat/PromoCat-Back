package org.promocat.promocat.utils.soap.operations.pojo;

import org.promocat.promocat.constraints.XmlField;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:02 27.07.2020
 * Информация по оказанной услуге.
 */
public final class IncomeService {

    /**
     * Цена.
     */
    @XmlField("Amount")
    private Double amount;

    /**
     * Наименование услуги.
     */
    @XmlField("Name")
    private String name;

    /**
     * Количество
     */
    @XmlField("Quantity")
    private Long quantity;


}
