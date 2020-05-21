package org.promocat.promocat.data_entities.city;

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
public class CityController {

    private final CityService cityService;

    public CityController(final CityService cityService) {
        this.cityService = cityService;
    }

    @RequestMapping(value = "/auth/cities", method = RequestMethod.POST)
    public ResponseEntity<String> uploadCities(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                cityService.addCities(CSVCityReader.readFromFile(file));
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
        return ResponseEntity.ok(cityService.getActiveCities());
    }

    @RequestMapping(value = "/admin/city", method = RequestMethod.POST)
    public ResponseEntity<CityDTO> activateCity() {
        return ResponseEntity.ok(new CityDTO());
    }
}
