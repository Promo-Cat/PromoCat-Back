package org.promocat.promocat.data_entities.movement;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.stock.Stock;
import org.promocat.promocat.data_entities.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "movement")
@EqualsAndHashCode(of = {}, callSuper = true)
public class Movement extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "distance")
    private Double distance;

}
