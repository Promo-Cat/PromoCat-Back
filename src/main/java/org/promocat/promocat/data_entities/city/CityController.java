package org.promocat.promocat.data_entities.city;

import io.swagger.annotations.Api;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.dto.CityDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Api(tags = {SpringFoxConfig.CITY})
public class CityController {

    private final CityService service;

    public CityController(final CityService service) {
        this.service = service;
    }

    @RequestMapping(value = "/auth/cities", method = RequestMethod.POST)
    public ResponseEntity<String> uploadCities(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                service.addCities(CSVCityReader.readFromFile(file));
                return ResponseEntity.ok("Города успешно добавлены!");
            } catch (IOException e) {
                return new ResponseEntity<>("Ошибка во время открытия файла", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } else {
            return ResponseEntity.badRequest().body("File is empty");
        }
    }

    @RequestMapping(value = "/auth/cities/active", method = RequestMethod.GET)
    public ResponseEntity<List<CityDTO>> getActiveCities() {
        return ResponseEntity.ok(service.getActiveCities());
    }

    // ------ Admin methods ------

    @RequestMapping(value = "/admin/city", method = RequestMethod.GET)
    public ResponseEntity<CityDTO> getCity(@RequestParam("city") String city) {
        return ResponseEntity.ok(service.findByCity(city));
    }

    @RequestMapping(value = "/admin/city/active", method = RequestMethod.PUT)
    public ResponseEntity<CityDTO> activateCity(@RequestParam("city") String city) {
        return ResponseEntity.ok(service.setActive(city));
    }
}
