package org.promocat.promocat.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.StockDurationConstraint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Created by Danil Lyskin at 19:54 12.05.2020
 */

@ApiModel(
        value = "Stock",
        description = "Object representation of companies stock."
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO extends AbstractDTO {

    @ApiModelProperty(
            value = "Stock name",
            dataType = "String",
            required = true
    )
    @NotBlank(message = "Название акции не может быть пустым.")
    private String name;

    @ApiModelProperty(
            value = "Stock activation status",
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    private Boolean isAlive;

    @ApiModelProperty(
            value = "Company id",
            dataType = "Long",
            required = true
    )
    @NotNull(message = "Id компании не может быть пустым.")
    private Long companyId;

    @ApiModelProperty(
            value = "Cities where the stock takes place",
            dataType = "List of StockCity entities",
            required = true,
            accessMode = ApiModelProperty.AccessMode.READ_ONLY
    )
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Set<StockCityDTO> cities;

    @ApiModelProperty(
            value = "Start time of stock",
            dataType = "Local date time",
            required = true
    )
    @NotNull(message = "Время начала акции не может быть пустым.")
    private LocalDateTime startTime;

    @ApiModelProperty(
            value = "Stock duration",
            allowableValues = "7, 14, 21, 28",
            dataType = "Long",
            required = true
    )
    @NotNull(message = "Время продолжительности акции не может быть пустым.")
    @StockDurationConstraint
    private Long duration;

    // TODO: 05.06.2020 DOCS
    private Double panel;

    @JsonIgnore
    private Set<MovementDTO> movements;
}
