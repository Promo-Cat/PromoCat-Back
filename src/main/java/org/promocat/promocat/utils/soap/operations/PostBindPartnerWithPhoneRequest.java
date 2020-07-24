package org.promocat.promocat.utils.soap.operations;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.attributes.ConnectionPermissions;

import java.util.List;

@Data
@AllArgsConstructor
public final class PostBindPartnerWithPhoneRequest {

    /**
     * Номер телефона налогоплательщика.
     * Формат +7(XXX)XXX-XX-XX.
     */
    @XmlField("Phone")
    private String phone;

    /**
     * Список разрешений, которые запрашиваются у налогоплательщика.
     */
    @XmlField("Permissions")
    private List<ConnectionPermissions> permissions;

}
