package com.jjangiji.hankkimoa.expense.service.dto.request;

import com.jjangiji.hankkimoa.expense.domain.Expense;
import com.jjangiji.hankkimoa.expense.domain.ExpenseSavingGoal;
import com.jjangiji.hankkimoa.restaurant.domain.Restaurant;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ExpenseCreateRequest(@NotNull(message = "식당 ID를 입력해주세요.") Long restaurantId,
                                   @NotNull(message = "식당 이름을 입력해주세요.") String restaurantName,
                                   @NotNull(message = "메뉴 이름을 입력해주세요.") String menuName,
                                   @NotNull(message = "지출 내역을 입력해주세요.") Integer expense,
                                   @NotNull(message = "메모를 입력해주세요.") String memo,
                                   @NotNull(message = "지출일을 입력해주세요.") LocalDate expenseDate,
                                   @NotNull(message = "평점을 입력해주세요.") Integer rating) {

    public Expense toExpense(ExpenseSavingGoal expenseSavingGoal, Restaurant restaurant) {
        return new Expense(expenseSavingGoal, restaurant, restaurantName, menuName, expense,
                memo, expenseDate, rating);
    }
}
