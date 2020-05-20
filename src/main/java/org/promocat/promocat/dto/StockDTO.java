package org.promocat.promocat.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Danil Lyskin at 19:54 12.05.2020
 */

@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StockDTO extends AbstractDTO{

    @JsonIgnore
    private static Long[] days = new Long[] {7L, 14L, 21L, 28L};

    public static Long getDay(int index) {
        return days[index];
    }

    public static int daysLength() {
        return days.length;
    }

    @NotBlank(message = "Название акции не может быть пустым.")
    private String name;

    @NotNull(message = "Количество постеров не может быть пустым.")
    private Long count;

    private Boolean isAlive;

    @NotNull(message = "Id компании не может быть пустым.")
    private Long companyId;

    @NotBlank(message = "Город не может быть пустым.")
    private String city;

    @NotNull(message = "Время начала акции не может быть пустым.")
    private LocalDateTime startTime;

    @NotNull(message = "Время продолжительности акции не может быть пустым.")
    private Long duration;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<PromoCodeDTO> codes;
}
