package org.promocat.promocat.utils.soap;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.internal.util.xml.XmlDocument;
import org.promocat.promocat.constraints.XmlField;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.*;
import javax.xml.transform.dom.DOMSource;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Optional;

@Slf4j
public abstract class SoapRequest {

    /**
     * Operation name taking for POJO name. (example: PostPlatformRegistrationRequest)
     */
    protected final String operation;
    protected final SOAPConnection soapConnection;
    protected SOAPMessage soapMessage;
    protected Object pojo;
    /**
     * GetMessage or SendMessage
     */
    protected String method;

    public SoapRequest(Object pojo) {
        SOAPConnection soapConnection1 = null;
        this.pojo = pojo;
        this.operation = pojo.getClass().getSimpleName();
        try {
            soapConnection1 = SOAPConnectionFactory.newInstance().createConnection();
        } catch (SOAPException e) {
            log.error("Failed to create SOAPConnection", e);
        }
        this.soapConnection = soapConnection1;
    }

    public Optional<SOAPMessage> send(String destinationURL) {
        SOAPMessage response = null;
        createMessage();
        try {
            response = soapConnection.call(createMessage(), destinationURL);
            response.writeTo(System.out);
            soapConnection.close();
            // FIXME: 23.07.2020 IOException
        } catch (SOAPException | IOException e) {
            log.error("Failed to send message", e);
        }
        return Optional.ofNullable(response);
    }

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
                    .addChildElement("Message", "ns")
                    .addChildElement(operation, "", "urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0");
            insertPojoFields(operationBody);
            soapMessage.saveChanges();
            soapMessage.writeTo(System.out);

            return soapMessage;
        } catch (SOAPException e) {
            log.error("Failed to create SOAP message", e);
        } catch (IOException e) {
            log.error("Failed to write soapMessage to System.out", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected MimeHeaders getHeaders(MimeHeaders headers) {
        headers.addHeader("FNS-OpenApi-UserToken", "fi7uTtOr5xPFEnxIvorCP8RzWQI0USN1TQoniL5glBpeRhZSqQLMR");
        headers.addHeader("FNS-OpenApi-Token", "");
        headers.addHeader("Content-Type", "application/xml; charset=utf-8");
        return headers;
    }

    private Node pojoToXml() {
        try {
            XmlMapper xmlMapper = new XmlMapper();
            String deserializedPojo = xmlMapper.writeValueAsString(pojo);
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            factory.setNamespaceAware(true);
            Document document = factory
                    .newDocumentBuilder()
                    .parse(
                            new InputSource(
                                    new StringReader(deserializedPojo)
                            )
                    );
            return document;
        } catch (SAXException e) {
            log.error("Failed to parse xml", e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void insertPojoFields(SOAPElement target) throws SOAPException, IllegalAccessException {
        for (Field declaredField : pojo.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(XmlField.class)) {
                XmlField annotation = declaredField.getAnnotation(XmlField.class);
                declaredField.setAccessible(true);
                target.addChildElement(annotation.value()).addTextNode((String)declaredField.get(pojo));
                declaredField.setAccessible(false);
            }
        }
    }
}
