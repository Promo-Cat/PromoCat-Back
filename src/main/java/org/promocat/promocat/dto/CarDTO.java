package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(
        value = "Car",
        description = "Object representation of users car.",
        parent = AbstractDTO.class
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO extends AbstractDTO {

    @NotNull(message = "Id пользователя должно быть задано.")
    private Long userId;

    @NotBlank(message = "Номер не может быть пустым.")
    private String number;

    @NotBlank(message = "Регион не может быть пустым.")
    private String region;
}
