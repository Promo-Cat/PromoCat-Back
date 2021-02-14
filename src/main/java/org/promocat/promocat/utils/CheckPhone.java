package org.promocat.promocat.utils;

/**
 * Created by Danil Lyskin at 23:15 14.02.2021
 */
public class CheckPhone {

    public static boolean isEqual(String userPhone, String npdPhone) {
        StringBuilder phone = new StringBuilder();
        for (int i = 0; i < userPhone.length(); i++) {
            if (Character.isDigit(userPhone.charAt(i))) {
                phone.append(userPhone.charAt(i));
            }
        }

        return phone.toString().equals(npdPhone);
    }
}
