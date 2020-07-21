package org.promocat.promocat.data_entities.parameters;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.dto.ParametersDTO;
import org.promocat.promocat.dto.pojo.DistanceWithCityDTO;
import org.promocat.promocat.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(tags = {SpringFoxConfig.PARAMETERS})
public class ParametersController {

    private final ParametersService parametersService;

    @Autowired
    public ParametersController(ParametersService parametersService) {
        this.parametersService = parametersService;
    }

    @ApiOperation(value = "Set new parameters.",
            notes = "Sets new parameters for application.",
            response = ParametersDTO.class)
    @ApiResponses(value = {@ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)})
    @RequestMapping(path = "/admin/parameters", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParametersDTO> setParameters(@RequestBody final ParametersDTO parameters) {
        parametersService.update(parameters);
        return ResponseEntity.ok(parametersService.getParameters());
    }


    @ApiOperation(value = "Get parameters.",
            notes = "Gets current parameters.",
            response = ParametersDTO.class
    )
    @ApiResponses(value = {@ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)})
    @RequestMapping(path = "/admin/parameters", method = RequestMethod.GET)
    public ResponseEntity<ParametersDTO> getParameters() {
        ParametersDTO parameters = parametersService.getParameters();
        return ResponseEntity.ok(parameters);
    }
}
