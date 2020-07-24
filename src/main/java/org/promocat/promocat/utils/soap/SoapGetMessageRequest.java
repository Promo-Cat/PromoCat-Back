package org.promocat.promocat.utils.soap;

import org.promocat.promocat.utils.soap.attributes.ConnectionPermissions;
import org.promocat.promocat.utils.soap.operations.PostBindPartnerWithPhoneRequest;

import java.util.List;

public class SoapGetMessageRequest extends SoapRequest {
    public SoapGetMessageRequest(Object pojo, String token) {
        super(pojo, token);
        this.method = "GetMessageRequest";
    }

    public static void main(String[] args) {
    }
}
