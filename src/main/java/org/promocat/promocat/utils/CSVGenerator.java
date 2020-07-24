package org.promocat.promocat.utils;

import au.com.bytecode.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.dto.UserDTO;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Created by Danil Lyskin at 09:23 23.07.2020
 */
@Slf4j
public class CSVGenerator {
    public static void generate(Path path, List<UserDTO> users) {
        StringBuilder result = new StringBuilder();
        users.forEach(e -> {
            result.append(e.getAccount()).append(",,,,").append(e.getBalance()).append('\n');
        });

        try {
            CSVWriter writer = new CSVWriter(new FileWriter(path.toString()));

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
