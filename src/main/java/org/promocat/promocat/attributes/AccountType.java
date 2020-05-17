package org.promocat.promocat.attributes;

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

    private final Long id;
    private final String type;

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

}
