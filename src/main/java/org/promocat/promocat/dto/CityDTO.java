package org.promocat.promocat.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:31 21.05.2020
 */
@ApiModel(
        value = "City",
        description = "Object representation of city used in PromoCat application.",
        parent = AbstractAccountDTO.class
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
// TODO docs
public class CityDTO extends AbstractDTO {
    private String address;
    private String postalCode;
    private String country;
    private String region;
    private String city;
    private String timezone;
    private String latitude;
    private String longitude;
    private String population;
    private Boolean active;
}
