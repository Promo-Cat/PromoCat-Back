package org.promocat.promocat.data_entities.company;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.exception.company.ApiCompanyNotFoundException;
import org.promocat.promocat.mapper.CompanyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@Service
public class CompanyService {
    private final CompanyMapper mapper;
    private final CompanyRepository repository;
    private final StockService stockService;

    @Autowired
    public CompanyService(final CompanyMapper mapper,
                          final CompanyRepository repository,
                          final StockService stockService) {
        this.mapper = mapper;
        this.repository = repository;
        this.stockService = stockService;
    }

    /**
     * Сохранение компании в БД.
     * @param dto объектное представление компании.
     * @return представление комании в БД. {@link CompanyDTO}
     */
    public CompanyDTO save(final CompanyDTO dto) {
        log.info("Trying to save company with telephone: {}", dto.getTelephone());
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    /**
     * Поиск компании по {@code id}.
     * @param id компании.
     * @return представление компании в БД. {@link CompanyDTO}
     * @throws ApiCompanyNotFoundException если такой компании не существует.
     */
    public CompanyDTO findById(final Long id) {
        Optional<Company> company = repository.findById(id);
        if (company.isPresent()) {
            log.info("Found company with id: {}", id);
            return mapper.toDto(company.get());
        } else {
            log.warn("No company with id: {}", id);
            throw new ApiCompanyNotFoundException(String.format("No company with such id: %d in db.", id));
        }
    }

    /**
     * Поиск компании по номеру телефона.
     * @param telephone номер телефона.
     * @return представление компании в БД. {@link CompanyDTO}
     * @throws ApiCompanyNotFoundException если такой компании не существует.
     */
    public CompanyDTO findByTelephone(final String telephone) {
        Optional<Company> company = repository.findByTelephone(telephone);
        if (company.isPresent()) {
            log.info("Found company with telephone: {}", telephone);
            return mapper.toDto(company.get());
        } else {
            log.warn("No company with telephone: {}", telephone);
            throw new ApiCompanyNotFoundException(String.format("No company with such telephone: %s in db.", telephone));
        }
    }

    /**
     * Поиск компании по имени организации.
     * @param organizationName имя организации.
     * @return представление компании в БД. {@link CompanyDTO}
     * @throws ApiCompanyNotFoundException если такой компании не существует.
     */
    public CompanyDTO findByOrganizationName(final String organizationName) {
        Optional<Company> company = repository.findByOrganizationName(organizationName);
        if (company.isPresent()) {
            log.info("Found company with org name: {}", organizationName);
            return mapper.toDto(company.get());
        } else {
            log.warn("No company with org name: {}", organizationName);
            throw new ApiCompanyNotFoundException(String.format("No company with such name: %s in db.", organizationName));
        }
    }

    /**
     * Поиск компании по почте.
     * @param mail почта компании.
     * @return представление компании в БД. {@link CompanyDTO}
     * @throws ApiCompanyNotFoundException если такой компании не существует.
     */
    public CompanyDTO findByMail(final String mail) {
        Optional<Company> company = repository.findByMail(mail);
        if (company.isPresent()) {
            log.info("Found company with org mail: {}", mail);
            return mapper.toDto(company.get());
        } else {
            log.warn("No company with org mail: {}", mail);
            throw new ApiCompanyNotFoundException(String.format("No company with such mail: %s in db.", mail));
        }
    }

    public Optional<CompanyDTO> findByToken(final String token) {
        return Optional.of(mapper.toDto(repository.findByToken(token).orElseThrow()));
    }

    public boolean isOwner(Long companyId, Long stockId) {
        CompanyDTO companyDTO = findById(companyId);
        StockDTO stockDTO = stockService.findById(stockId);
        return companyDTO.getId().equals(stockDTO.getCompanyId());
    }
}
