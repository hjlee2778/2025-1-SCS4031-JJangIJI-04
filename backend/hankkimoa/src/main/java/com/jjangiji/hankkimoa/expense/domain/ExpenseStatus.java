package com.jjangiji.hankkimoa.expense.domain;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum ExpenseStatus {

    GOOD(1000, Integer.MAX_VALUE),
    NOT_BAD(-1000, 1000),
    BAD(Integer.MIN_VALUE, -1000),
    ;

    private final int minDifference;
    private final int maxDifference;

    ExpenseStatus(int minDifference, int maxDifference) {
        this.minDifference = minDifference;
        this.maxDifference = maxDifference;
    }

    public static ExpenseStatus convert(int dailyRecommendExpense, int totalExpense) {
        int budgetDifference = dailyRecommendExpense - totalExpense;
        return Arrays.stream(values())
                .filter(status -> status.minDifference < budgetDifference && budgetDifference <= status.maxDifference)
                .findFirst()
                .orElse(BAD);
    }
}
