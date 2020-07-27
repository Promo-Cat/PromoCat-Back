package org.promocat.promocat.utils.soap.operations.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.attributes.NotificationStatus;

import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 12:45 27.07.2020
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class Notif {

    /**
     * Id оповещения.
     */
    @XmlField("id")
    private String id;

    /**
     * Заголовок оповещения.
     */
    @XmlField("title")
    private String title;

    /**
     * Сообщение оповещения.
     */
    @XmlField("message")
    private String message;

    /**
     * Статус оповещения.
     */
    @XmlField("status")
    private NotificationStatus status;

    /**
     * Дата создания.
     */
    @XmlField("createdAt")
    private ZonedDateTime createdAt;

    /**
     * Дата создания последнего обновления.
     */
    @XmlField("updatedAt")
    private ZonedDateTime updatedAt;


}
