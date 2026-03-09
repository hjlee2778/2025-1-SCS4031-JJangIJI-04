package com.jjangiji.hankkimoa.expense.service.dto.response;

import com.jjangiji.hankkimoa.expense.domain.DailyExpense;
import java.time.LocalDate;

public record DailyExpenseResponse(
        LocalDate date,
        Integer totalExpense,
        String status) {

    public DailyExpenseResponse(DailyExpense expenseByDate) {
        this(
            expenseByDate.getExpenseDate(),
            expenseByDate.calculateTotalExpense(),
            expenseByDate.getExpenseStatus().name()
        );
    }
}
