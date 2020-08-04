package org.promocat.promocat.utils.soap.attributes;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:05 24.07.2020
 */
public class RequestResult {

    /**
     * Заявка на выдачу прав рассмотрена
     */
    public static String COMPLETED = "COMPLETED";

    /**
     * Заявка на выдачу прав отклонена
     */
    public static String FAILED = "FAILED";

    /**
     * Заявка на выдачу прав находится на рассмотрении
     */
    public static String IN_PROGRESS = "IN_PROGRESS";
}
