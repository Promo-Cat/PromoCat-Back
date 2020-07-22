package org.promocat.promocat.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@ApiModel(
        value = "Parameters",
        description = "Parameters for promocat application."
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParametersDTO extends AbstractDTO {

    @ApiModelProperty(
            value = "Fare for drivers.",
            dataType = "Double",
            required = true
    )
    private Double fare;

    @ApiModelProperty(
            value = "Prepayment for company.",
            dataType = "Double",
            required = true
    )
    private Double prepayment;

    @ApiModelProperty(
            value = "Postpayment for company.",
            dataType = "Double",
            required = true
    )
    private Double postpayment;

}
