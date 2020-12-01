package org.promocat.promocat.utils;

import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.abstract_account.AbstractAccount;
import org.promocat.promocat.data_entities.abstract_account.AbstractAccountRepository;
import org.promocat.promocat.data_entities.admin.AdminRepository;
import org.promocat.promocat.data_entities.company.CompanyRepository;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;

@Component
public class AccountRepositoryManagerImpl implements AccountRepositoryManager {

    private final Map<AccountType, AbstractAccountRepository<? extends AbstractAccount>> map = new EnumMap<>(AccountType.class);

    @Autowired
    public AccountRepositoryManagerImpl(final UserRepository userRepository,
                                        final CompanyRepository companyRepository,
                                        final AdminRepository adminRepository) {
        map.put(AccountType.USER, userRepository);
        map.put(AccountType.COMPANY, companyRepository);
        map.put(AccountType.ADMIN, adminRepository);
    }

    @Override
    public AbstractAccountRepository<? extends AbstractAccount> getRepository(AccountType accountType) {
        return map.get(accountType);
    }
}
