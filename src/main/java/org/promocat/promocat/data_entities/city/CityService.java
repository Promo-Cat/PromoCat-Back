package org.promocat.promocat.data_entities.city;

import org.promocat.promocat.dto.CityDTO;
import org.promocat.promocat.mapper.CityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CityService {

    private final CityRepository repository;
    private final CityMapper mapper;

    @Autowired
    public CityService(final CityRepository repository, final CityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
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

        return mapper.toDto(repository.save(city));
    }

    public List<CityDTO> addCities(List<String[]> cities) {
        return cities.stream().map(this::addCity).collect(Collectors.toList());
    }

    public List<CityDTO> getActiveCities() {
        Optional<List<City>> city = repository.findByActiveTrue();
        // TODO Exception
        return city.map(cities -> cities.stream().map(mapper::toDto).collect(Collectors.toList())).orElse(null);
    }

    public CityDTO findByCity(String city) {
        Optional<City> cty = repository.findByCity(city);
        // TODO Exception
        return mapper.toDto(cty.orElse(null));
    }

    public CityDTO setActive(String city) {
        CityDTO dto = findByCity(city);
        dto.setActive(true);
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }
}
