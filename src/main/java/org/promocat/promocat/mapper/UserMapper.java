package org.promocat.promocat.mapper;

import org.promocat.promocat.data_entities.user.User;
import org.promocat.promocat.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:41 14.05.2020
 */
@Component
public class UserMapper extends AbstractMapper<User, UserDTO> {


    @Autowired
    public UserMapper() {
        super(User.class, UserDTO.class);
    }
}
