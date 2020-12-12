package org.promocat.promocat.dto.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Danil Lyskin at 20:42 12.12.2020
 */

@ApiModel(
        value = "Number of free and busy users",
        description = "DTO with number of free and busy users"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NumberOfBusyAndFreeDrivers {

    @ApiModelProperty(
            value = "Number of free users",
            dataType = "Long"
    )
    private Long free;

    @ApiModelProperty(
            value = "Number of busy users",
            dataType = "Long"
    )
    private Long busy;
}
