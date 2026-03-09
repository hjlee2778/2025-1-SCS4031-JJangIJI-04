package com.jjangiji.hankkimoa.auth.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jjangiji.hankkimoa.user.domain.LoginType;
import com.jjangiji.hankkimoa.user.domain.Role;
import com.jjangiji.hankkimoa.user.domain.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OauthInfoApiResponse(String id, String connected_at, KakaoAccountResponse kakao_account) {

    public User toUserEntity() {
        return new User(kakao_account.email(),
                kakao_account.profile().nickname(),
                kakao_account.profile().profile_image_url(),
                LoginType.KAKAO,
                Role.USER);
    }
}
