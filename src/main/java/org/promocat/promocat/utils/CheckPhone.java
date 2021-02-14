package org.promocat.promocat.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by Danil Lyskin at 23:15 14.02.2021
 */
@Slf4j
public class CheckPhone {

    public static boolean isEqual(String userPhone, String npdPhone) {
        StringBuilder phone = new StringBuilder();
        for (int i = 0; i < userPhone.length(); i++) {
            if (Character.isDigit(userPhone.charAt(i))) {
                phone.append(userPhone.charAt(i));
            }
        }

        log.info("Check equals user phoen {} and npd phone {}", phone.toString(), npdPhone);
        return phone.toString().equals(npdPhone);
    }
}
