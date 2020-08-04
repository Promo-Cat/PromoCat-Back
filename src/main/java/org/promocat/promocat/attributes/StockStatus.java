package org.promocat.promocat.attributes;

/**
 * Created by Danil Lyskin at 10:45 27.06.2020
 */
public enum StockStatus {
    POSTER_NOT_CONFIRMED,
    POSTER_CONFIRMED_WITHOUT_PREPAY,
    POSTER_CONFIRMED_WITH_PREPAY_NOT_ACTIVE,
    ACTIVE,
    STOCK_IS_OVER_WITHOUT_POSTPAY,
    STOCK_IS_OVER_WITH_POSTPAY,
    BAN
}
