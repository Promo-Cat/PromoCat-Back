package org.promocat.promocat.utils.soap.operations.application_registration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:35 27.07.2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class PostPlatformRegistrationRequest extends AbstractOperation {

    /**
     * Наименование партнера.
     */
    @XmlField("PartnerName")
    private String partnerName;

    /**
     * Тип партнера.
     * {@code BANK} – банк-Партнер,
     * {@code PARTNER} – платформа-Партнер.
     */
    @XmlField("PartnerType")
    private String partnerType;

    /**
     * Может ли НП подключаться сам.
     * {@code true} если может, {@code false} иначе.
     */
    @XmlField("PartnerConnectable")
    private String partnerConnectable;

    /**
     * ИНН партнера.
     */
    @XmlField("Inn")
    private String inn;

    /**
     * Описание партнера.
     */
    @XmlField("PartnerDescription")
    private String partnerDescription;

    /**
     * Текст с описанием от партнера.
     */
    @XmlField("PartnersText")
    private String partnersText;

    /**
     * Ссылка на приложение/сайт партнера.
     */
    @XmlField("TransitionLink")
    private String transitionLink;

    /**
     * Телефон партнера. Формат +7XXXXXXXXXX
     */
    @XmlField("Phone")
    private String phone;

    /**
     * Логотип партнера в base64.
     */
    @XmlField("PartnerImage")
    private String partnerImage;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return PostPlatformRegistrationResponse.class;
    }
}
