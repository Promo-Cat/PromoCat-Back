package org.promocat.promocat.mapper;

import org.promocat.promocat.data_entities.AbstractAccount;
import org.promocat.promocat.dto.AbstractAccountDTO;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractAccountMapper extends AbstractMapper<AbstractAccount, AbstractAccountDTO>{
    @Autowired
    public AbstractAccountMapper(Class<AbstractAccount> entityClass, Class<AbstractAccountDTO> dtoClass) {
        super(entityClass, dtoClass);
    }
}
