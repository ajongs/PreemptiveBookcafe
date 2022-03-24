package com.preemptivebookcafe.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.preemptivebookcafe.api.entity.User;
import com.preemptivebookcafe.api.enums.ErrorEnum;
import com.preemptivebookcafe.api.exception.AccessTokenException;
import com.preemptivebookcafe.api.exception.RequestInputException;
import com.preemptivebookcafe.api.exception.TokenException;
import com.preemptivebookcafe.api.repository.user.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.Base64UrlCodec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Component
@RequiredArgsConstructor
public class Jwt {

    private final UserRepository userRepository;


    @Value("${access_token}")
    private String accessToken;
    @Value("${refresh_token}")
    private String refreshToken;

    public String createToken(Long classNo, String subject) {
        Optional<User> optionalUser = userRepository.findByClassNo(classNo);
        if (!optionalUser.isPresent()) {
            throw new RequestInputException("RequestInputException", ErrorEnum.ID_ALREADY_EXISTS);
        }
        User user = optionalUser.get();
        String key = user.getSalt();

        Calendar calendar = Calendar.getInstance();
        if (subject.equals(accessToken)) {
            calendar.add(Calendar.HOUR, 1);
        } else {
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
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg",HS256)
                .signWith(HS256, key.getBytes(StandardCharsets.UTF_8))
                .compact();

    }

    public Boolean validateToken(String token, boolean flag) {
        //flag == false Refresh token
        //flag == true access token
        if (token == null || token.isEmpty()) {
            //throw new 토큰 빔
            throw new TokenException("TokenException", ErrorEnum.NULL_TOKEN);
        }
        token = token.substring(7); // "Bearer " 제거
        Map<String, Object> payload = getPayload(token, flag);


        String sub = (String)payload.get("sub");
        if(sub.equals(accessToken) && !flag) {
            throw new TokenException(ErrorEnum.FLAG_INVALID);
        }else if(sub.equals(refreshToken) && flag){
            throw new TokenException(ErrorEnum.FLAG_INVALID);
        }

        Optional<User> optionalUser = userRepository.findByClassNo((Long) payload.get("classNo"));
        if (!optionalUser.isPresent()) {
            //throw new token에 있는 유저가 존재하지 않음
            throw new TokenException(ErrorEnum.NO_USER_IN_TOKEN);
        }
        User user = optionalUser.get();
        String key = user.getSalt();

        try{
            Claims claims = Jwts.parser().setSigningKey(key.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody();
            String subject = claims.getSubject();

        }
        catch (SignatureException e){
            //서명 오류
            if(flag){ //accessToken
                //throw new 엑세스 토큰 오류 중 서명 오류
                throw new TokenException("TokenException", ErrorEnum.INVALID_ACCESS_TOKEN);
            }else{
                //throw new 리프레시 토큰 오류 중 서명 오류
                throw new TokenException("TokenException", ErrorEnum.INVALID_REFRESH_TOKEN);

            }
        }catch (MalformedJwtException e){
            //jwt 구조 오류
            if(flag){ //accessToken
                //throw new 엑세스 토큰 오류 중 구조 오류
                throw new TokenException("TokenException", ErrorEnum.MALFORMED_ACCESS_TOKEN);

            }else{
                //throw new 리프레시 토큰 오류 중 구조 오류
                throw new TokenException("TokenException", ErrorEnum.MALFORMED_REFRESH_TOKEN);

            }
        }catch (ExpiredJwtException e){
            //만료된 토큰
            if(flag){ //accessToken
                //throw new 엑세스 토큰 오류 중 만료 오류
                throw new TokenException("TokenException", ErrorEnum.EXPIRED_ACCESS_TOKEN);

            }else{
                //throw new 리프레시 토큰 오류 중 만료 오류
                throw new TokenException("TokenException", ErrorEnum.EXPIRED_REFRESH_TOKEN);

            }
        }catch (UnsupportedJwtException e){
            //다른 구조의 jwt토큰 ( jwt 토큰 구조는 맞으나 서버에서 사용하는 구조는 아님 == 다른 서버의 jwt 토큰)
            if(flag){
                //
                throw new TokenException("TokenException", ErrorEnum.UNSUPPORTED_ACCESS_TOKEN);

            }else{
                throw new TokenException("TokenException", ErrorEnum.UNSUPPORTED_REFRESH_TOKEN);

            }
        }

        return true;
    }

    private Map getPayload(String token, boolean flag) {
        String splitToken = token.split("\\.")[1];
        ObjectMapper ob = new ObjectMapper(); //json 파싱 직렬/역직렬화 객체
        Map<String, Object> payload = null;
        try {
            //Base64.getDecoder().decode(splitToken)
            payload = ob.readValue(new String(Base64UrlCodec.BASE64URL.decode(splitToken)), Map.class);
        } catch (IOException e) {
            if (flag) {
                //throw new AccessTokenException(ErrorEnum.INVALID_ACCESSTOKEN);
            } else {
                //throw new RefreshTokenException(ErrorEnum.INVALID_REFRESHTOKEN);
            }
            return payload;
        }
        return null;
    }
}
