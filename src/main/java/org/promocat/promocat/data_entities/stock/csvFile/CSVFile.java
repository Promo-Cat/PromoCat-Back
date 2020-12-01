package org.promocat.promocat.data_entities.stock.csvFile;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.abstract_account.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Blob;

/**
 * Created by Danil Lyskin at 12:46 24.07.2020
 */
@Entity
@Table(name = "csv_file")
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CSVFile extends AbstractEntity {

    private Blob file;
    private String name;

    /**
     * Байтовое представление CSV файла.
     */
    @Lob
    @NotNull(message = "Файл не может быть пустым")
    @Column(name = "file")
    public Blob getFile() {
        return file;
    }

    /**
     * Имя файла.
     */
    @NotBlank(message = "Имя файла не может быть пустым.")
    @Column(name = "name")
    public String getName() {
        return name;
    }
}
