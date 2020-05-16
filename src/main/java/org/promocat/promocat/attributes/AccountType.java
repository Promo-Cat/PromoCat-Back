package org.promocat.promocat.attributes;

import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.company.Company;
import org.promocat.promocat.data_entities.user.User;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 20:26 15.05.2020
 */
public enum AccountType {

    USER(0L, "User", User.class),
    COMPANY(1L, "Company", Company.class),
    ADMIN(2L, "Admin", User.class);

    AccountType(Long id, String type, Class<? extends AbstractAccount> accountEntity) {
        this.id = id;
        this.type = type;
        this.accountEntity = accountEntity;
    }

    private final Class<? extends AbstractAccount> accountEntity;
    private final Long id;
    private final String type;

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Class<? extends AbstractAccount> getAccountEntity() {
        return accountEntity;
    }
}
