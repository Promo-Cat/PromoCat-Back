package org.promocat.promocat.data_entities.news_feed;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.promocat.promocat.attributes.AccountType;
import org.promocat.promocat.data_entities.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Created by Danil Lyskin at 21:00 31.10.2020
 */

@Entity
@Table(name = "news_feed")
@EqualsAndHashCode(of = {}, callSuper = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsFeed extends AbstractEntity {

    private String name;
    private String description;
    private LocalDateTime startTime;
    private AccountType type;

    /**
     * Название новости.
     */
    @Column(name = "name")
    public String getName() {
        return name;
    }

    /**
     * Описание новости.
     */
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    /**
     * Дата и время начала новости.
     */
    @NotNull(message = "время начала должно быть задано")
    @Column(name = "start_time")
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * Тип новости.
     */
    @NotNull(message = "тип аккаунта должен быть задан")
    @Column(name = "type")
    public AccountType getType() {
        return type;
    }
}
