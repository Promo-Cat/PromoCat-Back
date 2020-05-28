package org.promocat.promocat.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 19:31 21.05.2020
 */
@ApiModel(
        value = "City",
        description = "Object representation of city used in PromoCat application."
)
@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityDTO extends AbstractDTO {

    @ApiModelProperty(
            value = "City address",
            dataType = "String",
            required = true
    )
    private String address;

    @ApiModelProperty(
            value = "Prefix of city postal code",
            dataType = "String",
            required = true
    )
    private String postalCode;

    @ApiModelProperty(
            value = "Country",
            dataType = "String",
            required = true
    )
    private String country;

    @ApiModelProperty(
            value = "Region",
            dataType = "String",
            required = true
    )
    private String region;

    @ApiModelProperty(
            value = "City",
            dataType = "String",
            required = true
    )
    private String city;

    @ApiModelProperty(
            value = "Timezone in UTC format",
            dataType = "String",
            required = true
    )
    private String timezone;

    @ApiModelProperty(
            value = "Latitude",
            dataType = "String",
            required = true
    )
    private String latitude;

    @ApiModelProperty(
            value = "Longitude",
            dataType = "String",
            required = true
    )
    private String longitude;

    @ApiModelProperty(
            value = "Population",
            dataType = "String",
            required = true
    )
    private String population;

    @ApiModelProperty(
            value = "Activation status",
            dataType = "Boolean"
    )
    private Boolean active;
}
