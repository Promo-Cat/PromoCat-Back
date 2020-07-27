package org.promocat.promocat.utils.soap.operations.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:11 24.07.2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public final class PostUnbindPartnerRequest extends AbstractOperation {

    /**
     * ИНН налогоплательщика.
     */
    @XmlField("Inn")
    private String inn;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return PostUnbindPartnerResponse.class;
    }
}
