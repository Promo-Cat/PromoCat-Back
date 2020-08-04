package org.promocat.promocat.utils;

import org.promocat.promocat.dto.StockDTO;

/**
 * Интерфейс, который переводит км в деньги
 */
public interface PaymentService {

    /**
     * Преобразует пройденное расстояние в деньги, с учётом коэффициентов и тд
     *
     * @param distance Дистанция, пройденная в км
     * @return Сколько денег (в неизвестно валюте, но скорее всего в рублях) заработано
     */
    Double distanceToMoney(Double distance);

    Double getPrepayment(StockDTO stockDTO);

    /**
     * Считает сколько компания должна выплатить водителям за акцию
     *
     * @param stockDTO DTO акции, по которой считается выплата
     * @return сумма выплаты
     */
    Double getDriversPayment(StockDTO stockDTO);

}
