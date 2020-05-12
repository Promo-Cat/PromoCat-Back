package org.promocat.promocat.data_entities.company.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.promocat.promocat.data_entities.company.CompanyRecord;
import org.promocat.promocat.data_entities.stock.StockRecord;
import org.promocat.promocat.data_entities.stock.dto.StockDTO;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Danil Lyskin at 20:21 12.05.2020
 */

@Setter
@Getter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class CompanyDTO {
    private Long id;
    private String organizationName;
    private String supervisorFirstName;
    private String supervisorSecondName;
    private String supervisorPatronymic;
    private String ogrn;
    private String inn;
    private String telephone;
    private String mail;
    private String city;
    private Set<StockDTO> stocks;

    public CompanyDTO(CompanyRecord companyRecord) {
        this.id = companyRecord.getCompany_id();
        this.organizationName = companyRecord.getOrganization_name();
        this.supervisorFirstName = companyRecord.getSupervisor_first_name();
        this.supervisorSecondName = companyRecord.getSupervisor_second_name();
        this.supervisorPatronymic = companyRecord.getSupervisor_patronymic();
        this.ogrn = companyRecord.getOgrn();
        this.inn = companyRecord.getInn();
        this.telephone = companyRecord.getTelephone();
        this.mail = companyRecord.getMail();
        this.city = companyRecord.getCity();
        Set<StockDTO> stocks = new HashSet<>();
        if (Objects.nonNull(companyRecord.getStocks())) {
            for (StockRecord stock : companyRecord.getStocks()) {
                stocks.add(new StockDTO(stock, this));
            }
        }
        this.stocks = stocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof CompanyDTO)) {
            return false;
        }

        CompanyDTO that = (CompanyDTO) o;
        return Objects.equals(id, that.id);
    }

    public boolean equalsFields(Object o) {
        CompanyDTO that = (CompanyDTO) o;

        return equals(o) && Objects.equals(that.getCity(), city)
                && Objects.equals(that.getOrganizationName(), organizationName)
                && Objects.equals(that.getSupervisorFirstName(), supervisorFirstName)
                && Objects.equals(that.getSupervisorSecondName(), supervisorSecondName)
                && Objects.equals(that.getSupervisorPatronymic(), supervisorPatronymic)
                && Objects.equals(that.getOgrn(), ogrn)
                && Objects.equals(that.getInn(), inn)
                && Objects.equals(that.getMail(), mail)
                && Objects.equals(that.getTelephone(), telephone)
                && Objects.equals(that.getStocks(), stocks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, organizationName, supervisorFirstName, supervisorSecondName, supervisorPatronymic, ogrn, inn, telephone, mail, city);
    }
}
