package org.promocat.promocat.utils.soap.operations.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.constraints.XmlInnerObject;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;
import org.promocat.promocat.utils.soap.operations.pojo.NewlyUnboundTaxpayersInfo;

import java.util.List;

/**
 * Ответ на GetNewlyUnboundTaxpayersRequest.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetNewlyUnboundTaxpayersResponse extends AbstractOperation {

    /**
     * Информация о НП НПД
     */
    @XmlField("Taxpayers")
    @XmlInnerObject(NewlyUnboundTaxpayersInfo.class)
    private List<NewlyUnboundTaxpayersInfo> taxpayers;

    /**
     * Есть ли ещё НП НПД на следующих страницах
     */
    @XmlField("HasMore")
    private boolean hasMore;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return null;
    }
}
