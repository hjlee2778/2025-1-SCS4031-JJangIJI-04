package com.jjangiji.hankkimoa.auth.service.dto.request;

import java.util.List;

public record SignupRequest(String nickname, List<Integer> categories) {
}
