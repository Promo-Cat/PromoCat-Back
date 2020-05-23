package org.promocat.promocat.data_entities.admin;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.dto.AdminDTO;
import org.promocat.promocat.dto.TelephoneDTO;
import org.promocat.promocat.exception.admin.ApiAdminNotFoundException;
import org.promocat.promocat.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    @Autowired
    public AdminService(final AdminRepository adminRepository, final AdminMapper adminMapper) {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
    }

    public boolean isAdmin(String telephone) {
        log.info("Requested do account with telephone {} has permissions", telephone);
        return adminRepository.existsByTelephone(telephone);
    }

    public AdminDTO getByTelephone(String telephone) {
        log.info("Requested admin by telephone {}", telephone);
        return adminMapper.toDto(adminRepository.getByTelephone(telephone).
                orElseThrow(() -> new ApiAdminNotFoundException(
                        String.format("Admin with such telephone: %s not found", telephone))));
    }

    public List<AdminDTO> getAll() {
        log.info("Requested list of all admins.");
        List<Admin> admins = adminRepository.findAll();
        return admins.stream().map(adminMapper::toDto).collect(Collectors.toList());
    }

    public AdminDTO add(String telephone) {
        if (adminRepository.existsByTelephone(telephone)) {
            log.warn("Attempt to add another admin with same telephone {}", telephone);
            // возвращает сущевствующего админа (лучше бы кинуть ексепшн)
            return adminMapper.toDto(adminRepository.getByTelephone(telephone).orElse(new Admin()));
        }
        Admin record = new Admin();
        record.setAccountType(AccountType.ADMIN);
        record.setTelephone(telephone);
        log.info("New admin added to DB");
        return adminMapper.toDto(adminRepository.save(record));
    }

    public void delete(Long id) {
        if (!adminRepository.existsById(id)) {
            log.error("Attempt to delete non admin with id {}, who doesn`t exist in db", id);
            throw new ApiAdminNotFoundException(String.format("Admin with id %d not found", id));
        }
        adminRepository.deleteById(id);
        log.info("Admin with id {} deleted successfully", id);
    }
}
