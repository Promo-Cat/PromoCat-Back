package org.promocat.promocat.data_entities.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CityService {

    private final CityRepository repository;

    @Autowired
    public CityService(final CityRepository repository) {
        this.repository = repository;
    }

    public City addCity(String[] cityFields) {
        City city = new City();
//        for (String cityField : cityFields) {
//            System.out.print(cityField + ",");
//        }
//        System.out.println();
        city.setAddress(cityFields[0]);
        city.setPostal_code(cityFields[1]);
        city.setCountry(cityFields[2]);
        city.setRegion(cityFields[5]);
        city.setCity(cityFields[9]);
        city.setTimezone(cityFields[19]);
        city.setLatitude(cityFields[20]);
        city.setLongitude(cityFields[21]);
        city.setPopulation(cityFields[22]);
        city.setActive(false);

        if (cityFields[1].equals("385200") || cityFields[1].equals("649000")) {
            city.setActive(true);
        }
        return repository.save(city);
    }

    public List<City> addCities(List<String[]> cities) {
        return cities.stream().map(this::addCity).collect(Collectors.toList());
    }

    public List<City> getActiveCities() {
        Optional<List<City>> city = repository.findByActiveTrue();
        return city.orElse(null);
    }
}
