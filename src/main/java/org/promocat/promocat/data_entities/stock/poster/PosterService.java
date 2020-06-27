package org.promocat.promocat.data_entities.stock.poster;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.PosterDTO;
import org.promocat.promocat.exception.util.ApiServerErrorException;
import org.promocat.promocat.mapper.PosterMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public PosterDTO save(final PosterDTO poster) {
        return posterMapper.toDto(posterRepository.save(posterMapper.toEntity(poster)));
    }

    public PosterDTO findById(final Long id) {
        Optional<Poster> poster = posterRepository.findById(id);
        if (poster.isPresent()) {
            return posterMapper.toDto(poster.get());
        } else {
            //TODO custom Exception
            throw new UsernameNotFoundException("Poster not found");
        }
    }

    public PosterDTO loadPoster(final MultipartFile file) {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            PosterDTO poster = new PosterDTO(fileName, file.getContentType(), file.getBytes());
            return save(poster);
        } catch (IOException e) {
            // TODO custom FileStorageException
            throw new ApiServerErrorException("Poster");
        }
    }
}
