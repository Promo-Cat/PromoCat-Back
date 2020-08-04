package org.promocat.promocat.utils.soap.operations.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;

import java.time.ZonedDateTime;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:20 24.07.2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class PostUnbindPartnerResponse extends AbstractOperation {

    /**
     * Время отмены регистрации пользователя в PromoCat.
     */
    @XmlField("UnregistrationTime")
    private ZonedDateTime unregistrationTime;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return null;
    }
}
