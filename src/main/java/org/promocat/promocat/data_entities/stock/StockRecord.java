package org.promocat.promocat.data_entities.stock;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor(force = true)
@Table(name = "stock")
public class StockRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Время начала акции не может быть пустым.")
    @Column
    private LocalDateTime start_time;

    @NotBlank(message = "Время продолжительности акции не может быть пустым.")
    @Column
    private LocalDateTime duration;

    @NotBlank(message = "Id организации не может быть пустым")
    @Column
    private Long company_id;
}
