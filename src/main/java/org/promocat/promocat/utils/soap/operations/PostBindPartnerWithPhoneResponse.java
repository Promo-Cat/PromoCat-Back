package org.promocat.promocat.utils.soap.operations;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.promocat.promocat.constraints.XmlField;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:35 24.07.2020
 */
@Data
@AllArgsConstructor
public final class PostBindPartnerWithPhoneResponse {

    /**
     * ID заявки на подключение.
     */
    @XmlField("Id")
    private String id;

}
