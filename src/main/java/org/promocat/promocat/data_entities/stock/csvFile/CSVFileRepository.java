package org.promocat.promocat.data_entities.stock.csvFile;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Danil Lyskin at 12:51 24.07.2020
 */
public interface CSVFileRepository extends JpaRepository<CSVFile, Long> {
    Optional<CSVFile> findByName(String name);
}
