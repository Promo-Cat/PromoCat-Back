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
public final class GetTaxpayerStatusResponse extends AbstractOperation {

    /**
     * Имя НП.
     */
    @XmlField("FirstName")
    private String firstName;

    /**
     * Фамилия НП.
     */
    @XmlField("SecondName")
    private String secondName;

    /**
     * Отчество НП.
     */
    @XmlField("Patronymic")
    private String patronymic;

    /**
     * Имя НП.
     */
    @XmlField("RegistrationTime")
    private String registrationTime;

    /**
     * ОКТМО региона преимущественного ведения деятельности на текущий отчетный период.
     */
    @XmlField("Region")
    private String region;

    /**
     * Телефон НП. Формат: 7XXXXXXXXXX
     */
    @XmlField("Phone")
    private String phone;


    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return null;
    }
}
