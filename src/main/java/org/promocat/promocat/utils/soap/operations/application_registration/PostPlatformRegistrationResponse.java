package org.promocat.promocat.utils.soap.operations.application_registration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 13:06 24.07.2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class PostPlatformRegistrationResponse extends AbstractOperation {

    /**
     * Id партнера.
     */
    @XmlField("PartnerID")
    private String partnerId;

    /**
     * Дата регистрации.
     */
    @XmlField("RegistrationDate")
    private String registrationDate;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return null;
    }
}
