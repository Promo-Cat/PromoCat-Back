package org.promocat.promocat.utils.soap.operations.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;

import java.time.ZonedDateTime;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewlyUnboundTaxpayersInfo {

    /**
     * ИНН пользователя
     */
    @XmlField("Inn")
    private String inn;

    /**
     * Имя пользователя
     */
    @XmlField("FirstName")
    private String firstName;

    /**
     * Фамилия пользователя
     */
    @XmlField("SecondName")
    private String secondName;

    /**
     * Отчество пользователя
     */
    @XmlField("Patronymic")
    private String patronymic;

    /**
     * Дата отвязки
     */
    @XmlField("UnboundTime")
    private ZonedDateTime unboundTime;

    /**
     * Дата постановки на учёт
     */
    @XmlField("RegistrationTime")
    private ZonedDateTime registrationTime;

    /**
     * Номер телефона
     */
    @XmlField("Phone")
    private String phone;

}
