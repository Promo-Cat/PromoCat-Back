package org.promocat.promocat.utils.soap;

import com.sun.xml.bind.XmlAccessorFactory;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.utils.soap.attributes.ConnectionPermissions;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;
import org.promocat.promocat.utils.soap.operations.PostBindPartnerWithPhoneRequest;
import org.promocat.promocat.utils.soap.operations.SendMessageResponse;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class SoapClient {

    private static final String TOKEN_REQUEST = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"urn://x-artefacts-gnivc-ru/inplat/servin/OpenApiMessageConsumerService/types/1.0\"><soapenv:Header/><soapenv:Body><ns:GetMessageRequest><ns:Message><ns1:AuthRequest xmlns:ns1=\"urn://x-artefacts-gnivc-ru/ais3/kkt/AuthService/types/1.0\"><ns1:AuthAppInfo><ns1:MasterToken>dPymKnYFZufero6MW3wcpF8p7lgrQefCOGxTlhgwdvYo08RXzKGQqPyrzl7k0tuHgfMFtWNbgC1FpioqtnHMyQkYATlFEycH5pIb54vQNj7eBXlQyCey4Axgvf2tZNRZ</ns1:MasterToken></ns1:AuthAppInfo></ns1:AuthRequest></ns:Message></ns:GetMessageRequest></soapenv:Body></soapenv:Envelope>";
    private static final String API_URL = "https://himself-ktr-api.nalog.ru:8090/ais3/smz/SmzIntegrationService";

    private String token;
    private ZonedDateTime tokenExpireTime;

    private String getNewToken() throws IOException {
        log.info("Getting new token");
        URL url = new URL("https://himself-ktr-api.nalog.ru:8090/open-api/AuthService");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("SOAPAction", "urn:GetMessageRequest");
        try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
            wr.write(TOKEN_REQUEST.getBytes());
        }
        log.info("Token response status {}", con.getResponseCode());
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        log.info("Token response message {}", con.getResponseMessage());
        return parseTokenXml(content.toString());
    }

    private String parseTokenXml(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));

            NodeList token = document.getElementsByTagName("Token");
            NodeList expireTime = document.getElementsByTagName("ExpireTime");
            if (token.getLength() == 0) {
                log.error("Token doesn`t present");
            }
            this.token = token.item(0).getFirstChild().getNodeValue();
            this.tokenExpireTime = ZonedDateTime.parse(expireTime.item(0).getFirstChild().getNodeValue());
            log.debug("{} - {}", this.token, this.tokenExpireTime.toString());
            return this.token;
        } catch (SAXException | IOException | ParserConfigurationException e) {
            log.error("Failed to parse token xml", e);
        }
        return null;
    }

    public synchronized String getToken() {
        if (token == null || tokenExpireTime.isBefore(ZonedDateTime.now())) {
            log.info("Token has expired. Getting new one.");
            try {
                getNewToken();
            } catch (IOException e) {
                log.error("Unable to get new token", e);
            }
        }
        return this.token;
    }

    public String send(AbstractOperation operation) {
        SoapRequest request = new SoapSendMessageRequest(operation, getToken());
        Optional<SOAPMessage> responseOptional = request.send(API_URL);
        if (responseOptional.isPresent()) {
            String messageId = getMessageId(responseOptional.get());
            log.info("MessageId: {}", messageId);
            SoapRequest getRequest = new SoapGetMessageRequest(new SendMessageResponse(messageId), getToken());
            responseOptional = getRequest.send(API_URL);
            if (responseOptional.isPresent()) {
                try {
                    Object res = soapXmlToPOJO(responseOptional.get(), operation.getResponseClass());
                    System.out.println(res.toString());
                } catch (SOAPException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }

            return messageId;
        } else {
            throw new RuntimeException("Response doesn`t present");
        }
    }

    private String getMessageId(SOAPMessage message) {
        NodeList messageIdNode = null;
        try {
            messageIdNode = message.getSOAPBody().getOwnerDocument().getElementsByTagName("MessageId");
        } catch (SOAPException e) {
            log.error("SOAPException", e);
        }
        if (messageIdNode.getLength() == 0) {
            log.error("Something goes wrong. No messageId in response");
        }
        return messageIdNode.item(0).getFirstChild().getNodeValue();
    }


    /**
     * Assembly new object of type {@code pojoClass} from {@code SOAPMessage xml}
     * @param xml given SOAP xml
     * @param pojoClass class of object that will be returned
     * @return new object with values from {@code SOAPMessage}
     */
    public Object soapXmlToPOJO(SOAPMessage xml, Class<?> pojoClass) throws SOAPException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (xml.getSOAPBody().getElementsByTagName(pojoClass.getSimpleName()).getLength() == 0) {
            log.error("Response doesn`t apply presented POJO class");
            // TODO: 24.07.2020 EXCEPTION (pojoClass doesn`t same with SOAP response body type)
            throw new RuntimeException("");
        }
        Object res = pojoClass.getDeclaredConstructor().newInstance();
        for (Field field : pojoClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(XmlField.class)) {
                XmlField annotation = field.getAnnotation(XmlField.class);
                // TODO: 24.07.2020 List support
                NodeList fieldValueInResponseNode = xml.getSOAPBody().getElementsByTagName(annotation.value());
                Object value = null;
                if (fieldValueInResponseNode.getLength() != 0) {
                    // TODO: 24.07.2020 Value doesn`t present in xml
                    value = fieldValueInResponseNode.item(0).getFirstChild().getNodeValue();
                }
                field.set(res, value);
            }
        }

        return res;
    }

    public static void main(String[] args) {
        PostBindPartnerWithPhoneRequest op = new PostBindPartnerWithPhoneRequest(
                "79062007099",
                List.of(ConnectionPermissions.INCOME_REGISTRATION, ConnectionPermissions.CANCEL_INCOME)
        );
        new SoapClient().send(op);
    }

}
