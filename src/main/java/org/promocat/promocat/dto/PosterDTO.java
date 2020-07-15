package org.promocat.promocat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Blob;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:07 15.07.2020
 */
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
// TODO Docs
public class PosterDTO extends AbstractDTO {
    private String fileNamePoster;
    private String dataTypePoster;
    private Blob blobPoster;
    private String fileNamePreview;
    private String dataTypePreview;
    private Blob blobPreview;
}
