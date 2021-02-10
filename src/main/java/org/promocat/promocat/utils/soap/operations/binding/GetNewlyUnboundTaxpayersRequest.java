package org.promocat.promocat.utils.soap.operations.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;

import java.time.ZonedDateTime;

/**
 * Получение списка вновь отвязанных от партнера НП НПД
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetNewlyUnboundTaxpayersRequest extends AbstractOperation {

    /**
     * Дата, начиная с которой отображать вновь отвязанных НП НПД
     */
    @XmlField("From")
    private ZonedDateTime from;

    /**
     * Дата, до которой отображать вновь отвязанных НП НПД
     */
    @XmlField("To")
    private ZonedDateTime to;

    /**
     * Максимальное количество НП НПД на странице
     */
    @XmlField("Limit")
    private Integer limit;

    /**
     * Отступ от начала списка
     */
    @XmlField("Offset")
    private Integer offset;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return null;
    }
}
