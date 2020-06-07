package org.promocat.promocat.utils;

import net.glxn.qrgen.javase.QRCode;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Danil Lyskin at 14:22 16.05.2020
 */
public class Generator {

    public static final String ALPHABETIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERS    = "0123456789";
    private static final Random RND       = new Random(System.currentTimeMillis());

    private static void fill(StringBuilder code, String charset) {
        code.append(charset.charAt(RND.nextInt(charset.length())));
    }

    public static String generate(final String config) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < config.length(); i++) {
            char c = config.charAt(i);
            if (c == '#') {
                fill(code, ALPHABETIC);
            } else if (c == '%') {
                fill(code, NUMBERS);
            } else {
                code.append(config.charAt(i));
            }
        }
        return code.toString();
    }

    public static BufferedImage generateQRCodeImage(final String barcodeText) {
        ByteArrayOutputStream stream = QRCode
                .from(barcodeText)
                .withSize(250, 250)
                .stream();
        ByteArrayInputStream bis = new ByteArrayInputStream(stream.toByteArray());

        try {
            return ImageIO.read(bis);
        } catch (IOException e) {
            // TODO exception
            throw new UsernameNotFoundException("TODO");
        }
    }
}
