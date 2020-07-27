package org.promocat.promocat.utils.soap.operations.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.attributes.RequestResult;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:05 24.07.2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class GetBindPartnerStatusResponse extends AbstractOperation {

    /**
     * Результат запроса.
     */
    @XmlField("Result")
    private RequestResult result;

    /**
     * ИНН налогоплательщика.
     */
    @XmlField("Inn")
    private String inn;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return null;
    }
}
