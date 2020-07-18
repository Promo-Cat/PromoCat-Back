package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:26 27.06.2020
 */
@ApiModel(value = "Multi part file", description = "Representation of any multipart file")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MultiPartFileDTO {

    @ApiModelProperty(value = "File name", dataType = "String")
    private String fileName;

    @ApiModelProperty(value = "Data type", dataType = "String")
    private String dataType;

    @ApiModelProperty(value = "File byte representation", dataType = "Blob")
    private Blob blob;
}
