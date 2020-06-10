package org.promocat.promocat.data_entities.parameters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ParametersService {

    /**
     * Стандартное значение комисси для инициализации таблицы с параметрами
     */
    // TODO: 05.06.2020 Сделать через инициализатор. @Value подгружается после загрузки контекста.
    private static final Double DEFAULT_PANEL_VALUE = 0.1;

    private final ParametersRepository parametersRepository;

    @Autowired
    public ParametersService(final ParametersRepository parametersRepository) {
        this.parametersRepository = parametersRepository;
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
        return getParameters().getPanel();
    }

    public void setPanel(Double panel) {
        Parameters parameters = getParameters();
        parameters.setPanel(panel);
        parametersRepository.save(parameters);
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
            parameters.setPanel(DEFAULT_PANEL_VALUE);
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
    private Parameters getParameters() {
        return parametersRepository.getOne(1L);
    }
}
