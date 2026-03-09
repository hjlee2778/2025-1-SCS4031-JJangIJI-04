package com.jjangiji.hankkimoa.expense.service.dto.response;

public record SavingGoalStatusResponse(Integer budget, Integer remainingBudget,
                                       Integer remainingPercentage, String message) {
}
