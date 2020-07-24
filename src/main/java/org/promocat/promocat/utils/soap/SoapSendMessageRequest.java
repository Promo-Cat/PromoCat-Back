package org.promocat.promocat.utils.soap;

public class SoapSendMessageRequest extends SoapRequest {
    public SoapSendMessageRequest(Object pojo) {
        super(pojo);
        this.method = "SendMessageRequest";
    }
}
