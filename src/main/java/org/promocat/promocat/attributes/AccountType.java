package org.promocat.promocat.attributes;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 20:26 15.05.2020
 */
public enum AccountType {

    ADMIN(0L, "Admin"),
    USER(1L, "User"),
    COMPANY(2L, "Company");

    AccountType(Long id, String type) {
        this.id = id;
        this.type = type;
    }

    private Long id;
    private String type;

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
