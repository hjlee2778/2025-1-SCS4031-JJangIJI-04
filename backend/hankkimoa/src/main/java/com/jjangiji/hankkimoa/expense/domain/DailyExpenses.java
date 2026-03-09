package com.jjangiji.hankkimoa.expense.domain;

import lombok.Getter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class DailyExpenses {

    private final List<DailyExpense> dailyExpenses;

    public DailyExpenses(List<Expense> expenses) {
        this.dailyExpenses = expenses.stream()
                .collect(Collectors.groupingBy(Expense::getExpenseDate, LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .map(entry -> new DailyExpense(entry.getKey(), entry.getValue()))
                .toList();
    }

    public int getSize() {
        return dailyExpenses.size();
    }

    public int getExpenseOverBudgetCount() {
        return (int) dailyExpenses.stream()
                .filter(dailyExpense -> dailyExpense.getExpenseStatus().equals(ExpenseStatus.BAD))
                .count();
    }
}
