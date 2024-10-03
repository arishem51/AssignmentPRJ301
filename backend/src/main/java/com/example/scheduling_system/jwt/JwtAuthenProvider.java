package com.example.scheduling_system.jwt;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtAuthenProvider {

    @Value("${JWT_SECRET_KEY}")
    private String jwtSecretKey;

    private SecretKey getJwtKey() {
        return Keys
                .hmacShaKeyFor(Decoders.BASE64
                        .decode(jwtSecretKey));
    }

    public String createToken(String username) {
        return Jwts.builder()
                .subject(username)
                .signWith(getJwtKey(), Jwts.SIG.HS256)
                .compact();
    }

    private Jws<Claims> jwtSingedClaimsToken(String token) {
        return Jwts.parser().verifyWith(getJwtKey()).build().parseSignedClaims(token);
    }

    public boolean verifyToken(String token) {
        try {
            jwtSingedClaimsToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameByToken(String token) {
        return jwtSingedClaimsToken(token).getPayload().getSubject();
    }

}