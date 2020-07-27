package org.promocat.promocat.utils.soap.operations.notifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;
import org.promocat.promocat.utils.soap.operations.pojo.NotificationsRequest;

import java.util.List;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:34 27.07.2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class GetNotificationsRequest extends AbstractOperation {

    /**
     * Лист требуемых оповещений. Не более 1000.
     */
    @XmlField("notificationsRequest")
    private List<NotificationsRequest> notificationsRequest;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return GetNotificationsResponse.class;
    }
}
