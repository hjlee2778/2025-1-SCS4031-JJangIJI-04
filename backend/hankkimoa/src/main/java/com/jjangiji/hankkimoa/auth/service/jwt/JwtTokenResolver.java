package com.jjangiji.hankkimoa.auth.service.jwt;

import com.jjangiji.hankkimoa.auth.service.AuthUser;
import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenResolver {

    private final JwtTokenProperties jwtTokenProperties;

    public AuthUser resolveAccessToken(String token) {
        return resolveTokenByType(token, TokenType.ACCESS_TOKEN);
    }

    public AuthUser resolveRefreshToken(String token) {
        return resolveTokenByType(token, TokenType.REFRESH_TOKEN);
    }

    private AuthUser resolveTokenByType(String token, TokenType tokenType) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtTokenProperties.getSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            validateTokenType(claims, tokenType);

            Long id = Long.valueOf(claims.getSubject());
            return AuthUser.from(id);
        } catch (ExpiredJwtException exception) {
            throw new HankkiMoaException(ExceptionCode.AUTHENTICATION_TOKEN_EXPIRED);
        } catch (JwtException exception) {
            throw new HankkiMoaException(ExceptionCode.AUTHENTICATION_TOKEN_INVALID);
        }
    }

    private void validateTokenType(Claims claims, TokenType tokenType) {
        String extractTokenType = claims.get(JwtTokenProperties.TOKEN_TYPE, String.class);
        if (!extractTokenType.equals(tokenType.name())) {
            throw new HankkiMoaException(ExceptionCode.AUTHENTICATION_TOKEN_TYPE_MISMATCH);
        }
    }
}
