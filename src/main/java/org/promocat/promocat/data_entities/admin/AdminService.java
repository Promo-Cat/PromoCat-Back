package org.promocat.promocat.data_entities.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(final AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public boolean isAdmin(String telephone) {
        return adminRepository.existsAdminByTelephone(telephone);
    }

    public Optional<Admin> getByTelephone(String telephone) {
        return adminRepository.getByTelephone(telephone);
    }

}
