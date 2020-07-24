package org.promocat.promocat.utils.soap;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.utils.soap.attributes.ConnectionPermissions;
import org.promocat.promocat.utils.soap.operations.PostBindPartnerWithPhoneRequest;
import org.promocat.promocat.utils.soap.operations.SendMessageResponse;

import javax.xml.soap.*;
import java.io.IOException;
import java.util.List;

@Slf4j
public class SoapGetMessageRequest extends SoapRequest {
    public SoapGetMessageRequest(SendMessageResponse pojo, String token) {
        super(pojo, token);
        this.method = "GetMessageRequest";
    }

    @Override
    public SOAPMessage createMessage() {
        try {
            SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
            SOAPEnvelope soapEnvelope = soapMessage.getSOAPPart().getEnvelope();
            SOAPBody soapPart = soapMessage.getSOAPBody();
            soapEnvelope.addNamespaceDeclaration("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
            soapEnvelope.addNamespaceDeclaration("ns", "urn://x-artefacts-gnivc-ru/inplat/servin/OpenApiAsyncMessageConsumerService/types/1.0");
            soapEnvelope.setPrefix("soapenv");
            soapMessage.getSOAPHeader().setPrefix("soapenv");
            soapPart.setPrefix("soapenv");
            SOAPElement operationBody = soapPart
                    .addChildElement(method, "ns")
                    .addChildElement("MessageId", "ns");
            operationBody.setTextContent(((SendMessageResponse)pojo).getMessageId());
            getHeaders(soapMessage.getMimeHeaders());
            soapMessage.saveChanges();
            System.out.println("\n---------------REQUEST--------------\n");
            soapMessage.writeTo(System.out);

            return soapMessage;
        } catch (SOAPException e) {
            log.error("Failed to create SOAP message", e);
        } catch (IOException e) {
            log.error("Failed to write soapMessage to System.out", e);
        }
        return null;
    }
}
