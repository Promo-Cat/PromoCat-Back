package org.promocat.promocat.data_entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.abstract_account.AbstractEntity;

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
     * Файл в байтовом представлении.
     */
    @Lob
    @Column(name = "file")
    public Blob getFile() {
        return file;
    }

    /**
     * Тип загруженного файла
     */
    @Column(name = "data_type")
    public String getDataType() {
        return dataType;
    }

    /**
     * Имя загруженного файла
     */
    @Column(name = "file_name")
    public String getFileName() {
        return fileName;
    }

}
