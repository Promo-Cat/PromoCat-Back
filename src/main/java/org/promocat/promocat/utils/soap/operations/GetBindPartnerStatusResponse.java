package org.promocat.promocat.utils.soap.operations;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.attributes.RequestResult;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:05 24.07.2020
 */
@Data
@AllArgsConstructor
public final class GetBindPartnerStatusResponse {

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

}
