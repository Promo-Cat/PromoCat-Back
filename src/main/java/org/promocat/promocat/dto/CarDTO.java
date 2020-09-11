package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel(
        value = "Car",
        description = "Object representation of users car."
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO extends AbstractDTO {

    @ApiModelProperty(
            value = "User id",
            dataType = "Long",
            required = true
    )
    private Long userId;

    @ApiModelProperty(
            value = "Car number",
            dataType = "String",
            required = true
    )
    @NotBlank(message = "Номер не может быть пустым.")
    @Pattern(regexp = "[АВЕКМНОРСТУХ]\\d{3}[АВЕКМНОРСТУХ]{2}", message = "Номер автомобиля задан некорректно.")
    private String number;

    @ApiModelProperty(
            value = "Car region",
            dataType = "String",
            required = true
    )
    @NotBlank(message = "Регион не может быть пустым.")
    @Pattern(regexp = "\\d{2,3}", message = "Регион автомобиля задан некорректно.")
    private String region;
}
