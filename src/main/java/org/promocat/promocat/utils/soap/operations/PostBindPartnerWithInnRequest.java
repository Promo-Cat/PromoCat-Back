package org.promocat.promocat.utils.soap.operations;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.attributes.ConnectionPermissions;

import java.util.List;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 10:32 24.07.2020
 */
@Data
@AllArgsConstructor
public final class PostBindPartnerWithInnRequest {

    /**
     * ИНН налогоплательщика.
     */
    @XmlField("Inn")
    private String inn;

    /**
     * Список разрешений, которые запрашиваются у налогоплательщика.
     */
    @XmlField("Permissions")
    private List<ConnectionPermissions> permissions;

}
