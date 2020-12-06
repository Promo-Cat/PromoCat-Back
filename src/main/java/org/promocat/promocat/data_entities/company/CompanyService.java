package org.promocat.promocat.data_entities.company;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.abstract_account.AbstractAccountService;
import org.promocat.promocat.data_entities.stock.StockService;
import org.promocat.promocat.dto.CompanyDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.exception.company.ApiCompanyNotFoundException;
import org.promocat.promocat.exception.util.ApiServerErrorException;
import org.promocat.promocat.mapper.CompanyMapper;
import org.promocat.promocat.util_entities.TokenService;
import org.promocat.promocat.utils.FirebaseNotificationManager;
import org.promocat.promocat.utils.TopicGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Slf4j
@Service
public class CompanyService extends AbstractAccountService {
    private final CompanyMapper companyMapper;
    private final CompanyRepository companyRepository;
    private final StockService stockService;
    private final TokenService tokenService;
    private final TopicGenerator topicGenerator;

    @Autowired
    public CompanyService(final CompanyMapper companyMapper,
                          final CompanyRepository companyRepository,
                          final StockService stockService,
                          final TokenService tokenService,
                          final TopicGenerator topicGenerator,
                          final FirebaseNotificationManager firebaseNotificationManager) {
        super(firebaseNotificationManager);
        this.companyMapper = companyMapper;
        this.companyRepository = companyRepository;
        this.stockService = stockService;
        this.tokenService = tokenService;
        this.topicGenerator = topicGenerator;
    }

    /**
     * Сохранение компании в БД.
     *
     * @param dto объектное представление компании.
     * @return представление комании в БД. {@link CompanyDTO}
     */
    public CompanyDTO save(final CompanyDTO dto) {
        log.info("Trying to save company with telephone: {}", dto.getTelephone());
        return companyMapper.toDto(companyRepository.save(companyMapper.toEntity(dto)));
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
            return companyMapper.toDto(company.get());
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
            return companyMapper.toDto(company.get());
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
            return companyMapper.toDto(company.get());
        } else {
            log.warn("No company with org name: {}", organizationName);
            throw new ApiCompanyNotFoundException(String.format("No company with such name: %s in db.", organizationName));
        }
    }

    /**
     * Получить все акции компании.
     *
     * @param dto объектное представление компании.
     * @return список акций компании {@link StockDTO}.
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
        return companyMapper.toDto(companyRepository
                .findByToken(token)
                .orElseThrow(
                        () -> new ApiCompanyNotFoundException(String.format("Company with token %s doesn`t found", token))
                ));
    }

    // TODO продумать навзвание
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

    /**
     * Возвращает DTO компании.
     * Получает {@link AccountType} из {@code token} и в зависимости от типа аккаунта получает либо из токена компании, либо по {@code companyId}.
     *
     * @param token     JWS-токен в котором хранятся данные о пользователе
     * @param companyId nullable id компании
     * @return DTO компании
     */
    public CompanyDTO getCompanyForStatistics(String token, Long companyId) {
        AccountType accountType = tokenService.getAccountType(token);
        if (accountType == AccountType.COMPANY) {
            return findByToken(token);
        } else if (accountType == AccountType.ADMIN) {
            return findById(companyId);
        } else {
            log.error("Non correct account type {}", accountType.getType());
            return null;
        }
    }

    public List<CompanyDTO> getAllCompanyByInn(String inn) {
        return companyRepository.findAllByInn(inn)
                .stream()
                .map(companyMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<CompanyDTO> getAllCompanyByInnAndVerified(String inn, boolean verified) {
        return companyRepository.findAllByInnAndVerified(inn, verified)
                .stream()
                .map(companyMapper::toDto)
                .collect(Collectors.toList());
    }

    public CompanyDTO verify(Long id) {
        CompanyDTO companyDTO = findById(id);
        companyDTO.setVerified(true);
        companyDTO = save(companyDTO);
        getAllCompanyByInn(companyDTO.getInn())
                .stream()
                .peek(x -> log.info("Found company with id {} and inn {}", x.getId(), x.getInn()))
                .filter(x -> !x.getId().equals(id))
                .forEach(x -> {
                    log.info("Deleting company with id {}",x.getId());
                    companyRepository.deleteById(x.getId());
                });
        return companyDTO;
    }

    public boolean existsByTelephone(String telephone) {
        return companyRepository.findByTelephone(telephone).isPresent();
    }

    public void deleteById(Long id) {
        companyRepository.deleteById(id);
    }

    /**
     * Подписывает компанию на "дефолтные темы (topic)"
     * @param company Компания, которая будет подписана на темы {@link CompanyDTO}
     */
    public void subscribeCompanyOnDefaultTopics(CompanyDTO company) {
        if (company.getGoogleToken() == null) {
            throw new ApiServerErrorException("Trying to subscribe company on topics. But company has no google token.");
        }
        subscribeOnTopic(company, topicGenerator.getNewsFeedTopicForCompany());
    }

    /**
     * Отписывает компанию от "дефолтных тем (topic)"
     * @param company Компания, которая будет отписана от тем {@link CompanyDTO}
     */
    public void unsubscribeCompanyFromDefaultTopics(CompanyDTO company) {
        if (company.getGoogleToken() == null) {
            throw new ApiServerErrorException("Trying to unsubscribe company from topics. But company has no google token.");
        }
        unsubscribeFromTopic(company, topicGenerator.getNewsFeedTopicForCompany());
    }

}
