package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.promocat.promocat.attributes.AccountType;

import java.time.LocalDateTime;

/**
 * Created by Danil Lyskin at 21:14 31.10.2020
 */

@ApiModel(
        value = "NewsFeed",
        description = "Object representation of news feed of PromoCat application." +
                "News name, news description."
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@AllArgsConstructor
public class NewsFeedDTO extends AbstractDTO {

    @ApiModelProperty(
            value = "Name of news",
            dataType = "String"
    )
    private String name;

    @ApiModelProperty(
            value = "Description",
            dataType = "String"
    )
    private String description;

    @ApiModelProperty(
            value = "Start time of news",
            dataType = "Local date time"
    )
    private LocalDateTime startTime;

    @ApiModelProperty(
            value = "News type"
    )
    private AccountType type;

    public NewsFeedDTO() {}
}
