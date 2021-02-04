package org.promocat.promocat.data_entities.notification_npd;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.config.SpringFoxConfig;
import org.promocat.promocat.dto.NotifNPDDTO;
import org.promocat.promocat.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Danil Lyskin at 10:43 02.02.2021
 */

@RestController
@Slf4j
@Api(tags = {SpringFoxConfig.NOTIFNPD})
public class NotifNPDController {

    private final NotifNPDService notifNPDService;

    @Autowired
    public NotifNPDController(final NotifNPDService notifNPDService) {

        this.notifNPDService = notifNPDService;
    }

    @ApiOperation(value = "Set flag open for notification",
            notes = "Returning notification",
            response = NotifNPDDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Notif or user not found", response = ApiException.class),
            @ApiResponse(code = 406, message = "Some DB problems", response = ApiException.class)
    })
    @RequestMapping(value = "/api/notifNPD/open/notif/{id}", method = RequestMethod.POST)
    public ResponseEntity<NotifNPDDTO> openNotif(@PathVariable("id") final Long id) {
        NotifNPDDTO dto = notifNPDService.findById(id);
        dto.setIsOpen(true);

        notifNPDService.sendOpenNotif(dto);

        return ResponseEntity.ok(notifNPDService.save(dto));
    }
}
