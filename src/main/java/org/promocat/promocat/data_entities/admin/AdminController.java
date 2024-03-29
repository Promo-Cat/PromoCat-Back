package org.promocat.promocat.data_entities.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.data_entities.stock.poster.PosterService;
import org.promocat.promocat.dto.AdminDTO;
import org.promocat.promocat.dto.MultiPartFileDTO;
import org.promocat.promocat.dto.pojo.TelephoneDTO;
import org.promocat.promocat.exception.ApiException;
import org.promocat.promocat.exception.util.ApiFileFormatException;
import org.promocat.promocat.exception.validation.ApiValidationException;
import org.promocat.promocat.utils.MimeTypes;
import org.promocat.promocat.utils.MultiPartFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Api(tags = {SpringFoxConfig.ADMIN})
@Controller
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final PosterService posterService;
    private final MultiPartFileUtils multiPartFileUtils;

    @Autowired
    public AdminController(final AdminService adminService,
                           final PosterService posterService,
                           final MultiPartFileUtils multiPartFileUtils) {
        this.adminService = adminService;
        this.posterService = posterService;
        this.multiPartFileUtils = multiPartFileUtils;
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
    @RequestMapping(path = "/admin", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminDTO> addAdmin(@Valid @RequestBody TelephoneDTO telephoneDTO) {
        return ResponseEntity.ok(adminService.add(telephoneDTO.getTelephone()));
    }

    @ApiOperation(value = "Get admins.",
            notes = "Returns all admins.",
            response = AdminDTO.class,
            responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 406,
                    message = "Some DB problems",
                    response = ApiException.class)
    })
    @RequestMapping(path = "/admin/", method = RequestMethod.GET)
    public ResponseEntity<List<AdminDTO>> getAdmins() {
        return ResponseEntity.ok(adminService.getAll());
    }

    @ApiOperation(value = "Delete admin.", notes = "Deletes admin with id specified in request.",
            response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Admin not found", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(path = "/admin/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAdmin(@PathVariable("id") Long id) {
        adminService.delete(id);
        return ResponseEntity.ok("{}");
    }

    @ApiOperation(value = "Add new example poster",
            notes = "Adding new example poster. Max size is 5MB, .pdf format required", response = String.class,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class),
            @ApiResponse(code = 500, message = "Some server problems", response = ApiException.class),
            @ApiResponse(code = 400, message = "File format or size problems", response = ApiException.class)
    })
    @RequestMapping(path = "/admin/poster/example", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addPosterExample(@RequestParam("file") MultipartFile file) {
        if (MimeTypes.MIME_APPLICATION_PDF.equals(file.getContentType())) {
            if (multiPartFileUtils.getSizeInMB(file.getSize()) <= 5) {
                adminService.savePoster(file);
                return ResponseEntity.ok("{}");
            } else {
                throw new ApiFileFormatException("File too large. Max size 5MB");
            }
        } else {
            throw new ApiFileFormatException(String.format(".pdf required %s provided", file.getContentType()));
        }
    }

    @ApiOperation(value = "Add new terms of use",
            notes = "Adding new terms of use. Max size is 5MB, .pdf format required", response = String.class,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class),
            @ApiResponse(code = 500, message = "Some server problems", response = ApiException.class),
            @ApiResponse(code = 400, message = "File format or size problems", response = ApiException.class)
    })
    @RequestMapping(path = "/admin/termsOfUse", method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addTermsOfUseExample(@RequestParam("file") MultipartFile file) {
        if (MimeTypes.MIME_APPLICATION_PDF.equals(file.getContentType())) {
            if (multiPartFileUtils.getSizeInMB(file.getSize()) <= 5) {
                adminService.saveTermsOfUse(file);
                return ResponseEntity.ok("{}");
            } else {
                throw new ApiFileFormatException("File too large. Max size 5MB");
            }
        } else {
            throw new ApiFileFormatException(String.format(".pdf required %s provided", file.getContentType()));
        }
    }

    @ApiOperation(value = "Get terms of use",
            notes = "Returning terms of use (.pdf)", response = Resource.class)
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class),
            @ApiResponse(code = 500, message = "Server problems", response = ApiException.class)
    })
    @RequestMapping(path = "/admin/termsOfUse", method = RequestMethod.GET)
    public ResponseEntity<Resource> getTermsOfUse() {
        MultiPartFileDTO termsOfUse = adminService.getTermsOfUse();
        return posterService.getResourceResponseEntity(termsOfUse);
    }

    @ApiOperation(value = "Register or update PromoCat on \"Moi nalog\".",
            notes = "Register or update PromoCat on \"Moi nalog\".", response = String.class)
    @RequestMapping(path = "/admin/tax/register", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<String> registerPartner() {
        adminService.registerPartner();
        return ResponseEntity.ok("{}");
    }
}
