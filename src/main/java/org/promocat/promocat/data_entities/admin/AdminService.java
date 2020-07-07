package org.promocat.promocat.data_entities.admin;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.dto.AdminDTO;
import org.promocat.promocat.dto.PosterDTO;
import org.promocat.promocat.exception.admin.ApiAdminAlreadyExistsException;
import org.promocat.promocat.exception.admin.ApiAdminNotFoundException;
import org.promocat.promocat.exception.util.ApiFileFormatException;
import org.promocat.promocat.exception.util.ApiServerErrorException;
import org.promocat.promocat.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
// TODO javadocs
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    @Value("${data.resources.poster}")
    private String PATH;

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
            throw new ApiAdminAlreadyExistsException(
                    String.format("Attempt to add another admin with same telephone %s", telephone));
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

    /**
     * Сохранение примера постера для рекламодателей.
     * @param file постер {@link MultipartFile}
     * @throws ApiServerErrorException если не получилось загрузить постер.
     */
    public void savePoster(final MultipartFile file) {
        Path pathToExample = Paths.get(PATH, "example.pdf");
        File poster = pathToExample.toFile();
        if (poster.delete()) {
            log.info("Old example poster deleted");
        } else {
            log.warn("Old example poster was not deleted");
        }
        try {
            file.transferTo(pathToExample);
            log.info("New example poster downloaded");
        } catch (IOException e) {
            throw new ApiFileFormatException("Poster example problem");
        }
    }


    /**
     * Получение примера постера.
     * @return представление постера в БД, {@link PosterDTO}
     * @throws ApiServerErrorException если постера не существует или не получилось его представить
     * в виде {@link PosterDTO}
     */
    public PosterDTO getPosterExample() {
        Path pathToExample = Paths.get(PATH, "example.pdf");
        if (Files.exists(pathToExample)) {
            PosterDTO poster = new PosterDTO();
            try {
                poster.setDataType(Files.probeContentType(pathToExample));
                try {
                    poster.setPoster(new SerialBlob(Files.readAllBytes(pathToExample)));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                poster.setFileName(pathToExample.getFileName().toString());
                return poster;
            } catch (IOException e) {
                throw new ApiFileFormatException("Poster example error");
            }
        } else {
            throw new ApiFileFormatException("Poster example does not exist");
        }
    }
}
