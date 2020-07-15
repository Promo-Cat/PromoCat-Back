package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Blob;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:07 15.07.2020
 */
@ApiModel(value = "Poster", description = "Object representation of poster.")
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PosterDTO extends AbstractDTO {

    @ApiModelProperty(value = "Posters file name", dataType = "String")
    private String fileNamePoster;

    @ApiModelProperty(value = "Posters data type", dataType = "String")
    private String dataTypePoster;

    @ApiModelProperty(value = "Poster in byte representation", dataType = "Blob")
    private Blob blobPoster;

    @ApiModelProperty(value = "Posters preview file name", dataType = "String")
    private String fileNamePreview;

    @ApiModelProperty(value = "Posters preview data type", dataType = "String")
    private String dataTypePreview;

    @ApiModelProperty(value = "Posters preview in byte representation", dataType = "Blob")
    private Blob blobPreview;
}
