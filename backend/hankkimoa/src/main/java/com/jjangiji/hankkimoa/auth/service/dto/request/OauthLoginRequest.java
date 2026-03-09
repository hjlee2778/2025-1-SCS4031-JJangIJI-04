package com.jjangiji.hankkimoa.auth.service.dto.request;

import jakarta.validation.constraints.NotBlank;

public record OauthLoginRequest(@NotBlank(message = "인가 코드가 존재하지 않습니다.") String code,
                                @NotBlank(message = "Redirect Uri가 존재하지 않습니다.") String redirectUri) {
}
