package org.promocat.promocat.data_entities.notification_npd;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.promocat.promocat.data_entities.abstract_account.AbstractEntity;
import org.promocat.promocat.data_entities.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Danil Lyskin at 10:18 02.06.2021
 */

@Entity
@Table(name = "notifs_npd")
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotifNPD extends AbstractEntity {

    private String notifId;
    private User user;
    private String title;
    private String body;
    private Boolean isOpen;

    @Column(name = "notif_id", unique = true)
    public String getNotifId() {
        return notifId;
    }

    @Cascade({CascadeType.SAVE_UPDATE})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    @Column(name = "body")
    public String getBody() {
        return body;
    }

    @NotNull(message = "Пометка не может быть пустой.")
    @Column(name = "is_open", columnDefinition = "boolean default false")
    public Boolean getIsOpen() {
        return isOpen;
    }
}
