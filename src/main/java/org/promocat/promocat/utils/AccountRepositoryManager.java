package org.promocat.promocat.utils;

import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.data_entities.AbstractAccountRepository;

public interface AccountRepositoryManager {

    AbstractAccountRepository<? extends AbstractAccount> getRepository(AccountType accountType);

}
