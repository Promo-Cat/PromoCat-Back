package org.promocat.promocat.data_entities.stock.csvFile;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.promocat.promocat.dto.CSVFileDTO;
import org.promocat.promocat.dto.MultiPartFileDTO;
import org.promocat.promocat.exception.util.ApiFileFormatException;
import org.promocat.promocat.exception.util.ApiServerErrorException;
import org.promocat.promocat.mapper.CSVFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Created by Danil Lyskin at 12:46 24.07.2020
 */
@Slf4j
@Service
public class CSVFileService {
    private final CSVFileRepository csvFileRepository;
    private final CSVFileMapper csvFileMapper;

    @Autowired
    CSVFileService(final CSVFileRepository csvFileRepository,
                   final CSVFileMapper csvFileMapper) {
        this.csvFileRepository = csvFileRepository;
        this.csvFileMapper = csvFileMapper;
    }


    /**
     * Сохранение файла в БД.
     *
     * @param file объектное представление постера.
     * @return предсавление файла в БД. {@link MultiPartFileDTO}
     */
    public CSVFileDTO save(final CSVFileDTO file) {
        log.info("Saving poster...");
        return csvFileMapper.toDto(csvFileRepository.save(csvFileMapper.toEntity(file)));
    }

    /**
     * Загрузка файла.
     *
     * @param file     файловое представление файла.
     * @param fileId уникальный идентификатор файла. Если равен {@code null}, то добавляется новый.
     * @return представление файла в БД. {@link MultiPartFileDTO}
     * @throws ApiFileFormatException  если не получилось сохранить файл.
     * @throws ApiServerErrorException если не получилось привести файл к {@link java.sql.Blob}
     */
    public CSVFileDTO loadFile(final File file, final Long fileId) {
        CSVFileDTO csvFileDTO = new CSVFileDTO();
        csvFileDTO.setId(fileId);
        csvFileDTO.setName(file.getName());
        try (FileInputStream input = new FileInputStream(file)) {
            MultipartFile multipartFile = new MockMultipartFile("fileItem",
                    file.getName(), "text/plain", IOUtils.toByteArray(input));
            try {
                csvFileDTO.setFile(new SerialBlob(multipartFile.getBytes()));
            } catch (SQLException e) {
                log.error(e.getLocalizedMessage());
                throw new ApiServerErrorException("Problems with setting file");
            }
        } catch (FileNotFoundException e) {
            log.error("Couldn't open file");
            throw new ApiServerErrorException("Problems with setting file");
        } catch (IOException e) {
            log.error("Couldn't write in file");
            throw new ApiServerErrorException("Problems with setting file");
        }
        return save(csvFileDTO);
    }

    @Transactional
    public ResponseEntity<Resource> getFile(String name) {
        Optional<CSVFile> op = csvFileRepository.findByName(name);
        if (op.isPresent()) {
            CSVFileDTO csvFileDTO = csvFileMapper.toDto(op.get());
            Blob blob = csvFileDTO.getFile();
            try {
                byte[] bytes = blob.getBytes(0, (int) blob.length());
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType("text/csv"))
                        .header(HttpHeaders.CONTENT_DISPOSITION,
                                "attachment; filename=\"" + csvFileDTO.getName() + "\"")
                        .body(new ByteArrayResource(bytes));
            } catch (SQLException e) {
                log.error("Couldn't write");
                throw new ApiServerErrorException("Some DB exception");
            }
        } else {
            throw new ApiServerErrorException("Some DB exception");
        }
    }
}
