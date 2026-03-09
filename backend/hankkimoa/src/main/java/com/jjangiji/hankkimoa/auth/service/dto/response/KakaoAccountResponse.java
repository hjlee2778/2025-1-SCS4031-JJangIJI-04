package com.jjangiji.hankkimoa.auth.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record KakaoAccountResponse(String email, String name, ProfileResponse profile) {
}
