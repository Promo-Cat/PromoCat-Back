package org.promocat.promocat.data_entities.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.dto.AdminDTO;
import org.promocat.promocat.dto.StockDTO;
import org.promocat.promocat.dto.TelephoneDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {SpringFoxConfig.ADMIN})
@Controller
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(final AdminService adminService) {
        this.adminService = adminService;
    }

    @ApiOperation(value = "Add new admin.",
            notes = "Adds new admin.",
            response = AdminDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400,
                    message = "Validation error",
                    response = ApiValidationException.class),
            @ApiResponse(code = 415,
                    message = "Not acceptable media type",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminDTO> addAdmin(@Valid @RequestBody TelephoneDTO telephoneDTO) {
        return ResponseEntity.ok(adminService.add(telephoneDTO.getTelephone()));
    }

    @ApiOperation(value = "Get admins.",
            notes = "Returns all admins.",
            response = AdminDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/", method = RequestMethod.GET)
    public ResponseEntity<List<AdminDTO>> getAdmins() {
        return ResponseEntity.ok(adminService.getAll());
    }

    @ApiOperation(value = "Delete admin.",
            notes = "Deletes admin with id specified in request.",
            response = AdminDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404,
                    message = "Admin not found",
                    response = ApiException.class),
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAdmin(@RequestParam("id") Long id) {
        adminService.delete(id);
        return ResponseEntity.ok("{}");
    }
}
