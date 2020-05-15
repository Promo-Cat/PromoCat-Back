package org.promocat.promocat.mapper;

import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.dto.AbstractAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AbstractAccountMapper extends AbstractMapper<AbstractAccount, AbstractAccountDTO> {

    @Autowired
    public AbstractAccountMapper() {
        super(AbstractAccount.class, AbstractAccountDTO.class);
    }
}
