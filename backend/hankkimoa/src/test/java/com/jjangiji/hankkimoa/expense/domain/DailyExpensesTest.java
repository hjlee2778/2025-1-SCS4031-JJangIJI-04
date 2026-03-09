package com.jjangiji.hankkimoa.expense.domain;

import com.jjangiji.hankkimoa.restaurant.domain.Address;
import com.jjangiji.hankkimoa.restaurant.domain.Category;
import com.jjangiji.hankkimoa.restaurant.domain.CategoryType;
import com.jjangiji.hankkimoa.restaurant.domain.Restaurant;
import com.jjangiji.hankkimoa.user.domain.LoginType;
import com.jjangiji.hankkimoa.user.domain.Role;
import com.jjangiji.hankkimoa.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

class DailyExpensesTest {

    private final User user = new User("hankkimoa@gmail.com", "한끼", "hankkiImage", LoginType.KAKAO, Role.USER);
    private final LocalDate now = LocalDate.now();
    private final ExpenseSavingGoal expenseSavingGoal = new ExpenseSavingGoal(1L, user,70_000, now, now.plusDays(6));
    private final Address address = new Address(0, 0, "서울 중구 퇴계로18길 20");
    private final Restaurant restaurant = new Restaurant(1L, new Category(CategoryType.한식), "한끼식당", "12345", 10000, address);

    @DisplayName("지출 초과 횟수 조회 성공")
    @Test
    void getExpenseOverBudgetCount() {
        // given
        Expense expense1 = new Expense(expenseSavingGoal, restaurant, "은화수식당", "돈가스", 10_000, "냠냠굿", now.minusDays(1), 5);
        Expense expense2 = new Expense(expenseSavingGoal, restaurant, "산타돈부리", "사케동", 13_000, "사케동 맛있다 ~", now, 5);

        DailyExpenses dailyExpenses = new DailyExpenses(List.of(expense1, expense2));

        // when
        int count = dailyExpenses.getExpenseOverBudgetCount();

        // then
        Assertions.assertThat(count).isEqualTo(1);
    }
}
