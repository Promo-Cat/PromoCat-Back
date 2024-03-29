package org.promocat.promocat.data_entities.stock;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.promocat.promocat.attributes.StockStatus;
import org.promocat.promocat.constraints.StockDurationConstraint;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.company.Company;
import org.promocat.promocat.data_entities.movement.Movement;
import org.promocat.promocat.data_entities.parameters.Parameters;
import org.promocat.promocat.data_entities.stock.poster.Poster;
import org.promocat.promocat.data_entities.stock.stock_city.StockCity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 09:05 14.05.2020
 */
@Entity
@Table(name = "stock")
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Stock extends AbstractEntity {

    private String name;
    private StockStatus status;
    private Company company;
    private LocalDateTime startTime;
    private Long duration;
    private Set<Movement> movements;
    private Set<StockCity> cities;
    private Double fare;
    private Double prepayment;
    private Double postpayment;
    private Poster poster;

    /**
     * Значение комиссии для конкретной акции.
     * Берётся из {@link Parameters} в момент создания акции.
     */
    @Column(name = "panel")
    public Double getFare() {
        return fare;
    }

    // TODO docs
    @Column(name = "prepayment")
    public Double getPrepayment() {
        return prepayment;
    }

    // TODO docs
    @Column(name = "postpayment")
    public Double getPostpayment() {
        return postpayment;
    }

    /**
     * Название акции.
     */
    @NotBlank(message = "Название акции не может быть пустым.")
    @Column(name = "name")
    public String getName() {
        return name;
    }

    /**
     * Статус акции.
     */
    @Column(name = "status")
    public StockStatus getStatus() {
        return status;
    }

    /**
     * Время начала акции.
     */
    @Column(name = "start_time")
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Время продолжительности акции.
     */
    @Column(name = "duration")
    @StockDurationConstraint
    public Long getDuration() {
        return duration;
    }

    /**
     * Id организации, которой принадлежит акция.
     */
    @Cascade({CascadeType.SAVE_UPDATE})
    @ManyToOne(fetch = FetchType.LAZY)
    public Company getCompany() {
        return company;
    }

    /**
     * Передвижения пользователей участвующих в акции.
     */
    @Cascade({CascadeType.SAVE_UPDATE})
    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY)
    public Set<Movement> getMovements() {
        return movements;
    }

    /**
     * Города, в которых запущена акция.
     */
    @Cascade({CascadeType.ALL})
    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY)
    public Set<StockCity> getCities() {
        return cities;
    }


    /**
     * Постер акции.
     */
    @Cascade({CascadeType.ALL})
    @OneToOne(orphanRemoval = true)
    @JoinColumn(name = "poster_id")
    public Poster getPoster() {
        return poster;
    }

}

