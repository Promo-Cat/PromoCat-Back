package org.promocat.promocat.data_entities.car.sts;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.promocat.promocat.data_entities.File;

import javax.persistence.*;

@Entity
@Table(name = "sts")
@EqualsAndHashCode(of = {}, callSuper = true)
@AllArgsConstructor
@Setter
public class Sts extends File {

}
