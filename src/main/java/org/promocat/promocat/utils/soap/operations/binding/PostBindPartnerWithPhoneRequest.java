package org.promocat.promocat.utils.soap.operations.binding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.attributes.ConnectionPermissions;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class PostBindPartnerWithPhoneRequest extends AbstractOperation {

    /**
     * Номер телефона налогоплательщика.
     * Формат 7XXXXXXXXXX.
     */
    @XmlField("Phone")
    private String phone;

    /**
     * Список разрешений, которые запрашиваются у налогоплательщика.
     */
    @XmlField("Permissions")
    private List<String> permissions;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return PostBindPartnerWithPhoneResponse.class;
    }
}
