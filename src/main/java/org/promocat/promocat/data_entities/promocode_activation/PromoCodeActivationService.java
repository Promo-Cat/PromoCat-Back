package org.promocat.promocat.data_entities.promocode_activation;

import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.dto.PromoCodeActivationDTO;
import org.promocat.promocat.dto.PromoCodeDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.UserDTO;
import org.promocat.promocat.mapper.PromoCodeActivationMapper;
import org.promocat.promocat.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PromoCodeActivationService {

    private final PromoCodeActivationRepository promoCodeActivationRepository;
    private final PromoCodeActivationMapper promoCodeActivationMapper;
    private final StockMapper stockMapper;

    @Autowired
    public PromoCodeActivationService(final PromoCodeActivationRepository promoCodeActivationRepository,
                                      final PromoCodeActivationMapper promoCodeActivationMapper,
                                      final StockMapper stockMapper) {
        this.promoCodeActivationRepository = promoCodeActivationRepository;
        this.promoCodeActivationMapper = promoCodeActivationMapper;
        this.stockMapper = stockMapper;
    }

    public PromoCodeActivationDTO create(UserDTO user, PromoCodeDTO promoCode) {
        PromoCodeActivationDTO res = new PromoCodeActivationDTO();
        res.setPromoCodeId(promoCode.getId());
        res.setUserId(user.getId());
        res.setDate(LocalDateTime.now());
        return promoCodeActivationMapper.toDto(promoCodeActivationRepository.save(promoCodeActivationMapper.toEntity(res)));
    }

    public List<StockDTO> getStocksByUserId(Long id) {
        return promoCodeActivationRepository.getAllByUserId(id)
                .stream()
                .map(x -> stockMapper.toDto(x.getPromoCode().getStock()))
                .collect(Collectors.toList());
    }
}
