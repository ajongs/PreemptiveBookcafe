package com.preemptivebookcafe.api.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {
    @Value("classpath:firebase/firebase_key.json")
    private Resource resource;

    @Value("${fcm.certification}")
    private String credentials;

    @PostConstruct
    public void initFireBase() throws IOException{

        try{
            FileInputStream serviceAccount = new FileInputStream(resource.getFile());
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            FirebaseApp.initializeApp(options);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
