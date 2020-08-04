package org.promocat.promocat.utils.soap.operations.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:37 27.07.2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class NotificationsRequest {

    /**
     * ИНН НП.
     */
    @XmlField("inn")
    private String inn;

    /**
     * Показывать прочитанные.
     */
    @XmlField("GetAcknowleged")
    private Boolean getAcknowleged;

    /**
     * Показывать заархивированные.
     */
    @XmlField("GetArchived")
    private Boolean getArchived;
}
