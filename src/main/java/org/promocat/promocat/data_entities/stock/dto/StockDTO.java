package org.promocat.promocat.data_entities.stock.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.promocat.promocat.data_entities.company.dto.CompanyDTO;
import org.promocat.promocat.data_entities.stock.StockRecord;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Created by Danil Lyskin at 19:54 12.05.2020
 */

@Setter
@Getter
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class StockDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime duration;
    private CompanyDTO companyDTO;

    private void fillIdStartTimeDuration(StockRecord stockRecord) {
        this.id = stockRecord.getId();
        this.startTime = stockRecord.getStart_time();
        this.duration = stockRecord.getDuration();
    }

    public StockDTO(StockRecord stockRecord, CompanyDTO companyDTO) {
        fillIdStartTimeDuration(stockRecord);
        this.companyDTO = companyDTO;
    }

    public StockDTO(StockRecord stockRecord) {
        fillIdStartTimeDuration(stockRecord);
        this.companyDTO = new CompanyDTO(stockRecord.getCompany());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (!(o instanceof StockDTO)) {
            return false;
        }

        StockDTO stockDTO = (StockDTO) o;
        return Objects.equals(id, stockDTO.id);
    }

    public boolean equalsFields(Object o) {
        StockDTO that = (StockDTO) o;

        return equals(o) && Objects.equals(that.getStartTime(), startTime)
                && Objects.equals(that.getDuration(), duration)
                && Objects.equals(that.getCompanyDTO(), companyDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, duration);
    }
}
