package org.promocat.promocat.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

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

    @ApiModelProperty(value = "Fare for drivers. Min value 0.", dataType = "Double")
    @Min(0)
    private Double fare;

    @ApiModelProperty(value = "Prepayment for company. Min value 0.", dataType = "Double")
    @Min(0)
    private Double prepayment;

    @ApiModelProperty(value = "Postpayment for company. Min value 0.", dataType = "Double")
    @Min(0)
    private Double postpayment;

}
