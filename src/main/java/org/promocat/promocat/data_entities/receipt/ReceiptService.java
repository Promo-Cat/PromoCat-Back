package org.promocat.promocat.data_entities.receipt;

import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.ReceiptDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.exception.user.ApiUserNotFoundException;
import org.promocat.promocat.mapper.ReceiptMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;
    private final UserRepository userRepository;

    public ReceiptService(final ReceiptRepository receiptRepository,
                          final ReceiptMapper receiptMapper,
                          final UserRepository userRepository) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
        this.userRepository = userRepository;
    }

    public ReceiptDTO save(ReceiptDTO receiptDTO) {
        return receiptMapper.toDto(receiptRepository.save(receiptMapper.toEntity(receiptDTO)));
    }

    public List<ReceiptDTO> getAllByUserTelephone(String telephone) {
        return receiptRepository
                .getAllByUser(
                        userRepository.getByTelephone(telephone).orElseThrow(ApiUserNotFoundException::new)
                ).stream()
                .map(receiptMapper::toDto)
                .collect(Collectors.toList());
    }
}
