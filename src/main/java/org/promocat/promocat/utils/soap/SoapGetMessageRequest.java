package org.promocat.promocat.utils.soap;

import org.promocat.promocat.utils.soap.attributes.ConnectionPermissions;
import org.promocat.promocat.utils.soap.operations.PostBindPartnerWithPhoneRequest;

import java.util.List;

public class SoapGetMessageRequest extends SoapRequest {
    public SoapGetMessageRequest(Object pojo) {
        super(pojo);
        this.method = "GetMessageRequest";
    }

    public static void main(String[] args) {
        SoapRequest request = new SoapGetMessageRequest(
                new PostBindPartnerWithPhoneRequest("79062587099",
                        List.of(ConnectionPermissions.INCOME_REGISTRATION))
        );
        request.createMessage();
    }
}
