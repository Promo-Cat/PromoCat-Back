package org.promocat.promocat.mapper;

import org.promocat.promocat.data_entities.File;
import org.promocat.promocat.dto.FileDTO;
import org.springframework.stereotype.Component;

@Component
public class FileMapper extends AbstractMapper<File, FileDTO> {

    public FileMapper() {
        super(File.class, FileDTO.class);
    }
}
