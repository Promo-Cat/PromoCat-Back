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
import java.util.Set;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@Service
public class CompanyService {
    private final CompanyMapper mapper;
    private final CompanyRepository companyRepository;
    private final StockService stockService;

    @Autowired
    public CompanyService(final CompanyMapper mapper,
                          final CompanyRepository companyRepository,
                          final StockService stockService) {
        this.mapper = mapper;
        this.companyRepository = companyRepository;
        this.stockService = stockService;
    }

    /**
     * Сохранение компании в БД.
     *
     * @param dto объектное представление компании.
     * @return представление комании в БД. {@link CompanyDTO}
     */
    public CompanyDTO save(final CompanyDTO dto) {
        log.info("Trying to save company with telephone: {}", dto.getTelephone());
        return mapper.toDto(companyRepository.save(mapper.toEntity(dto)));
    }

    /**
     * Поиск компании по {@code id}.
     *
     * @param id компании.
     * @return представление компании в БД. {@link CompanyDTO}
     * @throws ApiCompanyNotFoundException если такой компании не существует.
     */
    public CompanyDTO findById(final Long id) {
        Optional<Company> company = companyRepository.findById(id);
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
     *
     * @param telephone номер телефона.
     * @return представление компании в БД. {@link CompanyDTO}
     * @throws ApiCompanyNotFoundException если такой компании не существует.
     */
    public CompanyDTO findByTelephone(final String telephone) {
        Optional<Company> company = companyRepository.findByTelephone(telephone);
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
     *
     * @param organizationName имя организации.
     * @return представление компании в БД. {@link CompanyDTO}
     * @throws ApiCompanyNotFoundException если такой компании не существует.
     */
    public CompanyDTO findByOrganizationName(final String organizationName) {
        Optional<Company> company = companyRepository.findByOrganizationName(organizationName);
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
     *
     * @param mail почта компании.
     * @return представление компании в БД. {@link CompanyDTO}
     * @throws ApiCompanyNotFoundException если такой компании не существует.
     */
    public CompanyDTO findByMail(final String mail) {
        Optional<Company> company = companyRepository.findByMail(mail);
        if (company.isPresent()) {
            log.info("Found company with org mail: {}", mail);
            return mapper.toDto(company.get());
        } else {
            log.warn("No company with org mail: {}", mail);
            throw new ApiCompanyNotFoundException(String.format("No company with such mail: %s in db.", mail));
        }
    }

    /**
     * Получить все акции компании.
     *
     * @param dto объектное представление компании.
     * @return список акций компании {@link Set<StockDTO>}.
     */
    public Set<StockDTO> getAllStocks(final CompanyDTO dto) {
        return dto.getStocks();
    }

    /**
     * Поиск компании по token.
     *
     * @param token уникальный токин.
     * @return представление компании в БД. {@link CompanyDTO}
     * @throws ApiCompanyNotFoundException если такой компании не существует.
     */
    public CompanyDTO findByToken(final String token) {
        return mapper.toDto(companyRepository
                .findByToken(token)
                .orElseThrow(
                        () -> new ApiCompanyNotFoundException(String.format("Company with token %s doesn`t found", token))
                ));
    }

    /**
     * Проверка на принадлежность акции компании.
     *
     * @param companyId компании.
     * @param stockId   акции.
     * @return {@code true} если акция принадлежит компании, {@code false} иначе.
     */
    public boolean isOwner(final Long companyId, final Long stockId) {
        CompanyDTO companyDTO = findById(companyId);
        StockDTO stockDTO = stockService.findById(stockId);
        return companyDTO.getId().equals(stockDTO.getCompanyId());
    }
}
