package org.promocat.promocat.attributes;

import org.promocat.promocat.exception.login.ApiLoginAttemptNotFoundException;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 20:26 15.05.2020
 */
public enum AccountType {

    USER(0L, "User"),
    COMPANY(1L, "Company"),
    ADMIN(2L, "Admin");

    AccountType(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    public static AccountType of(String str) {
        for (AccountType value : values()) {
            if (value.getType().toLowerCase().equals(str.toLowerCase())) {
                return value;
            }
        }
        throw new ApiLoginAttemptNotFoundException("Account type: " + str + " doesn`t exist");
    }

    private final Long id;
    private final String type;

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

}
