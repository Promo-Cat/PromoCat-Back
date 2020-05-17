package org.promocat.promocat.data_entities.city;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "postal_code")
    private String postal_code;

    @Column(name = "country")
    private String country;

    @Column(name = "region")
    private String region;

    @Column(name = "city")
    private String city;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "geo_lat")
    private String latitude;

    @Column(name = "geo_lon")
    private String longitude;

    @Column(name = "population")
    private String population;

    @Column(name = "active")
    private Boolean active;
}
