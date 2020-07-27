package org.promocat.promocat.utils.soap.operations.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:02 27.07.2020
 * Информация по оказанной услуге.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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
