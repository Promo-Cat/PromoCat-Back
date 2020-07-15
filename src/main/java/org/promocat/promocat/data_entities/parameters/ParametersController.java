package org.promocat.promocat.data_entities.parameters;

import org.promocat.promocat.dto.ParametersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParametersController {

    private final ParametersService parametersService;

    @Autowired
    public ParametersController(ParametersService parametersService) {
        this.parametersService = parametersService;
    }

    @RequestMapping(path = "/admin/parameters", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParametersDTO> setPanel(@RequestBody ParametersDTO parameters) {
        parametersService.update(parameters);
        return ResponseEntity.ok(parametersService.getParameters());
    }

    @RequestMapping(path = "/admin/parameters", method = RequestMethod.GET)
    public ResponseEntity<ParametersDTO> getParameters() {
        ParametersDTO parameters = parametersService.getParameters();
        return ResponseEntity.ok(parameters);
    }
}
