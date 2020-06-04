package org.promocat.promocat.dto;

import lombok.Data;

import java.util.Objects;

@Data
// TODO docs
public class UserStockEarningStatisticDTO {

    private Double distance;
    private Double summary; // всего
    private Double panel; // комиссия так переводится, верю интернету
    private Double income; // доход, с учётом комиссии

    public UserStockEarningStatisticDTO(Double distance, Double summary, Double panel) {
        this.distance = Objects.isNull(distance) ? 0.0 : distance;
        this.summary = Objects.isNull(summary) ? 0.0 : summary;
        this.panel = Objects.isNull(panel) ? 0.0 : panel;
        this.income = this.summary - this.panel;
    }
}
