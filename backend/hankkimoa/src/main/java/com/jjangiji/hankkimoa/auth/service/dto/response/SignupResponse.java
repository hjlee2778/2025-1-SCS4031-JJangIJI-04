package com.jjangiji.hankkimoa.auth.service.dto.response;

import java.util.List;

public record SignupResponse(String nickname, String imageUrl,
                             boolean isExpenseOpen, List<Integer> categories) {
}
