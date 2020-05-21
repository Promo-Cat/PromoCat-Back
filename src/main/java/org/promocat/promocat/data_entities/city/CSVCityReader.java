package org.promocat.promocat.data_entities.city;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVCityReader {

    public static List<String[]> readFromFile(MultipartFile file) throws IOException {
        List<String[]> citiesFields = new ArrayList<>();
        return readFromStreamReader(new InputStreamReader(file.getInputStream()));
    }

    public static List<String[]> readFromStreamReader(InputStreamReader inputStreamReader) throws IOException {
        List<String[]> citiesFields = new ArrayList<>();
        CSVReader csvReader = new CSVReader(inputStreamReader);
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            citiesFields.add(line);
        }
        return citiesFields;
    }

}
