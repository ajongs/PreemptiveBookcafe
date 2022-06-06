package com.preemptivebookcafe.api.controller.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
        String registrationToken = token;

        // See documentation on defining a message payload.
        Message message = Message.builder()
                .putData("title", "박동민은 보아라")
                .putData("content", "갓원종의 메세지를")
                .setToken(registrationToken)
                .build();

        // Send a message to the device corresponding to the provided
        // registration token.
        String response = FirebaseMessaging.getInstance().send(message);
        // Response is a message ID string.
        System.out.println("Successfully sent message: " + response);

        return response;
    }
}
