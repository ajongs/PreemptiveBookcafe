package com.preemptivebookcafe.api.util;

import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.enums.ErrorEnum;
import com.preemptivebookcafe.api.exception.RequestInputException;
import com.preemptivebookcafe.api.repository.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class Jwt {

    private final UserRepository userRepository;

    String accessToken;
    String refreshToken;

    public String createToken(Long classNo, String subject){
        Optional<User> optionalUser = userRepository.findByClassNo(classNo);
        if(!optionalUser.isPresent()){
            throw new RequestInputException("RequestInputException",ErrorEnum.ID_ALREADY_EXISTS);
        }
        User user = optionalUser.get();
        String key = user.getSalt();

        Calendar calendar = Calendar.getInstance();
        if(subject.equals(accessToken)){
            calendar.add(Calendar.HOUR, 1);
        }
        else{
            calendar.add(Calendar.DATE, 8);
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("classNo", classNo);

        Claims claims = Jwts.claims(payload)
                .setSubject(subject)
                .setExpiration(new Date(calendar.getTimeInMillis()))
                .setIssuedAt(new Date())
                .setIssuer("PreemptiveBookCafe");

        //payload 생성 안하고 claims 생성 후 put 으로 넣어도 됌. claim 는 map 을 상속 받았기 때문.

        return Jwts.builder()
                .setClaims(claims)
                .setHeaderParam("typ","JWT")
                .signWith(SignatureAlgorithm.HS256, key.getBytes(StandardCharsets.UTF_8))
                .compact();

    }
}
