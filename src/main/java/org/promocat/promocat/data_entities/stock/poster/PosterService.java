package org.promocat.promocat.data_entities.stock.poster;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.PosterDTO;
import org.promocat.promocat.exception.stock.poster.ApiPosterNotFoundException;
import org.promocat.promocat.exception.util.ApiFileFormatException;
import org.promocat.promocat.exception.util.ApiServerErrorException;
import org.promocat.promocat.mapper.PosterMapper;
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
// TODO logs
public class PosterService {
    private final PosterRepository posterRepository;
    private final PosterMapper posterMapper;

    @Autowired
    public PosterService(final PosterRepository posterRepository,
                         final PosterMapper posterMapper) {
        this.posterRepository = posterRepository;
        this.posterMapper = posterMapper;
    }

    /**
     * Сохранение постера в БД.
     *
     * @param poster объектное представление постера.
     * @return предсавление постера в БД. {@link PosterDTO}
     */
    public PosterDTO save(final PosterDTO poster) {
        return posterMapper.toDto(posterRepository.save(posterMapper.toEntity(poster)));
    }

    /**
     * Поиск постера по id.
     *
     * @param id уникальный идентификатор постера
     * @return предсавление постера в БД. {@link PosterDTO}.
     * @throws ApiFileFormatException если постер не найден
     */
    public PosterDTO findById(final Long id) {
        Optional<Poster> poster = posterRepository.findById(id);
        if (poster.isPresent()) {
            return posterMapper.toDto(poster.get());
        } else {
            throw new ApiPosterNotFoundException(String.format("Poster with id: %d not found", id));
        }
    }

    /**
     * Загрузка постера.
     *
     * @param file     файловое представление постера.
     * @param posterId уникальный идентификатор постера. Если равен {@code null}, то добавляется новый,
     *                иначе обновляется постер с таким {@code id}.
     * @return представление постера в БД. {@link PosterDTO}
     * @throws ApiFileFormatException  если не получилось сохранить постер.
     * @throws ApiServerErrorException если не получилось привести постер к {@link java.sql.Blob}
     */
    public PosterDTO loadPoster(final MultipartFile file, final Long posterId) {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            PosterDTO poster = new PosterDTO();
            poster.setId(posterId);
            poster.setFileName(fileName);
            poster.setDataType(file.getContentType());
            try {
                poster.setPoster(new SerialBlob(file.getBytes()));
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ApiServerErrorException("Problems with setting poster");
            }
            return save(poster);
        } catch (IOException e) {
            throw new ApiFileFormatException("Some poster format problems");
        }
    }

    // TODO избавиться от ResponseEntity. Возвращать только Resource

    /**
     * Представление постера в виде файла.
     *
     * @param poster постер
     * @return Возвращает {@link ResponseEntity} {@link Resource}.
     */
    @Transactional
    public ResponseEntity<Resource> getResourceResponseEntity(final PosterDTO poster) {
        Blob blob = poster.getPoster();
        try {
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(poster.getDataType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + poster.getFileName() + "\"")
                    .body(new ByteArrayResource(bytes));
        } catch (SQLException e) {
            log.error(e.getLocalizedMessage());
            throw new ApiServerErrorException("Some DB exception");
        }
    }
}
