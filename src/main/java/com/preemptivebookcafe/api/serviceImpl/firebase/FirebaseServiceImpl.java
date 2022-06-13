package com.preemptivebookcafe.api.serviceImpl.firebase;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.google.firebase.messaging.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.preemptivebookcafe.api.dto.firebase.FcmMessage;
import com.preemptivebookcafe.api.service.firebase.FirebaseService;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class FirebaseServiceImpl implements FirebaseService {
    @Value("${fcm.certification}")
    private static String credentials;
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/preemptivebookcafe/messages:send";
    private final ObjectMapper objectMapper;
    private static final String PROJECT_ID = "preemptivebookcafe";
    private static final String BASE_URL = "https://fcm.googleapis.com";
    private static final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };
    public static final String MESSAGE_KEY = "message";

    private static HttpURLConnection getConnection() throws IOException {
        // [START use_access_token]
        URL url = new URL(BASE_URL + FCM_SEND_ENDPOINT);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
        httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
        return httpURLConnection;
        // [END use_access_token]
    }
    public static void sendMessage(JsonObject fcmMessage) throws IOException {
        HttpURLConnection connection = getConnection();
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(fcmMessage.toString());
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            String response = inputstreamToString(connection.getInputStream());
            System.out.println("Message sent to Firebase for delivery, response:");
            System.out.println(response);
        } else {
            System.out.println("Unable to send message to Firebase:");
            String response = inputstreamToString(connection.getErrorStream());
            System.out.println(response);
        }
    }

    private static JsonObject buildNotificationMessage(String title, String body) {
        JsonObject jNotification = new JsonObject();
        jNotification.addProperty("title", title);
        jNotification.addProperty("body", body);

        JsonObject jMessage = new JsonObject();
        jMessage.add("notification", jNotification);
        /*
            firebase
            1. topic
            2. token
            3. condition -> multiple topic
         */
        //jMessage.addProperty("topic", "news");
        jMessage.addProperty("token",
                "eHxCs-x6ouc:APA91bH_CbXavLwJYtoLhkC_ADb9XWdrHEZjIqhF5SYxh5uTvU5-YOxVK-o4Hnw0qTyMeZ3DinSVoYfN9oQU4oiT_EP1yUGxa3qtSP1J8L5d--vb5Upw67tcx8zuGyxCHrbQaeqQqObE");

        JsonObject jFcm = new JsonObject();
        jFcm.add(MESSAGE_KEY, jMessage);

        return jFcm;
    }
    public static void sendCommonMessage(String title, String body) throws IOException {
        JsonObject notificationMessage = buildNotificationMessage(title, body);
        System.out.println("FCM request body for message using common notification object:");
        prettyPrint(notificationMessage);
        sendMessage(notificationMessage);
    }

    private static void prettyPrint(JsonObject jsonObject) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(jsonObject) + "\n");
    }
    private static String getAccessToken() throws IOException {
        //21.6.23 아직까지도 공식 홈페이지에서 Deprecated 된 해당 문장을 수정하지 않고있다.
        //22.01.04 공식 홈페이지에서 제대로 수정이 되었다.
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(new ClassPathResource(credentials).getInputStream())
                .createScoped(Arrays.asList(SCOPES));
        googleCredential.refreshToken();
        return googleCredential.getAccessToken();
    }

    private static String inputstreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }
    public void sendReportMessage(String token) {

        Message message = Message.builder()

                .setNotification(Notification.builder()
                        .setTitle("회원님의 좌석이 부재중 신고가 접수 되었습니다.")
                        .setBody("45분 이내에 복귀하시지 않으면 퇴실 처리 됩니다.")
                        .build())
                 /*
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 1000)
                        .setNotification(AndroidNotification.builder()
                                .setIcon("stock_ticker_update")
                                .setColor("#f45342")
                                .setTitle("회원님의 좌석이 부재중 신고가 접수 되었습니다.")
                                .setBody("45분 이내 복귀하시지 않으면 퇴실 처리 됩니다.")
                                .build())
                        .build())
                /*
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setBadge(42)
                                .build())
                        .build())*/
                .setToken(token)
                .build();
        // [END multi_platforms_message]
        try{
            String response = FirebaseMessaging.getInstance().send(message);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
