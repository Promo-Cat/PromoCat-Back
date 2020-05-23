package org.promocat.promocat.utils;

import org.springframework.beans.factory.annotation.Autowired;
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

    public void send() throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg);

        helper.setTo("lyskin-2013@mail.ru");
        helper.setSubject("PromoCodes");
        helper.addAttachment("promo-code", Paths.get("src", "main", "resources", "promo-code.txt").toFile());
        javaMailSender.send(msg);
    }
}
