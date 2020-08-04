package org.promocat.promocat.utils.soap.attributes;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 10:49 24.07.2020
 */
public class ConnectionPermissions {

    /**
     * Отражение дохода от моего имени.
     */
    public static String INCOME_REGISTRATION = "INCOME_REGISTRATION";

    /**
     * Получение информации по моим налоговым начислениям.
     */
    public static String PAYMENT_INFORMATION = "PAYMENT_INFORMATION";

    /**
     * Оплата налоговых начислений от моего имени.
     */
    public static String TAX_PAYMENT = "TAX_PAYMENT";

    /**
     * Получение информации по моим доходам
     */
    public static String INCOME_LIST = "INCOME_LIST";

    /**
     * Получение сводной информации о доходе
     */
    public static String INCOME_SUMMARY = "INCOME_SUMMARY";

    /**
     * Корректировка сведений о моих доходах, поданных Партнером
     */
    public static String CANCEL_INCOME = "CANCEL_INCOME";

    /**
     * Корректировка сведений о моих доходах без ограничений
     */
    public static String CANCEL_ANY_INCOME = "CANCEL_ANY_INCOME";

    /**
     * Корректировка сведений моего профиля
     */
    public static String TAXPAYER_UPDATE = "TAXPAYER_UPDATE";

    /**
     * Управление Партнерами
     */
    public static String PERMISSIONS_MGMT = "PERMISSIONS_MGMT";
}
