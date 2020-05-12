package org.promocat.promocat.data_entities.stock;

import lombok.*;
import org.promocat.promocat.data_entities.company.CompanyRecord;
import org.promocat.promocat.data_entities.stock.dto.StockDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Getter
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
    private CompanyRecord company;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof StockRecord)) {
            return false;
        }

        StockRecord that = (StockRecord) o;
        return Objects.equals(id, that.id);
    }

    public boolean equalsFields(Object o) {
        StockRecord that = (StockRecord) o;

        return equals(o) && Objects.equals(that.getStart_time(), start_time)
                && Objects.equals(that.getDuration(), duration)
                && Objects.equals(that.getCompany(), company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start_time, duration);
    }
}
