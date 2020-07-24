package org.promocat.promocat.utils.soap;

import org.promocat.promocat.utils.soap.operations.PostBindPartnerWithPhoneRequest;

public class SoapGetMessageRequest extends SoapRequest {
    public SoapGetMessageRequest(Object pojo, String token) {
        super(pojo, token);
        this.method = "GetMessageRequest";
    }

    public static void main(String[] args) {

//        request.send();
    }
}
