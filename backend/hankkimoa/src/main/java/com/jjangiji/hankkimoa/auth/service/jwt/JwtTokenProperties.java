package com.jjangiji.hankkimoa.auth.service.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;

@Getter
@Component
public class JwtTokenProperties {

    protected static final String TOKEN_TYPE = "type";

    private final String secretKey;
    private final long accessTokenExpirationMillis;
    private final long refreshTokenExpirationMillis;

    public JwtTokenProperties(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.accessToken-expiration-millis}") long accessTokenExpirationMillis,
            @Value("${jwt.refreshToken-expiration-millis}") long refreshTokenExpirationMillis) {
        this.secretKey = secretKey;
        this.accessTokenExpirationMillis = accessTokenExpirationMillis;
        this.refreshTokenExpirationMillis = refreshTokenExpirationMillis;
    }

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
}
