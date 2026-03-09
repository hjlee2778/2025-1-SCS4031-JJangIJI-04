package com.jjangiji.hankkimoa.expense.service.dto.response;

import com.jjangiji.hankkimoa.user.domain.User;
import java.time.LocalDateTime;

public record CommunityExpenseResponse(
        String nickname,
        Long userId,
        String imageUrl,
        ExpenseWithEmojisResponse expenseWithEmojisResponse,
        LocalDateTime createdAt,
        SimpleSavingGoalStatusResponse savingGoalStatus)
{
    public CommunityExpenseResponse(
            User user,
            SimpleSavingGoalStatusResponse savingGoalStatus,
            ExpenseWithEmojisResponse expenseWithEmojisResponse,
            LocalDateTime createdAt)
    {
        this(
            user.getNickname(),
            user.getId(),
            user.getImageUrl(),
            expenseWithEmojisResponse,
            createdAt,
            savingGoalStatus
        );
    }
}
