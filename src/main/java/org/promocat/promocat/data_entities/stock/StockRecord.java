package org.promocat.promocat.data_entities.stock;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor(force = true)
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
    @NotNull(message = "Id организации не может быть пустым")
    @Column
    private Long company_id;
}
