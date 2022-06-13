package com.preemptivebookcafe.api.controller.firebase;

import com.google.firebase.messaging.*;
import com.preemptivebookcafe.api.dto.firebase.FcmMessage;
import com.preemptivebookcafe.api.serviceImpl.firebase.FirebaseServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("push")
public class FirebaseController {

    @PostMapping("/send/token")
    public String sendToToken() throws FirebaseMessagingException {

        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = req.getHeader("Authorization");
        // This registration token comes from the client FCM SDKs.
        //String registrationToken = token;
        System.out.println("firebase token "+token);
        // See documentation on defining a message payload.
        Message message = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle("박동민은 보아라")
                        .setBody("갓원종의 메시지를..")
                        .build())
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 1000)
                        .setNotification(AndroidNotification.builder()
                                .setIcon("stock_ticker_update")
                                .setColor("#f45342")
                                .build())
                        .build())
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setBadge(42)
                                .build())
                        .build())
                .setToken(token)
                .build();
        // [END multi_platforms_message]
        String response = FirebaseMessaging.getInstance().send(message);
        return response;
        /*
        Message message = Message.builder()
                .putData("title", "박동민은 보아라")
                .putData("content", "갓원종의 메세지를")
                .setToken(token)
                .build();

        // Send a message to the device corresponding to the provided
        // registration token.
        String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);

        return response;
        */

    }
    @PostMapping("/fcm")
    public ResponseEntity<?> reqFcm(
            @RequestParam(required = true) String title,
            @RequestParam(required = true) String body
    ) {


        try {

            FirebaseServiceImpl.sendCommonMessage(title, body);

        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok("요청됨");
    }
}
