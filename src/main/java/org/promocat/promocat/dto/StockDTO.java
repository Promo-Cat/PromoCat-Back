package org.promocat.promocat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    private String name;
    private Long count;
    private LocalDateTime start_time;
    private LocalDateTime duration;
    private Long companyId;
    private List<PromoCodeDTO> codes;
}
