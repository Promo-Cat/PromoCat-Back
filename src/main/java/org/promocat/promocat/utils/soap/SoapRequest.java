package org.promocat.promocat.utils.soap;

import lombok.extern.slf4j.Slf4j;
import org.promocat.promocat.constraints.XmlField;

import javax.xml.soap.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class SoapRequest {

    /**
     * Operation name taking for POJO name. (example: PostPlatformRegistrationRequest)
     */
    protected final String operation;
    protected final SOAPConnection soapConnection;
    protected Object pojo;
    protected String token;
    /**
     * GetMessage or SendMessage
     */
    protected String method;

    /**
     * Дефолтный конструктор, который создаёт запрос и открывает соединение
     * @param pojo Класс операции, которую необходимо отправить на SOAP сервис
     * @param token Актуальный {@code token} для работы с сервисом
     */
    public SoapRequest(Object pojo, String token) {
        SOAPConnection soapConnection1 = null;
        this.pojo = pojo;
        this.token = token;
        this.operation = pojo.getClass().getSimpleName();
        try {
            soapConnection1 = SOAPConnectionFactory.newInstance().createConnection();
        } catch (SOAPException e) {
            log.error("Failed to create SOAPConnection", e);
        }
        this.soapConnection = soapConnection1;
    }

    /**
     * Отправляет SOAP запрос на сервис по эндпоинту {@code destinationURL}
     * @param destinationURL {@code URL} сервиса
     * @return {@code Optional.empty()} - если что-то пошло не так и запрос не отправился, иначе ответ сервера на данный запрос
     */
    public Optional<SOAPMessage> send(String destinationURL) {
        SOAPMessage response = null;
        try {
            response = soapConnection.call(createMessage(), destinationURL);
            System.out.println("\n----------RESPONSE------------\n");
            response.writeTo(System.out);
            System.out.println("\n");
            soapConnection.close();
            // FIXME: 23.07.2020 IOException
        } catch (SOAPException | IOException e) {
            log.error("Failed to send message", e);
        }
        return Optional.ofNullable(response);
    }

    /**
     * Собирает сообщение ({@code xml} на основе данного объекта операции {@code pojo}.
     * Добавляет все необходимые тэги и добваляет поля из {@code pojo} в тело операции, а так же выставляет хедеры.
     * @return Объект сообщения
     */
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
            SOAPElement operationBody;
            // FIXME: 24.07.2020 refactor
            if (this instanceof SoapGetMessageRequest) {
                operationBody = soapPart
                        .addChildElement(method, "ns")
                        .addChildElement(operation, "", "urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0");
            } else {
                operationBody = soapPart
                        .addChildElement(method, "ns")
                        .addChildElement("Message", "ns")
                        .addChildElement(operation, "", "urn://x-artefacts-gnivc-ru/ais3/SMZ/SmzPartnersIntegrationService/types/1.0");
            }

            insertPojoFields(operationBody);
            setHeaders(soapMessage.getMimeHeaders());
            soapMessage.saveChanges();
            System.out.println("\n---------------REQUEST--------------\n");
            soapMessage.writeTo(System.out);
            System.out.println("\n");

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

    /**
     * Устанавливает необходимые для запроса хедеры (в основном токены)
     * @param headers ссылка на хедеры запроса
     */
    protected void setHeaders(MimeHeaders headers) {
        // Наш какой-то там токен, в BASE64 160 символов
        headers.addHeader("FNS-OpenApi-UserToken", "fi7uTtOr5xPFEnxIvorCP8RzWQI0USN1TQoniL5glBpeRhZSqQLMR");
        headers.addHeader("FNS-OpenApi-Token", token);
        headers.addHeader("Content-Type", "application/xml; charset=utf-8");
    }

    /**
     * Сериализует объект {@code pojo} и добавляет все его поля, помеченные аннотацией {@link XmlField} в тело запроса
     * @param target Тело запроса, в которое помещаются поля {@code pojo}
     */
    protected void insertPojoFields(SOAPElement target) throws SOAPException, IllegalAccessException {
        for (Field declaredField : pojo.getClass().getDeclaredFields()) {
            if (declaredField.isAnnotationPresent(XmlField.class)) {
                XmlField annotation = declaredField.getAnnotation(XmlField.class);
                declaredField.setAccessible(true);
                Object fieldValue = declaredField.get(pojo);
                declaredField.setAccessible(false);
                if (fieldValue instanceof List) {
                    List fieldValueList = (List) fieldValue;
                    for (Object el : fieldValueList) {
                        target.addChildElement(annotation.value()).addTextNode(el.toString());
                    }
                } else {
                    target.addChildElement(annotation.value()).addTextNode(fieldValue.toString());
                }
            }
        }
    }
}
