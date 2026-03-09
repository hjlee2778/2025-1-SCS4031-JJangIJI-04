package com.jjangiji.hankkimoa.auth.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProfileResponse(String nickname, String thumbnail_image_url, String profile_image_url) {
}
