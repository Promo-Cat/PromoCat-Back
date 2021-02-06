package org.promocat.promocat.data_entities.receipt;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.user.UserRepository;
import org.promocat.promocat.dto.ReceiptDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.dto.pojo.ReceiptCancelDTO;
import org.promocat.promocat.exception.receipt.ApiReceiptNotFoundException;
import org.promocat.promocat.exception.user.ApiUserNotFoundException;
import org.promocat.promocat.mapper.ReceiptMapper;
import org.promocat.promocat.utils.soap.SoapClient;
import org.promocat.promocat.utils.soap.operations.SmzPlatformError;
import org.promocat.promocat.utils.soap.operations.income.PostCancelReceiptRequestV2;
import org.promocat.promocat.utils.soap.operations.income.PostCancelReceiptResponseV2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Api(tags = {SpringFoxConfig.RECEIPT})
public class ReceiptService {

    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;
    private final UserRepository userRepository;
    private final SoapClient soapClient;

    public ReceiptService(final ReceiptRepository receiptRepository,
                          final ReceiptMapper receiptMapper,
                          final UserRepository userRepository,
                          final SoapClient soapClient) {
        this.receiptRepository = receiptRepository;
        this.receiptMapper = receiptMapper;
        this.userRepository = userRepository;
        this.soapClient = soapClient;
    }

    public ReceiptDTO save(ReceiptDTO receiptDTO) {
        return receiptMapper.toDto(receiptRepository.save(receiptMapper.toEntity(receiptDTO)));
    }

    public List<ReceiptDTO> getAllByUserTelephone(String telephone) {
        return receiptRepository
                .getAllByUserAndCancelReason(
                        userRepository
                                .getByTelephone(telephone)
                                .orElseThrow(ApiUserNotFoundException::new),
                        null
                ).stream()
                .map(receiptMapper::toDto)
                .collect(Collectors.toList());
    }

    public void cancelReceipt(ReceiptCancelDTO receiptCancelDTO,
                              UserDTO userDTO) {
        ReceiptDTO receiptDTO = receiptMapper.toDto(receiptRepository
                .getByReceiptId(receiptCancelDTO.getReceiptId())
                .orElseThrow(() -> new ApiReceiptNotFoundException("Receipt doesn't found")));
        PostCancelReceiptRequestV2 req = new PostCancelReceiptRequestV2();
        req.setInn(userDTO.getInn());
        req.setMessage(receiptCancelDTO.getReason().toString());
        req.setReceiptId(receiptCancelDTO.getReceiptId());

        PostCancelReceiptResponseV2 resp = (PostCancelReceiptResponseV2) soapClient.send(req);
        log.debug(resp.getRequestResult());

        receiptDTO.setCancelReason(receiptCancelDTO.getReason());
        save(receiptDTO);
        log.info("Чек с id (НПД) {} и id (из БД) {} отменён", receiptCancelDTO.getReceiptId(), receiptDTO.getId());
    }
}
