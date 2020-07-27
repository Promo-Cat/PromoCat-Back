package org.promocat.promocat.utils.soap;

public class SoapSendMessageRequest extends SoapRequest {
    public SoapSendMessageRequest(Object pojo, String token) {
        super(pojo, token);
        this.method = "SendMessageRequest";
    }
}
