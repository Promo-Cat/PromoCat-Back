package org.promocat.promocat.user;
// Created by Roman Devyatilov (Fr1m3n) in 20:25 05.05.2020

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
