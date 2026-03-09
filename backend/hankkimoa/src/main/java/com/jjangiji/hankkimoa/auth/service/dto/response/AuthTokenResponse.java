package com.jjangiji.hankkimoa.auth.service.dto.response;

public record AuthTokenResponse(String nickname, String imageUrl,
                                String accessToken, String refreshToken) {
}
