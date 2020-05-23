package org.promocat.promocat.data_entities.generator;

import java.util.Random;

/**
 * Created by Danil Lyskin at 14:22 16.05.2020
 */
public class Generator {

    // TODO перенести все в utils

    public static final String ALPHABETIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String NUMBERS    = "0123456789";
    private static final Random RND       = new Random(System.currentTimeMillis());

    private static void fill(StringBuilder code, int cnt, String charset, String postfix) {
        for (int i = 0; i < cnt; i++) {
            code.append(charset.charAt(RND.nextInt(charset.length())));
        }
        code.append(postfix);
    }

    public static String generate() {
        StringBuilder code = new StringBuilder();
        fill(code, 4, ALPHABETIC, "-");
        fill(code, 3, ALPHABETIC, "-");
        fill(code, 5, NUMBERS, "");
        return code.toString();
    }
}
