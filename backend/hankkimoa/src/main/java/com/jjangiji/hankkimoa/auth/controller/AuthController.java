package com.jjangiji.hankkimoa.auth.controller;

import com.jjangiji.hankkimoa.auth.config.AuthRequiredPrincipal;
import com.jjangiji.hankkimoa.auth.controller.cookie.CookieProvider;
import com.jjangiji.hankkimoa.auth.controller.cookie.CookieResolver;
import com.jjangiji.hankkimoa.auth.service.AuthService;
import com.jjangiji.hankkimoa.auth.service.dto.request.OauthLoginRequest;
import com.jjangiji.hankkimoa.auth.service.dto.request.SignupRequest;
import com.jjangiji.hankkimoa.auth.service.dto.response.AuthResponse;
import com.jjangiji.hankkimoa.auth.service.dto.response.AuthTokenResponse;
import com.jjangiji.hankkimoa.auth.service.dto.response.SignupResponse;
import com.jjangiji.hankkimoa.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;
    private final CookieProvider cookieProvider;
    private final CookieResolver cookieResolver;

    @PostMapping("/api/auth/kakao")
    public ResponseEntity<AuthResponse> oauthLogin(@Valid @RequestBody OauthLoginRequest request) {
        AuthTokenResponse response = authService.oauthLogin(request);

        ResponseCookie accessTokenCookie = cookieProvider.createAccessTokenCookie(response.accessToken());
        ResponseCookie refreshTokenCookie = cookieProvider.createRefreshTokenCookie(response.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new AuthResponse(response.nickname(), response.imageUrl()));
    }

    @PostMapping("/api/auth/signup")
    public ResponseEntity<SignupResponse> signup(@AuthRequiredPrincipal User user, @RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(user, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/auth/refresh")
    public ResponseEntity<Void> refreshToken(HttpServletRequest httpServletRequest) {
        cookieResolver.checkLoginRequired(httpServletRequest);

        String refreshToken = cookieResolver.extractRefreshToken(httpServletRequest);
        String accessToken = authService.refreshToken(refreshToken);

        ResponseCookie accessTokenCookie = cookieProvider.createAccessTokenCookie(accessToken);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .build();
    }

    @PostMapping("/api/auth/logout")
    public ResponseEntity<Void> logout(@AuthRequiredPrincipal User user,
                                       HttpServletRequest httpServletRequest) {
        String accessToken = cookieResolver.extractAccessToken(httpServletRequest);
        String refreshToken = cookieResolver.extractRefreshToken(httpServletRequest);

        authService.logout(accessToken, refreshToken);

        ResponseCookie deletedAccessTokenCookie = cookieProvider.deleteAccessTokenCookie();
        ResponseCookie deletedRefreshTokenCookie = cookieProvider.deleteRefreshTokenCookie();

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, deletedAccessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, deletedRefreshTokenCookie.toString())
                .build();
    }
}
