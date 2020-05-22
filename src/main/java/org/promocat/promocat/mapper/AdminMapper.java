package org.promocat.promocat.mapper;

import org.promocat.promocat.data_entities.admin.Admin;
import org.promocat.promocat.dto.AdminDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 20:16 22.05.2020
 */
@Component
public class AdminMapper extends AbstractMapper<Admin, AdminDTO> {

    @Autowired
    AdminMapper() {
        super(Admin.class, AdminDTO.class);
    }
}
