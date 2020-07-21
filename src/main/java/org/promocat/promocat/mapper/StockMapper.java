package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.company.CompanyRepository;
import org.promocat.promocat.data_entities.stock.Stock;
import org.promocat.promocat.data_entities.stock.poster.PosterRepository;
import org.promocat.promocat.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:17 14.05.2020
 */
@Component
public class StockMapper extends AbstractMapper<Stock, StockDTO> {

    private final ModelMapper mapper;
    private final CompanyRepository companyRepository;
    private final PosterRepository posterRepository;

    @Autowired
    public StockMapper(final ModelMapper mapper,
                       final CompanyRepository companyRepository,
                       final PosterRepository posterRepository) {
        super(Stock.class, StockDTO.class);
        this.mapper = mapper;
        this.companyRepository = companyRepository;
        this.posterRepository = posterRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Stock.class, StockDTO.class)
                .addMappings(m -> {
                    m.skip(StockDTO::setCompanyId);
                    m.skip(StockDTO::setPosterId);
                }).setPostConverter(toDtoConverter());
        mapper.createTypeMap(StockDTO.class, Stock.class)
                .addMappings(m -> {
                    m.skip(Stock::setCompany);
                    m.skip(Stock::setPoster);
                }).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Stock source, StockDTO destination) {
        destination.setCompanyId(getCompanyId(source));
        destination.setPosterId(getPosterId(source));
    }

    private Long getCompanyId(Stock source) {
        return Objects.isNull(source) || Objects.isNull(source.getCompany()) ? 0L : source.getCompany().getId();
    }

    private Long getPosterId(Stock source) {
        return Objects.isNull(source) || Objects.isNull(source.getPoster()) ? 0L : source.getPoster().getId();
    }

    @Override
    void mapSpecificFields(StockDTO source, Stock destination) {
        destination.setCompany(companyRepository.findById(source.getCompanyId()).orElse(null));
        Long posterId = source.getPosterId() == null ? 0 : source.getPosterId();
        destination.setPoster(posterRepository.findById(posterId).orElse(null));
    }
}
