package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.receipt.Receipt;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.ReceiptDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class ReceiptMapper extends AbstractMapper<Receipt, ReceiptDTO> {

    private final ModelMapper mapper;
    private final UserRepository userRepository;

    @Autowired
    public ReceiptMapper(final ModelMapper mapper, final UserRepository userRepository) {
        super(Receipt.class, ReceiptDTO.class);
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Receipt.class, ReceiptDTO.class)
                .addMappings(m -> m.skip(ReceiptDTO::setUserId))
                .setPostConverter(toDtoConverter());
        mapper.createTypeMap(ReceiptDTO.class, Receipt.class)
                .addMappings(m -> m.skip(Receipt::setUser))
                .setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Receipt source, ReceiptDTO destination) {
        destination.setUserId(getUserId(source));
    }


    private Long getUserId(Receipt source) {
        return Objects.isNull(source) || Objects.isNull(source.getUser()) ? null : source.getUser().getId();
    }

    @Override
    void mapSpecificFields(ReceiptDTO source, Receipt destination) {
        Long id = source.getUserId() == null ? -1 : source.getUserId();
        destination.setUser(userRepository.findById(id).orElse(null));

    }

}
