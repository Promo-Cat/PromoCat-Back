package org.promocat.promocat.utils.soap.operations.income;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:19 27.07.2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class PostCancelReceiptResponse extends AbstractOperation {


    /**
     * Результат запроса
     * DELETED - удалено.
     */
    @XmlField("RequestResult")
    private String requestResult;


    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return null;
    }
}
