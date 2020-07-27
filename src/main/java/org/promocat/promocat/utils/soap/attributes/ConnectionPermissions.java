package org.promocat.promocat.utils.soap.attributes;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 10:49 24.07.2020
 */
public enum ConnectionPermissions {

    /**
     * Отражение дохода от моего имени.
     */
    INCOME_REGISTRATION,

    /**
     * Получение информации по моим налоговым начислениям.
     */
    PAYMENT_INFORMATION,

    /**
     * Оплата налоговых начислений от моего имени.
     */
    TAX_PAYMENT,

    /**
     * Получение информации по моим доходам
     */
    INCOME_LIST,

    /**
     * Получение сводной информации о доходе
     */
    INCOME_SUMMARY,

    /**
     * Корректировка сведений о моих доходах, поданных Партнером
     */
    CANCEL_INCOME,

    /**
     * Корректировка сведений о моих доходах без ограничений
     */
    CANCEL_ANY_INCOME,

    /**
     * Корректировка сведений моего профиля
     */
    TAXPAYER_UPDATE,

    /**
     * Управление Партнерами
     */
    PERMISSIONS_MGMT
}
