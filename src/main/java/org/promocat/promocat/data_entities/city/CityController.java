package org.promocat.promocat.data_entities.city;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.dto.CityDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api(tags = {SpringFoxConfig.CITY})
public class CityController {

    private final CityService service;

    @Autowired
    public CityController(final CityService service) {
        this.service = service;
    }

    @ApiOperation(value = "Get active cities.",
            notes = "Returning all active cities.",
            response = StockDTO.class)
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
        return ResponseEntity.ok(service.getActiveCities());
    }

    // ------ Admin methods ------

    @ApiOperation(value = "Get information about city.",
            notes = "Returning information about city, which name specified in request.",
            response = StockDTO.class)
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
        return ResponseEntity.ok(service.findByCity(city));
    }

    @ApiOperation(value = "Activate city.",
            notes = "Activates city and returns information about city, which name specified in request.",
            response = StockDTO.class)
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
        return ResponseEntity.ok(service.setActive(city));
    }
}
