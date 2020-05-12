package org.promocat.promocat.data_entities.company;

import org.promocat.promocat.data_entities.company.dto.CompanyDTO;
import org.promocat.promocat.data_entities.stock.StockRecord;
import org.promocat.promocat.data_entities.stock.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Danil Lyskin at 20:29 12.05.2020
 */
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final StockRepository stockRepository;

    @Autowired
    public CompanyService(final CompanyRepository companyRepository,
                         final StockRepository stockRepository) {
        this.companyRepository = companyRepository;
        this.stockRepository = stockRepository;
    }

    @Transactional
    public CompanyDTO save(CompanyRecord company) {
        CompanyRecord res = companyRepository.save(company);
        if (Objects.nonNull(res.getStocks())) {
            for (StockRecord stock : res.getStocks()) {
                stock.setCompany(res);
                stockRepository.save(stock);
            }
        }

        return new CompanyDTO(res);
    }

    @Transactional
    public CompanyDTO findById(Long id) {
        Optional<CompanyRecord> companyRecord = companyRepository.findById(id);
        if (companyRecord.isPresent()) {
            return new CompanyDTO(companyRecord.get());
        } else {
            throw new UsernameNotFoundException(String.format("No company with such id: %d in db.", id));
        }
    }
}
