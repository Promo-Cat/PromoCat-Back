package org.promocat.promocat.data_entities.promocode_activation;

import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.data_entities.stock.stock_city.StockCityService;
import org.promocat.promocat.dto.*;
import org.promocat.promocat.mapper.PromoCodeActivationMapper;
import org.promocat.promocat.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PromoCodeActivationService {

    private final PromoCodeActivationRepository promoCodeActivationRepository;
    private final PromoCodeActivationMapper promoCodeActivationMapper;
    private final StockService stockService;
    private final StockMapper stockMapper;

    @Autowired
    public PromoCodeActivationService(final PromoCodeActivationRepository promoCodeActivationRepository,
                                      final PromoCodeActivationMapper promoCodeActivationMapper,
                                      final StockService stockService,
                                      final StockMapper stockMapper) {
        this.promoCodeActivationRepository = promoCodeActivationRepository;
        this.promoCodeActivationMapper = promoCodeActivationMapper;
        this.stockService = stockService;
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
                .map(x -> stockMapper.toDto(x.getPromoCode().getStockCity().getStock()))
                .collect(Collectors.toList());
    }

    public Long getCountByCityAndStock(Long cityId, Long stockId) {
        return promoCodeActivationRepository.countByCityAndStock(cityId, stockId);
    }

    public Long getSummaryCountByStock(Long stockId) {
        return promoCodeActivationRepository.countAllByStock(stockId);
    }

    public List<PromoCodeActivationStatisticDTO> getCountForEveryCityByStock(Long stockId) {
        StockDTO stock = stockService.findById(stockId);
        return stock.getCities()
                .stream()
                .map(x -> new PromoCodeActivationStatisticDTO(x.getCityId(), x.getPromoCodes()
                        .stream()
                        .filter(PromoCodeDTO::getIsActive)
                        .count()))
                .collect(Collectors.toList());
    }
}
