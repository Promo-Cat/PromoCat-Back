package org.promocat.promocat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// TODO убрать за собой
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class PromoCatApplication {

    public static void main(String[] args) {
        SpringApplication.run(PromoCatApplication.class, args);
    }

}
