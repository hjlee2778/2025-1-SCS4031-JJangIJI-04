package com.jjangiji.hankkimoa.restaurant.service.dto.reqeust;

import jakarta.validation.constraints.NotNull;

public record RecommendationFeedbackRequest(@NotNull(message = "피드백을 입력해주세요.") Integer feedback) {
}
