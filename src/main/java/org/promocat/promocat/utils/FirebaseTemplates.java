package org.promocat.promocat.utils;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

/**
 * Created by Danil Lyskin at 14:58 28.11.2020
 */

@Slf4j
public class FirebaseTemplates {

    private final static String PROJECT_ID = "promocat-10bb9";
    private final static String BASE_URL = "https://firebaseremoteconfig.googleapis.com";
    private final static String REMOTE_CONFIG_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/remoteConfig";
    private final static String[] SCOPES = { "https://www.googleapis.com/auth/firebase.remoteconfig" };

    /**
     * Retrieve a valid access token that can be use to authorize requests to the Remote Config REST
     * API.
     *
     * @return Access token.
     * @throws IOException
     */
    private static String getAccessToken() throws IOException {
        log.info("envs={}", System.getenv());
        log.info("GOOGLE_APPLICATION_CREDENTIALS={}", System.getenv("GOOGLE_APPLICATION_CREDENTIALS"));
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(new FileInputStream(System.getenv("GOOGLE_APPLICATION_CREDENTIALS")))
                .createScoped(Arrays.asList(SCOPES));
        googleCredential.refreshToken();
        return googleCredential.getAccessToken();
    }

    /**
     * Get current Firebase Remote Config template from server and store it locally.
     *
     * @throws IOException
     */
    public static void getTemplate() {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = getCommonConnection(BASE_URL + REMOTE_CONFIG_ENDPOINT);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");
        } catch (IOException e) {
            log.error("Couldn't connect to Firebase: {}", BASE_URL + REMOTE_CONFIG_ENDPOINT);
            return;
        }

        int code = 0;
        try {
            code = httpURLConnection.getResponseCode();
        } catch (IOException e) {
            log.error("Invalid code: {}", code);
            return;
        }
        if (code == 200) {
            InputStream inputStream = null;
            try {
                inputStream = new GZIPInputStream(httpURLConnection.getInputStream());
            } catch (IOException e) {
                log.error("Couldn't read stream from Firebase");
                return;
            }
            String response = inputstreamToString(inputStream);

            JsonObject json = JsonParser.parseString(response).getAsJsonObject();

            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            String jsonStr = gson.toJson(json.getAsJsonObject("parameters"));

            File file = new File("templates.json");
            PrintWriter printWriter = null;
            try {
                printWriter = new PrintWriter(new FileWriter(file));
            } catch (IOException e) {
                log.error("Couldn't open file for writing Json templates");
                return;
            }
            printWriter.print(jsonStr);
            printWriter.flush();
            printWriter.close();

            log.info("Template retrieved and has been written to config.json");

            String etag = httpURLConnection.getHeaderField("ETag");
            log.info("ETag from server: " + etag);
        } else {
            log.error(inputstreamToString(httpURLConnection.getErrorStream()));
        }
    }

    /**
     * Read contents of InputStream into String.
     *
     * @param inputStream InputStream to read.
     * @return String containing contents of InputStream.
     * @throws IOException
     */
    private static String inputstreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    /**
     * Create HttpURLConnection that can be used for both retrieving and publishing.
     *
     * @return Base HttpURLConnection.
     * @throws IOException
     */
    private static HttpURLConnection getCommonConnection(String endpoint) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
        return httpURLConnection;
    }
}
