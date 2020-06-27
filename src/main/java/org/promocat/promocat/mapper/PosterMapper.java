package org.promocat.promocat.mapper;

import org.promocat.promocat.data_entities.stock.poster.Poster;
import org.promocat.promocat.dto.PosterDTO;
import org.springframework.stereotype.Component;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:28 27.06.2020
 */
@Component
public class PosterMapper extends AbstractMapper<Poster, PosterDTO> {
    public PosterMapper() {
        super(Poster.class, PosterDTO.class);
    }
}
