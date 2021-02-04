package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.notification_npd.NotifNPD;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.NotifNPDDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * Created by Danil Lyskin at 10:35 02.02.2021
 */

@Component
public class NotifNPDMapper extends AbstractMapper<NotifNPD, NotifNPDDTO> {
    private final ModelMapper mapper;
    private final UserRepository userRepository;

    @Autowired
    public NotifNPDMapper(final ModelMapper mapper, final UserRepository userRepository) {
        super(NotifNPD.class, NotifNPDDTO.class);
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(NotifNPD.class, NotifNPDDTO.class)
                .addMappings(m -> m.skip(NotifNPDDTO::setUserId)).setPostConverter(toDtoConverter());
        mapper.createTypeMap(NotifNPDDTO.class, NotifNPD.class)
                .addMappings(m -> m.skip(NotifNPD::setUser)).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(NotifNPD source, NotifNPDDTO destination) {
        destination.setUserId(getUserId(source));
    }

    private Long getUserId(NotifNPD source) {
        return Objects.isNull(source) || Objects.isNull(source.getUser()) ? null : source.getUser().getId();
    }

    @Override
    void mapSpecificFields(NotifNPDDTO source, NotifNPD destination) {
        destination.setUser(userRepository.findById(source.getUserId()).orElse(null));
    }
}
