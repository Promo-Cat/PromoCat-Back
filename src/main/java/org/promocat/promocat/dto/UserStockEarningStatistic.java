package org.promocat.promocat.dto;

import lombok.Data;

@Data
public class UserStockEarningStatistic {

    private Double summary; // всего
    private Double panel; // комиссия так переводится, верю интернету
    private Double income; // доход, с учётом комиссии

    public UserStockEarningStatistic(Double summary, Double panel) {
        this.summary = summary;
        this.panel = panel;
        this.income = summary - panel;
    }
}
