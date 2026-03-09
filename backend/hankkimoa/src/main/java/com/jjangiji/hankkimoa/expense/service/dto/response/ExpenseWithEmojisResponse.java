package com.jjangiji.hankkimoa.expense.service.dto.response;

import com.jjangiji.hankkimoa.expense.domain.Expense;
import java.util.List;

public record ExpenseWithEmojisResponse(
                                        Long expenseId,
                                        String restaurant,
                                        String menu,
                                        Integer expense,
                                        String memo,
                                        List<EmojiResponse> emojis)
{
    public ExpenseWithEmojisResponse(Expense expense, List<EmojiResponse> emojis) {
        this(
            expense.getId(),
            expense.getRestaurantName(),
            expense.getMenuName(),
            expense.getExpense(),
            expense.getMemo(),
            emojis
        );
    }
}
