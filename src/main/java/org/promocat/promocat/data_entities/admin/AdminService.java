package org.promocat.promocat.data_entities.admin;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.city.City;
import org.promocat.promocat.dto.AdminDTO;
import org.promocat.promocat.dto.TelephoneDTO;
import org.promocat.promocat.exception.admin.ApiAdminNotFoundException;
import org.promocat.promocat.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        return adminRepository.existsAdminByTelephone(telephone);
    }

    public AdminDTO getByTelephone(String telephone) {
        return adminMapper.toDto(adminRepository.getByTelephone(telephone).
                orElseThrow(() -> new ApiAdminNotFoundException(
                        String.format("Admin with such telephone: %s not found", telephone))));
    }

    public List<AdminDTO> getAll() {
        List<Admin> admins = adminRepository.findAll();
        return admins.stream().map(adminMapper::toDto).collect(Collectors.toList());
    }

    public AdminDTO add(TelephoneDTO telephoneDTO) {
        Admin record = new Admin();
        record.setAccountType(AccountType.ADMIN);
        record.setTelephone(telephoneDTO.getTelephone());
        return adminMapper.toDto(adminRepository.save(record));
    }

    // TODO проверить на существование админа и кинуть ошибку
    public void delete(Long id) {
        if (!adminRepository.existsById(id)) {

            throw new ApiAdminNotFoundException(String.format("Admin with id %d not found", id));
        }
        adminRepository.deleteById(id);
    }
}
