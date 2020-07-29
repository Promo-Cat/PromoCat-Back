package org.promocat.promocat.data_entities.receipt;

import org.promocat.promocat.dto.ReceiptDTO;
import org.promocat.promocat.mapper.ReceiptMapper;
import org.springframework.stereotype.Service;

@Service
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;

    public ReceiptService(final ReceiptRepository receiptRepository,
                          final ReceiptMapper receiptMapper) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
    }

    public ReceiptDTO save(ReceiptDTO receiptDTO) {
        return receiptMapper.toDto(receiptRepository.save(receiptMapper.toEntity(receiptDTO)));
    }
}
