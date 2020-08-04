package org.promocat.promocat.data_entities.parameters;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.ParametersDTO;
import org.promocat.promocat.mapper.ParametersMapper;
import org.promocat.promocat.utils.EntityUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ParametersService {

    /**
     * Стандартное значение комисси для инициализации таблицы с параметрами
     */
    // TODO: 05.06.2020 Сделать через инициализатор. @Value подгружается после загрузки контекста.
    private static final Double DEFAULT_PANEL_VALUE = 1.0;
    private static final Double DEFAULT_PREPAYMENT_VALUE = 240.0;
    private static final Double DEFAULT_POSTPAYMENT_VALUE = 4.8;

    private final ParametersRepository parametersRepository;
    private final ParametersMapper parametersMapper;

    @Autowired
    public ParametersService(final ParametersRepository parametersRepository,
                             final ParametersMapper parametersMapper) {
        this.parametersRepository = parametersRepository;
        this.parametersMapper = parametersMapper;
        if (initializeParameters()) {
            log.info("Parameters record was not found and initialized.");
        }
    }

    /**
     * Возвращает значение комиссии из бд.
     * Если в бд изначально не было записи параметров - создаст и проинициализирует дефолтными значениями.
     *
     * @return значение комиссии (0 - 100)
     */
    public Double getPanel() {
        return getParameters().getFare();
    }

    public void setPanel(Double panel) {
        ParametersDTO parameters = getParameters();
        parameters.setFare(panel);
        save(parameters);
    }

    public void setPrepayment(Double prepayment) {
        ParametersDTO parameters = getParameters();
        parameters.setPrepayment(prepayment);
        save(parameters);
    }

    public void setPostpayment(Double postpayment) {
        ParametersDTO parameters = getParameters();
        parameters.setPostpayment(postpayment);
        save(parameters);
    }

    public void update(ParametersDTO parametersDTO) {
        ParametersDTO actualParameters = getParameters();
        EntityUpdate.copyNonNullProperties(parametersDTO, actualParameters);
        save(actualParameters);
    }

    public void save(ParametersDTO parametersDTO) {
        parametersRepository.save(parametersMapper.toEntity(parametersDTO));
    }

    /**
     * Проверяет наличие записи параметров в бд, при отсутствии таковой - создаёт.
     *
     * @return true - если запись была только создана, false - если запись была изначально
     */
    private boolean initializeParameters() {
        if (parametersRepository.existsById(1L)) {
            return false;
        } else {
            Parameters parameters = new Parameters();
            parameters.setFare(DEFAULT_PANEL_VALUE);
            parameters.setPostpayment(DEFAULT_POSTPAYMENT_VALUE);
            parameters.setPrepayment(DEFAULT_PREPAYMENT_VALUE);
            parameters.setId(1L);
            parametersRepository.save(parameters);
            return true;
        }
    }

    /**
     * Возвращает запись параметров из БД
     *
     * @return запись, которая хранит в себе параметры приложения
     */
    public ParametersDTO getParameters() {
        return parametersMapper.toDto(parametersRepository.findById(1L).orElseThrow());
    }
}