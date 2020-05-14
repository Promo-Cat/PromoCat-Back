package org.promocat.promocat.data_entities.city;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CityService {

    private final CityRepository cityRepository;

    @Autowired
    public CityService(final CityRepository cityRepository) {
        this.cityRepository = cityRepository;
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

        return cityRepository.save(city);
    }

    public List<City> addCities(List<String[]> cities) {
        return cities.stream().map(this::addCity).collect(Collectors.toList());
    }
}
