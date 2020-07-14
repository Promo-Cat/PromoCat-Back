package org.promocat.promocat.data_entities.stock.poster;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.promocat.promocat.data_entities.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.sql.Blob;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:01 27.06.2020
 */
@Entity
@Table(name = "poster")
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Poster extends AbstractEntity {
    private Blob blob;
    private String dataType;
    private String fileName;

    /**
     * Байтовое представление постера.
     */
    @Lob
    @Column(name = "poster")
    public Blob getBlob() {
        return blob;
    }

    /**
     * Тип постера.
     */
    @Column(name = "data_type")
    public String getDataType() {
        return dataType;
    }

    /**
     * Имя файла постера.
     */
    @Column(name = "file_name")
    public String getFileName() {
        return fileName;
    }
}
