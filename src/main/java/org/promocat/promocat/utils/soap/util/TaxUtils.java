package org.promocat.promocat.utils.soap.util;

import java.util.List;

import static org.promocat.promocat.utils.soap.attributes.ConnectionPermissions.CANCEL_INCOME;
import static org.promocat.promocat.utils.soap.attributes.ConnectionPermissions.INCOME_LIST;
import static org.promocat.promocat.utils.soap.attributes.ConnectionPermissions.INCOME_REGISTRATION;
import static org.promocat.promocat.utils.soap.attributes.ConnectionPermissions.PAYMENT_INFORMATION;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 10:53 29.07.2020
 */
public final class TaxUtils {

    /**
     * Список разрешений, которые запрашиваются у налогоплательщика.
     */
    public static final List<String> permissions = List.of(
            INCOME_REGISTRATION,
            PAYMENT_INFORMATION,
            CANCEL_INCOME,
            INCOME_LIST
    );

    public static String reformatPhone(final String phone) {
        String newPhone = phone.replace("+", "");
        newPhone = newPhone.replace("(", "");
        newPhone = newPhone.replace(")", "");
        newPhone = newPhone.replace("-", "");
        return newPhone;
    }
}
