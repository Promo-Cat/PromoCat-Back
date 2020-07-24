package org.promocat.promocat.utils.soap;

import org.promocat.promocat.utils.soap.operations.PostBindPartnerWithPhoneRequest;

public class SoapGetMessageRequest extends SoapRequest {
    public SoapGetMessageRequest(Object pojo) {
        super(pojo);
        this.method = "GetMessageRequest";
    }

    public static void main(String[] args) {
        SoapRequest request = new SoapGetMessageRequest(
                new PostBindPartnerWithPhoneRequest("79062587099", "INCOME_REGISTRATION")
        );
        request.createMessage();
    }
}
