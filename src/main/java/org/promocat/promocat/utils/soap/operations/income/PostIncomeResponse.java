package org.promocat.promocat.utils.soap.operations.income;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:57 27.07.2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class PostIncomeResponse extends AbstractOperation {

    /**
     * ID чека.
     */
    @XmlField("ReceiptId")
    private String receiptId;

    /**
     * Ссылка на чек.
     */
    @XmlField("Link")
    private String link;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return null;
    }
}
