package org.promocat.promocat.mapper;

import org.modelmapper.ModelMapper;
import org.promocat.promocat.data_entities.company.Company;
import org.promocat.promocat.data_entities.stock.Stock;
import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.dto.StockDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:14 14.05.2020
 */
@Component
public class CompanyMapper extends AbstractMapper<Company, CompanyDTO> {

    private final ModelMapper mapper;
    private final StockMapper stockMapper;

    @Autowired
    public CompanyMapper(final ModelMapper mapper, StockMapper stockMapper) {
        super(Company.class, CompanyDTO.class);
        this.mapper = mapper;
        this.stockMapper = stockMapper;
    }

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(Company.class, CompanyDTO.class)
                .addMappings(m -> {
                    m.skip(CompanyDTO::setCurrentStock);
                }).setPostConverter(toDtoConverter());
        mapper.createTypeMap(CompanyDTO.class, Company.class)
                .addMappings(m -> {
                    m.skip(Company::setCurrentStock);
                }).setPostConverter(toEntityConverter());
    }

    @Override
    public void mapSpecificFields(Company source, CompanyDTO destination) {
        destination.setCurrentStock(stockMapper.toDto(source.getCurrentStock()));
    }

    @Override
    public void mapSpecificFields(CompanyDTO source, Company destination) {
        destination.setCurrentStock(stockMapper.toEntity(source.getCurrentStock()));
    }
}
