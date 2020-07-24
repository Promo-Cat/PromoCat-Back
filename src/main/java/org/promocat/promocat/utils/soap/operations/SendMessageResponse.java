package org.promocat.promocat.utils.soap.operations;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.promocat.promocat.constraints.XmlField;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:15 24.07.2020
 */
@Data
@AllArgsConstructor
public class SendMessageResponse {

    /**
     * Id сообщения.
     */
    @XmlField("MessageId")
    private String messageId;

}
