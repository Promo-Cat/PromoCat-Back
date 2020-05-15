package org.promocat.promocat.mapper;

import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.dto.AbstractDTO;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:40 14.05.2020
 */
public interface Mapper<E extends AbstractEntity, D extends AbstractDTO> {
    E toEntity(D dto);

    D toDto(E entity);
}
