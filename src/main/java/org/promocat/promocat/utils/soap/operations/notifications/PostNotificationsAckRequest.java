package org.promocat.promocat.utils.soap.operations.notifications;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.constraints.XmlInnerObject;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;
import org.promocat.promocat.utils.soap.operations.pojo.PostNotificationsRequest;

import java.util.List;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:34 27.07.2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class PostNotificationsAckRequest extends AbstractOperation {


    @XmlField("notificationList")
    @XmlInnerObject(PostNotificationsAckRequest.class)
    private List<PostNotificationsRequest> notificationList;


    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return PostNotificationsAckResponse.class;
    }
}
