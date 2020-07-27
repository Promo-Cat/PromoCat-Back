package org.promocat.promocat.utils.soap.operations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:35 24.07.2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class PostBindPartnerWithPhoneResponse extends AbstractOperation {

    /**
     * ID заявки на подключение.
     */
    @XmlField("Id")
    private String id;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return null;
    }
}
