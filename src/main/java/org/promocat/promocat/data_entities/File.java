package org.promocat.promocat.data_entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import java.sql.Blob;

@MappedSuperclass
@EqualsAndHashCode(of = {}, callSuper = true)
@AllArgsConstructor
@Setter
@NoArgsConstructor
public class File extends AbstractEntity {

    private Blob file;
    private String dataType;
    private String fileName;

    /**
     * Фото автомобиля для фотоконтроля.
     */
    @Lob
    @Column(name = "file")
    public Blob getFile() {
        return file;
    }

    @Column(name = "data_type")
    public String getDataType() {
        return dataType;
    }

    @Column(name = "file_name")
    public String getFileName() {
        return fileName;
    }

}
