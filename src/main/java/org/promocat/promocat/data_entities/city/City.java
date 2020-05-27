package org.promocat.promocat.data_entities.city;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "city")
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
// TODO javadocs
public class City extends AbstractEntity {

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

    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    @Column(name = "postal_code")
    public String getPostalCode() {
        return postalCode;
    }

    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    @Column(name = "region")
    public String getRegion() {
        return region;
    }

    @Column(name = "city")
    public String getCity() {
        return city;
    }

    @Column(name = "timezone")
    public String getTimezone() {
        return timezone;
    }

    @Column(name = "geo_lat")
    public String getLatitude() {
        return latitude;
    }

    @Column(name = "geo_lon")
    public String getLongitude() {
        return longitude;
    }

    @Column(name = "population")
    public String getPopulation() {
        return population;
    }

    @Column(name = "active")
    public Boolean getActive() {
        return active;
    }

}
