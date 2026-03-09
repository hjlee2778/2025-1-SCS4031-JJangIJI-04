package com.jjangiji.hankkimoa.expense.service.dto.response;

import java.util.List;

public record MonthlyExpenseResponse(List<DailyExpenseResponse> dailyExpenseStatus,
                                     Integer monthlyExpenseRecordCount,
                                     Integer dailyExpenseOverBudgetCount) {
}
