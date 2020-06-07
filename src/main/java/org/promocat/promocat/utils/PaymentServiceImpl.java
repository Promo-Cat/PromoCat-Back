package org.promocat.promocat.utils;

import org.promocat.promocat.data_entities.movement.MovementService;
import org.promocat.promocat.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentServiceImpl implements PaymentService {

    private static final double MULTIPLIER = 0.1;

    @Override
    public Double distanceToMoney(Double distance) {
        return distance * MULTIPLIER;
    }

    // TODO: 06.06.2020 логика рассчёта
    @Override
    public Double getPrepayment(StockDTO stockDTO) {
        return 5.0;
    }

    @Override
    public Double getDriversPayment(StockDTO stockDTO) {
        return 5.0;
    }
}
