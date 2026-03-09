package com.jjangiji.hankkimoa.expense.domain;

import com.jjangiji.hankkimoa.common.BaseEntity;
import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.user.domain.User;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Objects;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class ExpenseSavingGoal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @NotNull(message = "예산이 NULL일 수 없습니다.")
    private Integer budget;

    @NotNull(message = "시작일이 NULL일 수 없습니다.")
    private LocalDate startDate;

    @NotNull(message = "종료일이 NULL일 수 없습니다.")
    private LocalDate endDate;

    public ExpenseSavingGoal(User user, Integer budget, LocalDate startDate, LocalDate endDate) {
        validateDate(startDate, endDate);
        this.user = user;
        this.budget = budget;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validateDate(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate) || startDate.isEqual(endDate)) {
            throw new HankkiMoaException(ExceptionCode.DATE_RANGE_INVALID);
        }
    }

    public ExpenseSavingGoal(Long id, User user, Integer budget, LocalDate startDate, LocalDate endDate) {
        this(user, budget, startDate, endDate);
        this.id = id;
    }

    public int getDailyRecommendExpense() {
        return budget / getDays();
    }

    public int calculateRemainingBudget(List<Expense> expenses) {
        int expenseSum = calculateUsedExpenses(expenses);
        return budget - expenseSum;
    }

    public int calculateUsedExpenses(List<Expense> expenses) {
        return expenses.stream()
                .mapToInt(Expense::getExpense).sum();
    }

    public int calculatePercentage(List<Expense> expenses) {
        return Math.round(((budget - calculateUsedExpenses(expenses)) * 100 / budget));
    }

    public int getDays() {
        return Period.between(startDate, endDate).plusDays(1).getDays();
    }

    public boolean isAfter(ExpenseSavingGoal expenseSavingGoal) {
        return startDate.isAfter(expenseSavingGoal.endDate);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ExpenseSavingGoal that = (ExpenseSavingGoal) object;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
