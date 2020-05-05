package org.promocat.promocat.car_number;
// Created by Roman Devyatilov (Fr1m3n) in 20:23 05.05.2020

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NumberService {

    final NumberRepository numberRepository;

    @Autowired
    public NumberService(final NumberRepository numberRepository) {
        this.numberRepository = numberRepository;
    }
}
