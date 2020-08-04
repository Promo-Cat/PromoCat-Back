package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.company.Company;
import org.promocat.promocat.data_entities.stock.StockRepository;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.dto.CompanyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Objects;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:14 14.05.2020
 */
@Component
public class CompanyMapper extends AbstractMapper<Company, CompanyDTO> {

    private final ModelMapper mapper;
    private final StockRepository stockRepository;

    @Autowired
    public CompanyMapper(final ModelMapper mapper,
                         final StockRepository stockRepository) {
        super(Company.class, CompanyDTO.class);
        this.mapper = mapper;
        this.stockRepository = stockRepository;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Company.class, CompanyDTO.class)
                .addMappings(m -> {
                    m.skip(CompanyDTO::setCurrentStockId);
                }).setPostConverter(toDtoConverter());
        mapper.createTypeMap(CompanyDTO.class, Company.class)
                .addMappings(m -> {
                    m.skip(Company::setCurrentStock);
                }).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Company source, CompanyDTO destination) {
        destination.setCurrentStockId(getStockId(source));
    }

    private Long getStockId(Company source) {
        return Objects.isNull(source) || Objects.isNull(source.getCurrentStock()) ? 0L : source.getCurrentStock().getId();

    }

    @Override
    public void mapSpecificFields(CompanyDTO source, Company destination) {
        Long currentStockId = source.getCurrentStockId() == null ? 0L : source.getCurrentStockId();
        destination.setCurrentStock(stockRepository.findById(currentStockId).orElse(null));
    }
}
