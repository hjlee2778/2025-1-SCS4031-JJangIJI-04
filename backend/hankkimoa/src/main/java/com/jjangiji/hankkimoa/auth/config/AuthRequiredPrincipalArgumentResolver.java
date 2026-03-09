package com.jjangiji.hankkimoa.auth.config;

import com.jjangiji.hankkimoa.auth.controller.cookie.CookieResolver;
import com.jjangiji.hankkimoa.auth.service.AuthUser;
import com.jjangiji.hankkimoa.auth.service.jwt.JwtTokenResolver;
import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.user.domain.User;
import com.jjangiji.hankkimoa.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class AuthRequiredPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;
    private final CookieResolver cookieResolver;
    private final JwtTokenResolver jwtTokenResolver;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return User.class.isAssignableFrom(parameter.getParameterType())
                && parameter.hasParameterAnnotation(AuthRequiredPrincipal.class);
    }

    @Override
    public User resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        cookieResolver.checkLoginRequired(request);
        String token = cookieResolver.extractAccessToken(request);
        return getAuthUser(token);
    }

    public User getAuthUser(String token) {
        AuthUser authUser = jwtTokenResolver.resolveAccessToken(token);
        return readUser(authUser.id());
    }

    private User readUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new HankkiMoaException(ExceptionCode.USER_NOT_FOUND));
    }
}

