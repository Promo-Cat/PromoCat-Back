package org.promocat.promocat.utils;

import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.abstract_account.AbstractAccount;
import org.promocat.promocat.data_entities.abstract_account.AbstractAccountRepository;

public interface AccountRepositoryManager {

    AbstractAccountRepository<? extends AbstractAccount> getRepository(AccountType accountType);

}
