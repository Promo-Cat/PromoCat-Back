package org.promocat.promocat.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.file.Path;
import java.util.List;

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

    private String getCity(String city) {
        int ind = 0;
        while (city.charAt(ind) != '$') {
            ind++;
        }
        return city.substring(++ind);
    }

    public void send(List<Path> files) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setFrom("devru@promocatcompany.com");
        helper.setTo("lyskin-2013@mail.ru");
        helper.setSubject("Промокоды");
        helper.setText("");
        for (Path file : files) {
            helper.addAttachment(getCity(file.getFileName().toString()), file.toFile());
        }
        javaMailSender.send(msg);
    }
}
