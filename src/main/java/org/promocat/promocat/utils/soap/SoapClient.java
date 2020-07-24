package org.promocat.promocat.utils.soap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Component
@Slf4j
public class SoapClient {

    private static final String TOKEN_REQUEST = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ns=\"urn://x-artefacts-gnivc-ru/inplat/servin/OpenApiMessageConsumerService/types/1.0\"><soapenv:Header/><soapenv:Body><ns:GetMessageRequest><ns:Message><ns1:AuthRequest xmlns:ns1=\"urn://x-artefacts-gnivc-ru/ais3/kkt/AuthService/types/1.0\"><ns1:AuthAppInfo><ns1:MasterToken>dPymKnYFZufero6MW3wcpF8p7lgrQefCOGxTlhgwdvYo08RXzKGQqPyrzl7k0tuHgfMFtWNbgC1FpioqtnHMyQkYATlFEycH5pIb54vQNj7eBXlQyCey4Axgvf2tZNRZ</ns1:MasterToken></ns1:AuthAppInfo></ns1:AuthRequest></ns:Message></ns:GetMessageRequest></soapenv:Body></soapenv:Envelope>";

    private String getNewToken() throws IOException {
        log.info("Getting new token");
        URL url = new URL("https://himself-ktr-api.nalog.ru:8090/open-api/AuthService");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("SOAPAction", "urn:GetMessageRequest");
        try( DataOutputStream wr = new DataOutputStream( con.getOutputStream())) {
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
        log.info("Token response content {}", content.toString());
        return con.getResponseMessage();
    }

    public static void main(String[] args) {
        try {
            new SoapClient().getNewToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
