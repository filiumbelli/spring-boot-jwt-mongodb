package com.mongo.sofsit.config.security.jwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil implements Serializable {
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60 * 1000;
    public String secretKey = "Secret";

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    public String generateToken(UserDetails userDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        return (getUsername(token).equals(userDetails.getUsername()) && isTokenExpired(token));
    }

    public String getUsername(String token) {
        return getClaims(token).getSubject();
    }

    public Claims getClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    public Date getExpirationDate(String token) {
        return getClaims(token).getExpiration();
    }

    private boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
            .setClaims(claims)
            .setSubject(subject)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }

}
