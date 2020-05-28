package org.promocat.promocat.data_entities.stock;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.constraints.StockDurationConstraint;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.company.Company;
import org.promocat.promocat.data_entities.movement.Movement;
import org.promocat.promocat.data_entities.promo_code.PromoCode;
import org.promocat.promocat.data_entities.stock.city_stock.StockCity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
    private Boolean isAlive;
    private Company company;
    private LocalDateTime startTime;
    private Long duration;
    // TODO List -> Set
    private Set<PromoCode> codes;
    private Set<Movement> movements;
    private Set<StockCity> cities;
    
    /**
     * Название акции.
     */
    @NotBlank(message = "Название акции не может быть пустым.")
    @Column(name = "name")
    public String getName() {
        return name;
    }

    /**
     * Активность акции.
     */
    @Column(name = "isAlive")
    public Boolean getIsAlive() {
        return isAlive;
    }


    /**
     * Время начала акции.
     */
    @NotNull(message = "Время начала акции не может быть пустым.")
    @Column(name = "start_time")
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Время продолжительности акции.
     */
    @NotNull(message = "Время продолжительности акции не может быть пустым.")
    @Column(name = "duration")
    @StockDurationConstraint
    public Long getDuration() {
        return duration;
    }

    /**
     * Id организации, которой принадлежит акция.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Company getCompany() {
        return company;
    }

    /**
     * Промокоды
     */
    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<PromoCode> getCodes() {
        return codes;
    }

    /**
     * TODO
     */
    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Movement> getMovements() {
        return movements;
    }

    /**
     * TODO
     */
    @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<StockCity> getCities() {
        return cities;
    }
}

