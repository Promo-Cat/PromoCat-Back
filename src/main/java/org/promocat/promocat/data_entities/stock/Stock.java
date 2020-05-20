package org.promocat.promocat.data_entities.stock;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;
import org.promocat.promocat.data_entities.company.Company;
import org.promocat.promocat.data_entities.promo_code.PromoCode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
    private String city;
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
    @Column(name = "count")
    public Long getCount() {
        return count;
    }

    /**
     * Активность промокода
     */
    @Column(name = "isAlive")
    public Boolean getIsAlive() {
        return isAlive;
    }

    /**
     * Город
     */
    @NotBlank(message = "Город не может быть пустым.")
    @Column(name = "city")
    public String getCity() {
        return city;
    }

    /**
     * Время начала акции.
     */
    @NotNull(message = "Время начала акции не может быть пустым.")
    @Column
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Время продолжительности акции.
     */
    @NotNull(message = "Время продолжительности акции не может быть пустым.")
    @Column
    public Long getDuration() {
        return duration;
    }

    /**
     * Id организации, которой принадлежит акция.
     */
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Company getCompany() {
        return company;
    }

    /**
     * Промокоды
     */
    @OneToMany(mappedBy = "stock", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public List<PromoCode> getCodes() {
        return codes;
    }
}

