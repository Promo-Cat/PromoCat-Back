package org.promocat.promocat.data_entities.admin;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.dto.AdminDTO;
import org.promocat.promocat.dto.MultiPartFileDTO;
import org.promocat.promocat.exception.admin.ApiAdminAlreadyExistsException;
import org.promocat.promocat.exception.admin.ApiAdminNotFoundException;
import org.promocat.promocat.exception.util.ApiFileFormatException;
import org.promocat.promocat.exception.util.ApiServerErrorException;
import org.promocat.promocat.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${data.resources.admin.examples}")
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
     *
     * @param file постер {@link MultipartFile}
     * @throws ApiServerErrorException если не получилось загрузить постер.
     */
    public void savePoster(final MultipartFile file) {
        Path pathToExample = Paths.get(PATH, "example.pdf");
        saveFile(file, pathToExample);
    }

    /**
     * Получение примера постера.
     *
     * @return представление постера в БД, {@link MultiPartFileDTO}
     * @throws ApiFileFormatException  если постера не существует или не получилось его представить
     *                                 в виде {@link MultiPartFileDTO}.
     * @throws ApiServerErrorException если не получилось привести постер к {@link java.sql.Blob}.
     */
    public MultiPartFileDTO getPosterExample() {
        Path pathToExample = Paths.get(PATH, "example.pdf");
        return createMultiPartFileFromPath(pathToExample);
    }

    /**
     * Сохранение пользовательского соглашения.
     *
     * @param file пользовательского соглашение {@link MultipartFile}
     * @throws ApiServerErrorException если не получилось загрузить пользовательского соглашение.
     */
    public void saveTermsOfUse(final MultipartFile file) {
        Path pathToTermsOfUse = Paths.get(PATH, "terms_of_use.pdf");
        saveFile(file, pathToTermsOfUse);
    }

    /**
     * Получение пользовательского соглашения.
     *
     * @return представление пользовательского соглашения в БД, {@link MultiPartFileDTO}
     * @throws ApiFileFormatException  если постера не существует или не получилось его представить
     *                                 в виде {@link MultiPartFileDTO}.
     * @throws ApiServerErrorException если не получилось привести постер к {@link java.sql.Blob}.
     */
    public MultiPartFileDTO getTermsOfUse() {
        Path pathToTermsOfUse = Paths.get(PATH, "terms_of_use.pdf");
        return createMultiPartFileFromPath(pathToTermsOfUse);
    }

    /**
     * Получение объекта файла.
     * @param pathToExample путь к файлу.
     * @return объектное представление файла. {@link MultiPartFileDTO}
     */
    private MultiPartFileDTO createMultiPartFileFromPath(final Path pathToExample) {
        if (Files.exists(pathToExample)) {
            MultiPartFileDTO poster = new MultiPartFileDTO();
            try {
                poster.setDataType(Files.probeContentType(pathToExample));
                try {
                    poster.setFile(new SerialBlob(Files.readAllBytes(pathToExample)));
                } catch (SQLException e) {
                    log.error(e.getLocalizedMessage());
                    throw new ApiServerErrorException("Problems with setting poster");
                }
                poster.setFileName(pathToExample.getFileName().toString());
                log.info("Returning poster example...");
                return poster;
            } catch (IOException e) {
                log.error(e.getLocalizedMessage());
                throw new ApiFileFormatException("Poster example error");
            }
        } else {
            log.error("Poster example does not exist");
            throw new ApiFileFormatException("Poster example does not exist");
        }
    }

    /**
     * Сохранение файла по заданному пути.
     * @param file файл.
     * @param pathToExample путь.
     */
    private void saveFile(final MultipartFile file, final Path pathToExample) {
        File terms_of_use = pathToExample.toFile();
        if (terms_of_use.delete()) {
            log.info("Old {} deleted", pathToExample.toString());
        } else {
            log.warn("Old {} was not deleted", pathToExample.toString());
        }
        try {
            file.transferTo(pathToExample);
            log.info("New {} downloaded", pathToExample.toString());
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            throw new ApiFileFormatException(String.format("File %s problem", pathToExample.toString()));
        }
    }

}
