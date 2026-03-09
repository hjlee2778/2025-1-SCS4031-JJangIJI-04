package com.jjangiji.hankkimoa.expense.domain;

import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

@Getter
public class DailyExpense {

    private final LocalDate expenseDate;
    private final List<Expense> expenses;

    public DailyExpense(LocalDate expenseDate, List<Expense> expenses) {
        validateExpenseByDate(expenseDate, expenses);
        this.expenseDate = expenseDate;
        this.expenses = expenses;
    }

    private void validateExpenseByDate(LocalDate expenseDate, List<Expense> expenses) {
        if (!expenses.stream().allMatch(expense -> expense.getExpenseDate().equals(expenseDate))) {
            throw new HankkiMoaException(ExceptionCode.EXPENSE_DATE_NOT_SAME);
        }
    }

    public ExpenseStatus getExpenseStatus() {
        return ExpenseStatus.convert(
                getDailyRecommendExpense(),
                calculateTotalExpense());
    }

    private int getDailyRecommendExpense() {
        return expenses.get(0)
                .getExpenseSavingGoal()
                .getDailyRecommendExpense();
    }

    public int calculateTotalExpense() {
        return expenses.stream()
                .mapToInt(Expense::getExpense)
                .sum();
    }
}
