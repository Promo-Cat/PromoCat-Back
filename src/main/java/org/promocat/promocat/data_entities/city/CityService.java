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

    public CityDTO addCity(String[] cityFields) {
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

    public List<CityDTO> addCities(List<String[]> cities) {
        return cities.stream().map(this::addCity).collect(Collectors.toList());
    }

    public boolean needToLoad() {
        return cityRepository.count() == 0;
    }

    public int loadFromFile(Path file) {
        try {
            return addCities(CSVCityReader.readFromStreamReader(new FileReader(file.toFile()))).size();
        } catch (FileNotFoundException e) {
            log.error("Файл {} не найден", file.toAbsolutePath().toString(), e);
        } catch (IOException e) {
            log.error("Ошибка I/O", e);
        }
        return 0;
    }

    public List<CityDTO> getActiveCities() {
        Optional<List<City>> city = cityRepository.findByActiveTrue();
        return city.map(cities -> cities.stream().map(cityMapper::toDto).collect(Collectors.toList())).orElse(new ArrayList<>());
    }

    public CityDTO findByCity(String city) {
        Optional<City> cty = cityRepository.findByCity(city);
        return cityMapper.toDto(cty.orElseThrow(() -> new ApiCityNotFoundException("No such city in db.")));
    }

    public CityDTO setActive(String city) {
        CityDTO dto = findByCity(city);
        dto.setActive(true);
        return cityMapper.toDto(cityRepository.save(cityMapper.toEntity(dto)));
    }
}
