package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel(
        value = "User ban",
        description = "Object representation of user ban in one stock of PromoCat application."
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class UserBanDTO extends AbstractDTO {

    @ApiModelProperty(
            value = "Id of the user who will be banned.",
            dataType = "Long",
            required = true
    )
    private Long userId;

    @ApiModelProperty(
            value = "Id of the stock in which user will be banned.",
            dataType = "Long",
            required = true
    )
    private Long stockId;

    @ApiModelProperty(
            value = "Number of funds banned.",
            dataType = "Double",
            required = true
    )
    private Double bannedEarnings;

}
