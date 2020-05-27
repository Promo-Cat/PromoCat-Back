package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.city.CityRepository;
import org.promocat.promocat.data_entities.company.CompanyRepository;
import org.promocat.promocat.data_entities.stock.Stock;
import org.promocat.promocat.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:17 14.05.2020
 */
@Component
public class StockMapper extends AbstractMapper<Stock, StockDTO>  {

    private final ModelMapper mapper;
    private final CompanyRepository companyRepository;
    private final CityRepository cityRepository;

    @Autowired
    public StockMapper(final ModelMapper mapper,
                final CompanyRepository companyRepository,
                final CityRepository cityRepository) {
        super(Stock.class, StockDTO.class);
        this.mapper = mapper;
        this.companyRepository = companyRepository;
        this.cityRepository = cityRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Stock.class, StockDTO.class)
                .addMappings(m -> { m.skip(StockDTO::setCompanyId); }).setPostConverter(toDtoConverter());
        mapper.createTypeMap(StockDTO.class, Stock.class)
                .addMappings(m -> { m.skip(Stock::setCompany); }).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Stock source, StockDTO destination) {
        destination.setCompanyId(getCompanyId(source));
    }

    private Long getCompanyId(Stock source) {
        return Objects.isNull(source) || Objects.isNull(source.getCompany()) ? null : source.getCompany().getId();
    }

    @Override
    void mapSpecificFields(StockDTO source, Stock destination) {
        destination.setCompany(companyRepository.findById(source.getCompanyId()).orElse(null));
    }
}
