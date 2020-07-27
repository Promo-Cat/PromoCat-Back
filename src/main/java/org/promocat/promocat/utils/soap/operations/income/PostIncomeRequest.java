package org.promocat.promocat.utils.soap.operations.income;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.attributes.IncomeType;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;
import org.promocat.promocat.utils.soap.operations.pojo.IncomeService;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:57 27.07.2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class PostIncomeRequest extends AbstractOperation {

    /**
     * ИНН НП.
     */
    @XmlField("Inn")
    private String inn;

    /**
     * Дата формирования.
     */
    @XmlField("RequestTime")
    private ZonedDateTime requestTime;

    /**
     * Дата расчёта.
     */
    @XmlField("OperationTime")
    private ZonedDateTime operationTime;

    /**
     * Источник/Тип дохода.
     */
    @XmlField("IncomeType")
    private IncomeType incomeType;

    /**
     * ИНН покупателя.
     */
    @XmlField("CustomerInn")
    private String customerInn;

    /**
     * Список услуг.
     */
    @XmlField("Services")
    private List<IncomeService> services;

    /**
     * Общая стоимость оказанных услуг.
     */
    @XmlField("TotalAmount")
    private Double totalAmount;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return PostIncomeResponse.class;
    }
}
