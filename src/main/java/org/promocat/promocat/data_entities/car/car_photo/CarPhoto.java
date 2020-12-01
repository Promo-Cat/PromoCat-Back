package org.promocat.promocat.data_entities.car.car_photo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.promocat.promocat.data_entities.File;

import javax.persistence.*;

@Entity
@Table(name = "car_photo")
@EqualsAndHashCode(of = {}, callSuper = true)
@AllArgsConstructor
@Setter
public class CarPhoto extends File {

}
