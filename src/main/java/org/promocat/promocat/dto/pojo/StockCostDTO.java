package org.promocat.promocat.dto.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockCostDTO {

    private Double prepayment;
    private Double driversPayment;

}
