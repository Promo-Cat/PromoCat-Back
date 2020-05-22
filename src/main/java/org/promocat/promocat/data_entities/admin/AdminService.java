package org.promocat.promocat.data_entities.admin;

import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.dto.TelephoneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Admin> getAll() {
        return adminRepository.findAll();
    }

    public Admin add(TelephoneDTO telephoneDTO) {
        Admin record = new Admin();
        record.setAccountType(AccountType.ADMIN);
        record.setTelephone(telephoneDTO.getTelephone());
        return adminRepository.save(record);
    }

    public void delete(Long id) {
        adminRepository.deleteById(id);
    }
}
