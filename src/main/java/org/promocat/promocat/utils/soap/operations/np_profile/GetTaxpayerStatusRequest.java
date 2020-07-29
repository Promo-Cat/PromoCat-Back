package org.promocat.promocat.utils.soap.operations.np_profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:51 27.07.2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class GetTaxpayerStatusRequest extends AbstractOperation {

    /**
     * ИНН НП.
     */
    @XmlField("Inn")
    private String inn;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return GetTaxpayerStatusResponse.class;
    }
}
