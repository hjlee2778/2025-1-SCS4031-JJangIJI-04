package com.jjangiji.hankkimoa.expense.service.dto.response;

import java.util.List;

public record TodayExpenses(SavingGoalStatusResponse savingGoalStatus,
                            List<ExpenseWithEmojisResponse> expenses) {
}
