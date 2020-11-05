package org.promocat.promocat.data_entities.car.car_photo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.File;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Table(name = "car_photo")
@EqualsAndHashCode(of = {}, callSuper = true)
@AllArgsConstructor
@Setter
public class CarPhoto extends File {

}
