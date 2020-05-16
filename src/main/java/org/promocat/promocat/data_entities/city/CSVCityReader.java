package org.promocat.promocat.data_entities.city;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVCityReader {

    public static List<String[]> readFromFile(MultipartFile file) throws IOException {
        List<String[]> citiesFields = new ArrayList<>();
        CsvToBean<City> cityCsvToBean = new CsvToBean<>();
        CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()));
        String[] line;
        while ((line = csvReader.readNext()) != null) {
            citiesFields.add(line);
        }
        return citiesFields;
//        return cities;
    }

    private static ColumnPositionMappingStrategy<City> setColumnMapping() {
        ColumnPositionMappingStrategy<City> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(City.class);
        String[] columns = new String[] {"address", "postalCode", "country", "region", "city", "geoLat", "geoLon", "population"};
        strategy.setColumnMapping(columns);
        return strategy;
    }
}
