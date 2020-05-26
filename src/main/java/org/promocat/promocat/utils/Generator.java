package org.promocat.promocat.utils;

import java.util.Random;

/**
 * Created by Danil Lyskin at 14:22 16.05.2020
 */
public class Generator {

    // TODO перенести все в utils

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
}
