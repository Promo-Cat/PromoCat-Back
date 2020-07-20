package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
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

    private Long userId;
    private Long stockId;
    private Double bannedEarnings;

}
