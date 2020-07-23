package org.promocat.promocat.utils;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Danil Lyskin at 09:23 23.07.2020
 */
@Slf4j
public class CSVGenerator {

    public static void generate(String path, Map<String, Object> map) {
        StringBuilder result = new StringBuilder();

        try {
            CSVReader reader = new CSVReader(new FileReader(path), ',');
            String[] nextLine;

            while ((nextLine = reader.readNext()) != null) {
                for (String s : nextLine) {
                    if (s.startsWith("#") && s.endsWith("#")) {
                        Object temp = map.getOrDefault(s.substring(1, s.length() - 1), null);
                        s = Objects.isNull(temp) ? "" : temp.toString();
                    }
                    result.append(s).append(',');
                }
                result.deleteCharAt(result.length() - 1);
                result.append('\n');
            }

            reader.close();
        } catch (IOException e) {
            log.error("Template file couldn't open");
        }

        String csv = "data.csv";
        try {
            CSVWriter writer = new CSVWriter(new FileWriter(csv));

            String[] records = result.toString().split("\n");
            for (String record : records) {
                writer.writeNext(record.split(","));
            }
            writer.close();
        } catch (IOException e) {
            log.error("Couldn't write");
        }
    }
}
