package org.promocat.promocat.utils.soap.operations.notifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.constraints.XmlInnerObject;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;
import org.promocat.promocat.utils.soap.operations.pojo.NotificationsResponse;

import java.util.List;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:34 27.07.2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class GetNotificationsResponse extends AbstractOperation {

    /**
     * Список оповещений. Не более 1000.
     */
    @XmlField("notificationsResponse")
    @XmlInnerObject(NotificationsResponse.class)
    private List<NotificationsResponse> notificationsResponse;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return null;
    }
}
