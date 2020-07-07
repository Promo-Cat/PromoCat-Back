package org.promocat.promocat.data_entities.stock.poster;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.PosterDTO;
import org.promocat.promocat.exception.util.ApiFileFormatException;
import org.promocat.promocat.exception.util.ApiServerErrorException;
import org.promocat.promocat.mapper.PosterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:23 27.06.2020
 */
@Slf4j
@Service
// TODO logs and javadocs
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
     * @param poster объектное представление постера.
     * @return предсавление постера в БД. {@link PosterDTO}
     */
    public PosterDTO save(final PosterDTO poster) {
        return posterMapper.toDto(posterRepository.save(posterMapper.toEntity(poster)));
    }

    /**
     * Поиск постера по id.
     * @param id уникальный идентификатор постера
     * @return предсавление постера в БД. {@link PosterDTO}.
     * @throws ApiFileFormatException если постер не найден
     */
    public PosterDTO findById(final Long id) {
        Optional<Poster> poster = posterRepository.findById(id);
        if (poster.isPresent()) {
            return posterMapper.toDto(poster.get());
        } else {
            // TODO poster not found exception
            throw new ApiFileFormatException("Poster not found");
        }
    }

    /**
     * Загрузка постера.
     * @param file файловое представление постера.
     * @return представление постера в БД. {@link PosterDTO}
     * @throws ApiFileFormatException если не получилось сохранить постер.
     */
    public PosterDTO loadPoster(final MultipartFile file) {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            PosterDTO poster = new PosterDTO();
            poster.setFileName(fileName);
            poster.setDataType(file.getContentType());
            try {
                poster.setPoster(new SerialBlob(file.getBytes()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            return save(poster);
        } catch (IOException e) {
            throw new ApiFileFormatException("Some poster format problems");
        }
    }
}
