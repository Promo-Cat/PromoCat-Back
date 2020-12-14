package org.promocat.promocat.dto.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiveawayDTO {

    @ApiModelProperty(
            value = "Global progress in giveaway. How many drivers ends up at least 10 stocks.",
            dataType = "Long"
    )
    private Long globalProgress;

    @ApiModelProperty(
            value = "Personal progress in giveaway. How many stocks driver has ended up.",
            dataType = "Long"
    )
    private Long personalProgress;

    @ApiModelProperty(
            value = "Personal user's code, which given him in giveaway",
            dataType = "String"
    )
    private String personalCode;

}
