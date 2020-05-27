package org.promocat.promocat.data_entities.movement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.tomcat.jni.Local;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.stock.Stock;
import org.promocat.promocat.data_entities.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "movement")
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movement extends AbstractEntity {

    private User user;
    private Stock stock;
    private LocalDateTime date;
    private Double distance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    @ManyToOne
    @JoinColumn(name = "stock_id")
    public Stock getStock() {
        return stock;
    }

    @Column(name = "date")
    public LocalDateTime getDate() {
        return date;
    }

    @Column(name = "distance")
    public Double getDistance() {
        return distance;
    }
}
