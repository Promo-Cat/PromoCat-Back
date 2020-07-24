package org.promocat.promocat.utils.soap.operations;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.promocat.promocat.constraints.XmlField;

@Data
@AllArgsConstructor
public class PostBindPartnerWithPhoneRequest {

    @XmlField("Phone")
    private String phone;

    @XmlField("Permissions")
    private String permissions;

}
