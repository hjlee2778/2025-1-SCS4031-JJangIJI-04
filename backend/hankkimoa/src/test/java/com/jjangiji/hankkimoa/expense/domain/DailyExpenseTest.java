package com.jjangiji.hankkimoa.expense.domain;

import com.jjangiji.hankkimoa.common.exception.ExceptionCode;
import com.jjangiji.hankkimoa.common.exception.HankkiMoaException;
import com.jjangiji.hankkimoa.restaurant.domain.Address;
import com.jjangiji.hankkimoa.restaurant.domain.CategoryType;
import com.jjangiji.hankkimoa.restaurant.domain.Category;
import com.jjangiji.hankkimoa.restaurant.domain.Restaurant;
import com.jjangiji.hankkimoa.user.domain.LoginType;
import com.jjangiji.hankkimoa.user.domain.Role;
import com.jjangiji.hankkimoa.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

class DailyExpenseTest {

    private final User user = new User("hankkimoa@gmail.com", "한끼", "hankkiImage", LoginType.KAKAO, Role.USER);
    private final ExpenseSavingGoal expenseSavingGoal = new ExpenseSavingGoal(1L, user, 80_000, LocalDate.now(), LocalDate.now().plusDays(7));
    private final Address address = new Address(0, 0, "서울 중구 퇴계로18길 20");
    private final Restaurant restaurant = new Restaurant(1L, new Category(CategoryType.한식), "한끼식당", "12345", 10000, address);
    private final LocalDate now = LocalDate.now();

    @DisplayName("지출일 생성 실패 : 지출일이 서로 일치하지 않을 때")
    @Test
    void create() {
        // given
        Expense expense1 = new Expense(expenseSavingGoal, restaurant, "은화수식당", "돈가스", 10_000, "냠냠굿", now.minusDays(1), 5);
        Expense expense2 = new Expense(expenseSavingGoal, restaurant, "왕순이김밥", "김밥", 5_000, "가성비굿", now, 5);

        // when & then
        Assertions.assertThatCode(() -> new DailyExpense(now, List.of(expense1, expense2)))
                .isInstanceOf(HankkiMoaException.class)
                .hasMessage(ExceptionCode.EXPENSE_DATE_NOT_SAME.getMessage());
    }

    @DisplayName("지출일 총 지출 금액 계산 성공")
    @Test
    void calculateTotalPrice() {
        // given
        Expense expense1 = new Expense(expenseSavingGoal, restaurant, "은화수식당", "돈가스", 10_000, "냠냠굿", now, 5);
        Expense expense2 = new Expense(expenseSavingGoal, restaurant, "왕순이김밥", "김밥", 5_000, "가성비굿", now, 5);
        DailyExpense dailyExpense = new DailyExpense(now, List.of(expense1, expense2));

        // when
        int result = dailyExpense.calculateTotalExpense();

        // then
        Assertions.assertThat(result).isEqualTo(15_000);
    }
}
