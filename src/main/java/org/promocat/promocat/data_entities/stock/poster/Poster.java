package org.promocat.promocat.data_entities.stock.poster;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private Blob blobPoster;
    private String dataTypePoster;
    private String fileNamePoster;
    private Blob blobPreview;
    private String dataTypePreview;
    private String fileNamePreview;

    /**
     * Байтовое представление постера.
     */
    @Lob
    @Column(name = "poster")
    public Blob getBlobPoster() {
        return blobPoster;
    }

    /**
     * Тип постера.
     */
    @Column(name = "data_type_poster")
    public String getDataTypePoster() {
        return dataTypePoster;
    }

    /**
     * Имя постера.
     */
    @Column(name = "file_name_poster")
    public String getFileNamePoster() {
        return fileNamePoster;
    }

    /**
     * Байтовое представление превью постера.
     */
    @Lob
    @Column(name = "preview")
    public Blob getBlobPreview() {
        return blobPreview;
    }

    /**
     * Тип превью постера.
     */
    @Column(name = "data_type_preview")
    public String getDataTypePreview() {
        return dataTypePreview;
    }

    /**
     * Имя превью постера.
     */
    @Column(name = "file_name_preview")
    public String getFileNamePreview() {
        return fileNamePreview;
    }
}
