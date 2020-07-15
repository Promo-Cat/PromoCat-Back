package org.promocat.promocat.data_entities.movement;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.stock.Stock;
import org.promocat.promocat.data_entities.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "movement")
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movement extends AbstractEntity {

    private User user;
    private Stock stock;
    private LocalDate date;
    private Double distance;
    private Double earnings;
//    private Double panel;

    @Cascade({CascadeType.SAVE_UPDATE})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    @Cascade({CascadeType.SAVE_UPDATE})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_id")
    public Stock getStock() {
        return stock;
    }

    @NotNull(message = "Дата не может быть пустой.")
    @Column(name = "date")
    public LocalDate getDate() {
        return date;
    }

    @NotNull(message = "Дистанция не может быть пустой.")
    @Column(name = "distance")
    public Double getDistance() {
        return distance;
    }

    @NotNull(message = "Заработок не может быть пустым.")
    @Column(name = "earnings")
    public Double getEarnings() {
        return earnings;
    }

//    @NotNull(message = "Комиссия не может быть пустой.")
//    @Column(name = "panel")
//    public Double getPanel() {
//        return panel;
//    }
}
