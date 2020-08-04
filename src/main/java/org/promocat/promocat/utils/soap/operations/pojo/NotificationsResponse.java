package org.promocat.promocat.utils.soap.operations.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.constraints.XmlInnerObject;

import java.util.List;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:42 27.07.2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class NotificationsResponse {

    /**
     * ИНН НП.
     */
    @XmlField("inn")
    private String inn;

    /**
     * Список оповещений.
     */
    @XmlField("notif")
    @XmlInnerObject(Notif.class)
    private List<Notif> notifs;
}
