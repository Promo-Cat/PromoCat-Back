package org.promocat.promocat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Created by Danil Lyskin at 20:21 12.05.2020
 */

@EqualsAndHashCode(of = {}, callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTO extends AbstractDTO {
    private String organization_name;
    private String supervisor_first_name;
    private String supervisor_second_name;
    private String supervisor_patronymic;
    private String ogrn;
    private String inn;
    private String telephone;
    private String mail;
    private String city;
    private Set<StockDTO> stocks;
}
