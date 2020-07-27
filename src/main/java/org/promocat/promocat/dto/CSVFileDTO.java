package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.sql.Blob;

/**
 * Created by Danil Lyskin at 12:57 24.07.2020
 */
@ApiModel(value = "CSVFile")
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CSVFileDTO extends AbstractDTO {

    @ApiModelProperty(value = "File in byte representation", dataType = "Blob")
    private Blob file;

    @ApiModelProperty(value = "File name", dataType = "String")
    private String name;
}
