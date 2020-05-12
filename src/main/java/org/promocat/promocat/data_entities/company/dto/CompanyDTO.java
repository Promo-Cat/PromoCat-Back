package org.promocat.promocat.data_entities.company.dto;

import lombok.Data;
import org.promocat.promocat.data_entities.company.CompanyRecord;
import org.promocat.promocat.data_entities.stock.StockRecord;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Danil Lyskin at 20:21 12.05.2020
 */

@Data
public class CompanyDTO {
    private Long companyId;
    private String organizationName;
    private String supervisorFirstName;
    private String supervisorSecondName;
    private String supervisorPatronymic;
    private String ogrn;
    private String inn;
    private String telephone;
    private String mail;
    private String city;
    private Set<StockRecord> stocks;

    public CompanyDTO(CompanyRecord companyRecord) {
        this.companyId = companyRecord.getCompany_id();
        this.organizationName = companyRecord.getOrganization_name();
        this.supervisorFirstName = companyRecord.getSupervisor_first_name();
        this.supervisorSecondName = companyRecord.getSupervisor_second_name();
        this.supervisorPatronymic = companyRecord.getSupervisor_patronymic();
        this.ogrn = companyRecord.getOgrn();
        this.inn = companyRecord.getInn();
        this.telephone = companyRecord.getTelephone();
        this.mail = companyRecord.getMail();
        this.city = companyRecord.getCity();
        Set<StockRecord> stocks = new HashSet<>();
        if (Objects.nonNull(companyRecord.getStocks())) {
            stocks.addAll(companyRecord.getStocks());
        }
        this.stocks = stocks;
    }
}
