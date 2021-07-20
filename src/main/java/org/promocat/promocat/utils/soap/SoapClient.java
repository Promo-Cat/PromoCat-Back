package org.promocat.promocat.utils.soap;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.constraints.XmlField;
import org.promocat.promocat.constraints.XmlInnerObject;
import org.promocat.promocat.exception.soap.SoapException;
import org.promocat.promocat.exception.soap.SoapResponseClassException;
import org.promocat.promocat.exception.soap.SoapSmzPlatformErrorException;
import org.promocat.promocat.utils.soap.operations.AbstractOperation;
import org.promocat.promocat.utils.soap.operations.SendMessageResponse;
import org.promocat.promocat.utils.soap.operations.SmzPlatformError;
import org.promocat.promocat.utils.soap.operations.binding.GetNewlyUnboundTaxpayersRequest;
import org.promocat.promocat.utils.soap.operations.binding.GetNewlyUnboundTaxpayersResponse;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс который реализует логику общения с SOAP сервисом налоговой.
 */
@Component
@Slf4j
public class SoapClient {

    /**
     * Просто строка SOAP запроса на получение временного токена.
     */
    private static final String TOKEN_REQUEST = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"urn://x-artefacts-gnivc-ru/inplat/servin/OpenApiMessageConsumerService/types/1.0\"><soapenv:Header/><soapenv:Body><ns:GetMessageRequest><ns:Message><ns1:AuthRequest xmlns:ns1=\"urn://x-artefacts-gnivc-ru/ais3/kkt/AuthService/types/1.0\"><ns1:AuthAppInfo><ns1:MasterToken>i3NdQSThrpQDFyepaXafzysvRw4PCdhKztpyNVa9lCDTw7fd3dPkL6P2GwiyTuQie6A6rxxoraVaIwANRO9bc7771UmE8EmoiEeCoJbplfBABjSpKtF6hlioVW9ttHM1" +
            "</ns1:MasterToken></ns1:AuthAppInfo></ns1:AuthRequest></ns:Message></ns:GetMessageRequest></soapenv:Body></soapenv:Envelope>";
    /**
     * {@code URL} на который будут отправляться все запросы
     */
    private static final String API_URL = " https://apinpd.nalog.ru:4430/ais3/smz/SmzIntegrationService";

    /**
     * Последний полученный временный токен
     */
    private String token;
    /**
     * Время, когда {@code token} просрочится
     */
    private ZonedDateTime tokenExpireTime;

    /**
     * Запрашивает у SOAP сервиса налоговой новый токен.
     *
     * @return Свежий токен
     * @throws IOException
     */
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

    /**
     * Достаёт из {@code xml} значение нового токена и устанавливает его значение в {@code token} и время жизни в {@code tokenExpireTime}
     *
     * @param xml Ответ на запрос токена в формате XML.
     * @return новый токен из {@code xml}
     */
    private String parseTokenXml(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));

            NodeList token = document.getElementsByTagName("ns2:Token");
            NodeList expireTime = document.getElementsByTagName("ns2:ExpireTime");
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

    /**
     * Возвращает "живой" токен.
     * Если токен, который на данный момент хранится в поле {@code token} просрочился - запрашивает новый.
     *
     * @return актуальный токен
     */
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

    /**
     * Метод, который создаёт запрос к SOAP сервису и отправляет его.
     *
     * @param operation Объект класса-наследника {@link AbstractOperation}, который описывает необходимую операцию
     * @return объект класса, связанного с {@code operation}
     */
    @SneakyThrows
    public Object send(AbstractOperation operation) {
        SoapRequest request = new SoapSendMessageRequest(operation, getToken());
        System.out.println(request);
        Optional<SOAPMessage> responseOptional = request.send(API_URL);
        if (responseOptional.isPresent()) {
            String messageId = getMessageId(responseOptional.get());
            log.info("MessageId: {}", messageId);
            Thread.sleep(1000);
            SoapRequest getRequest = new SoapGetMessageRequest(new SendMessageResponse(messageId), getToken());
            responseOptional = getRequest.send(API_URL);
            if (responseOptional.isPresent()) {
                try {
                    Object res = soapXmlToPOJO(responseOptional.get().getSOAPBody(), operation.getResponseClass(), false);
                    if (res instanceof SmzPlatformError) {
                        throw new SoapSmzPlatformErrorException((SmzPlatformError) res);
                    }
                    return res;
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

            return null;
        } else {
            throw new RuntimeException("Response doesn`t present");
        }
    }

    /**
     * Достаёт {@code messageId} из ответа на запрос {@code GetMessage}
     * В дальнейшем этот {@code messageId} используется для получения ответа на запрос
     *
     * @param message Ответ сервера на запрос, в котором хранится идентификатор сообщения с ответом
     * @return идентификатор сообщения с ответом на запрос
     */
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
     *
     * @param xml       given SOAP xml
     * @param pojoClass class of object that will be returned
     * @return new object with values from {@code SOAPMessage}
     */
    public Object soapXmlToPOJO(Element xml, Class<?> pojoClass, boolean inner) throws SOAPException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!inner && xml.getElementsByTagName(pojoClass.getSimpleName()).getLength() == 0) {
            log.error("Response doesn`t apply presented POJO class. Expected: {}", pojoClass.getSimpleName());
            try {
                return soapXmlToPOJO(xml, SmzPlatformError.class, false);
            } catch (SoapException e) {
                throw new SoapResponseClassException(pojoClass.getSimpleName());
            }
        }
        Object res = pojoClass.getDeclaredConstructor().newInstance();
        for (Field field : pojoClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(XmlField.class)) {
                field.setAccessible(true);
                boolean fieldIsList = field.getType() == List.class;
                XmlField annotation = field.getAnnotation(XmlField.class);
                NodeList fieldValueInResponseNode = xml.getElementsByTagName(annotation.value());
                Object value = fieldIsList ? new ArrayList<>() : null;
                for (int i = 0; i < fieldValueInResponseNode.getLength(); i++) {
                    Object temp = field.isAnnotationPresent(XmlInnerObject.class) ?
                            soapXmlToPOJO((Element) fieldValueInResponseNode.item(i), field.getAnnotation(XmlInnerObject.class).value(), true) :
                            fieldValueInResponseNode.item(i).getFirstChild() == null ?
                                    null :
                                    fieldValueInResponseNode.item(i).getFirstChild().getNodeValue();

                    if (field.getType() == ZonedDateTime.class) {
                        temp = ZonedDateTime.parse((String) temp);
                    }
                    if (fieldIsList) {
                        ((List) value).add(temp);
                    } else if (field.getType() == Boolean.class) {
                        value = Boolean.parseBoolean((String) temp);
                    } else  {
                        value = temp;
                    }
                }
                field.set(res, value);
                field.setAccessible(false);
            }
        }
        return res;
    }

    // tests
    public static void main(String[] args) throws Exception {
//        PostBindPartnerWithPhoneRequest op = new PostBindPartnerWithPhoneRequest(
//                "79062587099",
//                List.of("INCOME_REGISTRATION")
//        );
        SoapClient client = new SoapClient();
//        PostBindPartnerWithPhoneResponse response = (PostBindPartnerWithPhoneResponse) client.send(op);
//        log.info("Post bind hue mae id {}", response.getId());

//        GetBindPartnerStatusRequest getStatusOp = new GetBindPartnerStatusRequest(
//                "13396"
//        );
//
//        GetBindPartnerStatusResponse statusResponse = (GetBindPartnerStatusResponse) client.send(getStatusOp);
//
//        log.info("Result: {}", statusResponse.getResult());
//        log.info("Inn: {}", statusResponse.getInn());
        System.setProperty("java.net.useSystemProxies", "true");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "8080");

        //        GetTaxpayerStatusRequest req1 = new GetTaxpayerStatusRequest();
//        req1.setInn("471204164572");
//        client.send(req1);
        GetNewlyUnboundTaxpayersRequest request = new GetNewlyUnboundTaxpayersRequest();
        request.setFrom(ZonedDateTime.now().minusDays(1));
        request.setTo(ZonedDateTime.now());
        GetNewlyUnboundTaxpayersResponse response = (GetNewlyUnboundTaxpayersResponse) client.send(request);
        System.out.println();
    }

}
