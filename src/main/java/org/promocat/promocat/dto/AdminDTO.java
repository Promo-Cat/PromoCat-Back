package org.promocat.promocat.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 20:15 22.05.2020
 */
@ApiModel(
        value = "Admin",
        description = "Object representation of admin.",
        parent = AbstractDTO.class
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
public class AdminDTO extends AbstractAccountDTO {
}
