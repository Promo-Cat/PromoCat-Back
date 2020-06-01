package org.promocat.promocat.utils;

/**
 * Интерфейс, который переводит км в деньги
 */
public interface DistanceToMoneyConverter {

    /**
     * Преобразует пройденное расстояние в деньги, с учётом коэффициентов и тд
     * @param distance Дистанция, пройденная в км
     * @return Сколько денег (в неизвестно валюте, но скорее всего в рублях) заработано
     */
    Double convert(Double distance);

}
