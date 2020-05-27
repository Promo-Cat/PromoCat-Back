package org.promocat.promocat.mapper;

import org.promocat.promocat.data_entities.city.City;
import org.promocat.promocat.dto.CityDTO;
import org.springframework.stereotype.Component;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:34 21.05.2020
 */
@Component
public class CityMapper extends AbstractMapper<City, CityDTO> {
    public CityMapper() {
        super(City.class, CityDTO.class);
    }

}
