package org.promocat.promocat.utils;

import org.promocat.promocat.data_entities.parameters.ParametersService;
import org.promocat.promocat.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class PaymentServiceImpl implements PaymentService {

    private static final double MULTIPLIER = 0.1;
    private final ParametersService parametersService;

    @Autowired
    public PaymentServiceImpl(ParametersService parametersService) {
        this.parametersService = parametersService;
    }

    @Override
    public Double distanceToMoney(Double distance) {
        return distance * parametersService.getParameters().getPanel();
    }

    // TODO: 06.06.2020 логика рассчёта
    @Override
    public Double getPrepayment(StockDTO stockDTO) {
        return parametersService.getParameters().getPrepayment();
    }

    @Override
    public Double getDriversPayment(StockDTO stockDTO) {
        return parametersService.getParameters().getPostpayment();
    }
}
