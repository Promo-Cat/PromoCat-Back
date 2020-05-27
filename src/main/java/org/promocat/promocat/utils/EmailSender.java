package org.promocat.promocat.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.file.Paths;

/**
 * Created by Danil Lyskin at 17:42 23.05.2020
 */
@Service
public class EmailSender {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailSender(final JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void send(String file, Long stockId, Long cnt) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setFrom("devru@promocatcompany.com");
        helper.setTo("lyskin-2013@mail.ru");
        helper.setSubject("Промокоды для акции c id: " + stockId);
        helper.setText("Количество запрошенных промокодов: " + cnt);
        helper.addAttachment("promo-code.txt", Paths.get(file).toFile());
        javaMailSender.send(msg);
    }
}
