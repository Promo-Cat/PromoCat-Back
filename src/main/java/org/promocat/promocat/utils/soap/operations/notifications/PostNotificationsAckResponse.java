package org.promocat.promocat.utils.soap.operations.notifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:34 27.07.2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class PostNotificationsAckResponse extends AbstractOperation {


    /**
     * Статус запроса
     * OK – оповещения отмечены как прочитанные
     * NOK – оповещения не отмечены как прочитанные. Необходимо попробовать повторить операцию.
     */
    @XmlField("status")
    private String status;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return null;
    }
}
