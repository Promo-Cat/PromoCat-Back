package org.promocat.promocat.utils.soap.operations.rights;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.attributes.ConnectionPermissions;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;

import java.util.List;

/**
 * @author Grankin Maxim (maximgran@gmail.com) at 11:46 27.07.2020
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class GetGrantedPermissionsResponse extends AbstractOperation {

    /**
     * Список разрешений предоставленых партнеру.
     */
    @XmlField("GrantedPermissionsList")
    private List<ConnectionPermissions> grantedPermissionsList;

    @Override
    public Class<? extends AbstractOperation> getResponseClass() {
        return null;
    }
}
