package org.promocat.promocat.data_entities.city;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.dto.CityDTO;
import org.promocat.promocat.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = {SpringFoxConfig.CITY})
@Slf4j
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(final CityService cityService) {
        this.cityService = cityService;
    }

    @ApiOperation(value = "Get active cities.",
            notes = "Returning all active cities.",
            response = CityDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "City not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/auth/cities/active", method = RequestMethod.GET)
    public ResponseEntity<List<CityDTO>> getActiveCities() {
        return ResponseEntity.ok(cityService.getActiveCities());
    }

    @ApiOperation(value = "Get all cities.",
            notes = "Returning all cities.",
            response = CityDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/auth/cities", method = RequestMethod.GET)
    public ResponseEntity<List<CityDTO>> getAllCities() {
        return ResponseEntity.ok(cityService.getAllCities());
    }

    // ------ Admin methods ------

    @ApiOperation(value = "Get information about city.",
            notes = "Returning information about city, which id specified in request.",
            response = CityDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "City not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/city/{id}", method = RequestMethod.GET)
    public ResponseEntity<CityDTO> getCityById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cityService.findById(id));
    }

    @ApiOperation(value = "Get information about city.",
            notes = "Returning information about city, which name specified in request.",
            response = CityDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "City not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/city", method = RequestMethod.GET)
    public ResponseEntity<CityDTO> getCity(@RequestParam("city") String city) {
        return ResponseEntity.ok(cityService.findByCity(city));
    }

    @ApiOperation(value = "Activate city.",
            notes = "Activates city and returns information about city, which name specified in request.",
            response = CityDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "City not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/city/active", method = RequestMethod.PUT)
    public ResponseEntity<CityDTO> activateCity(@RequestParam("city") String city) {
        return ResponseEntity.ok(cityService.setActive(city));
    }

    @ApiOperation(value = "Activate city by id.",
            notes = "Activates city and returns information about city, which name specified in request.",
            response = CityDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "City not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(value = "/admin/city/active/{id}", method = RequestMethod.PUT)
    public ResponseEntity<CityDTO> activateCityById(@PathVariable("id") Long id) {
        CityDTO city = cityService.findById(id);
        return ResponseEntity.ok(cityService.setActive(city.getCity()));
    }
}
