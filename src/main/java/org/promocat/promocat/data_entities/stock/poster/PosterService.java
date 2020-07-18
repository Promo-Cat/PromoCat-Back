package org.promocat.promocat.data_entities.stock.poster;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.promocat.promocat.config.GeneratorConfig;
import org.promocat.promocat.dto.MultiPartFileDTO;
import org.promocat.promocat.dto.PosterDTO;
import org.promocat.promocat.exception.stock.poster.ApiPosterNotFoundException;
import org.promocat.promocat.exception.util.ApiFileFormatException;
import org.promocat.promocat.exception.util.ApiServerErrorException;
import org.promocat.promocat.mapper.PosterMapper;
import org.promocat.promocat.utils.Generator;
import org.promocat.promocat.utils.MultiPartFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.transaction.Transactional;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:23 27.06.2020
 */
@Slf4j
@Service
public class PosterService {
    private final PosterRepository posterRepository;
    private final PosterMapper posterMapper;
    private final MultiPartFileUtils multiPartFileUtils;

    @Autowired
    public PosterService(final PosterRepository posterRepository,
                         final PosterMapper posterMapper,
                         final MultiPartFileUtils multiPartFileUtils) {
        this.posterRepository = posterRepository;
        this.posterMapper = posterMapper;
        this.multiPartFileUtils = multiPartFileUtils;
    }

    /**
     * Сохранение постера в БД.
     *
     * @param poster объектное представление постера.
     * @return предсавление постера в БД. {@link MultiPartFileDTO}
     */
    public PosterDTO save(final PosterDTO poster) {
        log.info("Saving poster...");
        return posterMapper.toDto(posterRepository.save(posterMapper.toEntity(poster)));
    }

    /**
     * Поиск постера по id.
     *
     * @param id уникальный идентификатор постера
     * @return предсавление постера в БД. {@link MultiPartFileDTO}.
     * @throws ApiFileFormatException если постер не найден
     */
    public PosterDTO findById(final Long id) {
        Optional<Poster> poster = posterRepository.findById(id);
        if (poster.isPresent()) {
            log.info("Poster with id: {} found", id);
            return posterMapper.toDto(poster.get());
        } else {
            log.warn("Poster with id was not: {} found", id);
            throw new ApiPosterNotFoundException(String.format("Poster with id: %d not found", id));
        }
    }

    /**
     * Загрузка постера.
     *
     * @param file     файловое представление постера.
     * @param posterId уникальный идентификатор постера. Если равен {@code null}, то добавляется новый,
     *                 иначе обновляется постер с таким {@code id}.
     * @return представление постера в БД. {@link MultiPartFileDTO}
     * @throws ApiFileFormatException  если не получилось сохранить постер.
     * @throws ApiServerErrorException если не получилось привести постер к {@link java.sql.Blob}
     */
    public PosterDTO loadPoster(final MultipartFile file, final Long posterId) {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            PosterDTO poster = new PosterDTO();
            poster.setId(posterId);
            poster.setFileNamePoster(fileName);
            poster.setDataTypePoster(file.getContentType());
            try {
                log.info("Poster with id: {} set", posterId);
                poster.setBlobPoster(new SerialBlob(file.getBytes()));
            } catch (SQLException e) {
                log.error(e.getLocalizedMessage());
                throw new ApiServerErrorException("Problems with setting poster");
            }
            MultiPartFileDTO preview = createPosterPreview(file);
            poster.setBlobPreview(preview.getBlob());
            poster.setDataTypePreview(preview.getDataType());
            poster.setFileNamePreview(preview.getFileName());
            return save(poster);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            throw new ApiFileFormatException("Some poster format problems");
        }
    }

    /**
     * Создание {@code png} превью постера
     *
     * @param file Multipart представление постера.
     * @return {@link MultiPartFileDTO} представление превью постера.
     */
    private MultiPartFileDTO createPosterPreview(final MultipartFile file) {
        File pdf = getPosterPdf(file);
        File image = multiPartFileUtils.pdfToImage(pdf);
        MultiPartFileDTO preview = multiPartFileUtils.fileImageToMultipartFileDTO(image);
        if (image.delete()) {
            log.info("Temp image {} deleted", image.getAbsolutePath());
        } else {
            log.info("Temp image {} was not deleted", image.getAbsolutePath());
        }
        if (pdf.delete()) {
            log.info("Temp pdf {} deleted", pdf.getAbsolutePath());
        } else {
            log.info("Temp pdf {} was not deleted", pdf.getAbsolutePath());
        }
        return preview;
    }

    /**
     * Получение {@code png} превью постера из {@link PosterDTO}
     *
     * @param dto DTO постера.
     * @return {@link MultiPartFileDTO} представление превью постера.
     */
    public MultiPartFileDTO getPosterPreview(final PosterDTO dto) {
        MultiPartFileDTO posterPreview = new MultiPartFileDTO();
        posterPreview.setBlob(dto.getBlobPreview());
        posterPreview.setDataType(dto.getDataTypePreview());
        posterPreview.setFileName(dto.getFileNamePreview());
        return posterPreview;
    }

    /**
     * Получение {@code pdf} постера из {@link PosterDTO}
     *
     * @param dto DTO постера.
     * @return {@link MultiPartFileDTO} представление постера.
     */
    public MultiPartFileDTO getPoster(final PosterDTO dto) {
        MultiPartFileDTO poster = new MultiPartFileDTO();
        poster.setBlob(dto.getBlobPoster());
        poster.setDataType(dto.getDataTypePoster());
        poster.setFileName(dto.getFileNamePoster());
        return poster;
    }

    /**
     * Получение {@code pdf} файла поcтера из {@link MultipartFile}.
     *
     * @param file Multipart представление постера.
     * @return {@code pdf} файл постера.
     * @throws ApiServerErrorException если не получилось создать {@code temp} файл постера.
     */
    private File getPosterPdf(final MultipartFile file) {
        try {
            Blob blobPdf = new SerialBlob(file.getBytes());
            File outputFile = new File(System.getProperty("java.io.tmpdir") +
                    Generator.generate(GeneratorConfig.FILE_NAME) + file.getOriginalFilename());

            try (FileOutputStream fout = new FileOutputStream(outputFile)) {
                IOUtils.copy(blobPdf.getBinaryStream(), fout);
            } catch (IOException | SQLException e) {
                log.error("Poster to pdf exception: {}", e.getMessage());
                throw new ApiServerErrorException("Poster to pdf exception");
            }
            return outputFile;
        } catch (IOException | SQLException e) {
            log.error("Poster to pdf exception: {}", e.getMessage());
            throw new ApiServerErrorException("Poster to pdf exception");
        }
    }

    // TODO избавиться от ResponseEntity. Возвращать только Resource

    /**
     * Представление постера в виде файла.
     *
     * @param file постер
     * @return Возвращает {@link ResponseEntity} {@link Resource}.
     */
    /*
        - Зачем нужна транзакция?
        - Оказывается postgres требует получение Blob в отдельной транзакции, тк он слишком большой.
        - https://stackoverflow.com/questions/3164072/large-objects-may-not-be-used-in-auto-commit-mode
     */
    @Transactional
    public ResponseEntity<Resource> getResourceResponseEntity(final MultiPartFileDTO file) {
        Blob blob = file.getBlob();
        try {
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            log.info("Returning multipart file: {}", file.getFileName());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(file.getDataType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + file.getFileName() + "\"")
                    .body(new ByteArrayResource(bytes));
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
            throw new ApiServerErrorException("Some DB exception");
        }
    }
}
