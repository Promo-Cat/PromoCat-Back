package org.promocat.promocat.data_entities.admin;

import org.promocat.promocat.dto.TelephoneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(final AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping(path = "/admin/", method = RequestMethod.POST)
    public ResponseEntity<Admin> addAdmin(@RequestBody TelephoneDTO telephoneDTO) {
        return ResponseEntity.ok(adminService.add(telephoneDTO));
    }

    @RequestMapping(path = "/admin/", method = RequestMethod.GET)
    public ResponseEntity<List<Admin>> getAdmins() {
        return ResponseEntity.ok(adminService.getAll());
    }

    @RequestMapping(path = "/admin/", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAdmin(@RequestParam("id") Long id) {
        adminService.delete(id);
        return ResponseEntity.ok("{}");
    }
}
