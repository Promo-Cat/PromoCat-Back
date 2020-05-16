package org.promocat.promocat.data_entities.stock;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.data_entities.AbstractEntity;
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
    private LocalDateTime start_time;
    private LocalDateTime duration;
    private Company company;
    List<PromoCode> codes;

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
    //@NotNull(message = "Количество промокодов не может быть нулем.")
    @Column(name = "count")
    public Long getCount() {
        return count;
    }
    /**
     * Время начала акции.
     */
    @NotNull(message = "Время начала акции не может быть пустым.")
    @Column
    public LocalDateTime getStart_time() {
        return start_time;
    }

    /**
     * Время продолжительности акции.
     */
    @NotNull(message = "Время продолжительности акции не может быть пустым.")
    @Column
    public LocalDateTime getDuration() {
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

