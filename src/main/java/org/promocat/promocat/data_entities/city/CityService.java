package org.promocat.promocat.data_entities.city;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.CityDTO;
import org.promocat.promocat.exception.city.ApiCityNotFoundException;
import org.promocat.promocat.mapper.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CityService {

    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Autowired
    public CityService(final CityRepository cityRepository, final CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    /**
     * Добавление города в базу данных.
     * @param cityFields массив строк с описаием города. (подробнее https://github.com/hflabs/city/blob/master/city.csv)
     * @return объектное представление города в БД {@link CityDTO}.
     */
    private CityDTO addCity(String[] cityFields) {
        City city = new City();
        city.setAddress(cityFields[0]);
        city.setPostalCode(cityFields[1]);
        city.setCountry(cityFields[2]);
        city.setRegion(cityFields[5]);
        city.setCity(cityFields[9]);
        city.setTimezone(cityFields[19]);
        city.setLatitude(cityFields[20]);
        city.setLongitude(cityFields[21]);
        city.setPopulation(cityFields[22]);
        city.setActive(false);

        return cityMapper.toDto(cityRepository.save(city));
    }

    /**
     * Добавление городов в базу данных.
     * @param cities массив городов с описание каждого города
     *               (подробнее https://github.com/hflabs/city/blob/master/city.csv)
     * @return список всех добавленыых городов {@link List<CityDTO>}.
     */
    public List<CityDTO> addCities(List<String[]> cities) {
        log.info("Adding cities");
        return cities.stream().map(this::addCity).collect(Collectors.toList());
    }

    /**
     * Проверка наличия городов бд.
     * @return {@code true} если количество равно {@code 0}, иначе {@code false}.
     */
    public boolean needToLoad() {
        return cityRepository.count() == 0;
    }

    /**
     * Подгружает города из csv файла.
     * @param file путь до csv файла
     * @return кол-во добавленных городов
     */
    public int loadFromFile(Path file) {
        try {
            return addCities(CSVCityReader.readFromStreamReader(new FileReader(file.toFile()))).size();
        } catch (FileNotFoundException e) {
            log.error("File {} not found", file.toAbsolutePath().toString(), e);
        } catch (IOException e) {
            log.error("I/O Error", e);
        }
        return 0;
    }

    /**
     * Получение списка активных городов.
     * @return список активных городов {@link List<CityDTO>}.
     */
    public List<CityDTO> getActiveCities() {
        Optional<List<City>> city = cityRepository.findByActiveTrue();
        log.info("Getting active cities");
        return city.map(cities -> cities.stream().map(cityMapper::toDto).collect(Collectors.toList())).orElse(new ArrayList<>());
    }

    /**
     * Получение информации о городе по названию города.
     * @param city название города.
     * @return объектное представление города в БД. {@link CityDTO}
     * @throws ApiCityNotFoundException если такого города нет в БД.
     */
    public CityDTO findByCity(String city) {
        Optional<City> cty = cityRepository.findByCity(city);
        log.info("Trying to find full info about city: {}", city);
        return cityMapper.toDto(cty.orElseThrow(() -> new ApiCityNotFoundException("No such city in db.")));
    }

    /**
     * Устанавливает город в активное состояние.
     * @param city название города.
     * @return объектное представление города в БД. {@link CityDTO}.
     */
    public CityDTO setActive(String city) {
        CityDTO dto = findByCity(city);
        dto.setActive(true);
        log.info("City: {} activated", city);
        return cityMapper.toDto(cityRepository.save(cityMapper.toEntity(dto)));
    }

    /**
     * TODO
     * @param id
     * @return
     */
    public boolean isActiveById(Long id) {
        City city = cityRepository.findById(id).orElseThrow(() -> new ApiCityNotFoundException("No such city in db."));
        return city.getActive().equals(Boolean.TRUE);
    }
}
