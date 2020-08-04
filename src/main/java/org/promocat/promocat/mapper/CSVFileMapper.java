package org.promocat.promocat.mapper;

import org.promocat.promocat.data_entities.stock.csvFile.CSVFile;
import org.promocat.promocat.dto.CSVFileDTO;
import org.springframework.stereotype.Component;

/**
 * Created by Danil Lyskin at 12:59 24.07.2020
 */
@Component
public class CSVFileMapper extends AbstractMapper<CSVFile, CSVFileDTO> {
    public CSVFileMapper() {
        super(CSVFile.class, CSVFileDTO.class);
    }
}
