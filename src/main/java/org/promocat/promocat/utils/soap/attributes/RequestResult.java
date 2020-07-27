package org.promocat.promocat.utils.soap.attributes;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:05 24.07.2020
 */
public enum RequestResult {

    /**
     * Заявка на выдачу прав рассмотрена
     */
    COMPLETED,

    /**
     * Заявка на выдачу прав отклонена
     */
    FAILED,

    /**
     * Заявка на выдачу прав находится на рассмотрении
     */
    IN_PROGRESS
}
