package org.promocat.promocat.data_entities.stock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.data_entities.company.CompanyRecord;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Table(name = "stock")
public class StockRecord {

    /**
     * Id акции.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Время начала акции.
     */
    @NotNull(message = "Время начала акции не может быть пустым.")
    @Column
    private LocalDateTime start_time;

    /**
     * Время продолжительности акции.
     */
    @NotNull(message = "Время продолжительности акции не может быть пустым.")
    @Column
    private LocalDateTime duration;

    /**
     * Id организации, которой принадлежит акция.
     */
    @ManyToOne
    @JoinColumn(name = "company_id")
    private CompanyRecord company;
}
