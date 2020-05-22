package org.promocat.promocat.data_entities.stock;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.constraints.StockDurationConstraint;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.city.City;
import org.promocat.promocat.data_entities.company.Company;
import org.promocat.promocat.data_entities.promo_code.PromoCode;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

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
    private Long count;
    private Boolean isAlive;
    private City city;
    private Company company;
    private LocalDateTime startTime;
    private Long duration;
    private List<PromoCode> codes;

    /**
     * Название акции.
     */
    @NotBlank(message = "Название акции не может быть пустым.")
    @Column(name = "name")
    public String getName() {
        return name;
    }

    /**
     * Количество промокодов.
     */
    @NotNull(message = "Количество промокодов не может быть нулем.")
    @Column(name = "count")
    public Long getCount() {
        return count;
    }

    /**
     * Активность акции.
     */
    @Column(name = "isAlive")
    public Boolean getIsAlive() {
        return isAlive;
    }

    /**
     * Город
     */
    @NotNull(message = "Город не может быть пустым.")
    @ManyToOne
    @JoinColumn(name = "city_id")
    public City getCity() {
        return city;
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
    public List<PromoCode> getCodes() {
        return codes;
    }
}

