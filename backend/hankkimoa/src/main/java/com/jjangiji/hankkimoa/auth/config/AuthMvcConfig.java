package com.jjangiji.hankkimoa.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class AuthMvcConfig implements WebMvcConfigurer {

    private final AuthRequiredPrincipalArgumentResolver authRequiredPrincipalArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authRequiredPrincipalArgumentResolver);
    }
}
