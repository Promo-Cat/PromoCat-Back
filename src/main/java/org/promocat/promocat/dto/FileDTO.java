package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Blob;

@ApiModel(
        value = "File",
        description = "Object representation of any file in PromoCat application."
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO extends AbstractDTO {

    @ApiModelProperty(value = "File name", dataType = "String")
    private String fileName;

    @ApiModelProperty(value = "Data type", dataType = "String")
    private String dataType;

    @ApiModelProperty(value = "File byte representation", dataType = "Blob")
    private Blob file;

}
