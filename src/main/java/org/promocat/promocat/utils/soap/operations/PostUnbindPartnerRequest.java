package org.promocat.promocat.utils.soap.operations;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.promocat.promocat.constraints.XmlField;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:11 24.07.2020
 */
@Data
@AllArgsConstructor
public final class PostUnbindPartnerRequest {

    /**
     * ИНН налогоплательщика.
     */
    @XmlField("Inn")
    private String inn;

}
