package org.promocat.promocat;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.io.IOException;

// TODO убрать за собой
@Slf4j
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class PromoCatApplication {

    public static void main(String[] args) {
        try {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            log.error("Не получилось инициализировать firebase", e);
        }
        SpringApplication.run(PromoCatApplication.class, args);
    }

}
