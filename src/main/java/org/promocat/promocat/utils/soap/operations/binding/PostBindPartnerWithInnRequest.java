package org.promocat.promocat.utils.soap.operations.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.attributes.ConnectionPermissions;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;

import java.util.List;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 10:32 24.07.2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class PostBindPartnerWithInnRequest extends AbstractOperation {

    /**
     * ИНН налогоплательщика.
     */
    @XmlField("Inn")
    private String inn;

    /**
     * Список разрешений, которые запрашиваются у налогоплательщика.
     */
    @XmlField("Permissions")
    private List<String> permissions;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return PostBindPartnerWithInnRequest.class;
    }
}
