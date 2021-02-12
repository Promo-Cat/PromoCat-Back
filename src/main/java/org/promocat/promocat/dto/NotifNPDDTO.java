package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

/**
 * Created by Danil Lyskin at 10:28 02.02.2021
 */

@ApiModel(
        value = "NotifNPD",
        description = "Object representation users notifications."
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotifNPDDTO extends AbstractDTO {

    @ApiModelProperty(
            value = "Notifications create time",
            dataType = "ZoneDateTime",
            required = true
    )
    private ZonedDateTime time;

    @ApiModelProperty(
            value = "Notifications ID",
            dataType = "String",
            required = true
    )
    @NotNull(message = "ID не может быть пустым.")
    private String notifId;

    @ApiModelProperty(
            value = "Users id",
            dataType = "Long",
            required = true
    )
    @NotNull(message = "Id пользователя не может быть пустым.")
    private Long userId;

    @ApiModelProperty(
            value = "Notifications title",
            dataType = "String",
            required = true
    )
    private String title;

    @ApiModelProperty(
            value = "Notifications body",
            dataType = "String",
            required = true
    )
    private String body;

    @ApiModelProperty(
            value = "Notifications flag",
            dataType = "Boolean",
            required = true
    )
    @NotNull(message = "Пометка для уведомления не может быть пустой.")
    private Boolean isOpen;
}
