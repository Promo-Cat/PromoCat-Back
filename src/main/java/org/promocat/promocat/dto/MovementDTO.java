package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.data_entities.stock.Stock;
import org.promocat.promocat.data_entities.user.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@ApiModel(
        value = "Movement",
        description = "Object representation users movement."
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovementDTO extends AbstractDTO {

    @ApiModelProperty(
            value = "Users id",
            dataType = "Long",
            required = true
    )
    @NotNull(message = "Id пользователя не может быть пустым.")
    private Long userId;

    @ApiModelProperty(
            value = "Stock id",
            dataType = "Long",
            required = true
    )
    @NotNull(message = "Id акции не может быть пустым.")
    private Long stockId;

    @ApiModelProperty(
            value = "Date",
            dataType = "LocalDate in format {YYYY-MM-DD}",
            required = true
    )
    @NotNull(message = "Дата не может быть пустой.")
    private LocalDate date;

    @ApiModelProperty(
            value = "Distance",
            dataType = "Double",
            required = true
    )
    @NotNull(message = "Дистанция не может быть пустой.")
    private Double distance;

    @ApiModelProperty(
            value = "Earnings",
            dataType = "Double",
            required = true
    )
    private Double earnings;

    @ApiModelProperty(
            value = "Panel",
            dataType = "Double",
            required = true
    )
    private Double panel;



}
